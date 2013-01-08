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
import com.nabla.wapp.client.ui.TitleDecoder;


public class UploadEditBox extends UiBinderFormItemSpeudoWidget<UploadItemImpl> implements HasText {

	public UploadEditBox() {
		super(new UploadItemImpl(), true);
	}

	@Override
	public String getText() {
		return impl.getTitle();
	}

	@Override
	public void setText(String text) {
		new TitleDecoder(text).apply(impl);
	}

	public void setValidFileExtensions(final String extensions) {
		impl.setValidFileExtensions(extensions);
	}

	public void setMaximumFiles(int max) {
		if (max < 1)
			max = 1;
		impl.setMaximumFiles(max);
		// compute widget height auto according to maximum number of files
		// 1->30
		// 2->50
		// n->30+20*(n-1)
		final Integer h = 30 + 20 * (max - 1);
		setHeight(h.toString());
	}

	public void cleanup() {
		impl.cleanup();
	}

}
