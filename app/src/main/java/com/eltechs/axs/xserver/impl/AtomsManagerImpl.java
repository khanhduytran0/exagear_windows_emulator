package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.AtomsManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public final class AtomsManagerImpl implements AtomsManager {
    private final ArrayList<Atom> atoms = new ArrayList<>();
    private final Map<String, Integer> atomsIds = new TreeMap();

    public AtomsManagerImpl() {
        this.atoms.add(null);
    }

    public int internAtom(String str) {
        Integer num = (Integer) this.atomsIds.get(str);
        if (num == null) {
            num = Integer.valueOf(this.atoms.size());
            this.atoms.add(new Atom(num.intValue(), str));
            this.atomsIds.put(str, num);
        }
        return num.intValue();
    }

    public int getAtomId(String str) {
        Integer num = (Integer) this.atomsIds.get(str);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public boolean isAtomRegistered(String str) {
        return getAtomId(str) != 0;
    }

    public Atom getAtom(int i) {
        if (i < this.atoms.size()) {
            return (Atom) this.atoms.get(i);
        }
        return null;
    }

    public Atom getAtom(String str) {
        return getAtom(getAtomId(str));
    }
}
