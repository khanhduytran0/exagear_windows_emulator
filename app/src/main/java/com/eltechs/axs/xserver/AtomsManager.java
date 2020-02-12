package com.eltechs.axs.xserver;

public interface AtomsManager {
    Atom getAtom(int i);

    Atom getAtom(String str);

    int getAtomId(String str);

    int internAtom(String str);

    boolean isAtomRegistered(String str);
}
