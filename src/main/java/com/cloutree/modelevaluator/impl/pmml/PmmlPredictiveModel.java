package com.cloutree.modelevaluator.impl.pmml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dmg.pmml.DataField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.IOUtil;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.manager.PMMLManager;

import com.cloutree.modelevaluator.PredictiveModel;
import com.cloutree.modelevaluator.PredictiveModelFile;
import com.cloutree.modelevaluator.PredictiveModelResult;
import com.cloutree.modelevaluator.exception.InvalidModelException;
import com.cloutree.modelevaluator.scripting.ScriptFactory;
import com.cloutree.modelevaluator.scripting.ScriptProcessor;

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
 * {@link PredictiveModel} implementation for PMML.
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public class PmmlPredictiveModel implements PredictiveModel {

    static Logger log = Logger.getLogger(PmmlPredictiveModel.class.getName());
    
    private PredictiveModelFile modelFile;
    
    private PMML pmml;
    private PMMLManager pmmlManager;
    private Evaluator evaluator;
    
    String preProcessor;
    String postProcessor;
    
    @SuppressWarnings("unchecked")
    public PredictiveModelResult eval(Map<String, Object> parameters) {
	
		PmmlPredictiveModelResult result = new PmmlPredictiveModelResult(this, parameters);
		Map<FieldName, Object> arguments = new LinkedHashMap<FieldName, Object>();
		List<FieldName> activeFields = this.evaluator.getActiveFields();
		Map<FieldName, ?> pmmlResult;
	
		ScriptProcessor processor = ScriptFactory.getScriptProcessor(ScriptFactory.Types.JAVASCRIPT);
		
		// Do PreProcessing on Parameters
		if(this.preProcessor != null && !this.preProcessor.isEmpty())
			processor.doScriptProcessing(this.preProcessor, parameters);
		
		for(FieldName activeField : activeFields){
			DataField dataField = this.evaluator.getDataField(activeField);
			Object value = parameters.get(dataField.getName().getValue());
			
			if(value == null || value.toString().isEmpty()) {
			    result.addError("No parameter found for: " + dataField.getName());
			    log.log(Level.WARNING, "No parameter found for: " + dataField.getName());
			} else {
			    try {
			    	arguments.put(activeField, evaluator.prepare(activeField, value));
			    } catch (Exception e) {
			    	result.addError("Field " + activeField.getValue() + " has invalid value " + value);
			    	log.log(Level.SEVERE, e.getMessage());
			    	result.setValid(false);
			    	return result;
			    }
			}
	
		}
		
		try {
		    pmmlResult = evaluator.evaluate(arguments);
		} catch (Exception e) {
		    result.addError("Unable to evaluate model: " + e.getMessage());
		    log.log(Level.WARNING, "Unable to evaluate model: " + e.getMessage());
		    return result;
		}
		
		List<FieldName> predictedFields = evaluator.getPredictedFields();
		for(FieldName predictedField : predictedFields){
			DataField dataField = evaluator.getDataField(predictedField);
			Object predictedValue = pmmlResult.get(predictedField);
			result.addPredictedValue(dataField.getName().getValue(), predictedValue);
		}
	
		List<FieldName> resultFields = this.evaluator.getOutputFields();
		for(FieldName resultField : resultFields){
			OutputField outputField = evaluator.getOutputField(resultField);
			Object outputValue = pmmlResult.get(resultField);
			result.addOutputValue(outputField.getName().getValue(), outputValue);
		}
		
		if(this.postProcessor != null && !this.postProcessor.isEmpty()) {
			processor.doScriptProcessing(this.postProcessor, (Map<String, Object>) result.getOutputValues());
			processor.doScriptProcessing(this.postProcessor, (Map<String, Object>) result.getPredictedValues());
		}
		
		return result;
    }

    public void setPredictiveModelFile(PredictiveModelFile file) {
		this.modelFile = file;
		
		try {
		    this.pmml = IOUtil.unmarshal(this.modelFile.getFile());
		} catch (Exception e) {
		    log.log(Level.SEVERE, e.getMessage());
		    return;
		}
	
		this.pmmlManager = new PMMLManager(this.pmml);
	
		// Load the default model
		this.evaluator = (Evaluator)pmmlManager.getModelManager(null, ModelEvaluatorFactory.getInstance());
	
    }

    public Map<String, String> getParameterSpecs() {
	
		if(this.evaluator == null) {
		    return null;
		}
		
		Map<String, String> result = new HashMap<String, String>();
		
		List<FieldName> activeFields = evaluator.getActiveFields();
		
		for(FieldName activeField : activeFields) {
		    DataField dataField = evaluator.getDataField(activeField);
		    result.put(dataField.getName().getValue(), dataField.getDataType().name());
		}
		
		return result;
    }

    public boolean validateModel() throws InvalidModelException {
    	// Not yet supported
    	log.log(Level.WARNING, "Model tried to be validated but not supported by PMML");
    	return true;
    }
    

	public void setPreProcessor(String preProcessor) {
		this.preProcessor = preProcessor;
	}

	public void setPostProcessor(String postProcessor) {
		this.postProcessor = postProcessor;
	}

	/* (non-Javadoc)
	 * @see com.cloutree.modelevaluator.PredictiveModel#getPreprocessor()
	 */
	@Override
	public String getPreProcessor() {
		return this.preProcessor;
	}

	/* (non-Javadoc)
	 * @see com.cloutree.modelevaluator.PredictiveModel#getPostProcessor()
	 */
	@Override
	public String getPostProcessor() {
		return this.postProcessor;
	}
    
}
