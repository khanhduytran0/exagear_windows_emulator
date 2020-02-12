package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.proto.output.POD;
import java.nio.charset.Charset;

@POD({"length", "str"})
public class Str {
    public final byte length;
    public final byte[] str;

    public Str(String str2) {
        this.length = (byte) str2.length();
        this.str = str2.getBytes(Charset.forName("latin1"));
    }
}
