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

import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStatusChangedHandler;
import gwtupload.client.MultiUploader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.ui.Resource;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.Validator;


class UploadItemImpl extends CanvasItem implements OnStatusChangedHandler, OnFinishUploaderHandler {

	private static final Logger			logger = LoggerFactory.getLog(UploadItemImpl.class);
	private final Canvas				wrapper = new Canvas();
	private final MultiUploader			impl = new MultiUploader(FileInputType.LABEL);
	// field_name, server message=id
	private final Map<String, Integer>	values = new HashMap<String, Integer>();

	public UploadItemImpl() {
		impl.setMaximumFiles(1);
		impl.addOnStatusChangedHandler(this);
		impl.addOnFinishUploadHandler(this);

		wrapper.addChild(impl);

		this.setCanvas(wrapper);
		this.setShouldSaveValue(true);	// very important otherwise cannot get value to model  !!!!

		final Validator requiredValidator = new CustomValidator() {
			@Override
			protected boolean condition(@SuppressWarnings("unused") final Object value) {
				for (IUploader uploader : impl.getUploaders()) {
					switch (uploader.getStatus()) {
					case INPROGRESS:
					case CANCELING:
					case QUEUED:
					case SUBMITING:
						return false;
					default:
						break;
					}
				}
				return true;
			}
		};
		requiredValidator.setErrorMessage(Resource.strings.uploadInProgress());
		requiredValidator.setStopIfFalse(true);
		this.setValidators(requiredValidator);
	}

	public void setValidFileExtensions(final String extensions) {
		if (extensions != null)
			impl.setValidExtensions(extensions.split(","));
	}

	@Override
	public void setWidth(final String w) {
		super.setWidth(w);
		impl.setWidth("100%");
		wrapper.setWidth(w);
	}

	@Override
	public void setHeight(final String h) {
		super.setHeight(h);
		impl.setHeight("100%");
		wrapper.setHeight(h);
	}

	public void setMaximumFiles(int max) {
		impl.setMaximumFiles(max);
	}

	public void cleanup() {
		impl.cancel();
	}

	@Override
	public void onStatusChanged(IUploader uploader) {
		logger.fine(this.getName() + ": status changed to " + uploader.getStatus().toString());
		switch (uploader.getStatus()) {
		case DELETED:
			values.remove(uploader.getInputName());
			updateValue();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFinish(IUploader uploader) {
		if (uploader.getStatus() == Status.SUCCESS) {
			final IUploader.UploadedInfo info = uploader.getServerInfo();
			if (info != null && info.message != null && !info.message.isEmpty()) {
				values.put(uploader.getInputName(), Integer.valueOf(info.message));
				updateValue();
			}
		}
	}

	private void updateValue() {
		if (!values.isEmpty()) {
			if (impl.getMaximumFiles() > 1) {
				final Collection<Integer> ids = values.values();
				logger.fine(getName() + ": ids = " + ids.toString());
				getForm().setValue(getName(), JSOHelper.convertToJavaScriptArray(ids.toArray(new String[0])));
			} else {
				final Integer value = values.values().iterator().next();
				logger.fine(getName() + ": id = " + value);
				getForm().setValue(getName(), value);
			}
		} else
			getForm().clearValue(getName());
	}

}
