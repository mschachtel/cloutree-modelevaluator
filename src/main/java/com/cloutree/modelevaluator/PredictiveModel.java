package com.cloutree.modelevaluator;

import java.util.Map;

import com.cloutree.modelevaluator.exception.InvalidModelException;

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
 * Interface to be used by the API programmer. This represents the model itself and is, together
 * with {@link PredictiveModelResult} and {@link PredictiveModelFactory}, the most important type for the programmer.
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public interface PredictiveModel {

	/**
	 * This method evaluates a model with given parameters.
	 * 
	 * @param parameters
	 * @return {@link PredictiveModelResult}
	 */
    public PredictiveModelResult eval(Map<String, Object> parameters);
    
    /**
     * The predictive model file should usually be set by the {@link PredictiveModelFactory}
     * 
     * @param file
     */
    public void setPredictiveModelFile(PredictiveModelFile file);
    
    /**
     * Sometimes a programmer might want to write generic code. To do so it is possible to retrieve the 
     * parameters required by the model at runtime. <br><br>
     * 
     * NOTE: PMML supports this feature while other models might not and would return null
     * 
     * @return Map (parameter name to type), null if not supported by model
     */
    public Map<String, String> getParameterSpecs();
    
    /**
     * Should validate a model. 
     * <br><br>
     * NOTE: Not supported by PMML yet.
     * 
     * @return false if a model is invalid, true if valid
     * @throws InvalidModelException
     */
    public boolean validateModel() throws InvalidModelException;
    
    /**
     * For pre-processing a JavaScript can be provided to automatically run over the arguments 
     * before the model gets evaluated.
     * 
     * @param script
     */
    public void setPreProcessor(String script);
    
    /**
     * For post-processing a JavaScript can be provided to automatically run over the return values 
     * after the model-result will be returned.
     * 
     * @param script
     */
    public void setPostProcessor(String script);
    
    /**
     * Returns the previously provided Script for pre-processing
     * 
     * @return script
     */
    public String getPreProcessor();
    
    /**
     * Returns the previously provided Script for post-processing
     * 
     * @return script
     */
    public String getPostProcessor();
    
}
