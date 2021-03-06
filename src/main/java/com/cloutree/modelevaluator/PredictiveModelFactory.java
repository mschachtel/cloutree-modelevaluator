package com.cloutree.modelevaluator;

import java.io.File;
import java.util.logging.Logger;

import com.cloutree.modelevaluator.exception.InvalidModelException;
import com.cloutree.modelevaluator.impl.pmml.PmmlPredictiveModel;

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
 * This is the factory to create a {@link PredictiveModel} for a respective model-type. Please use this as the only way
 * to instantiate a {@link PredictiveModel}!
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public class PredictiveModelFactory {

	static Logger log = Logger.getLogger(PredictiveModelFactory.class.getName());
	
    protected static PredictiveModel getPredictiveModel(ModelTypes implementation) throws Exception {
	
		if(implementation.equals(ModelTypes.PMML)) {
			PredictiveModel pmmlModel = new PmmlPredictiveModel();
		    return pmmlModel;
		} 
		
		// TODO Activate as soon as native R is stable
//		else if(implementation.equals(ModelTypes.NativeR)) {
//			
//			// TODO Remove as soon as native R is stable
//			log.log(Level.WARNING, "Native R not tested yet!");
//			
//			PredictiveModel rModel = new RPredictiveModel();
//			
//			return rModel;
//		}
		
		throw new InvalidModelException();
	
    }
    
    /**
     * Create an instance of {@link PredictiveModel} with given {@link ModelTypes} and a path to the model file.
     * 
     * @param implementation
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PredictiveModel getPredictiveModel(ModelTypes implementation, String filePath) throws Exception {
    	
    	File file = new File(filePath);
    	return getPredictiveModel(implementation,file);
    	
    }
    
    /**
     * Create an instance of {@link PredictiveModel} with given {@link ModelTypes} and {@link File}.
     * 
     * @param implementation
     * @param filePath
     * @return
     * @throws Exception
     */
    public static PredictiveModel getPredictiveModel(ModelTypes implementation, File file) throws Exception {
	
	    PredictiveModel predModel = getPredictiveModel(implementation);
	    if(predModel == null)
	    	throw new InvalidModelException();
		
	    // Set File
	    if(!file.exists()) {
	    	throw new Exception("Model File not found! " + file.getAbsolutePath());
	    }
	    
	    PredictiveModelFile modelFile = new PredictiveModelFile(file);
	    predModel.setPredictiveModelFile(modelFile);
	    
	    return predModel;
	
    }
    
}