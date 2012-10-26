package com.nabla.wapp.server.xml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Value;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;

class ImportVisitorStrategy<C extends IImportContext> extends VisitorStrategy {

	private static final Log					log = LogFactory.getLog(ImportVisitorStrategy.class);

	public static final String					KEY_CTX = "wapp_ctx";
	private static final String					ROW_FIELD = "xmlRow";
	private static final String					ROW_MAP_ID_FIELD = "xmlRowMapId";

	private boolean								sessionInitialized = false;
	private final C								ctx;
	private static final Map<Class, Boolean>	rowFieldCache = new HashMap<Class, Boolean>();
	private static final Map<Class, Boolean>	rowMapIdFieldCache = new HashMap<Class, Boolean>();
	private final Map<InputNode, Integer>		nodeIds = new HashMap<InputNode, Integer>();
	private int									nextId = 0;

	public ImportVisitorStrategy(final C ctx) {
		this(null, ctx);
	}

	public ImportVisitorStrategy(final Visitor visitor, final C ctx) {
		super(visitor);
		this.ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Value read(Type type, NodeMap<InputNode> node, Map session) throws Exception {
		if (!sessionInitialized) {
			session.put(KEY_CTX, ctx);
			sessionInitialized = true;
		}
		final Class clazz = type.getType();
		final InputNode n = node.getNode();
		Boolean hasRowField = rowFieldCache.get(clazz);
		if (hasRowField == null) {
			final Field field = getClassField(clazz, ROW_FIELD);
			hasRowField = field != null &&
						(field.getType() == Integer.class) &&
						(field.getAnnotation(Attribute.class) != null);
			if (log.isDebugEnabled())
				log.debug(clazz.getName() + " has row field: " + hasRowField);
			rowFieldCache.put(clazz, hasRowField);
		}
		if (hasRowField) {
			final Integer row = n.getPosition().getLine();
			n.getAttributes().put(ROW_FIELD, row.toString());
		}
		Boolean hasRowMapIdField = rowMapIdFieldCache.get(clazz);
		if (hasRowMapIdField == null) {
			final Field field = getClassField(clazz, ROW_MAP_ID_FIELD);
			hasRowMapIdField = field != null &&
						(field.getType() == Integer.class) &&
						(field.getAnnotation(Attribute.class) != null);
			if (log.isDebugEnabled())
				log.debug(clazz.getName() + " has row map id field: " + hasRowMapIdField);
			rowMapIdFieldCache.put(clazz, hasRowMapIdField);
		}
		if (hasRowMapIdField) {
			final Integer id = ++nextId;
			nodeIds.put(n, id);	// store link between node and id
			n.getAttributes().put(ROW_MAP_ID_FIELD, id.toString());	// tell class instance of its row map id
		}
		if (!n.isRoot()) {
			final Integer parentId = nodeIds.get(n.getParent());
			if (parentId != null)
				ctx.getRowMap(parentId).put(n.getName(), n.getPosition().getLine());
		}
		return super.read(type, node, session);
	}

	private static Field getClassField(final Class clazz, final String name) {
		if (clazz == null)
			return null;
		try {
			return clazz.getDeclaredField(ROW_MAP_ID_FIELD);
		} catch (Throwable _) {
		}
		return getClassField(clazz.getSuperclass(), name);
	}
}