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

import java.util.LinkedList;
import java.util.List;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.form.fields.FilterCriteriaFunction;
import com.smartgwt.client.widgets.form.fields.FormItemCriteriaFunction;
import com.smartgwt.client.widgets.form.fields.FormItemFunctionContext;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author nabla
 *
 */
public class SmartGwtSelectItem extends SelectItem implements FormItemCriteriaFunction/*FilterCriteriaFunction*/ {

	private final Criteria						staticCriterias = new Criteria();
	private final List<FilterCriteriaFunction>	dynamicCriterias = new LinkedList<FilterCriteriaFunction>();

	public SmartGwtSelectItem() {
		setPickListFilterCriteriaFunction(this);
	//	setPickListFilterCriteriaFunction(this);
	}

	@Override
	public native ListGridRecord getSelectedRecord() /*-{
    var self = this.@com.smartgwt.client.core.DataClass::getJsObj()();
    var ret = self.pickList.getSelectedRecord();
    if(ret == null || ret === undefined) return null;
    var retVal = @com.smartgwt.client.core.RefDataClass::getRef(Lcom/google/gwt/core/client/JavaScriptObject;)(ret);
    if(retVal == null) {
        retVal = @com.smartgwt.client.widgets.grid.ListGridRecord::new(Lcom/google/gwt/core/client/JavaScriptObject;)(ret);
    }
    return retVal;
}-*/;

	public void addCriteria(final Criteria criteria) {
		staticCriterias.addCriteria(criteria);
	}

	public void addCriteria(final FilterCriteriaFunction fnCriteria) {
		dynamicCriterias.add(fnCriteria);
	}

	@Override
	public Criteria getCriteria(@SuppressWarnings("unused") FormItemFunctionContext ctx) {
		final Criteria ret = new Criteria();
		ret.addCriteria(staticCriterias);
		for (FilterCriteriaFunction fn : dynamicCriterias) {
			Criteria criteria = fn.getCriteria();
			if (criteria != null)
				ret.addCriteria(criteria);
		}
		return ret;
	}
	/*
	@Override
	public Criteria getCriteria() {
		final Criteria ret = new Criteria();
		ret.addCriteria(staticCriterias);
		for (FilterCriteriaFunction fn : dynamicCriterias) {
			Criteria criteria = fn.getCriteria();
			if (criteria != null)
				ret.addCriteria(criteria);
		}
		return ret;
	}*/

}
