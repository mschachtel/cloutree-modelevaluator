package com.cloutree.modelevaluator.impl.pmml;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.cloutree.modelevaluator.PredictiveModelFile;
import com.cloutree.modelevaluator.PredictiveModelResult;
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
 * PmmlPredictiveModelTest
 *
 * @author marc
 *
 * Since 20.11.2013
 */

public class PmmlPredictiveModelTest {

	private String preProcessor = "if(NAME=='Mark'){ NAME='Marc' }";
	private String postProcessor = "if(VALUE<'0.5'){ VALUE='0' } else { VALUE='1'}";
	
	@Test
	public void shouldEvaluateModel() {
		
		PmmlPredictiveModel modelPmml = new PmmlPredictiveModel();
		URL url = this.getClass().getResource("/testPMML.xml");
		
		Assert.assertNotNull(url);
		
		String path = url.getPath();
		File file = new File(path);
		
		Assert.assertTrue(file.exists());
		
		PredictiveModelFile modelFile = new PredictiveModelFile(file);
		
		modelPmml.setPredictiveModelFile(modelFile);
		modelPmml.setPreProcessor(this.preProcessor);
		modelPmml.setPostProcessor(this.postProcessor);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("NAME", "Mark");
		
		PredictiveModelResult result = modelPmml.eval(parameters);

		result.serialize(false);
		
		Assert.assertNotNull(result);
		Assert.assertTrue(result.isValid());
		
	}
	
}
