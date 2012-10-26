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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.nabla.wapp.client.command.ICommandUi;
import com.nabla.wapp.client.command.ICommandUiManager;
import com.nabla.wapp.client.general.Assert;
import com.nabla.wapp.client.general.LoggerFactory;
import com.nabla.wapp.client.general.Util;
import com.nabla.wapp.client.model.field.IdField;
import com.nabla.wapp.client.ui.IHasWidgets.Helper;
import com.nabla.wapp.shared.model.IFieldReservedNames;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;

/**
 * @author nabla
 *
 */
public class TreeGrid extends com.smartgwt.client.widgets.tree.TreeGrid implements HasWidgets, IPostCreateProcessing {

	private static final Logger	logger = LoggerFactory.getLog(TreeGrid.class);
	private List<TreeGridColumn>	children = new LinkedList<TreeGridColumn>();
	private Tree					defaultProperties;

	public TreeGrid() {
		setAlternateRecordStyles(true);
		setCanEdit(false);
		setShowOpenIcons(false);
		setShowDropIcons(false);
		setClosedIconSuffix("");
		setShowSelectedStyle(false);
		setFixedRecordHeights(true);
		// TODO: to be removed when SmartGWT supports it
		setAttribute("selectionProperty", IFieldReservedNames.RECORD_SELECTED, true);
		this.setRecordEnabledProperty(IFieldReservedNames.RECORD_ENABLED);
//		this.setRecordDeletedProperty(IFieldReservedNames.RECORD_DELETED);
		setIsFolderProperty(IFieldReservedNames.TREEGRID_IS_FOLDER);
	}

	public void setModel(final DataSource model) {
		setAutoFetchData(true);
		setDataSource(model);
	}

	// BUG in SmartGWT: lots of row height warning being displayed
	// DO NOT USE
	public void setShowCheckbox(final boolean show) {
		setSelectionAppearance(show ? SelectionAppearance.CHECKBOX : SelectionAppearance.ROW_STYLE);
	}

	public void setCommand(final ICommandUiManager cmd) {
		Assert.argumentNotNull(cmd);

		cmd.addUi(new ICommandUi() {
			@Override
			public void setVisible(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public void setText(@SuppressWarnings("unused") final String value) {
			}

			@Override
			public void setEnabled(final boolean value) {
				setCanEdit(value);
			}

			@Override
			public void setChecked(@SuppressWarnings("unused") final boolean value) {
			}

			@Override
			public boolean getEnabled() {
				return false;
			}
		});
	}

	public void setModelType(final TreeModelType type) {
		Tree tree = getTree();
		if (tree == null) {
			if (defaultProperties == null)
				defaultProperties = new Tree();
			defaultProperties.setModelType(type);
			setDataProperties(defaultProperties);
		} else
			tree.setModelType(type);
	}

	public void setIsFolderProperty(final String name) {
		Tree tree = getTree();
		if (tree == null) {
			if (defaultProperties == null)
				defaultProperties = new Tree();
			defaultProperties.setIsFolderProperty(name);
			setDataProperties(defaultProperties);
		} else
			tree.setIsFolderProperty(name);
	}

	@Override
	public void add(final Widget w) {
		Assert.notNull(children);

		Helper.onCreate(w);
		if (w instanceof TreeGridColumn)
			children.add((TreeGridColumn)w);
		else if (w instanceof TreeGridModel)
			this.setData(((TreeGridModel)w).getImpl());
		else {
			logger.log(Level.SEVERE,"adding a widget of type '" + w.getClass().toString() + "' to a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
		}
	}

	@Override
	public void clear() {
	}

	@Override
	public Iterator<Widget> iterator() {
        return null;
	}

	@Override
	public boolean remove(@SuppressWarnings("unused") final Widget w) {
		logger.log(Level.SEVERE,"removing children widget from a " + Util.getClassSimpleName(this.getClass()) + " is not supported");
        return false;
	}

	@Override
	public void onCreate() {
		Assert.notNull(children);

		final List<com.smartgwt.client.widgets.tree.TreeGridField> fields = new LinkedList<com.smartgwt.client.widgets.tree.TreeGridField>();
		int dataField = -1;
		for (int i = 0; i < children.size(); ++i) {
			final TreeGridColumn c = children.get(i);
			if (dataField == -1)
				dataField = i;
			else
				dataField = -2;
			fields.add(c.getField(this));
		}
		if (dataField >= 0) {
			// set following default because only 1 column of data
			final TreeGridColumn c = children.get(dataField);
			c.setCanFreeze(false);
			c.setCanGroupBy(false);
		}
		this.setFields(fields.toArray(new com.smartgwt.client.widgets.tree.TreeGridField[0]));
		children = null;
	}

	public void reload() {
		discardAllEdits();
		invalidateCache();
	}

	public List<Integer> getSelectedRecordIds() {
		final List<Integer> ids = new LinkedList<Integer>();
		final ListGridRecord[] records = getSelectedRecords();
		if (records != null && records.length > 0) {
			for (final ListGridRecord record : records)
				ids.add(record.getAttributeAsInt(IdField.NAME));
		}
		return ids;
	}

}
