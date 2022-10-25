package com.tranred.utils;

import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateLabelFormatter extends AbstractFormatter {
    private static final long serialVersionUID = 1L;
    private final String datePattern = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public DateLabelFormatter() {
    }

    public Object stringToValue(String text) throws ParseException {
        return this.dateFormatter.parseObject(text);
    }

    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar)value;
            return this.dateFormatter.format(cal.getTime());
        } else {
            return "";
        }
    }
}
