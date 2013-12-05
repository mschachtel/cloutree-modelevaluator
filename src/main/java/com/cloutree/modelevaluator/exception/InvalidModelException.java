package com.cloutree.modelevaluator.exception;

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
 * Exception that is thrown as soon as a model isn't valid.
 *
 * @author marc
 *
 * Since 09.08.2013
 */

public class InvalidModelException extends Exception {

    private static final long serialVersionUID = 1L;

	@Override
	public String getLocalizedMessage() {
		// TODO Auto-generated method stub
		return this.getMessage();
	}

	@Override
	public String getMessage() {
		return "Model not valid!";
	}


}
