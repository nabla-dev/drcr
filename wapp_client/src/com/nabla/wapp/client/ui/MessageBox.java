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

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.AsyncProvider;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Command;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;


public class MessageBox implements IMessageBox {

	public interface IDetailedMessageBoxProvider extends AsyncProvider<IDetailedMessageBox, Throwable> {}

	private static final Logger				logger = LoggerFactory.getLog(MessageBox.class);
	private String								defaultTitle;
	private final IDetailedMessageBoxProvider	detailedDialogFactory;

	public MessageBox(final String defaultTitle, final IDetailedMessageBoxProvider detailedDialogFactory) {
		setDefaultTitle(defaultTitle);
		this.detailedDialogFactory = detailedDialogFactory;
	}

	@Override
	public void setDefaultTitle(final String title) {
		defaultTitle = title;
	}

	@Override
	public String getDefaultTitle() {
		return defaultTitle;
	}

	@Override
	public void info(final String message) {
		info(defaultTitle, message);
	}

	@Override
	public void info(final String title, final String message) {
		SC.say(title, formatMessage(message));
	}

	@Override
	public void error(final String message) {
		error(defaultTitle, message);
	}

	@Override
	public <E extends Enum<E>> void error(final E error) {
		error(Application.getInstance().getLocalizedError(error));
	}

	@Override
	public void error(final String title, final String message) {
		/*SC.*/warn(title, formatMessage(message)/*, new BooleanCallback() {
			@Override
			public void execute(@SuppressWarnings("unused") Boolean __) {
			}
		}, new com.smartgwt.client.widgets.Dialog()*/);
	}

	// until SmartGWT SC support such function
	private static native void warn(String title, String message) /*-{
     $wnd.isc.warn(message, {title:title});
	}-*/;

	@Override
	public void confirm(final String message, final BooleanCallback callback) {
		confirm(defaultTitle, message, callback);
	}

	@Override
	public void confirm(final String title, final String message, final BooleanCallback callback) {
		SC.confirm(title, formatMessage(message), callback);
	}

	@Override
	public void confirm(final String message, final Command callback) {
		confirm(defaultTitle, message, callback);
	}

	@Override
	public void confirm(final String title, final String message, final Command callback) {
		confirm(title, message, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value)
					callback.execute();
			}
		});
	}

	@Override
	public void ask(final String message, final BooleanCallback callback) {
		ask(defaultTitle, message, callback);
	}

	@Override
	public void ask(final String title, final String message, final BooleanCallback callback) {
		SC.ask(title, formatMessage(message), callback);
	}

	@Override
	public void ask(final String message, final Command callback) {
		ask(defaultTitle, message, callback);
	}

	@Override
	public void ask(final String title, final String message, final Command callback) {
		ask(title, message, new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				if (value)
					callback.execute();
			}
		});
	}

	@Override
	public void prompt(final String message, final ValueCallback callback) {
		prompt(defaultTitle, message, callback);
	}

	@Override
	public void prompt(final String title, final String message, final ValueCallback callback) {
		SC.askforValue(title, formatMessage(message), callback);
	}

	@Override
	public void error(final Throwable caught) {
		if (caught == null)
			error(CommonServerErrors.INTERNAL_ERROR);
		else {
			if (caught instanceof ValidationException) {
				final ValidationException e = (ValidationException) caught;
				final Map<String, String> errors = e.getErrors();
				String message = null;
				for (Map.Entry<String, String> error : errors.entrySet()) {
					final String msg = error.getKey() + ": " +  Application.getInstance().getLocalizedError(error.getValue());
					if (message == null)
						message = msg;
					else
						message += "\n" + msg;
				}
				if (message == null)
					error(CommonServerErrors.INTERNAL_ERROR);
				else
					error(message);
			} else if (caught instanceof DispatchException) {
				final DispatchException e = (DispatchException) caught;
				logger.info("message = " + e.getMessage());
				final String message = Application.getInstance().getLocalizedError(e.getMessage());
				logger.info("user message = " + message);
				final String details = e.getDetails();
				logger.info("details = " + details);
				if (details != null && !details.isEmpty()) {
					detailedDialogFactory.get(new Callback<IDetailedMessageBox, Throwable>() {
						@Override
						public void onFailure(@SuppressWarnings("unused") Throwable __) {
							error(message);
						}

						@Override
						public void onSuccess(IDetailedMessageBox dlg) {
							dlg.setMessage(message);
							dlg.setDetails(details);
							dlg.revealDisplay();
						}
					});
				} else
					error(message);
			} else {
				logger.log(Level.WARNING, "unsupported error", caught);
				error(CommonServerErrors.INTERNAL_ERROR);
			}
		}
	}

	static protected String formatMessage(final String message) {
		logger.finer("message='" + message + "'");
		if (message != null && !message.isEmpty() && Character.isLetterOrDigit(message.charAt(message.length() - 1))) {
			// add final dot if missing
			final String ret = message + ".";
			return ret;
		}
		return message;
	}

}
