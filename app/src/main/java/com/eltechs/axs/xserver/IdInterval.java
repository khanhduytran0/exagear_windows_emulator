package com.eltechs.axs.xserver;

public class IdInterval implements Comparable<IdInterval> {
    private final int idBase;
    private final int idMask;

    public IdInterval(int i, int i2) {
        this.idBase = i;
        this.idMask = i2;
    }

    public int getIdBase() {
        return this.idBase;
    }

    public int getIdMask() {
        return this.idMask;
    }

    public boolean isInInterval(int i) {
        return (i | this.idMask) == (this.idBase | this.idMask);
    }

    public int compareTo(IdInterval idInterval) {
        if (this.idBase < idInterval.idBase) {
            return -1;
        }
        return this.idBase > idInterval.idBase ? 1 : 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof IdInterval)) {
            return false;
        }
        IdInterval idInterval = (IdInterval) obj;
        if (this.idBase == idInterval.idBase && this.idMask == idInterval.idMask) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.idBase | this.idMask;
    }
}
