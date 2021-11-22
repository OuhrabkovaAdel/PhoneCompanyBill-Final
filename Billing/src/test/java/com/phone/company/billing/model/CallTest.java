package com.phone.company.billing.model;

import static org.junit.jupiter.api.Assertions.*;

class CallTest {

    @org.junit.jupiter.api.Test
    void setCallFromCSV() {
        Call call = new Call();
        call.setCallFromCSV("420774577453","13-01-2020 18:10:15","13-01-2020 18:12:57", "dd-MM-yyyy HH:mm:ss");
        assertEquals(18L, call.getHourOfCall());
        assertEquals(3, call.getTimeCallInMinutes());
        assertEquals(420774577453L, call.getPhoneNumber());
    }
}