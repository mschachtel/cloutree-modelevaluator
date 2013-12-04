package com.cloutree.modelevaluator.impl.r;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * PmmlPredictiveModelResult
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public class RPredictiveModelResult implements PredictiveModelResult {

    private RPredictiveModel model;
    private Map<String, Object> outputValues;
    private Map<String, Object> predictedValues;
    private boolean valid;
    private List<String> errors;
    private Map<String, Object> parameters;
    
    protected RPredictiveModelResult(RPredictiveModel model, Map<String, Object> parameters) {
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
   

}
