package com.bluestone.holidayservice.model;

public enum HolidaysApiQueryParams {
    KEY,
    COUNTRY,
    YEAR;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
