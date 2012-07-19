package com.nabla.wapp.server.dispatch;

import com.nabla.wapp.server.general.Util;
import com.nabla.wapp.shared.dispatch.IAction;
import com.nabla.wapp.shared.dispatch.IResult;

public class ActionHandlerMap<A extends IAction<R>, R extends IResult> implements IActionHandlerMap<A, R> {

	private final Class<A>								actionClass;
    private final Class<? extends IActionHandler<A, R>>	handlerClass;

    @SuppressWarnings("unchecked")
	public ActionHandlerMap(Class<? extends IActionHandler<A, R>> handlerClass) {
        this.actionClass = Util.getFirstGenericDeclaration(handlerClass);
        this.handlerClass = handlerClass;
    }

    @Override
	public Class<A> getActionClass() {
        return actionClass;
    }

    @Override
	public Class<? extends IActionHandler<A, R>> getActionHandlerClass() {
        return handlerClass;
    }

}