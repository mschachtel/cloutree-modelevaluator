package com.cloutree.modelevaluator;

import org.junit.Assert;
import org.junit.Test;

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
 * PredictiveModelFactoryTest
 *
 * @author marc
 *
 * Since 20.11.2013
 */

public class PredictiveModelFactoryTest {

	@Test
	public void shouldFactoriesPmmlPredictiveModel() {
		
		PredictiveModel modelPmml = null;
		
		try {
			modelPmml = PredictiveModelFactory.getPredictiveModel(ModelTypes.PMML);
		} catch (Exception e) {
			Assert.assertTrue("Threw exception: " + e.getLocalizedMessage(), false);
		}
		
		Assert.assertTrue(modelPmml != null);
		Assert.assertTrue(modelPmml instanceof PmmlPredictiveModel);

	}
	
//	@Test
//	public void shouldFactoriesRPredictiveModel() {
//		
//		PredictiveModel modelR = null;
//		
//		try {
//			modelR = PredictiveModelFactory.getPredictiveModel(ModelTypes.NativeR);
//		} catch (Exception e) {
//			Assert.assertTrue("Threw exception: " + e.getLocalizedMessage(), false);
//		}
//		
//		Assert.assertTrue(modelR != null);
//		Assert.assertTrue(modelR instanceof RPredictiveModel);
//
//	}
	
}
