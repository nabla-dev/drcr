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
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.general.Assert;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;


public abstract class AbstractFormItemIcon extends Widget implements HasText {

	private String		name;
	protected Integer	width;
	protected Integer	height;
	private String		toolTip;
	
	@Override
	public void setWidth(String w) {
		width = Integer.valueOf(w);
	}
	
	@Override
	public void setHeight(String h) {
		height = Integer.valueOf(h);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getText() {
		return toolTip;
	}

	@Override
	public void setText(String text) {
		this.toolTip = text;
	}

	protected FormItemIcon initialize(final FormItemIcon impl) {
		Assert.argumentNotNull(impl);
		
		if (name != null)
			impl.setName(name);
		if (width != null)
			impl.setWidth(width);
		if (height != null)
			impl.setHeight(height);
		if (toolTip != null)
			impl.setPrompt(toolTip);
		return impl;
	}
	
}
