package com.cloutree.modelevaluator.impl.pmml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.cloutree.modelevaluator.PredictiveModel;
import com.cloutree.modelevaluator.PredictiveModelResult;

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
 * {@link PredictiveModelResult} implementation for PMML.
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public class PmmlPredictiveModelResult implements PredictiveModelResult {

    private PmmlPredictiveModel model;
    private Map<String, Object> outputValues;
    private Map<String, Object> predictedValues;
    private boolean valid;
    private List<String> errors;
    private Map<String, Object> parameters;
    
    protected PmmlPredictiveModelResult(PmmlPredictiveModel model, Map<String, Object> parameters) {
		this.model = model;
		this.outputValues = new HashMap<String, Object>();
		this.predictedValues = new HashMap<String, Object>();
		this.errors = new LinkedList<String>();
		this.parameters = parameters;
		this.valid = true;
    }
    
    protected void addOutputValue(String name, Object value) {
    	this.outputValues.put(name, value);
    }
    
    protected void addPredictedValue(String name, Object value) {
    	this.predictedValues.put(name, value);
    }
    
    protected void addError(String error) {
    	this.errors.add(error);
    }
    
    protected void setValid(boolean valid) {
    	this.valid = valid;
    }
    
    public Map<String, ?> getOutputValues() {
    	return this.outputValues;
    }
    
    public Map<String, ?> getPredictedValues() {
    	return this.predictedValues;
    }

    public boolean isValid() {
    	return this.valid;
    }

    public List<String> getErrors() {
    	return this.errors;
    }

    public Map<String, Object> getParameters() {
    	return this.parameters;
    }

    public PredictiveModel getPredictiveModel() {
    	return this.model;
    }

	/* (non-Javadoc)
	 * @see com.cloutree.modelevaluator.PredictiveModelResult#toJSON()
	 */
	@Override
	public String toJSON() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		JsonArrayBuilder outputBuilder = Json.createArrayBuilder();
		
		// Output Values
		Set<String> outputKeys = this.outputValues.keySet();
		for(String outputKey : outputKeys) {
		    Object val = this.outputValues.get(outputKey);
		    if(val != null) {
			outputBuilder.add(Json.createObjectBuilder().add(outputKey, val.toString()));
		    }
		}
		builder.add("outputValues", outputBuilder);
		
		JsonArrayBuilder predictedBuilder = Json.createArrayBuilder();
		Set<String> predictedKeys = this.predictedValues.keySet();
		for(String predictedKey : predictedKeys) {
		    Object val = this.predictedValues.get(predictedKey);
		    if(val != null) {
			predictedBuilder.add(Json.createObjectBuilder().add(predictedKey, val.toString()));
		    }
		}
		builder.add("predictedValues", predictedBuilder);
		
		JsonArrayBuilder errorBuilder = Json.createArrayBuilder();
		for(String error : this.errors) {
		    errorBuilder.add(error);
		}
		builder.add("errors", errorBuilder);
		
		JsonObject jsonObject = builder.build();
		return jsonObject.toString();

	}
}
