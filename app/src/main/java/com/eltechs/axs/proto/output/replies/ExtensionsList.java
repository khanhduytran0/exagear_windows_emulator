package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.proto.output.PODWriter;
import java.util.List;

@POD({"names", "pad"})
public class ExtensionsList {
    public final Str[] names;
    public final byte[] pad;

    public ExtensionsList(List<String> list) {
        this.names = new Str[list.size()];
        int i = 0;
        int i2 = 0;
        for (String str : list) {
            this.names[i] = new Str(str);
            i2 += PODWriter.getOnWireLength(this.names[i]);
            i++;
        }
        this.pad = new byte[ProtoHelpers.calculatePad(i2)];
    }
}
