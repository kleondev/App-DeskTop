package com.tranred.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Numero {
    public static final String PATTERN_PUNTO_DECIMAL = "#,##0.00";
    public static final String PATTERN_DECIMAL = "0.00";
    public static final Locale LOCALE_US;
    public static final Locale LOCALE_VE;
    private final DecimalFormat formateador;

    public Numero() {
        this.formateador = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(LOCALE_VE));
    }

    public Numero(String pattern) {
        this.formateador = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(LOCALE_VE));
    }

    public Numero(String pattern, Locale locale) {
        this.formateador = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
    }

    public String formatNumeroSinPunto(String str) {
        if (str == null) {
            return null;
        } else {
            Double monto = Double.parseDouble(str);
            return this.formatNumero(monto);
        }
    }

    public String formatNumeroSinPunto(Integer str) {
        if (str == null) {
            return null;
        } else {
            Double monto = str.doubleValue();
            return this.formatNumero(monto);
        }
    }

    public String formatNumeroSinPunto(Long str) {
        if (str == null) {
            return null;
        } else {
            Double monto = str.doubleValue();
            return this.formatNumero(monto);
        }
    }

    public String formatNumero(String str) {
        if (str == null) {
            return null;
        } else {
            Double monto = Double.parseDouble(str);
            return this.formatNumero_(monto);
        }
    }

    public String formatNumero_(Double str) {
        if (str == null) {
            return null;
        } else {
            this.formateador.setMaximumFractionDigits(2);
            return this.formateador.format(str);
        }
    }

    public String formatNumero(BigDecimal str) {
        return str == null ? null : this.formatNumero_(str.setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public String formatNumero(Double str) {
        if (str == null) {
            return null;
        } else {
            str = str / 100.0D;
            this.formateador.setMaximumFractionDigits(2);
            return this.formateador.format(str);
        }
    }

    public static Long sum(String... str) {
        Long n = 0L;
        String[] var2 = str;
        int var3 = str.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            n = n + Long.parseLong(s);
        }

        return n;
    }

    public static BigDecimal sum(BigDecimal... augends) {
        BigDecimal bd = new BigDecimal(0.0D);
        BigDecimal[] var2 = augends;
        int var3 = augends.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            BigDecimal a = var2[var4];
            bd = bd.add(a);
        }

        return bd;
    }

    static {
        LOCALE_US = Locale.US;
        LOCALE_VE = new Locale("es", "VE");
    }
}
