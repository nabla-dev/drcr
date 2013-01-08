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
package com.nabla.wapp.server.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class Role extends HashMap<Integer, Role> {

	private static final long	serialVersionUID = 1L;

	private final Integer		id;
	private final String		name;
	private Set<Integer>		definition;

	public Role(final Integer id, final String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Set<Integer> getDefinition() {
		if (definition == null) {
			definition = new HashSet<Integer>();
			definition.add(id);
			for (final Map.Entry<Integer, Role> e : this.entrySet())
				definition.addAll(e.getValue().getDefinition());
		}
		return definition;
	}

}
