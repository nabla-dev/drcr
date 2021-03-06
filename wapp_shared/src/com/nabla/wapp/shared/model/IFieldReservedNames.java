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
package com.nabla.wapp.shared.model;


public interface IFieldReservedNames {

	public static final String	TREEGRID_PARENT_ID = "parentId";	// == Tree.getParentIdField
	public static final String	TREEGRID_IS_FOLDER = "folder";	// == Tree.getIsFolderProperty

	public static final String	RECORD_ENABLED = "enabled";	// == ListGrid.getRecordEnabledProperty
	public static final String	RECORD_SELECTED = "selected";	//
	public static final String	RECORD_CAN_SELECT = "canSelect";	//

	public static final String	RECORD_DELETED = "deleted";

	public static final String	DEFAULT_VALUE = "defaultValue";

}
