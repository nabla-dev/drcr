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

import java.util.LinkedList;
import java.util.List;

import com.nabla.wapp.client.general.Assert;

/**
 * @author nabla
 *
 */
public class WizardPageStack {

	private final List<IWizardPage>		pages = new LinkedList<IWizardPage>();

	public boolean isEmpty() {
		return pages.isEmpty();
	}

	public int size() {
		return pages.size();
	}

	public void push(final IWizardPage page) {
		Assert.argumentNotNull(page);

		pages.add(0, page);
	}

	public IWizardPage pop() {
		if (pages.isEmpty())
			return null;
		IWizardPage ret = pages.get(0);
		ret.unbind();
		pages.remove(0);
		return ret;
	}

	public IWizardPage top() {
		return pages.isEmpty() ? null : pages.get(0);
	}

	public IWizardPage popToFirstErroneousPage() {
		if (isEmpty())
			return null;
		int i = getFirstErroneousPageIndex();
		for (int j = 0; j < i; ++j) {
			pages.get(0).unbind();
			pages.remove(0);
		}
		return pages.get(0);
	}

	private int getFirstErroneousPageIndex() {
		int lastIndex = pages.size() - 1;
		Assert.state(lastIndex >= 0);
		for (int i = lastIndex; i >= 0; --i) {
			if (pages.get(i).hasErrors())
				return i;
		}
		return lastIndex;
	}
}
