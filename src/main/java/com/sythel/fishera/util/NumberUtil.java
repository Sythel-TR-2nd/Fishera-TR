package com.sythel.fishera.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class NumberUtil {

    private static final DecimalFormat MONEY_FORMAT;

    static {

        DecimalFormatSymbols symbols =
                new DecimalFormatSymbols(Locale.US);

        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        MONEY_FORMAT =
                new DecimalFormat("#,##0.00", symbols);

    }

    private NumberUtil() {
    }

    public static String formatMoney(double value) {

        return MONEY_FORMAT.format(value);

    }

}