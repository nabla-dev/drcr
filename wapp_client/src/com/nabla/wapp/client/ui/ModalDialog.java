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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;

/**
 * @author nabla
 *
 */
public class ModalDialog extends Dialog {

	private static final Logger					log = LoggerFactory.getLog(ModalDialog.class);
	private static final Map<Class, ModalDialog>	openedDialogs = new HashMap<Class, ModalDialog>();

	public ModalDialog() {
		super();
		this.setShowShadow(true);
	//	this.setAutoSize(true);
		this.setCanDragReposition(true);
		this.setCanDragResize(true);
		this.setAutoCenter(true);
		this.setIsModal(true);
	}

	@Override
	public void show(){
		final Class clazz = this.getClass();
		if (openedDialogs.containsKey(clazz)) {
			log.warning("stopped opening dialog box '" + this.getTitle() + "' - class = '" + clazz.getName() + "'");
		} else {
			super.show();
			openedDialogs.put(clazz, this);
		}
	}

	@Override
	public void hide() {
		openedDialogs.remove(this.getClass());
		super.hide();
	}

	public static void hideAll() {
		for (ModalDialog dlg : openedDialogs.values())
			dlg.hide();
	}
}
