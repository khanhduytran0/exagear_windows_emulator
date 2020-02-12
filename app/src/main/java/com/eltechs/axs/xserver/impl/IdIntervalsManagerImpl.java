package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.IdInterval;
import com.eltechs.axs.xserver.IdIntervalsManager;
import java.util.Iterator;
import java.util.TreeSet;

public class IdIntervalsManagerImpl implements IdIntervalsManager {
    private static final int MINIMAL_ID_MASK_BITS = 18;
    private static final int ZERO_TOP_BITS = 3;
    private final TreeSet<IdInterval> freeIds = new TreeSet<>();

    public IdIntervalsManagerImpl(int i) {
        Assert.isTrue(i > 0);
        int numberOfLeadingZeros = 32 - Integer.numberOfLeadingZeros(i);
        if (Integer.bitCount(i) == 1) {
            numberOfLeadingZeros--;
        }
        Assert.isTrue(numberOfLeadingZeros <= 11, String.format("The number of intervals is too big: %d.", new Object[]{Integer.valueOf(i)}));
        int i2 = 29 - numberOfLeadingZeros;
        int i3 = (1 << i2) - 1;
        for (int i4 = 1; i4 < i; i4++) {
            this.freeIds.add(new IdInterval(i4 << i2, i3));
        }
    }

    public IdInterval getInterval() {
        if (this.freeIds.isEmpty()) {
            return null;
        }
        Iterator it = this.freeIds.iterator();
        IdInterval idInterval = (IdInterval) it.next();
        it.remove();
        return idInterval;
    }

    public void freeInterval(IdInterval idInterval) {
        Assert.isTrue(this.freeIds.add(idInterval));
    }
}
