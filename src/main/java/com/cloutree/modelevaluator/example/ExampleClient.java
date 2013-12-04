package com.cloutree.modelevaluator.example;

import java.util.HashMap;
import java.util.Map;

import com.cloutree.modelevaluator.ModelTypes;
import com.cloutree.modelevaluator.PredictiveModel;
import com.cloutree.modelevaluator.PredictiveModelFactory;

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
 * TODO Docu
 *
 */

public class ExampleClient 
{
    public static void main( String[] args )
    {
    	
    	if(args == null || args.length < 2) {
    		System.out.println("Argument missing, please use as follows: [command] [type (PMML or nativeR)] [absolute model file path] [comma separated parameters (optional)]");
			return;
    	}
    	
    	String type = args[0];
    	String path = args[1];
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	ModelTypes modelType;
    	
    	if(type.equalsIgnoreCase("PMML")) {
    		modelType = ModelTypes.PMML;
    	} else if(type.equalsIgnoreCase("nativeR")) {
    		modelType = ModelTypes.NativeR;
    	} else {
    		System.out.println("Model Type " + type + "unknown!");
    		return;
    	}
    	
    	if(args.length > 2) {
    		params = buildParameters(args[2]);
    	}
    	
    	PredictiveModel model = null;
    	
    	try {
			model = PredictiveModelFactory.getPredictiveModel(modelType, path);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return;
		}
    	
    	model.eval(params);
    	
    }
    
    private static Map<String, Object> buildParameters(String csvParams) {
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	return result;
    	
    }
}
