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
package com.nabla.wapp.server.xml;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

/**
 * @author FNorais
 *
 */
public class CurrentXmlLine implements Visitor {

	private int	currentLine = 0;

	public void reset() {
		currentLine = 0;
	}

	public int getValue() {
		return currentLine;
	}

	@Override
	public void read(@SuppressWarnings("unused") Type arg0, NodeMap<InputNode> node) throws Exception {
		int nodeLine = node.getNode().getPosition().getLine();
		if (nodeLine > currentLine)
			currentLine = nodeLine;
	}

	@Override
	public void write(@SuppressWarnings("unused") Type arg0, @SuppressWarnings("unused") NodeMap<OutputNode> arg1) throws Exception {
	}

}
