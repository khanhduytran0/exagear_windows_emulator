package com.eltechs.axs.proto;

import com.eltechs.axs.proto.input.annotations.AnnotationDrivenRequestDispatcherConfigurer;
import com.eltechs.axs.proto.input.impl.CoreXProtocolDispatcher;
import com.eltechs.axs.proto.input.impl.DRI2ExtensionDispatcher;
import com.eltechs.axs.proto.input.impl.GLXExtensionDispatcher;
import com.eltechs.axs.proto.input.impl.MITShmExtensionDispatcher;
import com.eltechs.axs.proto.input.impl.RootXRequestHandler;
import com.eltechs.axs.proto.input.impl.XRequestParameterReaderFactories;
import com.eltechs.axs.proto.input.impl.XTestExtensionDispatcher;
import com.eltechs.axs.requestHandlers.X11ProtocolExtensionIds;
import com.eltechs.axs.requestHandlers.core.AtomManipulationRequests;
import com.eltechs.axs.requestHandlers.core.ColormapManipulationRequests;
import com.eltechs.axs.requestHandlers.core.CursorManipulationRequests;
import com.eltechs.axs.requestHandlers.core.DrawablesManipulationRequests;
import com.eltechs.axs.requestHandlers.core.DrawingRequests;
import com.eltechs.axs.requestHandlers.core.EventsRelatedRequests;
import com.eltechs.axs.requestHandlers.core.ExtensionInquiries;
import com.eltechs.axs.requestHandlers.core.FocusManipulationRequests;
import com.eltechs.axs.requestHandlers.core.FontManipulationRequests;
import com.eltechs.axs.requestHandlers.core.ForceScreenSaverRequest;
import com.eltechs.axs.requestHandlers.core.GrabManipulationRequests;
import com.eltechs.axs.requestHandlers.core.GraphicsContextManipulationRequests;
import com.eltechs.axs.requestHandlers.core.KeyboardRelatedRequest;
import com.eltechs.axs.requestHandlers.core.PixmapManipulationRequests;
import com.eltechs.axs.requestHandlers.core.PointerRelatedRequests;
import com.eltechs.axs.requestHandlers.core.SelectionManipulationRequests;
import com.eltechs.axs.requestHandlers.core.SystemRequests;
import com.eltechs.axs.requestHandlers.core.WindowManipulationRequests;
import com.eltechs.axs.requestHandlers.dri2.DRI2Requests;
import com.eltechs.axs.requestHandlers.glx.GLXRequests;
import com.eltechs.axs.requestHandlers.mitshm.MITShmRequests;
import com.eltechs.axs.requestHandlers.xtest.XTestRequests;
import com.eltechs.axs.xserver.XServer;

public class RootXRequestHandlerConfigurer {
    private RootXRequestHandlerConfigurer() {
    }

    public static RootXRequestHandler createRequestHandler(XServer xServer) {
        RootXRequestHandler rootXRequestHandler = new RootXRequestHandler(xServer);
        rootXRequestHandler.installExtensionHandler(0, configureCoreXProtocolDispatcher(rootXRequestHandler, xServer));
        rootXRequestHandler.installExtensionHandler(X11ProtocolExtensionIds.XTEST, configureXTestDispatcher(xServer));
        if (xServer.isShmAvailable()) {
            rootXRequestHandler.installExtensionHandler(140, configureMITShmDispatcher(xServer));
        }
        if (xServer.isHWRenderingAvailable()) {
            rootXRequestHandler.installExtensionHandler(X11ProtocolExtensionIds.DRI2, configureDRI2Dispatcher(xServer));
            rootXRequestHandler.installExtensionHandler(X11ProtocolExtensionIds.GLX, configureGLXDispatcher(xServer));
        }
        return rootXRequestHandler;
    }

    private static CoreXProtocolDispatcher configureCoreXProtocolDispatcher(RootXRequestHandler rootXRequestHandler, XServer xServer) {
        CoreXProtocolDispatcher coreXProtocolDispatcher = new CoreXProtocolDispatcher();
        new AnnotationDrivenRequestDispatcherConfigurer(coreXProtocolDispatcher, XRequestParameterReaderFactories.CONTEXT_PARAM_READERS_FACTORY, XRequestParameterReaderFactories.REQUEST_PARAM_READERS_FACTORY).configureDispatcher(new AtomManipulationRequests(xServer), new ExtensionInquiries(xServer, rootXRequestHandler), new GraphicsContextManipulationRequests(xServer), new DrawablesManipulationRequests(xServer), new WindowManipulationRequests(xServer), new PixmapManipulationRequests(xServer), new DrawingRequests(xServer), new FocusManipulationRequests(xServer), new CursorManipulationRequests(xServer), new ColormapManipulationRequests(xServer), new KeyboardRelatedRequest(xServer), new PointerRelatedRequests(xServer), new GrabManipulationRequests(xServer), new FontManipulationRequests(xServer), new EventsRelatedRequests(xServer), new SystemRequests(xServer), new ForceScreenSaverRequest(xServer), new SelectionManipulationRequests(xServer));
        return coreXProtocolDispatcher;
    }

    private static XTestExtensionDispatcher configureXTestDispatcher(XServer xServer) {
        XTestExtensionDispatcher xTestExtensionDispatcher = new XTestExtensionDispatcher();
        new AnnotationDrivenRequestDispatcherConfigurer(xTestExtensionDispatcher, XRequestParameterReaderFactories.CONTEXT_PARAM_READERS_FACTORY, XRequestParameterReaderFactories.REQUEST_PARAM_READERS_FACTORY).configureDispatcher(new XTestRequests(xServer));
        return xTestExtensionDispatcher;
    }

    private static MITShmExtensionDispatcher configureMITShmDispatcher(XServer xServer) {
        MITShmExtensionDispatcher mITShmExtensionDispatcher = new MITShmExtensionDispatcher();
        new AnnotationDrivenRequestDispatcherConfigurer(mITShmExtensionDispatcher, XRequestParameterReaderFactories.CONTEXT_PARAM_READERS_FACTORY, XRequestParameterReaderFactories.REQUEST_PARAM_READERS_FACTORY).configureDispatcher(new MITShmRequests(xServer));
        return mITShmExtensionDispatcher;
    }

    private static DRI2ExtensionDispatcher configureDRI2Dispatcher(XServer xServer) {
        DRI2ExtensionDispatcher dRI2ExtensionDispatcher = new DRI2ExtensionDispatcher();
        new AnnotationDrivenRequestDispatcherConfigurer(dRI2ExtensionDispatcher, XRequestParameterReaderFactories.CONTEXT_PARAM_READERS_FACTORY, XRequestParameterReaderFactories.REQUEST_PARAM_READERS_FACTORY).configureDispatcher(new DRI2Requests(xServer));
        return dRI2ExtensionDispatcher;
    }

    private static GLXExtensionDispatcher configureGLXDispatcher(XServer xServer) {
        GLXExtensionDispatcher gLXExtensionDispatcher = new GLXExtensionDispatcher();
        new AnnotationDrivenRequestDispatcherConfigurer(gLXExtensionDispatcher, XRequestParameterReaderFactories.CONTEXT_PARAM_READERS_FACTORY, XRequestParameterReaderFactories.REQUEST_PARAM_READERS_FACTORY).configureDispatcher(new GLXRequests(xServer));
        return gLXExtensionDispatcher;
    }
}
