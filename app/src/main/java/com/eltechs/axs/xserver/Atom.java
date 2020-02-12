package com.eltechs.axs.xserver;

public final class Atom {
    private final int id;
    private final String name;

    public Atom(int i, String str) {
        this.id = i;
        this.name = str;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Atom)) {
            return false;
        }
        if (this.id == ((Atom) obj).id) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.id;
    }

    public String toString() {
        return String.format("[%d: %s]", new Object[]{Integer.valueOf(this.id), this.name});
    }
}
