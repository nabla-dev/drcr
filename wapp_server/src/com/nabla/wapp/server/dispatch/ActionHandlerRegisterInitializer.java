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

import java.util.List;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

/**
 * The <code></code> object is used to
 *
 */
public class ActionHandlerRegisterInitializer {

	private ActionHandlerRegisterInitializer() {}

    @Inject
    @SuppressWarnings("unchecked")
    public static void linkHandlers(Injector injector, ActionHandlerRegister registry) {
        final List<Binding<IActionHandlerMap>> bindings = injector.findBindingsByType(TypeLiteral.get(IActionHandlerMap.class));
        for (Binding<IActionHandlerMap> binding : bindings) {
        	IActionHandlerMap map = binding.getProvider().get();
        	registry.addHandlerClass(map.getActionClass(), map.getActionHandlerClass());
        }
    }

}
