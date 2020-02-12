package com.eltechs.axs.productsRegistry;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.ed.BuildConfig;

public abstract class ProductIDs {
    public static final int ARCANUM_DEMO = 1;
    public static final int CIV3 = 8;
    public static final int DOOM = 0;
    public static final int ENGLISH123 = 5;
    public static final int HERETIC = 3;
    public static final int HEROES2 = 7;
    public static final int HEROES3 = 4;
    public static final int OFFICE_DEMO = 2;
    public static final int PETKA = 6;
    public static final int RPG = 10;
    public static final int SHTIRLITZ1 = 11;
    public static final int STRATEGIES = 9;
    public static final int WDESKTOP = 12;

    private ProductIDs() {
    }

    public static final String getPackageName(int i) {
        switch (i) {
            case 0:
                return "com.eltechs.doombyeltechs";
            case 1:
                return "com.eltechs.arcanum";
            case 2:
                return "com.eltechs.msoffice";
            case 3:
                return "com.eltechs.hereticbyeltechs";
            case 4:
            case 7:
            case 8:
                Assert.state(false, String.format("The product %d has been discontinued.", new Object[]{Integer.valueOf(i)}));
                break;
            case 5:
                return "com.eltechs.english123";
            case 6:
                return "ru.buka.petka1";
            case 9:
                return "com.eltechs.es";
            case 10:
                return "com.eltechs.erpg";
            case 11:
                return "ru.buka.shtirlitz_1";
            case 12:
                return BuildConfig.APPLICATION_ID;
        }
        Assert.isTrue(false, "Invalid product ID");
        return null;
    }
}
