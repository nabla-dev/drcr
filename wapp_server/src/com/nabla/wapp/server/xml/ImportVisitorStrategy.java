package com.nabla.wapp.server.xml;

import java.util.Map;

import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

import com.nabla.wapp.server.csv.ICsvErrorList;
import com.nabla.wapp.server.general.Assert;

class ImportVisitorStrategy extends VisitorStrategy {

	private static final String	KEY_ROW = "wapp_row";
	private static final String	KEY_ERROR_LIST = "wapp_errors";
	private static final String	KEY_CTX = "wapp_ctx";

	private boolean				sessionInitialized = false;
	private final ICsvErrorList	errors;
	private final Object			ctx;

	public ImportVisitorStrategy(final ICsvErrorList errors, final Object ctx) {
		super(null);
		Assert.argumentNotNull(errors);
		this.errors = errors;
		this.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Value read(Type type, NodeMap<InputNode> node, Map session) throws Exception {
		if (!sessionInitialized) {
			session.put(KEY_ERROR_LIST, errors);
			session.put(KEY_CTX, ctx);
			sessionInitialized = true;
		}
		final Integer row = node.getNode().getPosition().getLine();
		session.put(KEY_ROW, row);
		errors.setLine(row);
		return super.read(type, node, session);
	}

	public static Integer getRow(final Map session) {
		Assert.argumentNotNull(session);
		return (Integer)session.get(KEY_ROW);
	}

	public static ICsvErrorList getErrorList(final Map session) {
		Assert.argumentNotNull(session);
		return (ICsvErrorList)session.get(KEY_ERROR_LIST);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getContext(final Map session) {
		Assert.argumentNotNull(session);
		return (T)session.get(KEY_CTX);
	}

}