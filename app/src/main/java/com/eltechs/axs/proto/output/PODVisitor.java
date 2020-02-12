package com.eltechs.axs.proto.output;

import com.eltechs.axs.helpers.Assert;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;

public class PODVisitor {

    public interface Callback {
        void apply(Object obj) throws IOException;
    }

    private PODVisitor() {
    }

    public static void visit(Object obj, Callback callback) throws IOException {
        if (obj.getClass().isArray()) {
            visitArray(obj, callback);
        } else if (obj instanceof Collection) {
            visitCollection((Collection) obj, callback);
        } else if (obj.getClass().getAnnotation(POD.class) != null) {
            visitFields(obj, callback);
        } else {
            callback.apply(obj);
        }
    }

    public static void visitArray(Object obj, Callback callback) throws IOException {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            visit(Array.get(obj, i), callback);
        }
    }

    public static void visitCollection(Collection<?> collection, Callback callback) throws IOException {
        for (Object visit : collection) {
            visit(visit, callback);
        }
    }

    private static void visitFields(Object obj, Callback callback) throws IOException {
        Class cls = obj.getClass();
        String[] value = ((POD) cls.getAnnotation(POD.class)).value();
        int length = value.length;
        int i = 0;
        while (i < length) {
            String str = value[i];
            try {
                visit(cls.getField(str).get(obj), callback);
                i++;
            } catch (IllegalAccessException | NoSuchFieldException unused) {
                Assert.isTrue(false, String.format("Classes tagged with @POD must contain only public fields, but the field %s::%s is not accessible.", new Object[]{obj.getClass().getName(), str}));
                return;
            }
        }
    }
}
