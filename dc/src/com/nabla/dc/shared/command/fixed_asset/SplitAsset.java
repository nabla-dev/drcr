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
package com.nabla.dc.shared.command.fixed_asset;

import com.nabla.dc.shared.ServerErrors;
import com.nabla.dc.shared.model.fixed_asset.ISplitAsset;
import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.dispatch.IRecordAction;
import com.nabla.wapp.shared.dispatch.StringResult;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;
import com.nabla.wapp.shared.validator.ValidatorContext;

/**
 * @author nabla
 *
 */
public class SplitAsset implements IRecordAction<StringResult>, ISplitAsset {

	int					id;
	String				nameA;
	String				nameB;
	@Nullable String	referenceA;
	@Nullable String	referenceB;
	int					costA;
	int					total;

	SplitAsset() {}	// for serialization only

	public SplitAsset(int id, final String nameA, final String nameB, @Nullable String referenceA, @Nullable String referenceB, int costA, int total) {
		this.id = id;
		this.nameA = nameA;
		this.nameB = nameB;
		this.referenceA = referenceA;
		this.referenceB = referenceB;
		this.costA = costA;
		this.total = total;
	}

	@Override
	public boolean validate(IErrorList errors) throws DispatchException {
		int n = errors.size();

		NAME_CONSTRAINT.validate(NAME_A, nameA, errors, ValidatorContext.ADD);
		NAME_CONSTRAINT.validate(NAME_B, nameB, errors, ValidatorContext.ADD);
		if (nameA != null && nameB != null && nameA.equalsIgnoreCase(nameB)) {
			errors.add(NAME_A, ServerErrors.FA_SPLIT_ASSET_NOT_SAME_NAME);
			errors.add(NAME_B, ServerErrors.FA_SPLIT_ASSET_NOT_SAME_NAME);
		}
		if (referenceA != null) {
			if (referenceA.isEmpty())
				referenceA = null;
			else
				REFERENCE_CONSTRAINT.validate(REFERENCE_A, referenceA, errors, ValidatorContext.UPDATE);
		}
		if (referenceB != null) {
			if (referenceB.isEmpty())
				referenceB = null;
			else
				REFERENCE_CONSTRAINT.validate(REFERENCE_B, referenceB, errors, ValidatorContext.UPDATE);
		}
		if (costA < 1 || costA >= total)
			errors.add(COST_A, CommonServerErrors.INVALID_VALUE);

		return n == errors.size();
	}

	public int getId() {
		return id;
	}

	public String getNameA() {
		return nameA;
	}

	public String getNameB() {
		return nameB;
	}

	public String getReferenceA() {
		return referenceA;
	}

	public String getReferenceB() {
		return referenceB;
	}

	public int getCostA() {
		return costA;
	}

	public int getCostB() {
		return total - costA;
	}

	public int getTotal() {
		return total;
	}

}
