package com.example.unilibrary.db;

import androidx.room.TypeConverter;

import com.example.unilibrary.enums.BookStatus;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static String fromBookStatus(BookStatus status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static BookStatus toBookStatus(String value){
        return value == null ? null : BookStatus.valueOf(value);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime(); // Date → número pra salvar
    }

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value); // número → Date ao ler
    }
}
