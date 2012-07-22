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

import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.ui.HLayout;
import com.nabla.wapp.client.ui.HLayoutSpacer;
import com.nabla.wapp.client.ui.IHasWidgets;
import com.nabla.wapp.client.ui.Resource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.fields.CanvasItem;

/**
 * @author nabla
 *
 */
public class ButtonBar extends UiBinderFormItemSpeudoWidgetList<CanvasItem> implements IHasWidgets {

	protected final HLayout	buttons = new HLayout();

	public ButtonBar() {
		super(new CanvasItem());
		setColSpan(2);
		impl.setAlign(Alignment.RIGHT);
		setShowTitle(false);
		buttons.setAutoHeight();
		buttons.setAutoWidth();
		setDefaultMargin(Resource.bundle.style().DIALOG_DEFAULT_SPACING());
		impl.setCanvas(buttons);
		impl.setCanFocus(true);
	}

	public void setDefaultMargin(final int margin) {
		buttons.setLayoutTopMargin(margin);
		buttons.setMembersMargin(margin);
	}

	@Override
	public void add(final Widget w) {
		Assert.argumentNotNull(w);

		if (w instanceof IButton || w instanceof HLayoutSpacer)
			buttons.add(w);
		else
			super.add(w);
	}

	@Override
	public <T> T findChild(final Class<T> type, final boolean recursive) {
		return IHasWidgets.Helper.findChild(buttons.getMembers(), type, recursive);
	}

}
