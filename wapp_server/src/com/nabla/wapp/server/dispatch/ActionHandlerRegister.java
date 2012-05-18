/**
* Copyright 2012 nabla
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*
*/
package com.nabla.wapp.server.dispatch;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IResult;

/**
 * The <code></code> object is used to
 *
 */
@Singleton
public class ActionHandlerRegister {

	private final Injector																	injector;
	private final Map<Class<? extends IAction<?>>, Class<? extends IActionHandler<?, ?>>>	handlerClasses;
	private final Map<Class, IActionHandler<?, ?>>											handlers;

    @Inject
    public ActionHandlerRegister(Injector injector) {
        this.injector = injector;
        handlerClasses = new HashMap<Class<? extends IAction<?>>, Class<? extends IActionHandler<?, ?>>>(100);
        handlers = new HashMap<Class, IActionHandler<?, ?>>(100);
    }


    public <A extends IAction<R>, R extends IResult> void addHandlerClass(Class<A> actionClass, Class<? extends IActionHandler<A, R>> handlerClass) {
    	handlerClasses.put(actionClass, handlerClass);
    }

    @SuppressWarnings("unchecked")
	public <A extends IAction<R>, R extends IResult> IActionHandler<A, R> findHandler(A action) {
        IActionHandler<?, ?> handler = handlers.get(action.getClass());
        if (handler == null) {
            final Class<? extends IActionHandler<?, ?>> handlerClass = handlerClasses.get(action.getClass());
            if (handlerClass != null) {
                handler = injector.getInstance(handlerClass);
                if (handler != null)
                	handlers.put(action.getClass(), handler);
            }
        }
        return (IActionHandler<A, R>) handler;
    }

}
