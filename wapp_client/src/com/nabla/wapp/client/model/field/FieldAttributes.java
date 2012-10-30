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
package com.nabla.wapp.client.model.field;

import com.nabla.wapp.client.general.JSHelper;
import com.nabla.wapp.shared.general.Nullable;
import com.smartgwt.client.data.DataSourceField;

/**
 * @author nabla
 *
 */
public enum FieldAttributes {

	HIDDEN { @Override void apply(final DataSourceField field) { field.setHidden(true); } },
	VISIBLE { @Override void apply(final DataSourceField field) { field.setHidden(false); } },
	REQUIRED { @Override void apply(final DataSourceField field) { field.setRequired(true); } },
	OPTIONAL { @Override void apply(final DataSourceField field) { field.setRequired(false); } },
	CAN_EDIT { @Override void apply(final DataSourceField field) { field.setCanEdit(true); } },
	READ_ONLY { @Override void apply(final DataSourceField field) { field.setCanEdit(false); field.setCanSave(false); } },
	CAN_FILTER { @Override void apply(final DataSourceField field) { field.setCanFilter(true); } },
	CANNOT_FILTER { @Override void apply(final DataSourceField field) { field.setCanFilter(false); } },
	CAN_SAVE { @Override void apply(final DataSourceField field) { field.setCanSave(true); } },
	CAN_EXPORT { @Override void apply(final DataSourceField field) { field.setCanExport(true); } },
	IGNORE { @Override void apply(final DataSourceField field) { field.setIgnore(true); } },
	PRIMARY_KEY { @Override void apply(final DataSourceField field) { field.setPrimaryKey(true); } };

	abstract void apply(final DataSourceField field);

	public static void applyAll(final DataSourceField field, @Nullable final FieldAttributes... attributes) {
		if (attributes != null) {
			for (final FieldAttributes attribute : attributes)
				attribute.apply(field);
		}
	}

	/**
     * Get model field attribute value
     *
     * @param field	- model field
     * @param attribute - name of attribute
     * @return null or value
     */
	public static @Nullable Boolean getAttribute(final DataSourceField field, final String attribute) {
		return JSHelper.getAttributeAsBoolean(field.getJsObj(), attribute);
	}

	/**
     *  Whether this field should be hidden from users
     *
     * @param field	- model field
     * @return null or value
     */
	public static @Nullable Boolean getHidden(final DataSourceField field) {
		return getAttribute(field, "hidden");
	}

	/**
     *  Whether this field is required
     *
     * @param field	- model field
     * @return null or value
     */
	public static @Nullable Boolean getRequired(final DataSourceField field) {
		return getAttribute(field, "required");
	}

	/**
     *  Whether this field can update or saved
     *
     * @param field	- model field
     * @return null or value
     */
	public static @Nullable Boolean getCanSave(final DataSourceField field) {
		return getAttribute(field, "canSave");
	}

	/**
     *  Whether this field can be edited
     *
     * @param field	- model field
     * @return null or value
     */
	public static @Nullable Boolean getCanEdit(final DataSourceField field) {
		return getAttribute(field, "canEdit");
	}


	/**
     *  Whether this field is read only
     *
     * @param field	- model field
     * @return value
     */
	public static boolean getIsReadOnly(final DataSourceField field) {
		Boolean canEdit = getCanEdit(field);
		if (canEdit == null)
			canEdit = true;
		Boolean canSave = getCanSave(field);
		if (canSave == null)
			canSave = true;
		return !canEdit && !canSave;
	}
}
