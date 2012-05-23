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


/**
 * @author nabla
 *
 */
public class TileGridImageField extends TileGridField {

	public TileGridImageField() {
		impl.setType("image");
	}
	
	@Override
	public void setWidth(final String value) {
		try {
			Integer w = Integer.valueOf(value);
			impl.setImageWidth(w);
		} catch (NumberFormatException __) {
			impl.setAttribute("imageWidth", value);	
		}
	}
	
	public Integer getImageWidth() {
		return impl.getImageWidth();
	}
	
	@Override
	public void setHeight(final String value) {
		try {
			Integer h = Integer.valueOf(value);
			impl.setImageHeight(h);
		} catch (NumberFormatException __) {
			impl.setAttribute("imageHeight", value);	
		}
	}

	public Integer getImageHeight() {
		return impl.getImageHeight();
	}
	
}