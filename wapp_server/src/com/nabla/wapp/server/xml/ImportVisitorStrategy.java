package com.nabla.wapp.server.xml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

import com.nabla.wapp.server.general.Assert;
import com.nabla.wapp.shared.model.IErrorList;

class ImportVisitorStrategy extends VisitorStrategy {

	private static final Log			log = LogFactory.getLog(ImportVisitorStrategy.class);

	private static final String			KEY_ERROR_LIST = "wapp_errors";
	private static final String			KEY_CTX = "wapp_ctx";

	private static final String			ROW_FIELD = "xml_row";

	private boolean						sessionInitialized = false;
	private final IErrorList<Integer>	errors;
	private final Object				ctx;
	private final Map<Class, Boolean>	rowFieldCache = new HashMap<Class, Boolean>();

	public ImportVisitorStrategy(final IErrorList<Integer> errors, final Object ctx) {
		super(null);
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
		final Class clazz = type.getType();
		Boolean hasRow = rowFieldCache.get(clazz);
		if (hasRow == null) {
			try {
				final Field rowField = clazz.getDeclaredField(ROW_FIELD);
				hasRow = (rowField.getType() == Integer.class) &&
							(rowField.getAnnotation(Attribute.class) != null);
			} catch (Throwable _) {
				hasRow = false;
			}
			if (log.isDebugEnabled())
				log.debug(clazz.getName() + " has row field: " + hasRow);
			rowFieldCache.put(clazz, hasRow);
		}
		if (hasRow) {
			final Integer row = node.getNode().getPosition().getLine();
			node.getNode().getAttributes().put(ROW_FIELD, row.toString());
		}
		return super.read(type, node, session);
	}

	@SuppressWarnings("unchecked")
	public static IErrorList<Integer> getErrorList(final Map session) {
		Assert.argumentNotNull(session);
		return (IErrorList<Integer>)session.get(KEY_ERROR_LIST);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getContext(final Map session) {
		Assert.argumentNotNull(session);
		return (T)session.get(KEY_CTX);
	}

}