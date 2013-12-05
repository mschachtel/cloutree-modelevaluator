package com.cloutree.modelevaluator.impl.r;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;

import com.cloutree.modelevaluator.PredictiveModel;
import com.cloutree.modelevaluator.PredictiveModelFile;
import com.cloutree.modelevaluator.PredictiveModelResult;
import com.cloutree.modelevaluator.exception.InvalidModelException;
import com.cloutree.modelevaluator.impl.pmml.PmmlPredictiveModel;
import com.cloutree.modelevaluator.impl.pmml.scripting.ScriptFactory;
import com.cloutree.modelevaluator.impl.pmml.scripting.ScriptProcessor;

/**
 * Cloutree Modelevaluator
 * A wrapper for different predictive analysis libraries to be used by Java Apps.
 * 
 * Copyright (C) 2014  Marc Schachtel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

/**
 * {@link PredictiveModel} implenentation for Native R.
 * 
 * @author mschachtel
 *
 * Since 04.12.2013
 * 
 */

public class RPredictiveModel implements PredictiveModel {
	
	static Logger log = Logger.getLogger(PmmlPredictiveModel.class.getName());
	
	Rengine engine;
	PredictiveModelFile modelFile;
	
	String preProcessor;
    String postProcessor;

    public RPredictiveModel() {
    	
    	this.engine = new Rengine();
    	
        if (!this.engine.waitForR())
        {
            log.log(Level.SEVERE, "Not able to load R - Please make sure it is installed properly");
            return;
        }
        
    }
    
	@SuppressWarnings("unchecked")
	public PredictiveModelResult eval(Map<String, Object> parameters) {
		
		RPredictiveModelResult result = new RPredictiveModelResult(this, parameters);
		
		//Check some stuff
		if(this.modelFile == null || this.modelFile.getFile() == null) {
			log.log(Level.WARNING, "Model-File not set properly");
			result.addError("Model-File not set properly");
			return result;
		}
		
		ScriptProcessor processor = ScriptFactory.getScriptProcessor(ScriptFactory.Types.JAVASCRIPT);
		
		// Do PreProcessing on Parameters
		if(this.preProcessor != null && !this.preProcessor.isEmpty())
			processor.doScriptProcessing(this.preProcessor, parameters);
		
		// Compile parameters for R
		String rParamaterAssignString = "params <- data.frame(";
		boolean initial = true;
		for(String key : parameters.keySet()) {
			try {
				String obj = (String)parameters.get(key);
				if(initial) {
					rParamaterAssignString = rParamaterAssignString + key + "=" + obj;
					initial= false;
				} else {
					rParamaterAssignString = rParamaterAssignString + "," + key + "=" + obj;
				}
			} catch(ClassCastException e) {
				log.log(Level.WARNING, "Parameter " + key + "->" + parameters.get(key) + " seems to be no String, which was expected for native R! Continouing without this parameter now...");
				result.addError("Parameter " + key + "->" + parameters.get(key) + " could not be read (String expected)!");
			}
		}
		
		//Get model name out of file
		this.engine.eval("modelname<-load('"+ this.modelFile.getFile().getPath());
		String modelName = this.engine.eval("modelname").asString();
		
		REXP rResult = this.engine.eval("predict(" + modelName + "," + rParamaterAssignString);
		
		if(rResult == null || rResult.getType() == REXP.XT_NULL) {
			result.addError("Empty R result, model has an error");
		} else {
			Map<String, Object> tempPredictions = new HashMap<String, Object>();
			this.processRResult(rResult, tempPredictions, "result");
		}
		
		// Do Post-Processing
		if(this.postProcessor != null && !this.postProcessor.isEmpty())
			processor.doScriptProcessing(this.postProcessor, (Map<String, Object>) result.getOutputValues());
			processor.doScriptProcessing(this.postProcessor, (Map<String, Object>) result.getPredictedValues());
		
		return result;
	}

	public void setPredictiveModelFile(PredictiveModelFile file) {
		this.modelFile = file;
	}

	public Map<String, String> getParameterSpecs() {
		// Not supported by native R
		log.log(Level.WARNING, "Parameter Specs requested but not yet supported by native R");
		return null;
	}

	public boolean validateModel() throws InvalidModelException {
		// Not yet supported
		log.log(Level.WARNING, "Model tried to be validated but not supported by native R");
		return true;
	}

	public void setPreProcessor(String script) {
		this.preProcessor = script;
	}

	public void setPostProcessor(String script) {
		this.postProcessor = script;
	}
	
	private void processRResult(REXP rResult, Map<String, Object> predictedValues, String name) {

		switch(rResult.getType()) {
		
			case REXP.XT_ARRAY_DOUBLE:
				double[] doubleArray = rResult.asDoubleArray();
				for(int i=0; i<doubleArray.length; i++) {
					predictedValues.put(name + "_" + Integer.toString(i), doubleArray[i]);
				}
				break;
				
			case REXP.XT_ARRAY_INT:
				int[] intArray = rResult.asIntArray();
				for(int i=0; i<intArray.length; i++) {
					predictedValues.put(name + "_" + Integer.toString(i), intArray[i]);
				}
				break;
				
			case REXP.XT_ARRAY_STR:
				String[] strArray = rResult.asStringArray();
				for(int i=0; i<strArray.length; i++) {
					predictedValues.put(name + "_" + Integer.toString(i), strArray[i]);
				}
				break;
				
			case REXP.XT_BOOL:
				boolean bool = rResult.asBool().isTRUE();
				predictedValues.put(name, bool);
				break;
				
			case REXP.XT_DOUBLE:
				double doub = rResult.asDouble();
				predictedValues.put(name, doub);
				break;
				
			case REXP.XT_INT:
				int integ = rResult.asInt();
				predictedValues.put(name, integ);
				break;
				
			case REXP.XT_STR:
				String str = rResult.asString();
				predictedValues.put(name, str);
				break;
				
			case REXP.XT_VECTOR:
				RVector rVector = rResult.asVector();
				Map<String, Object> vectorSubResult = new HashMap<String, Object>();
				for (Object vName : rVector.getNames()) {
					processRResult(rVector.at(vName.toString()), vectorSubResult, vName.toString());
				}
				predictedValues.put(name, vectorSubResult);
				break;
				
			case REXP.XT_LIST:
				RList rList = rResult.asList();
				Map<String, Object> listSubResult = new HashMap<String, Object>();
				for(String lName : rList.keys()) {
					processRResult(rList.at(lName), listSubResult, lName);
				}
				predictedValues.put(name, listSubResult);
				break;
			default:
				log.log(Level.WARNING, "Unsopported R expression/result type " + rResult.getType() + ". Will not add it to predicted values");
		}
		
	}

}
