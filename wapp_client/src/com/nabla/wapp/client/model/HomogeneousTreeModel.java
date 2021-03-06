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

import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.tree.TreeNode;

public abstract class HomogeneousTreeModel<R extends TreeNode> extends CModel<R> {

	protected HomogeneousTreeModel(final IRecordFactory<R> recordFactory) {
		super(recordFactory);
	}

	protected HomogeneousTreeModel() {
		super();
	}

	public Integer getParentId(final DSRequest request) {
		return JSOHelper.getAttributeAsInt(request.getData(), IFieldReservedNames.TREEGRID_PARENT_ID);
	}
}
