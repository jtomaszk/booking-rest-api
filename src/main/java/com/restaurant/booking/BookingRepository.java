package com.restaurant.booking;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository {

    private static List<Booking> db = new LinkedList<>();

    public void add(Booking booking) {
        db.add(booking);
    }

    public List<Booking> get(LocalDate forDate) {
        return db.stream().filter(i -> i.getDate().equals(forDate)).collect(Collectors.toList());
    }
}
