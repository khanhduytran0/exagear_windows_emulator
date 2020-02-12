package com.eltechs.axs.xconnectors;

import com.eltechs.axs.proto.input.ProcessingResult;
import java.io.IOException;

public interface RequestHandler<Context> {
    ProcessingResult handleRequest(Context context, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException;
}
