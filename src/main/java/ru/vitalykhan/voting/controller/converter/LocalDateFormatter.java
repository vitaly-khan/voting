package ru.vitalykhan.voting.controller.converter;

import org.springframework.format.Formatter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) {
        Assert.notNull(text, "Date string mustn't be null!" );
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate lt, Locale locale) {
        return lt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
