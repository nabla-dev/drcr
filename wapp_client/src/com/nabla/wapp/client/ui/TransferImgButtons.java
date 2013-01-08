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


public enum TransferImgButtons {
	LEFT { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.LEFT; } },
	LEFT_ALL { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.LEFT_ALL; } },
	RIGHT { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.RIGHT; } },
	RIGHT_ALL { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.RIGHT_ALL; } },
	UP { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.UP; } },
	UP_FIRST { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.UP_FIRST; } },
	DOWN { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.DOWN; } },
	DOWN_LAST { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.DOWN_LAST; } },
	DELETE { @Override public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert() { return com.smartgwt.client.widgets.TransferImgButton.DELETE; } };

	abstract public com.smartgwt.client.widgets.TransferImgButton.TransferImg convert();

}
