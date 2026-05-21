package com.example.unilibrary.db;

import androidx.room.TypeConverter;

import com.example.unilibrary.enums.BookStatus;

public class Converters {

    @TypeConverter
    public static String fromBookStatus(BookStatus status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static BookStatus toBookStatus(String value){
        return value == null ? null : BookStatus.valueOf(value);
    }
}
