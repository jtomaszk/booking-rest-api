package com.restaurant.booking;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Booking {
    private String name;
    private int tableSize;
    private LocalDate date;
    private LocalTime startTime;
}
