package com.eltechs.axs.xserver.impl;

public abstract class SmallIdsGenerator {
    private static int id = 1;

    private SmallIdsGenerator() {
    }

    public static int generateId() {
        int i = id;
        id = i + 1;
        return i;
    }
}
