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

import com.google.gwt.user.client.ui.HasText;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.fields.FormItem;


public class GroupBox extends CanvasItem implements HasText {

	private final Form	impl = new Form();

	public GroupBox() {
		impl.setIsGroup(true);
		setShowTitle(false);
		setCanvas(impl);
	}

	public void setFields(final FormItem... fields) {
		impl.setFields(fields);
	}

	@Override
	public String getText() {
		return impl.getGroupTitle();
	}

	@Override
	public void setText(final String text) {
		impl.setGroupTitle(text);
	}

	@Override
	public String getTitle() {
		return getText();
	}

	@Override
	public void setTitle(final String title) {
		setText(title);
	}

}
