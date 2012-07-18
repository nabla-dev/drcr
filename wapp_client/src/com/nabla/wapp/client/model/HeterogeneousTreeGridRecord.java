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
package com.nabla.wapp.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.nabla.wapp.client.general.JSHelper;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.model.field.TreeStringIdField;
import com.nabla.wapp.client.model.field.TreeStringParentIdField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * @author FNorais
 *
 */
public class HeterogeneousTreeGridRecord extends TreeNode {

	public HeterogeneousTreeGridRecord(final Record impl) {
		super(impl.getJsObj());
	}

	public HeterogeneousTreeGridRecord(final JavaScriptObject js) {
		super(js);
	}

	public Integer getId() {
		return TreeStringIdField.extractId(this);
	}

	public Integer getParentId() {
		return TreeStringParentIdField.extractId(this);
	}

	public boolean isNew() {
		return !JSHelper.isAttribute(getJsObj(), IdField.NAME);
	}

	protected Boolean getBoolean(final String attribute) {
		return JSHelper.getAttributeAsBoolean(this.getJsObj(), attribute);
	}
}
