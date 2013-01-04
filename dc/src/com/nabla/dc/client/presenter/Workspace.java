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
package com.nabla.dc.client.presenter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.nabla.dc.client.ui.WorkspaceUi;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.mvp.AbstractCanvasPresenter;
import com.nabla.wapp.client.mvp.AbstractTabPresenter;
import com.nabla.wapp.client.mvp.ICanvasDisplay;
import com.nabla.wapp.client.mvp.ITabDisplay;
import com.nabla.wapp.client.mvp.TabManager;
import com.nabla.wapp.client.server.UserSession;
import com.nabla.wapp.shared.slot.ISlot2;
import com.nabla.wapp.shared.slot.ISlotManager;
import com.nabla.wapp.shared.slot.ISlotManager1;

/**
 * @author nabla
 *
 */
public class Workspace extends AbstractCanvasPresenter<Workspace.IDisplay> implements ITabManager {

	public interface IDisplay extends ICanvasDisplay {
		ISlotManager getLogoutSlots();
		void setUserName(final String userName);
		ISlotManager1<ITabDisplay> getTabClosedSlots();
		void addTab(final ITabDisplay tab);
	}

	private final TabManager	tabs = new TabManager();

	public Workspace(final IDisplay ui) {
		super(ui);
	}

	public Workspace(final String userName) {
		this(new WorkspaceUi(userName));
	}

	ISlotManager getLogoutSlots() {
		return getDisplay().getLogoutSlots();
	}

	@Override
	public void bind() {
		super.bind();
		Application.getInstance().getUserSessionManager().getUserSessionChangedSlots().connect(onUserSessionChanged);
		registerHandler(getDisplay().getTabClosedSlots().connect(tabs.getTabClosedSlot()));

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				addTab(new UserCompanyList(Workspace.this));
			//	addTab(new ReportList());
			}
		});
	}

	@Override
	public void unbind() {
		tabs.clear();
		super.unbind();
	}

	@Override
	public <D extends ITabDisplay> void addTab(final AbstractTabPresenter<D> tab) {
		getDisplay().addTab(tabs.add(tab));
	}

	private final ISlot2<UserSession, UserSession> onUserSessionChanged = new ISlot2<UserSession, UserSession>() {
		@Override
		public void invoke(final UserSession oldSession, final UserSession newSession) {
			if (newSession != null && (oldSession == null || oldSession.getName().equals(newSession.getName())))
				getDisplay().setUserName(newSession.getName());
		}
	};

}
