package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.OpcodeHandler;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.BoundToXServer;
import com.eltechs.axs.xconnectors.XRequest;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationDrivenOpcodeHandler implements OpcodeHandler {
    private final Method handlerMethod;
    private final Object handlerObject;
    private final Subsystem[] locks;
    private final AnnotationDrivenRequestParser requestParser;
    private final XServer xServer;

    public AnnotationDrivenOpcodeHandler(final Object obj, final Method method, final Subsystem[] subsystemArr, final AnnotationDrivenRequestParser annotationDrivenRequestParser) {
        this.handlerObject = obj;
        this.handlerMethod = method;
        this.locks = subsystemArr;
        this.requestParser = annotationDrivenRequestParser;
		this.xServer = getXServer();
    }

    private XServer getXServer() {
        Assert.isTrue(handlerObject instanceof BoundToXServer, String.format("Request handler objects must be bound to a X-Server, but %s is not.", new Object[]{this.handlerObject.getClass().getSimpleName()}));
        return ((BoundToXServer) this.handlerObject).getXServer();
    }

    public void handleRequest(XClient xClient, int i, byte b, XRequest xRequest, XResponse xResponse) throws XProtocolError, IOException {
        XLock lock;
        
        try {
            lock = this.xServer.getLocksManager().lock(this.locks);
            this.handlerMethod.invoke(this.handlerObject, this.requestParser.getRequestHandlerParameters(xClient, this.xServer, xRequest, xResponse, i, b));
            if (lock != null) {
                lock.close();
                return;
            }
            return;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof IOException) {
                throw ((IOException) targetException);
            } else if (targetException instanceof XProtocolError) {
                throw ((XProtocolError) targetException);
            } else {
                targetException.printStackTrace();
                return;
            }
        } catch (IllegalAccessException unused) {
            return;
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
			throw new RuntimeException(th2);
        }
        // throw th;
    }
}
