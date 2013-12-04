
package com.cloutree.modelevaluator.impl.pmml.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

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
 * JavaScriptProcessor
 *
 * @author marc
 *
 * Since 02.09.2013
 */

public class JavaScriptProcessor implements ScriptProcessor {

	static Logger log = Logger.getLogger(JavaScriptProcessor.class.getName());
	
    private String script;
    
    public JavaScriptProcessor() {
	
		if(ContextFactory.getGlobal() == null || ContextFactory.getGlobal() instanceof SandboxContextFactory) {
		    ContextFactory.initGlobal(new SandboxContextFactory());
		}
		
    }
    
    public void doScriptProcessing(String script, Map<String, Object> parameters) {

		Map<String, Object> result = new HashMap<String, Object>();;
		
		this.setScript(script);
		
		try {
		    result = this.process(parameters);
		} catch (Exception e) {
		    log.log(Level.SEVERE, e.getMessage());
		    return;
		}
		
		for(String resultKey : result.keySet()) {
		    if(parameters.containsKey(resultKey)) {
			parameters.remove(resultKey);
			parameters.put(resultKey, result.get(resultKey));
		    }
		}
    }
    
    private Map<String, Object> process(Map<String, Object> parameters) throws Exception {
	
		Context context = ContextFactory.getGlobal().enterContext();
		
		try {
	            // Initialize the standard objects (Object, Function, etc.)
	            // This must be done before scripts can be executed. Returns
	            // a scope object that we use in later calls.
	            Scriptable scope = context.initStandardObjects();
	            
	            // Secure Context
	            this.secureContext(context);
	
	            // Collect the arguments into a single string.
	            if(parameters != null) {
	        	    for(String parameter : parameters.keySet()) {
	        		Object param = Context.javaToJS(parameters.get(parameter), scope);
	        		ScriptableObject.putProperty(scope, parameter, param);
	        	    }
	            }
	
	            // Now evaluate the string we've collected.
	            context.evaluateString(scope, this.script, "ClouTree", 1, null);
	
	            // Collect new variable values
	            
	            Map<String, Object> resultValues = new HashMap<String, Object>();
	            
	            for(String parameter : parameters.keySet()) {
			Object param = scope.get(parameter, scope);
			resultValues.put(parameter, param);
		    }
	            
	            return resultValues;
	            
	        } finally {
	            // Exit from the context.
	            Context.exit();
	        }
	    
    }

    private void setScript(String script) {
    	this.script = script;
    }
    
    private void secureContext(Context context) {
		context.setClassShutter(new ClassShutter() {
			public boolean visibleToScripts(String className) {					
				if(className.startsWith("adapter") || className.startsWith("org.jpmml"))
					return true;
				return false;
			}
		});
    }
    
    private static class SandboxNativeJavaObject extends NativeJavaObject {

		private static final long serialVersionUID = 1L;
	
		public SandboxNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
			super(scope, javaObject, staticType);
		}
	 
		@Override
		public Object get(String name, Scriptable start) {
			if (name.equals("getClass")) {
				return NOT_FOUND;
			}
	 
			return super.get(name, start);
		}
	    }
	    
	    public static class SandboxWrapFactory extends WrapFactory {
			@Override
			public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
				return new SandboxNativeJavaObject(scope, javaObject, staticType);
			}
	    }
	    
	    public class SandboxContextFactory extends ContextFactory {
			@Override
			protected Context makeContext() {
				Context cx = super.makeContext();
				cx.setWrapFactory(new SandboxWrapFactory());
				return cx;
			}
    }

}
