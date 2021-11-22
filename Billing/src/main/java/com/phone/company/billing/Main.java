package com.phone.company.billing;

public class Main {


    public static void main(String[] args) {

        System.out.println("Final Phone Bill cost: ");
        System.out.println("\t" + TelephoneBillCalculator.calculate("Import/import.csv"));

    }
}
