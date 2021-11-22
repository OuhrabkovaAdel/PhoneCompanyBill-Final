package com.phone.company.billing;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneBillCalculatorTest {

    @org.junit.jupiter.api.Test
    void calculateTest1() {
        String importFileTest = "C:\\Users\\Adel\\Desktop\\PhoneCompanyBill\\Billing\\src\\test\\java\\com\\phone\\company\\billing\\importTest1.csv";
        BigDecimal finalCost = TelephoneBillCalculator.calculate(importFileTest);
        assertEquals(new BigDecimal("48.3"), finalCost);
    }

    @org.junit.jupiter.api.Test
    void calculateTest2() {
        String importFileTest = "C:\\Users\\Adel\\Desktop\\PhoneCompanyBill\\Billing\\src\\test\\java\\com\\phone\\company\\billing\\importTest2.csv";
        BigDecimal finalCost = TelephoneBillCalculator.calculate(importFileTest);
        assertEquals(new BigDecimal("7.7"), finalCost);
    }
}