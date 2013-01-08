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
package com.nabla.wapp.client.rebind;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.uibinder.rebind.IndentedWriter;
import com.nabla.wapp.client.command.IRequireRootRole;
import com.nabla.wapp.client.command.IRequiredRole;


public class GenericCommandSetGenerator extends Generator {

	private static final String		PACKAGE = "package %1$s;";
	private static final String		IMPORT = "import %1$s;";
	private static final String		COMMAND_SET = "com.nabla.wapp.client.command.IBasicCommandSet";

	public GenericCommandSetGenerator() {}

	@Override
	public String generate(final TreeLogger logger, final GeneratorContext ctx, final String interfaceTypeName) throws UnableToCompleteException {
		final TypeOracle typeOracle = ctx.getTypeOracle();
		final JClassType interfaceType = typeOracle.findType(interfaceTypeName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "unable to find metadata for type '" + interfaceTypeName + "'", null);
			throw new UnableToCompleteException();
		}
		if (interfaceType.isInterface() == null) {
			logger.log(TreeLogger.ERROR, interfaceType.getQualifiedSourceName() + " is not an interface", null);
			throw new UnableToCompleteException();
		}
		final String packageName = interfaceType.getPackage().getName();
		final String className = interfaceType.getName().replace(".", "_") + "GeneratedImpl";

		final PrintWriter writer = ctx.tryCreate(logger, packageName, className);
		if (writer == null) {
			logger.log(TreeLogger.DEBUG, "source code for '" + packageName + "." + className + "' has already been written", null);
		} else {
			writeClass(logger.branch(TreeLogger.INFO, "Generating command set '" + packageName + "." + className + "'", null),
						new IndentedWriter(writer), packageName, className, interfaceType);
			ctx.commit(logger, writer);
		}
		return packageName + "." + className;
	}

	private void writeClass(final TreeLogger logger, final IndentedWriter writer, final String packageName, final String className, final JClassType interfaceType) throws UnableToCompleteException {
		final List<JMethod> methods = new LinkedList<JMethod>();
		getAllMethods(interfaceType, methods);
		final Set<String> returnTypes = new HashSet<String>();
		final Map<String, String[]> commandRequiredRoles = new HashMap<String, String[]>();
		final Set<String> requiredRoles = new HashSet<String>();
		final Set<String> rootCommands = new HashSet<String>();
		int maxRoleCountPerMethod = 0;
		boolean singleRolePerMethod = false;	// true if at least one command needs a unique role
		for (final JMethod method : methods) {
			if (method.getParameters().length > 0) {
				logger.log(TreeLogger.ERROR, "unsupported parameters for method '" + method.getName() + "'", null);
				throw new UnableToCompleteException();
			}
			returnTypes.add(method.getReturnType().getQualifiedSourceName());
			final IRequiredRole role = method.getAnnotation(IRequiredRole.class);
			if (role != null) {
				final String[] roles = role.value();
				logger.log(TreeLogger.INFO, "method '" + method.getName() + "' restricted to roles " + Arrays.asList(roles).toString());
				requiredRoles.addAll(Arrays.asList(roles));
				commandRequiredRoles.put(method.getName(), roles);
				if (maxRoleCountPerMethod < roles.length)
					maxRoleCountPerMethod = roles.length;
				if (roles.length == 1)
					singleRolePerMethod = true;
			}
			final IRequireRootRole rootRole = method.getAnnotation(IRequireRootRole.class);
			if (rootRole != null) {
				logger.log(TreeLogger.INFO, "method '" + method.getName() + "' restricted to root only");
				rootCommands.add(method.getName());
			}
		}
		writer.write(PACKAGE, packageName);
		writer.newline();
		for (final String type : returnTypes)
			writer.write(IMPORT, type);
		writer.write(IMPORT, "java.util.HashMap");
		writer.write(IMPORT, "java.util.Map");
		writer.write(IMPORT, "java.util.Set");
		writer.write(IMPORT, "java.util.HashSet");
		writer.write(IMPORT, "java.util.Collections");
		writer.write(IMPORT, "com.smartgwt.client.data.Record");
		writer.write(IMPORT, "com.nabla.wapp.shared.slot.ISlot1");
		writer.write(IMPORT, "com.nabla.wapp.shared.general.StringSet");
		writer.write(IMPORT, "com.nabla.wapp.client.auth.IAuthSessionManager");
		writer.write(IMPORT, "com.nabla.wapp.client.general.Application");
		writer.write(IMPORT, "com.nabla.wapp.client.command.ICurrentRecordProvider");
		writer.newline();
		writer.write("public class %1$s implements %2$s {", className, interfaceType.getName());
		writer.indent();
			for (final JMethod method : methods)
				writer.write("private final %1$s m_%2$s = new %1$s();", method.getReturnType().getSimpleSourceName(), method.getName());
		writer.newline();
			writer.write("private static final StringSet roles = new StringSet();");
			for (final Map.Entry<String, String[]> e : commandRequiredRoles.entrySet()) {
				if (e.getValue().length < 2)
					continue;
				writer.write("private static final String[] %1$s_Roles = {", e.getKey());
				writer.indent();
					final String[] roles = e.getValue();
					writer.write("\"%1$s\"", roles[0]);
					for (int i = 1; i < roles.length; ++i)
						writer.write(",\"%1$s\"", roles[i]);
				writer.outdent();
				writer.write("});");
			}

			writer.newline();
			writer.write("static {");
			writer.indent();
				for (final String role : requiredRoles)
					writer.write("roles.add(\"%1$s\");", role);
			writer.outdent();
			writer.write("}");	// static

			writer.newline();
			writer.write("private Integer objectId;");
		    writer.write("public %1$s() {}", className);
		  	// constructor

		    writer.newline();
		    writer.write("public void setObjectId(final Integer objectId) {");
		    writer.indent();
		    writer.write("this.objectId = objectId;");
		    writer.outdent();
		    writer.write("}");

		    writer.newline();
			for (final JMethod method : methods)
				writer.write("@Override public %1$s %2$s() { return m_%2$s; }", method.getReturnType().getSimpleSourceName(), method.getName());

			writer.newline();
			writer.write("@Override public StringSet getRequiredRoles() { return roles; }");

			writer.newline();
			writer.write("@Override public void applyRoles(final Set<String> userRoles, boolean isRoot) {");
			writer.indent();
			if (requiredRoles.isEmpty()) {
				for (final JMethod method : methods) {
					if (rootCommands.contains(method.getName()))
						writer.write("m_%1$s.setEnabled(isRoot);", method.getName());
					else
						writer.write("m_%1$s.setEnabled(true);", method.getName());
				}
			} else {
				for (final JMethod method : methods) {
					if (rootCommands.contains(method.getName()))
						writer.write("m_%1$s.setEnabled(isRoot);", method.getName());
					else {
						final String[] commandRoles = commandRequiredRoles.get(method.getName());
						if (commandRoles == null)
							writer.write("m_%1$s.setEnabled(true);", method.getName());
						else if (commandRoles.length == 1)
							writer.write("m_%1$s.setEnabled(isEnabled(\"%2$s\", userRoles));", method.getName(), commandRoles[0]);
						else
							writer.write("m_%1$s.setEnabled(isEnabled(%1$s_Roles, userRoles));", method.getName());
					}
				}
			}
			writer.outdent();
		    writer.write("}");	// applyRoles

			writer.newline();
			writer.write("@Override public void updateUi(final IAuthSessionManager sessionManager) {");
			writer.indent();
				writer.write("sessionManager.IsUserInRole(objectId, getRequiredRoles(), new ISlot1<Set<String>>() {");
				writer.indent();
					writer.write("@Override public void invoke(final Set<String> roles) {");
					writer.indent();
						writer.write("applyRoles(roles, sessionManager.isRoot());");
					writer.outdent();
					writer.write("}");
				writer.outdent();
				writer.write("});");
			writer.outdent();
		    writer.write("}");	// updateUi

			writer.newline();
			writer.write("@Override public void updateUi() {");
			writer.indent();
				writer.write("updateUi(Application.getInstance().getUserSessionManager());");
			writer.outdent();
		    writer.write("}");	// updateUi

	    	if (maxRoleCountPerMethod > 1) {
			    writer.newline();
			    writer.write("private boolean isEnabled(final String[] requiredRoles, final Set<String> userRoles) {");
			    writer.indent();
				    writer.write("if (userRoles == null || userRoles.isEmpty())");
				    writer.indent();
				    	writer.write("return false;");
				    writer.outdent();
				    writer.write("for (String requiredRole : requiredRoles) {");
				    writer.indent();
					    writer.write("if (userRoles.contains(requiredRole))");
					    writer.indent();
					    	writer.write("return true;");
					    writer.outdent();
					writer.outdent();
				    writer.write("}");
				    writer.write("return false;");
				writer.outdent();
			    writer.write("}");	// isEnabled
	    	}
	    	if (singleRolePerMethod) {
			    writer.newline();
			    writer.write("private boolean isEnabled(final String requiredRole, final Set<String> userRoles) {");
			    writer.indent();
				    writer.write("return userRoles != null && userRoles.contains(requiredRole);");
				writer.outdent();
			    writer.write("}");	// isEnabled
	    	}
		writer.outdent();
	    writer.write("}");	// class
	}

	private void getAllMethods(final JClassType interfaceType, final List<JMethod> methods) {
		if (!interfaceType.getQualifiedSourceName().equals(COMMAND_SET)) {
			methods.addAll(Arrays.asList(interfaceType.getMethods()));
			for (final JClassType subclass : interfaceType.getImplementedInterfaces())
				getAllMethods(subclass, methods);
		}
	}
}
