package com.cloutree.modelevaluator;

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
 * Modeltypes define the already implemented libraries for predictive modelling. Currently implemented: <br>
 * - PMML: Standard PMML exported files (Implementation based on JPMML)
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public enum ModelTypes {

    PMML, 
//    NativeR
    ;
    
    @Override
    public String toString() {
    	return this.name();
    }

}
