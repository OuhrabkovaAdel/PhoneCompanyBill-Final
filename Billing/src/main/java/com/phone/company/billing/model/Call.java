package com.phone.company.billing.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Call {
    private long phoneNumber;
    private LocalDateTime timeFrom;
    private LocalDateTime tomeTo;

    public void setCallFromCSV(String phoneNumber, String timeFrom, String tomeTo, String fromatter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fromatter);
        this.phoneNumber = Long.valueOf(phoneNumber);
        this.timeFrom =  LocalDateTime.parse(timeFrom, formatter);
        this.tomeTo =  LocalDateTime.parse(tomeTo, formatter);
    }

    // getting hour when call started
     public long getHourOfCall(){
         return timeFrom.getHour();
    }

    // processing LocalDayTimeValues to difference in minutes
    public int getTimeCallInMinutes(){
         long diffInMilli = (ZonedDateTime.of(tomeTo, ZoneId.systemDefault())).toInstant().toEpochMilli() - (ZonedDateTime.of(timeFrom, ZoneId.systemDefault())).toInstant().toEpochMilli();
         return (int) Math.ceil(diffInMilli * 0.00001666667);
     }

    public long getPhoneNumber() {
        return phoneNumber;
    }
}
