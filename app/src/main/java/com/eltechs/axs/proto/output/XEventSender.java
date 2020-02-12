package com.eltechs.axs.proto.output;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributes;
import com.eltechs.axs.xserver.events.ButtonPress;
import com.eltechs.axs.xserver.events.ButtonRelease;
import com.eltechs.axs.xserver.events.ConfigureNotify;
import com.eltechs.axs.xserver.events.ConfigureRequest;
import com.eltechs.axs.xserver.events.CreateNotify;
import com.eltechs.axs.xserver.events.DestroyNotify;
import com.eltechs.axs.xserver.events.EnterNotify;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.events.Expose;
import com.eltechs.axs.xserver.events.InputDeviceEvent;
import com.eltechs.axs.xserver.events.KeyPress;
import com.eltechs.axs.xserver.events.KeyRelease;
import com.eltechs.axs.xserver.events.LeaveNotify;
import com.eltechs.axs.xserver.events.MapNotify;
import com.eltechs.axs.xserver.events.MapRequest;
import com.eltechs.axs.xserver.events.MappingNotify;
import com.eltechs.axs.xserver.events.MotionNotify;
import com.eltechs.axs.xserver.events.PointerWindowEvent;
import com.eltechs.axs.xserver.events.PropertyNotify;
import com.eltechs.axs.xserver.events.ResizeRequest;
import com.eltechs.axs.xserver.events.SelectionClear;
import com.eltechs.axs.xserver.events.SelectionNotify;
import com.eltechs.axs.xserver.events.SelectionRequest;
import com.eltechs.axs.xserver.events.UnmapNotify;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class XEventSender {
    private static final Map<Class<? extends Event>, EventWriter<?>> eventWriters = new HashMap();
    private final XResponse response;

    private interface EventWriter<E extends Event> {
        void sendEvent(XResponse xResponse, E e) throws IOException;
    }

    static {
        eventWriters.put(MapNotify.class, new EventWriter<MapNotify>() {
            public void sendEvent(XResponse xResponse, final MapNotify mapNotify) throws IOException {
                xResponse.sendEvent((byte) mapNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(mapNotify.getOriginatedAt().getId());
                        byteBuffer.putInt(mapNotify.getMappedWindow().getId());
                        byteBuffer.put(mapNotify.getMappedWindow().getWindowAttributes().isOverrideRedirect() ? (byte) 1 : 0);
                    }
                });
            }
        });
        eventWriters.put(UnmapNotify.class, new EventWriter<UnmapNotify>() {
            public void sendEvent(XResponse xResponse, final UnmapNotify unmapNotify) throws IOException {
                xResponse.sendEvent((byte) unmapNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(unmapNotify.getOriginatedAt().getId());
                        byteBuffer.putInt(unmapNotify.getUnmappedWindow().getId());
                        byteBuffer.put(unmapNotify.isFromConfigure() ? (byte) 1 : 0);
                    }
                });
            }
        });
        eventWriters.put(CreateNotify.class, new EventWriter<CreateNotify>() {
            public void sendEvent(XResponse xResponse, final CreateNotify createNotify) throws IOException {
                xResponse.sendEvent((byte) createNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        Window window = createNotify.getWindow();
                        Rectangle boundingRectangle = window.getBoundingRectangle();
                        WindowAttributes windowAttributes = window.getWindowAttributes();
                        byteBuffer.putInt(createNotify.getParent().getId());
                        byteBuffer.putInt(window.getId());
                        byteBuffer.putShort((short) boundingRectangle.x);
                        byteBuffer.putShort((short) boundingRectangle.y);
                        byteBuffer.putShort((short) boundingRectangle.width);
                        byteBuffer.putShort((short) boundingRectangle.height);
                        byteBuffer.putShort((short) windowAttributes.getBorderWidth());
                        byteBuffer.put(windowAttributes.isOverrideRedirect() ? (byte) 1 : 0);
                    }
                });
            }
        });
        eventWriters.put(DestroyNotify.class, new EventWriter<DestroyNotify>() {
            public void sendEvent(XResponse xResponse, final DestroyNotify destroyNotify) throws IOException {
                xResponse.sendEvent((byte) destroyNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(destroyNotify.getOriginatedAt().getId());
                        byteBuffer.putInt(destroyNotify.getDeletedWindow().getId());
                    }
                });
            }
        });
        eventWriters.put(PropertyNotify.class, new EventWriter<PropertyNotify>() {
            public void sendEvent(XResponse xResponse, final PropertyNotify propertyNotify) throws IOException {
                xResponse.sendEvent((byte) propertyNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(propertyNotify.getWindow().getId());
                        byteBuffer.putInt(propertyNotify.getName().getId());
                        byteBuffer.putInt(propertyNotify.getTimestamp());
                        byteBuffer.put(propertyNotify.isDelete() ? (byte) 1 : 0);
                    }
                });
            }
        });
        eventWriters.put(Expose.class, new EventWriter<Expose>() {
            public void sendEvent(XResponse xResponse, final Expose expose) throws IOException {
                xResponse.sendEvent((byte) expose.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(expose.getWindow().getId());
                        byteBuffer.putShort((short) expose.getX());
                        byteBuffer.putShort((short) expose.getY());
                        byteBuffer.putShort((short) expose.getWidth());
                        byteBuffer.putShort((short) expose.getHeight());
                        byteBuffer.putShort((short)0);
                    }
                });
            }
        });
        eventWriters.put(ResizeRequest.class, new EventWriter<ResizeRequest>() {
            public void sendEvent(XResponse xResponse, final ResizeRequest resizeRequest) throws IOException {
                xResponse.sendEvent((byte) resizeRequest.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(resizeRequest.getWindow().getId());
                        byteBuffer.putShort((short) resizeRequest.getWidth());
                        byteBuffer.putShort((short) resizeRequest.getHeight());
                    }
                });
            }
        });
        eventWriters.put(MapRequest.class, new EventWriter<MapRequest>() {
            public void sendEvent(XResponse xResponse, final MapRequest mapRequest) throws IOException {
                xResponse.sendEvent((byte) mapRequest.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(mapRequest.getParentWindow().getId());
                        byteBuffer.putInt(mapRequest.getMappedWindow().getId());
                    }
                });
            }
        });
        eventWriters.put(ConfigureNotify.class, new EventWriter<ConfigureNotify>() {
            public void sendEvent(XResponse xResponse, final ConfigureNotify configureNotify) throws IOException {
                xResponse.sendEvent((byte) configureNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        int i;
                        byteBuffer.putInt(configureNotify.getEvent().getId());
                        byteBuffer.putInt(configureNotify.getWindow().getId());
                        Window aboveSibling = configureNotify.getAboveSibling();
                        if (aboveSibling == null) {
                            i = 0;
                        } else {
                            i = aboveSibling.getId();
                        }
                        byteBuffer.putInt(i);
                        byteBuffer.putShort((short) configureNotify.getX());
                        byteBuffer.putShort((short) configureNotify.getY());
                        byteBuffer.putShort((short) configureNotify.getWidth());
                        byteBuffer.putShort((short) configureNotify.getHeight());
                        byteBuffer.putShort((short) configureNotify.getBorderWidth());
                        byteBuffer.putShort(configureNotify.isOverrideRedirect() ? (short) 1 : 0);
                    }
                });
            }
        });
        eventWriters.put(ConfigureRequest.class, new EventWriter<ConfigureRequest>() {
            public void sendEvent(XResponse xResponse, final ConfigureRequest configureRequest) throws IOException {
                xResponse.sendEvent((byte) configureRequest.getId(), (byte) configureRequest.getStackMode().ordinal(), new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        int i;
                        byteBuffer.putInt(configureRequest.getParent().getId());
                        byteBuffer.putInt(configureRequest.getWindow().getId());
                        Window sibling = configureRequest.getSibling();
                        if (sibling == null) {
                            i = 0;
                        } else {
                            i = sibling.getId();
                        }
                        byteBuffer.putInt(i);
                        byteBuffer.putShort((short) configureRequest.getX());
                        byteBuffer.putShort((short) configureRequest.getY());
                        byteBuffer.putShort((short) configureRequest.getWidth());
                        byteBuffer.putShort((short) configureRequest.getHeight());
                        byteBuffer.putShort((short) configureRequest.getBorderWidth());
                        byteBuffer.putShort((short) configureRequest.getParts().getRawMask());
                    }
                });
            }
        });
        EventWriter r0 = new EventWriter<InputDeviceEvent>() {
            public void sendEvent(XResponse xResponse, final InputDeviceEvent inputDeviceEvent) throws IOException {
                xResponse.sendEvent((byte) inputDeviceEvent.getId(), inputDeviceEvent.getDetail(), new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        Window child = inputDeviceEvent.getChild();
                        byteBuffer.putInt(inputDeviceEvent.getTimestamp());
                        byteBuffer.putInt(inputDeviceEvent.getRoot().getId());
                        byteBuffer.putInt(inputDeviceEvent.getEvent().getId());
                        byteBuffer.putInt(child != null ? child.getId() : 0);
                        byteBuffer.putShort((short)inputDeviceEvent.getRootX());
                        byteBuffer.putShort((short)inputDeviceEvent.getRootY());
                        byteBuffer.putShort((short)inputDeviceEvent.getEventX());
                        byteBuffer.putShort((short)inputDeviceEvent.getEventY());
                        byteBuffer.putShort((short) inputDeviceEvent.getState().getRawMask());
                        byteBuffer.put((byte)1);
                    }
                });
            }
        };
        eventWriters.put(MotionNotify.class, r0);
        eventWriters.put(ButtonPress.class, r0);
        eventWriters.put(ButtonRelease.class, r0);
        eventWriters.put(KeyPress.class, r0);
        eventWriters.put(KeyRelease.class, r0);
        EventWriter r02 = new EventWriter<PointerWindowEvent>() {
            public void sendEvent(XResponse xResponse, final PointerWindowEvent pointerWindowEvent) throws IOException {
                xResponse.sendEvent((byte) pointerWindowEvent.getId(), (byte) pointerWindowEvent.getDetail().ordinal(), new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        Window child = pointerWindowEvent.getChild();
                        byteBuffer.putInt(pointerWindowEvent.getTimestamp());
                        byteBuffer.putInt(pointerWindowEvent.getRoot().getId());
                        byteBuffer.putInt(pointerWindowEvent.getEvent().getId());
                        byteBuffer.putInt(child != null ? child.getId() : 0);
                        byteBuffer.putShort((short)pointerWindowEvent.getRootX());
                        byteBuffer.putShort((short)pointerWindowEvent.getRootY());
                        byteBuffer.putShort((short)pointerWindowEvent.getEventX());
                        byteBuffer.putShort((short)pointerWindowEvent.getEventY());
                        byteBuffer.putShort((short) pointerWindowEvent.getState().getRawMask());
                        byteBuffer.put((byte)(byte) pointerWindowEvent.getMode().ordinal());
                        byteBuffer.put((byte)pointerWindowEvent.getSameScreenAndFocus());
                    }
                });
            }
        };
        eventWriters.put(EnterNotify.class, r02);
        eventWriters.put(LeaveNotify.class, r02);
        eventWriters.put(SelectionClear.class, new EventWriter<SelectionClear>() {
            public void sendEvent(XResponse xResponse, final SelectionClear selectionClear) throws IOException {
                xResponse.sendEvent((byte) selectionClear.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.putInt(selectionClear.getTimestamp());
                        byteBuffer.putInt(selectionClear.getOwner().getId());
                        byteBuffer.putInt(selectionClear.getSelection().getId());
                    }
                });
            }
        });
        eventWriters.put(SelectionRequest.class, new EventWriter<SelectionRequest>() {
            public void sendEvent(XResponse xResponse, final SelectionRequest selectionRequest) throws IOException {
                xResponse.sendEvent((byte) selectionRequest.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        Atom property = selectionRequest.getProperty();
                        byteBuffer.putInt(selectionRequest.getTimestamp());
                        byteBuffer.putInt(selectionRequest.getOwner().getId());
                        byteBuffer.putInt(selectionRequest.getRequestor().getId());
                        byteBuffer.putInt(selectionRequest.getSelection().getId());
                        byteBuffer.putInt(selectionRequest.getTarget().getId());
                        byteBuffer.putInt(property != null ? property.getId() : 0);
                    }
                });
            }
        });
        eventWriters.put(SelectionNotify.class, new EventWriter<SelectionNotify>() {
            public void sendEvent(XResponse xResponse, final SelectionNotify selectionNotify) throws IOException {
                xResponse.sendEvent((byte) selectionNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        Atom property = selectionNotify.getProperty();
                        byteBuffer.putInt(selectionNotify.getTimestamp());
                        byteBuffer.putInt(selectionNotify.getRequestor().getId());
                        byteBuffer.putInt(selectionNotify.getSelection().getId());
                        byteBuffer.putInt(selectionNotify.getTarget().getId());
                        byteBuffer.putInt(property != null ? property.getId() : 0);
                    }
                });
            }
        });
        eventWriters.put(MappingNotify.class, new EventWriter<MappingNotify>() {
            public void sendEvent(XResponse xResponse, final MappingNotify mappingNotify) throws IOException {
                xResponse.sendEvent((byte) mappingNotify.getId(), (byte) 0, new ResponseDataWriter() {
                    public void write(ByteBuffer byteBuffer) {
                        byteBuffer.put((byte)(byte) mappingNotify.getRequest().ordinal());
                        byteBuffer.put((byte)(byte) mappingNotify.getFirstKeycode());
                        byteBuffer.put((byte)(byte) mappingNotify.getCount());
                    }
                });
            }
        });
    }

    public XEventSender(XResponse xResponse) {
        this.response = xResponse;
    }

    public void sendEvent(Event event) {
        EventWriter eventWriter = (EventWriter) eventWriters.get(event.getClass());
        if (eventWriter == null) {
            Assert.notImplementedYet();
        }
        try {
            eventWriter.sendEvent(this.response, event);
        } catch (IOException unused) {
        }
    }
}
