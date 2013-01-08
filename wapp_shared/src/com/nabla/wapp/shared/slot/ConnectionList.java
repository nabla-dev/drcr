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

package com.nabla.wapp.shared.slot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;



public class ConnectionList<Slot extends IBasicSlot> {

	public static final int AT_BACK = -1;

	private boolean								isBlocked = false;
	private final LinkedList<Connection<Slot>>	connections = new LinkedList<Connection<Slot>>();

	private class SlotIterator implements ISlotIterator<Slot> {

		private final Iterator<Connection<Slot>>	iter;
		private Connection<Slot>					next;

		SlotIterator(final Iterator<Connection<Slot>> __iter) {
			iter = __iter;
			getNext();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Slot next() {
			if (next == null)
				throw new NoSuchElementException("no more slot");
			final Slot rslt = next.slot();
			getNext();
			return rslt;
		}

		private void getNext() {
			while (iter.hasNext()) {
				next = iter.next();
				if (next.isConnected() && !next.isBlocked())
					return;
			}
			next = null;
		}
	}

	public boolean isBlocked() {
		return isBlocked;
	}
	public void setBlocked(boolean b) {
		isBlocked = b;
	}

	public boolean isEmpty() {
		final Iterator<Connection<Slot>> iter = connections.iterator();
		while (iter.hasNext()) {
			if (iter.next().isConnected())
				return false;
			iter.remove();
		}
		return true;
	}

	public int size() {
		int rslt = 0;
		final Iterator<Connection<Slot>> iter = connections.iterator();
		while (iter.hasNext()) {
			if (iter.next().isConnected())
				++rslt;
			else
				iter.remove();
		}
		return rslt;
	}

	public Connection<Slot> connect(final Slot slot) {
		return connect(slot, AT_BACK);
	}

	public Connection<Slot> connect(final Slot slot, int at) {
		Connection<Slot> conn = new Connection<Slot>(slot);
		if (at < 0 || at > connections.size())
			connections.add(conn);
		else
			connections.add(at, conn);
		return conn;
	}

	public void disconnect_all_slots() {
		final Iterator<Connection<Slot>> iter = connections.iterator();
		while (iter.hasNext())
			iter.next().disconnect();
		connections.clear();
	}

	public void disconnect(final Slot slot) {
		final Iterator<Connection<Slot>> iter = connections.iterator();
		while (iter.hasNext()) {
			final Connection<Slot> conn = iter.next();
			if (conn.slot() == slot) {
				conn.disconnect();
				iter.remove();
			} else if (!conn.isConnected())
				iter.remove();
		}
	}

	public ISlotIterator<Slot> iterator() {
		return new SlotIterator(connections.iterator());
	}

}
