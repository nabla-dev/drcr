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
package com.nabla.dc.server.handler.fixed_asset;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Validate;

import com.nabla.fixed_assets.shared.DisposalTypes;
import com.nabla.fixed_assets.shared.IPrivileges;
import com.nabla.fixed_assets.shared.TransactionClasses;
import com.nabla.fixed_assets.shared.TransactionTypes;
import com.nabla.fixed_assets.shared.command.UpdateAssetDisposal;
import com.nabla.fixed_assets.shared.model.IAsset;
import com.nabla.wapp.server.auth.IUserSessionContext;
import com.nabla.wapp.server.basic.general.UserPreference;
import com.nabla.wapp.server.database.ConnectionTransactionGuard;
import com.nabla.wapp.server.database.Database;
import com.nabla.wapp.shared.database.IRecordField;
import com.nabla.wapp.shared.database.IRecordTable;
import com.nabla.wapp.server.database.StatementFormat;
import com.nabla.wapp.server.database.UpdateStatement;
import com.nabla.wapp.server.model.AbstractOperationHandler;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.InternalErrorException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.model.ValidationException;

/**
 * @author nabla
 *
 */
public class UpdateAssetDisposalHandler extends AbstractOperationHandler<UpdateAssetDisposal, UpdateAssetDisposalHandler.Record> {

	@Root(name="data")
	@IRecordTable(name="asset")
	public static class Record {

		@Element
		@IRecordField(id=true)
		public Integer			id;
		@Element
		public Date				disposal_date;
		@Element
		@IRecordField
		public DisposalTypes	disposal_type;
		@Element(required=false)
		@IRecordField
		public Integer			proceeds;

		private Connection	conn;

		@Validate
		public void validate() throws DispatchException {
			switch (disposal_type) {
			case SOLD:
				if (proceeds == null)
					proceeds = 0;
				else if (proceeds < 0)
					throw new ValidationException(IAsset.PROCEEDS, CommonServerErrors.INVALID_VALUE);
				break;
			default:
				proceeds = 0;
				break;
			}
		}

		public void setConnection(final Connection conn) {
			this.conn = conn;
		}

		public Date getOldDisposalDate() throws SQLException {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT date FROM transaction WHERE asset_id=? AND class='COST' AND type='CLOSING';", id);
			try {
				final ResultSet rs = stmt.executeQuery();
				return rs.next() ? rs.getDate(1) : null;
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}

		public void revertOldDisposal() throws SQLException {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT command FROM redo WHERE asset_id=?;", id);
			try {
				final Statement redo = conn.createStatement();
				try {
					final ResultSet rs = stmt.executeQuery();
					while (rs.next())
						redo.execute(rs.getString(1));
				} finally {
					try { redo.close(); } catch (final SQLException e) {}
				}
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
			Database.executeUpdate(conn, "DELETE FROM redo WHERE asset_id=?;", id);
		}

		public int getCost() throws SQLException {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM transaction WHERE asset_id=? AND class='COST';", id);
			try {
				final ResultSet rs = stmt.executeQuery();
				return rs.next() ? rs.getInt(1) : 0;
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}

		public int getDepreciation() throws SQLException {
			final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT SUM(amount) FROM transaction WHERE asset_id=? AND class='DEP';", id);
			try {
				final ResultSet rs = stmt.executeQuery();
				return rs.next() ? rs.getInt(1) : 0;
			} finally {
				try { stmt.close(); } catch (final SQLException e) {}
			}
		}

		public String addDisposalTransaction(int amount, TransactionClasses clazz, TransactionTypes type) throws SQLException {
			return MessageFormat.format("DELETE FROM transaction WHERE id={0,number,0};",
					Database.addRecord(conn,
"INSERT INTO transaction (asset_id,date,amount,class,type) VALUES(?,?,?,?,?)",
				id, disposal_date, amount, clazz.toString(), type.toString()));
		}

		public void dispose() throws SQLException, DispatchException {
			final PreparedStatement redo = conn.prepareStatement(
"INSERT INTO redo (asset_id, command) VALUES(?,?);");
			try {
				redo.setInt(1, id);
				// backup transaction after disposal if any
				if (log.isDebugEnabled())
					log.debug("backing up transactions after disposal");
				final PreparedStatement stmt = StatementFormat.prepare(conn,
"SELECT * FROM transaction WHERE asset_id=? AND date>=?;", id, disposal_date);
				try {
					final ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						final String command = MessageFormat.format(
"INSERT INTO transaction (id,asset_id,date,amount,class,type,dep_period) VALUES({0,number,0},{1,number,0},''{2,date,yyyy-MM-dd}'',{3,number,0},''{4}'',''{5}'',{6,number,0});",
								rs.getInt("id"),
								id,
								rs.getDate("date"),
								rs.getInt("amount"),
								rs.getString("class"),
								rs.getString("type"),
								Database.getInteger(rs, "dep_period"));
						if (log.isTraceEnabled())
							log.trace("redo = " + command);
						redo.setString(2, command);
						redo.addBatch();
					}
				} finally {
					try { stmt.close(); } catch (final SQLException e) {}
				}
				// remove any transaction after disposal date
				if (log.isDebugEnabled())
					log.debug("removing transactions after disposal");
				Database.executeUpdate(conn,
"DELETE FROM transaction WHERE asset_id=? AND date>=?;", id, disposal_date);
				// add disposal transactions
				if (log.isDebugEnabled())
					log.debug("removing transactions after disposal");
				redo.setString(2, addDisposalTransaction(-1 * getCost(), TransactionClasses.COST, TransactionTypes.CLOSING));
				redo.addBatch();
				redo.setString(2, addDisposalTransaction(-1 * getDepreciation(), TransactionClasses.DEP, TransactionTypes.CLOSING));
				redo.addBatch();
				if (!Database.isBatchCompleted(redo.executeBatch())) {
					if (log.isDebugEnabled())
						log.debug("fail to save disposal transactions");
					throw new InternalErrorException("failed to add disposal transactions");
				}
			} finally {
				try { redo.close(); } catch (final SQLException e) {}
			}
		}
	}

	private static final Log						log = LogFactory.getLog(UpdateAssetDisposalHandler.class);
	private static final UpdateStatement<Record>	sql = new UpdateStatement<Record>(Record.class);

	public UpdateAssetDisposalHandler() {
		super(true, IPrivileges.ASSET_EDIT);
	}

	@Override
	protected String execute(final Record asset, final IUserSessionContext ctx) throws DispatchException, SQLException {
		asset.setConnection(ctx.getWriteConnection());
		final Date oldDisposalDate = asset.getOldDisposalDate();
		final ConnectionTransactionGuard guard = new ConnectionTransactionGuard(ctx.getWriteConnection());
		try {
			// update asset record
			sql.execute(ctx.getWriteConnection(), asset);
			if (oldDisposalDate != null && asset.disposal_date != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("reverting old disposal data");
				asset.revertOldDisposal();
			}
			if (oldDisposalDate == null || asset.disposal_date != oldDisposalDate) {
				if (log.isDebugEnabled())
					log.debug("disposing asset...");
				asset.dispose();
				UserPreference.save(ctx, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_date", asset.disposal_date);
				UserPreference.save(ctx, IAsset.DISPOSAL_PREFERENCE_GROUP, "disposal_type", asset.disposal_type);
			}
			guard.setSuccess();
		} finally {
			guard.close();
		}
		// in case value is modified here, tell client side so that it will be displayed correctly in listgrid
		return "<data><proceeds>" + asset.proceeds.toString() + "</proceeds></data>";
	}

}
