package com.bluestone.holidayservice.model;

public enum HolidaysApiQueryParams {
    KEY,
    COUNTRY,
    YEAR,
    PUBLIC;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
