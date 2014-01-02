package com.cloutree.modelevaluator;

import java.util.List;
import java.util.Map;

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
 * Result interface for any {@link PredictiveModel} returned by eval-method. This wraps all results of any {@link ModelTypes}
 * in a standardized fashion.
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public interface PredictiveModelResult {

	/**
	 * Returns the output values of a model result. Those are not the results of the prediction but optional results.
	 * 
	 * @return
	 */
    public Map<String, ?> getOutputValues();
    
    /**
	 * Returns the predicted values of a model result. Those are the main results for any model.
	 * 
	 * @return
	 */
    public Map<String, ?> getPredictedValues();
    
    /**
	 * It might happen that a model was not evaluated or the evaluation failed. In this case the result is invalid.
	 * 
	 * @return
	 */
    public boolean isValid();
    
    /**
	 * During the evaluation some errors might occur, which are stored in this list. <br><br>
	 * 
	 * NOTE: Having errors does not mean the model evaluation failed. This can be checked via isValid.
	 * 
	 * @return
	 */
    public List<String> getErrors();

    /**
	 * Map of parameter-name to object with the parameters the model was evaluated against.
	 * 
	 * @return
	 */
    public Map<String, Object> getParameters();
    
    /**
	 * The {@link PredictiveModel} the result is attached to.
	 * 
	 * @return
	 */
    public PredictiveModel getPredictiveModel();
    
    /**
     * Writes a JSON-String to be used for interfacing to other applications/services.
     * This method is deprecated sine 1.2, please use {@link #serialize()}
     *  
     * @return JSON String
     */
    @Deprecated
    public String toJSON();
    
    /**
     * Serializes the object into a JSON String, which can be deserialized using {@link #deserialize(String)} on the other side.
     * Using this JSON-type of serialization enables deserialization outside of java. Class-information can be ignored in this case.
     *  
     * @param toJava indicates to include class-information for later deserialization with modelevaluator
     * @return JSON String
     */
    public String serialize(boolean toJava);
    
    /**
     * Deserializes a json string that was generated using {@link #serialize()} before. 
     *  
     * @param String version of a {@link PredictiveModelResult}
     * @return Deserialized {@link PredictiveModelResult}
     * @throws Exception 
     */
    public PredictiveModelResult deserialize(String serialization) throws ClassCastException, Exception;
    
}
