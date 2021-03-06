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
package com.nabla.wapp.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nabla.wapp.client.general.Application;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.command.AbstractRemove;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.FetchResult;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.dispatch.VoidResult;
import com.nabla.wapp.shared.general.ArgumentList;
import com.nabla.wapp.shared.model.ValidationException;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public abstract class CModel<R extends Record> extends Model {

	private static final Logger						log = LoggerFactory.getLog(CModel.class);
//	private static final IFilterOperatorSqlResource	operatorSql = GWT.create(IFilterOperatorSqlResource.class);
	private final IRecordFactory<R>					recordFactory;

	protected CModel(final IRecordFactory<R> recordFactory) {
		super();
		this.recordFactory = recordFactory;
		this.setDispatcher(Application.getInstance().getDispatcher());
	}

	protected CModel() {
		super();
		this.recordFactory = null;
		this.setDispatcher(Application.getInstance().getDispatcher());
	}

	public AbstractRemove getRemoveCommand() {
		return null;
	}

	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return null;
	}

	public IRecordAction<StringResult> getAddCommand(@SuppressWarnings("unused") final R record) {
		return null;
	}

	public IRecordAction<StringResult> getUpdateCommand(@SuppressWarnings("unused") final R record) {
		return null;
	}

	public AbstractFetch getFetchRecordCommand(@SuppressWarnings("unused") final Set<Integer> ids) {
		return null;
	}

	public IRecordFactory<R> getRecordFactory() {
		return recordFactory;
	}

	@Override
	protected Object transformRequest(final DSRequest request) {
		Assert.argumentNotNull(request);

		log.fine(request.getOperationType().toString() + " request ID = '" + request.getRequestId() + "'");
		if (this.getClientOnly()) {
			if (clientOnlyData != null && clientOnlyData.length > 0) {
				log.fine("spoofed response for client only");
				onResponse(request, getClientOnlyResponse(request, clientOnlyData));
				return request.getData();
			}
		}
		try {
			switch (request.getOperationType()) {
				case FETCH:
					onFetch(request);
					break;
				case UPDATE:
					onUpdate(request);
					break;
				case ADD:
					onAdd(request);
					break;
				case REMOVE:
					onRemove(request);
					break;
				default:
					break;
			}
		} catch (final Throwable caught) {
			log.log(Level.SEVERE,"error while dispatching " + request.getOperationType().toString() + " request", caught);
			onFailure(request, caught);
		}
		return request.getData();
	}

	private void onFetch(final DSRequest request) {
		AbstractFetch cmd = getFetchCommand(request);
		if (cmd == null) {
			log.warning("unimplemented data source operation '" + request.getOperationType().toString() + "'");
			onResponse(request, new Response(request, Response.STATUS_SUCCESS));
		} else {
			cmd.setRange(request.getStartRow(), request.getEndRow());
			SortSpecifier[] sort = request.getSortBy();
			if (sort != null && sort.length > 0) {
				final ArgumentList sortClause = new ArgumentList();
				for (SortSpecifier column : sort)
					sortClause.add(column.getField() + " " + (column.getSortDirection() == SortDirection.ASCENDING ? "ASC" : "DESC"));
				cmd.setOrderBy(sortClause.toString());
			}
			if (request.getCriteria() != null) {
			//	final Filter criteria = new Filter();
			//	criteria.addFilter(request.getCriteria(), this);
			//	cmd.setFilter(criteria.getTitle(operatorSql, false));
			}
			getDispatcher().execute(cmd, new AsyncCallback<FetchResult>() {

				@Override
				public void onFailure(final Throwable caught) {
					CModel.this.onFailure(request, caught);
				}

				@Override
				public void onSuccess(final FetchResult result) {
					Assert.argumentNotNull(result);
log.fine(request.getRequestId() + " response = \n" + result.getRecords());
					final Record[] records = recordsFromJson(result.getRecords());
					final Response response = new Response(request, records);
					if (result.isRange()) {
						response.setStartRow(result.getStartRow());
						response.setEndRow(result.getEndRow());
					}
					response.setTotalRows(result.getTotalRows());
					if (getClientOnly()) {
						log.fine("client only: store response for future request");
						clientOnlyData = records;
						onResponse(request, getClientOnlyResponse(request, clientOnlyData));
					} else
						onResponse(request, response);
				}
			});
		}
	}

	private void onUpdate(final DSRequest request) {
		if (recordFactory == null) {
			log.warning("no record factory provided for data source operation '" + request.getOperationType().toString() + "'");
			onResponse(request, new Response(request, Response.STATUS_SUCCESS));
		} else {
			final IRecordAction<StringResult> cmd = getUpdateCommand(recordFactory.get(request.getData()));
			if (cmd == null) {
				log.warning("unimplemented data source operation '" + request.getOperationType().toString() + "'");
				onResponse(request, new Response(request, Response.STATUS_SUCCESS));
			} else {
				final ValidationException errors = new ValidationException();
				try {
					if (cmd.validate(errors)) {
						getDispatcher().execute(cmd, new AsyncCallback<StringResult>() {

							@Override
							public void onFailure(final Throwable caught) {
								CModel.this.onFailure(request, caught);
							}

							@Override
							public void onSuccess(final StringResult result) {
								if (log.isLoggable(Level.FINE)) {
									if (result != null)
										log.fine(request.getRequestId() + " response = \n" + result.get());
								}
								final Record[] records = (result != null) ?  recordsFromJsonResponse(request, result.get()) : getEditedRecords(request);
								onResponse(request, new Response(request, records));
							}
						});
					} else
						onFailure(request, errors);
				} catch (final DispatchException x) {
					onFailure(request, x);
				}
			}
		}
	}

	private void onAdd(final DSRequest request) {
		Assert.notNull(recordFactory);

		final IRecordAction<StringResult> cmd = getAddCommand(recordFactory.get(request.getData()));
		if (cmd == null) {
			log.warning("unimplemented data source operation '" + request.getOperationType().toString() + "'");
			onResponse(request, new Response(request, Response.STATUS_SUCCESS));
		} else {
			final ValidationException errors = new ValidationException();
			try {
				if (cmd.validate(errors)) {
					getDispatcher().execute(cmd, new AsyncCallback<StringResult>() {
						@Override
						public void onSuccess(final StringResult result) {
							if (log.isLoggable(Level.FINE)) {
								if (result != null)
									log.fine(request.getRequestId() + " response = \n" + result.get());
							}
							final Record[] records = (result != null) ?  recordsFromJsonResponse(request, result.get()) : getEditedRecords(request);
							onResponse(request, new Response(request, records));
						}

						@Override
						public void onFailure(final Throwable caught) {
							CModel.this.onFailure(request, caught);
						}
					});
				} else
					onFailure(request, errors);
			} catch (final DispatchException x) {
				onFailure(request, x);
			}
		}
	}

	private void onRemove(final DSRequest request) {
		AbstractRemove cmd = getRemoveCommand();
		if (cmd == null) {
			log.warning("unimplemented data source operation '" + request.getOperationType().toString() + "'");
			onResponse(request, new Response(request, Response.STATUS_SUCCESS));
		} else {
			JavaScriptObject data = request.getData();
			Assert.notNull(data);
			DataSourceField pkey = getPrimaryKeyField();
			Assert.notNull(pkey);
			if (JSOHelper.isArray(data)) {
				final JavaScriptObject[] dataArray = JSOHelper.toArray(data);
				for (int i = 0; i < dataArray.length; ++i)
					cmd.add(JSOHelper.getAttributeAsInt(dataArray[i], pkey.getName()));
			} else
				cmd.add(JSOHelper.getAttributeAsInt(data, pkey.getName()));
			getDispatcher().execute(cmd, new AsyncCallback<VoidResult>() {

				@Override
				public void onFailure(final Throwable caught) {
					CModel.this.onFailure(request, caught);
				}

				@Override
				public void onSuccess(@SuppressWarnings("unused") final VoidResult __) {
					onResponse(request, new Response(request, getEditedRecords(request)));
				}

			});
		}
	}

	public static Record[] jsonToRecords(final String jsonRecords) {
		return ListGridRecord.convertToRecordArray(JSON.decode(jsonRecords));
	}

	protected Record[] recordsFromJsonResponse(final DSRequest request, final String jsonRecords) {
		if (jsonRecords == null)
			return getEditedRecords(request);
		return combineRecords(getEditedRecords(request), recordsFromJson(jsonRecords));
	}

	protected Record[] recordsFromJson(final String jsonRecords) {
		return jsonToRecords(jsonRecords);
	}

	public void updateCache(final Integer recordId, final UpdateModelCacheOperations operation) {
		final Set<Integer> ids = new HashSet<Integer>();
		ids.add(recordId);
		updateCache(ids, operation);
	}

	public void updateCache(final Integer recordId) {
		updateCache(recordId, UpdateModelCacheOperations.UPDATE);
	}

	public void updateCache(final Set<Integer> recordIds) {
		updateCache(recordIds, UpdateModelCacheOperations.UPDATE);
	}

	public void updateCache(final Set<Integer> recordIds, final UpdateModelCacheOperations operation) {
		Assert.argumentNotNull(recordIds);

		final AbstractFetch cmd = getFetchRecordCommand(recordIds);
		if (cmd == null) {
			log.warning("unimplemented data source operation 'refresh'");
		} else {
			getDispatcher().execute(cmd, new AsyncCallback<FetchResult>() {
				@Override
				public void onFailure(final Throwable caught) {
					log.log(Level.FINE, "failed to refresh records", caught);
				}

				@Override
				public void onSuccess(final FetchResult result) {
					if (result == null) {
						log.warning("'refresh' command returned 'null' string");
					} else {
						log.fine("refreshRecords - response = \n" + result.getRecords());
						updateCache(recordsFromJson(result.getRecords()), operation);
					}
				}
			});
		}
	}

	public void updateCache(final Record record, final UpdateModelCacheOperations operation) {
		Assert.argumentNotNull(record);

		final Record[] records = new Record[1];
		records[0] = record;
		updateCache(records, operation);
	}

	public void updateCache(final Record record) {
		updateCache(record, UpdateModelCacheOperations.UPDATE);
	}

	public void updateCache(final Record[] records, final UpdateModelCacheOperations operation) {
		Assert.argumentNotNull(records);

		final DSResponse response = new DSResponse();
		response.setStatus(DSResponse.STATUS_SUCCESS);
		response.setData(records);
		final DSRequest request = new DSRequest();
		request.setOperationType(operation.getDatasourceOperationType());
		updateCaches(response, request);
	}

	public void updateCache(final Record[] records) {
		updateCache(records, UpdateModelCacheOperations.UPDATE);
	}

}
