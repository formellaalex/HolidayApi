package com.bluestone.holidayservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidaysApiResponse {

    private List<Holiday> holidays = new ArrayList<>();

    @Getter
    @Setter
    public static class Holiday {

        private String date;
        private String country;

        @JsonProperty("public")
        private String pub;
        private String name;
        private Weekday weekday;
        private String uuid;
        private String observed;

        @Getter
        @Setter
        public static class Weekday {
            private Observed date;
            private Observed observed;
        }

        @Getter
        @Setter
        public static class Observed {
            private String name;
            private String numeric;
        }
    }
}
