package com.eltechs.axs.helpers;

public abstract class Assert {
    private Assert() {
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("[Assertion failed]: this argument is required, it can not be null.");
        }
    }

    public static void notNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void isTrue(boolean z) {
        if (!z) {
            throw new IllegalArgumentException("[Assertion failed]: this expression must be true.");
        }
    }

    public static void isTrue(boolean z, String str) {
        if (!z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void isFalse(boolean z) {
        if (z) {
            throw new IllegalArgumentException("[Assertion failed]: this expression must be false.");
        }
    }

    public static void isFalse(boolean z, String str) {
        if (z) {
            throw new IllegalArgumentException(str);
        }
    }

    public static void state(boolean z) {
        if (!z) {
            throw new IllegalStateException("[Assertion failed]: this expression must be true.");
        }
    }

    public static void state(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }

    public static void unreachable() {
        throw new IllegalStateException("[Assertion failed]: this code must not be reached.");
    }

    public static void unreachable(String str) {
        throw new IllegalStateException(str);
    }

    public static void notImplementedYet() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public static void notImplementedYet(String str) {
        throw new UnsupportedOperationException(str);
    }

    public static void abort(String str, Throwable th) {
        throw new IllegalStateException(str, th);
    }
}
