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
import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;

/**
 * @author nabla
 *
 */
public class ModalDialog extends Dialog {

	private static final Logger				log = LoggerFactory.getLog(ModalDialog.class);
	private static final List<ModalDialog>	openedDialogs = new LinkedList<ModalDialog>();

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
		if (openedDialogs.isEmpty()) {
			super.show();
			openedDialogs.add(this);
		} else {
			log.warning("stopped opening dialog box '" + this.getTitle() + "'");
		}
	}

	@Override
	public void hide() {
		openedDialogs.remove(this);
		super.hide();
	}

	public static void hideAll() {
		while (!openedDialogs.isEmpty())
			openedDialogs.get(0).hide();
	}
}
