package com.tranred.utils;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javafx.util.StringConverter;

public class Fecha {
    private Date fecha;
    private Locale locale = new Locale("es", "VE");

    public Fecha() {
        this.fecha = new Date();
    }

    public Fecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Fecha sumarDias(Integer cantidad) {
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(this.fecha);
        selectedValue.add(5, cantidad);
        this.fecha = selectedValue.getTime();
        return this;
    }

    public Fecha restarDias(Integer cantidad) {
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(this.fecha);
        selectedValue.add(5, -cantidad);
        this.fecha = selectedValue.getTime();
        return this;
    }

    public Fecha restarAños(Integer cantidad) {
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(this.fecha);
        selectedValue.add(1, -cantidad);
        this.fecha = selectedValue.getTime();
        return this;
    }

    public Fecha añadirMinutos(Integer cantidad) {
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(this.fecha);
        selectedValue.add(12, cantidad);
        this.fecha = selectedValue.getTime();
        return this;
    }

    public String getString(String patron) {
        DateFormat outputFormat = new SimpleDateFormat(patron, this.locale);
        return outputFormat.format(this.fecha);
    }

    public String getJulianDate() {
        DateFormat outputFormat = new SimpleDateFormat("yyDDD", this.locale);
        return outputFormat.format(this.fecha);
    }

    public String getJulianDateDays() {
        DateFormat outputFormat = new SimpleDateFormat("DDD", this.locale);
        return outputFormat.format(this.fecha);
    }

    public int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.fecha);
        return c.get(7);
    }

    public int getDayOfMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.fecha);
        return c.get(5);
    }

    public static String getTimeFromMillis(Long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1L), TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1L));
        return hms;
    }

    public Fecha[] getFistAndLastDayOfMonthBefore() {
        Fecha[] fechas = new Fecha[2];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.fecha);
        calendar.add(2, -1);
        calendar.set(5, 1);
        Date firstDayOfMonth = calendar.getTime();
        fechas[0] = new Fecha(firstDayOfMonth);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.add(5, -1);
        Date lastDayOfMonth = calendar.getTime();
        fechas[1] = new Fecha(lastDayOfMonth);
        return fechas;
    }

    public void setToFistDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.fecha);
        calendar.set(5, 1);
        this.fecha = calendar.getTime();
    }

    public static String parseDate(String date, String inPattern, String outPattern) {
        DateFormat inputFormat = new SimpleDateFormat(inPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern);

        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException var6) {
            return null;
        }
    }

    public static Date parseDate(String date, String inPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);

        try {
            return inputFormat.parse(date);
        } catch (ParseException var4) {
            return null;
        }
    }

    public static StringConverter<LocalDate> getConverter(final String pattern) {
        return new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            public String toString(LocalDate date) {
                return date != null ? this.dateFormatter.format(date) : "";
            }

            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, this.dateFormatter) : null;
            }
        };
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
