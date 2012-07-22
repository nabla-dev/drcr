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
package com.nabla.dc.client.model.company;


import com.nabla.dc.shared.command.company.ChangeCompanyLogo;
import com.nabla.dc.shared.command.company.FetchCompanyName;
import com.nabla.dc.shared.model.company.ICompany;
import com.nabla.wapp.client.model.CModel;
import com.nabla.wapp.client.model.field.FieldAttributes;
import com.nabla.wapp.client.model.field.TextField;
import com.nabla.wapp.client.model.field.UploadFileField;
import com.nabla.wapp.shared.command.AbstractFetch;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.smartgwt.client.data.DSRequest;

/**
 * @author nabla
 *
 */
public class ChangeLogoModel extends CModel<LogoRecord> {

	static public class Fields {
		public String companyName() { return ICompany.NAME; }
		public String logoFile() { return ICompany.LOGO_FILE; }
	}

	private static final Fields		fields = new Fields();

	public Fields fields() {
		return fields;
	}

	private final Integer	companyId;

	public ChangeLogoModel(final Integer companyId) {
		super(LogoRecord.factory);
		this.companyId = companyId;

		setFields(
			new TextField(fields.companyName(), FieldAttributes.READ_ONLY),
			new UploadFileField(fields.logoFile(), FieldAttributes.REQUIRED)
				);
	}

	@Override
	public AbstractFetch getFetchCommand(@SuppressWarnings("unused") final DSRequest request) {
		return new FetchCompanyName(companyId);
	}

	@Override
	public IAction<StringResult> getAddCommand(final LogoRecord record) {
		return new ChangeCompanyLogo(companyId, record.getFileId());
	}

	@Override
	public IAction<StringResult> getUpdateCommand(final LogoRecord record) {
		return new ChangeCompanyLogo(companyId, record.getFileId());
	}

}
