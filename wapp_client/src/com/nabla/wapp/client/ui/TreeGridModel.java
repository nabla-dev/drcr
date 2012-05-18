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
package com.nabla.wapp.client.ui;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.ui.IHasWidgets.Helper;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * The <code>TreeGridModel</code> object is used to
 *
 */
public class TreeGridModel extends UiBinderSpeudoWidgetList implements IPostCreateProcessing {

	private final Tree		data = new Tree();
	private List<TreeNode>	nodes = new LinkedList<TreeNode>();

	public Tree getImpl() {
		return data;
	}

	@Override
	public void add(final Widget w) {
		Helper.onCreate(w);

		if (w instanceof ITreeGridRecord)
			nodes.add(((ITreeGridRecord)w).getImpl());
		else
			super.add(w);
	}

	public void setType(TreeModelType type) {
		data.setModelType(type);
	}

	public void setAutoOpenRoot(Boolean autoOpenRoot) {
		data.setAutoOpenRoot(autoOpenRoot);
	}

	public void setShowRoot(Boolean showRoot) {
		data.setShowRoot(showRoot);
	}

	@Override
	public void onCreate() {
		data.setData(nodes.toArray(new TreeNode[0]));
		nodes = null;
	}
}
