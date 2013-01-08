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

import com.google.gwt.i18n.client.Constants;



public interface ITextResource extends Constants {

	@DefaultStringValue("* required information")
	String formRequiredInfoMessage();

	@DefaultStringValue("*")
	String requiredHintTag();

	@DefaultStringValue("Required field")
	String requiredFieldErrorMessage();

	@DefaultStringValue("Filter")
	String filterBuilderFilterButton();

	@DefaultStringValue("Clear")
	String filterBuilderClearButton();

	@DefaultStringValue("Save")
	String filterBuilderSaveButton();

	@DefaultStringValue("Restore")
	String filterBuilderRestoreButton();

	@DefaultStringValue("Name")
	String filterBuilderFilterNameLabel();

	@DefaultStringValue("Edit Filters")
	String filterBuilderFilterEditFilters();
/*
	@DefaultStringValue("no items to show")
	String emptyListGrid();

	@DefaultStringValue("(none)")
	String emptyCellValue();

	@DefaultStringValue("Loading data")
	String loadingListGrid();

	@DefaultStringValue("Are you sure you want to cancel editing the current cell?")
	String cancelEditingConfirmation();

	@DefaultStringValue("Are you sure you want to discard your changes?")
	String confirmDiscardEdits();

	@DefaultStringValue("Duplicate records are not allowed")
	String duplicateDrag();
	*/
	@DefaultStringValue("*Previous")
	String wizardPreviousButton();

	@DefaultStringValue("*Next")
	String wizardNextButton();

	@DefaultStringValue("*Finish")
	String wizardFinishButton();

	@DefaultStringValue("Upload in progress")
	String uploadInProgress();

}
