package com.wma.util

import org.springframework.data.convert.Jsr310Converters
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class LocalDateConverters {

    @Converter(autoApply = true)
    static class LocalDateTimeDateConverter implements AttributeConverter<LocalDateTime, Date> {

        @Override
        Date convertToDatabaseColumn(LocalDateTime date) {
            return Jsr310Converters.LocalDateTimeToDateConverter.INSTANCE.convert(date)
        }

        @Override
        LocalDateTime convertToEntityAttribute(Date date) {
            return Jsr310Converters.DateToLocalDateTimeConverter.INSTANCE.convert(date)
        }
    }

    @Converter(autoApply = true)
    static class LocalTimeDateConverter implements AttributeConverter<LocalDate, Date> {

        @Override
        Date convertToDatabaseColumn(LocalDate date) {
            return Jsr310Converters.LocalDateToDateConverter.INSTANCE.convert(date)
        }

        @Override
        LocalDate convertToEntityAttribute(Date date) {
            return Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(date)
        }
    }

    @Converter(autoApply = true)
    static class InstantDateConverter implements AttributeConverter<Instant, Date> {

        @Override
        Date convertToDatabaseColumn(Instant date) {
            return Jsr310Converters.LocalDateToDateConverter.INSTANCE.convert(date)
        }

        @Override
        Instant convertToEntityAttribute(Date date) {
            return Jsr310Converters.DateToLocalDateConverter.INSTANCE.convert(date)
        }
    }

    @Converter(autoApply = true)
    static class LocalTimeAttributeConverter implements AttributeConverter<LocalTime,Time> {
        @Override
        Time convertToDatabaseColumn(LocalTime localTime) {
            return (localTime == null ? null : Time.valueOf(localTime));
        }

        @Override
        LocalTime convertToEntityAttribute(Time time) {
            return (time == null ? null : time.toLocalTime());
        }
    }
}

