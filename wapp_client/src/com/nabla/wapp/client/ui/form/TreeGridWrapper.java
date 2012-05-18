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
package com.nabla.wapp.client.ui.form;

import com.nabla.wapp.client.ui.TitleDecoder;
import com.nabla.wapp.client.ui.TreeGrid;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.fields.CanvasItem;

/**
 * @author nabla
 *
 */
class TreeGridWrapper extends UiBinderFormItemSpeudoWidget<CanvasItem> {

	public TreeGridWrapper(final TreeGrid tree) {
		super(new CanvasItem(), true);
		impl.setName(tree.getName());
		impl.setShouldSaveValue(true);
		impl.setTitleOrientation(TitleOrientation.TOP);
		impl.setWidth(tree.getWidthAsString());
		impl.setHeight(tree.getHeightAsString());
		impl.setCanvas(tree);
		final String title = tree.getTitle();
		if (title != null)
			new TitleDecoder(title).apply(impl);
		impl.setColSpan(tree.getColSpan());
	}

}
