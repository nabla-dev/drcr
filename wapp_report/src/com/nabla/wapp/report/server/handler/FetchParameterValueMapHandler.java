/**
* Copyright 2013 nabla
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
package com.nabla.wapp.report.server.handler;

import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.nabla.wapp.report.server.ReportManager;
import com.nabla.wapp.report.server.ReportTemplate;
import com.nabla.wapp.report.server.SelectionValue;
import com.nabla.wapp.report.shared.command.FetchParameterValueMap;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.json.JsonResponse;
import com.nabla.wapp.server.model.AbstractFetchHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;


public class FetchParameterValueMapHandler extends AbstractFetchHandler<FetchParameterValueMap> {

	private final ReportManager	reportManager;

	@Inject
	public FetchParameterValueMapHandler(final ReportManager reportManager) {
		super();
		this.reportManager = reportManager;
	}

	@Override
	public FetchResult execute(final FetchParameterValueMap cmd, final IUserSessionContext ctx) throws DispatchException, SQLException {
		List<SelectionValue> valueMap = getValueMap(cmd, ctx);
		final JsonResponse response = new JsonResponse();
		Integer startRow = null;
		Integer endRow = null;
		int totalRows = valueMap.size();
		if (totalRows > 0) {
			if (cmd.isRange()) {
				if (cmd.getStartRow() < totalRows) {
					startRow = cmd.getStartRow();
					endRow = startRow;
					while (endRow < cmd.getEndRow() && endRow < totalRows) {
						SelectionValue value = valueMap.get(endRow);
						response.put(value.getValue().toString(), value.getPrompt());
					}
				}
			} else {
				for (SelectionValue value : valueMap)
					response.put(value.getValue().toString(), value.getPrompt());
				startRow = 0;
				endRow = totalRows;
			}
		}
		return new FetchResult(startRow, endRow, totalRows, response.toJSONString());
	}

	private List<SelectionValue> getValueMap(final FetchParameterValueMap cmd, final IUserSessionContext ctx) throws SQLException, DispatchException {
		final ReportTemplate template = reportManager.open(cmd.getReportId(), ctx);
		return template.getParameterValueMap(cmd.getParameter(), cmd.getParameterValues().toMap());
	}
}
