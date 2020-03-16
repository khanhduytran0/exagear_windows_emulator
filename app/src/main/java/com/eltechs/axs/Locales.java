package com.eltechs.axs;

public abstract class Locales {
    public static final String CHARSET_DEU = "de_DE.UTF-8";
    public static final String CHARSET_FRA = "fr_FR.UTF-8";
    public static final String CHARSET_POL = "pl_PL.UTF-8";
    public static final String CHARSET_POR = "pt_PT.UTF-8";
    public static final String CHARSET_RUS = "ru_RU.UTF-8";
    public static final String CHARSET_SPA = "es_ES.UTF-8";
    private static final String[] SUPPORTED_LOCALES = {"C", "en_US", "en_US.UTF8", "ru_RU.CP1251", CHARSET_RUS, "pl_PL.CP1250", CHARSET_POL, "cs_CZ.CP1250", "cs_CZ.UTF-8", "de_DE.CP1252", CHARSET_DEU, "es_ES.CP1252", CHARSET_SPA, "fr_FR.CP1252", CHARSET_FRA, "pt_PT.CP1252", CHARSET_POR, "pt_BR.CP1252", "pt_BR.UTF-8"};

    public static String[] getSupportedLocales() {
        return (String[]) SUPPORTED_LOCALES.clone();
    }

    public static String guessLocale() {
        char c;
        String iSO3Language = Globals.getAppContext().getResources().getConfiguration().locale.getISO3Language();
        switch (iSO3Language.hashCode()) {
            case 98385:
                if (iSO3Language.equals("ces")) {
                    c = 3;
                    break;
                }
            case 99348:
                if (iSO3Language.equals("deu")) {
                    c = 4;
                    break;
                }
            case 101653:
                if (iSO3Language.equals("fra")) {
                    c = 5;
                    break;
                }
            case 111181:
                if (iSO3Language.equals("pol")) {
                    c = 2;
                    break;
                }
            case 111187:
                if (iSO3Language.equals("por")) {
                    c = 7;
                    break;
                }
            case 113296:
                if (iSO3Language.equals("rus")) {
                    c = 0;
                    break;
                }
            case 114084:
                if (iSO3Language.equals("spa")) {
                    c = 6;
                    break;
                }
            case 115868:
                if (iSO3Language.equals("ukr")) {
                    c = 1;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                return CHARSET_RUS;
            case 2:
            case 3:
                return CHARSET_POL;
            case 4:
                return CHARSET_DEU;
            case 5:
                return CHARSET_FRA;
            case 6:
                return CHARSET_SPA;
            case 7:
                return CHARSET_POR;
            default:
                return "C";
        }
    }
}
