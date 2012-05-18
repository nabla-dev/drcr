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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.internal.UniqueAnnotations;
import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IResult;

/**
 * @author nabla
 *
 */
public abstract class AbstractHandlerModule extends AbstractModule {

	private static class ActionHandlerMap<A extends IAction<R>, R extends IResult> implements IActionHandlerMap<A, R> {

		private final Class<A>								actionClass;
	    private final Class<? extends IActionHandler<A, R>>	handlerClass;

	    @SuppressWarnings("unchecked")
		public ActionHandlerMap(Class<? extends IActionHandler<A, R>> handlerClass) {
	        this.actionClass = Util.getFirstGenericDeclaration(handlerClass);
	        this.handlerClass = handlerClass;
	    }

	    @Override
		public Class<A> getActionClass() {
	        return actionClass;
	    }

	    @Override
		public Class<? extends IActionHandler<A, R>> getActionHandlerClass() {
	        return handlerClass;
	    }

	}

	@Override
	protected void configure() {
		bind(ActionHandlerRegister.class).in(Singleton.class);
        requestStaticInjection(ActionHandlerRegisterInitializer.class);
	}

	protected <A extends IAction<R>, R extends IResult> void bindHandler(Class<? extends IActionHandler<A, R>> handlerClass ) {
		bind(IActionHandlerMap.class).annotatedWith(UniqueAnnotations.create()).toInstance(new ActionHandlerMap<A, R>(handlerClass));
	}

}
