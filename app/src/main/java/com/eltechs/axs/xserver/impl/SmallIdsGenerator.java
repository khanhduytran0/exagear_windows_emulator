package com.eltechs.axs.xserver.impl;

import android.util.Log;

public abstract class SmallIdsGenerator {
    private static int id = 1;

    public static int generateId() {
        int i = id;
        id = i + 1;
        return i;
    }
}
