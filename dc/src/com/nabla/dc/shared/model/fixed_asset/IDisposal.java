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
package com.nabla.dc.shared.model.fixed_asset;

import java.util.Date;

import com.nabla.wapp.shared.dispatch.DispatchException;
import com.nabla.wapp.shared.general.CommonServerErrors;
import com.nabla.wapp.shared.general.Nullable;
import com.nabla.wapp.shared.model.IErrorList;

/**
 * @author FNorais
 *
 */
public interface IDisposal {
	public static class Validator {
		public static <P> boolean execute(final IDisposal t, final P pos, final IErrorList<P> errors) throws DispatchException {
			switch (t.getType()) {
			case SOLD:
				if (t.getProceeds() == null)
					t.setProceeds(0);
				else if (t.getProceeds() < 0) {
					errors.add(pos, t.getProceedsField(), CommonServerErrors.INVALID_VALUE);
					return false;
				}
				break;
			default:
				t.setProceeds(0);
				break;
			}
			return true;
		}
	}

	Date getDate();
	DisposalTypes getType();
	@Nullable Integer getProceeds();
	void setProceeds(@Nullable final Integer value);

	String getDateField();
	String getProceedsField();
}
