package com.phone.company.billing;

import com.phone.company.billing.model.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


public class TelephoneBillCalculator {
    private static final Logger log = LoggerFactory.getLogger(TelephoneBillCalculator.class);

    public static BigDecimal calculate(String phoneLog) {
        log.debug("In calculate(): " + phoneLog);

        Map<Long, BigDecimal> mapOfCostPerNumber = new HashMap<>();
        Map<Long, Integer> mapOfCallPerNumber = new HashMap<>();

        BigDecimal finalSum = new BigDecimal("0");

        String line = "";
        String splitBy = ",";
       String formatter = "dd-MM-yyyy HH:mm:ss";

        try {
            BufferedReader br = new BufferedReader(new FileReader(phoneLog));
            while ((line = br.readLine()) != null) {
                BigDecimal costPerCall = new BigDecimal(0.0);
                String[] oneLine;
                oneLine = line.split(splitBy);
                log.debug("In calculate() -> phone: " + oneLine[0] + ", from:" + oneLine[1] + ", to:" + oneLine[2]);

                // setting values from file to new Call object
                Call call = new Call();
                call.setCallFromCSV(oneLine[0], oneLine[1], oneLine[2], formatter);


                // processing LocalDayTimeValues to difference in minutes
                int timeOfCallInMinutes = call.getTimeCallInMinutes();

                // getting hour when call started
                long hour = call.getHourOfCall();
                log.debug("In calculate() -> hour: " + hour + ", diffInMin:" + timeOfCallInMinutes);

                // getting phoneNumber called
                long phoneNumber = call.getPhoneNumber();

                // calculating cost for one call
                if (hour > 16 || hour < 8) {
                    if (timeOfCallInMinutes > 5) {
                        costPerCall = costPerCall.add(BigDecimal.valueOf(2.5 + ((timeOfCallInMinutes - 5) * 0.2)));
                        log.debug("In calculate() out if main hours -> costPerCall: " + costPerCall);
                    } else {
                        costPerCall = costPerCall.add(BigDecimal.valueOf(timeOfCallInMinutes * 0.5));
                        log.debug("In calculate() out if main hours -> costPerCall: " + costPerCall);
                    }
                } else {
                    if (timeOfCallInMinutes > 5) {
                        costPerCall = costPerCall.add(BigDecimal.valueOf(5 + ((timeOfCallInMinutes - 5) * 0.2)));
                        log.debug("In calculate() in main hours -> costPerCall: " + costPerCall);
                    } else {
                        costPerCall = costPerCall.add(BigDecimal.valueOf(timeOfCallInMinutes));
                        log.debug("In calculate() in if main hours -> costPerCall: " + costPerCall);
                    }
                }

                // adding info about call cost to Map<phoneNumber, Cost>
                if (mapOfCostPerNumber.containsKey(phoneNumber)) {
                    BigDecimal valueForNumber = mapOfCostPerNumber.get(phoneNumber);
                    mapOfCostPerNumber.replace(phoneNumber, valueForNumber.add(costPerCall));
                } else {
                    mapOfCostPerNumber.put(phoneNumber, costPerCall);
                }

                // adding info about calls per phone number to Map<phoneNumber, numberOfCalls>
                if (mapOfCallPerNumber.containsKey(phoneNumber)) {
                    int valueForNumber = mapOfCallPerNumber.get(phoneNumber);
                    mapOfCallPerNumber.replace(phoneNumber, ++valueForNumber);
                } else {
                    mapOfCallPerNumber.put(phoneNumber, 1);
                }

                log.debug("In calculate() mapOfCallPerNumber: " + mapOfCallPerNumber.keySet() + mapOfCallPerNumber.values());
                log.debug("In calculate() mapOfCallPerNumber: " + mapOfCostPerNumber.keySet() + mapOfCostPerNumber.values());
            }
            finalSum = finalCostProcessing(mapOfCostPerNumber, mapOfCallPerNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalSum;
    }

    private static BigDecimal finalCostProcessing(Map<Long, BigDecimal> mapOfCostPerNumber, Map<Long, Integer> mapOfCallPerNumber) {
        log.debug("In finalCostProcessing()");

        BigDecimal sum = new BigDecimal("0");

        // getting final cost of calls from all numbers
        Collection<BigDecimal> costValues = mapOfCostPerNumber.values();
        for (BigDecimal cost : costValues) {
            sum = sum.add(cost);
        }

        // getting max number of calls from one number
        Integer maxCalls = mapOfCallPerNumber.values()
                .stream()
                .max(Integer::compare)
                .get();

        // getting phoneNumber with max number of calls
        // if more then one getting higher number
        Long mostCalledNumber = mapOfCallPerNumber.entrySet()
                .stream()
                .filter(entry -> maxCalls.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .max(Long::compareTo)
                .  get();


        // returning final cost without call cost from phoneNumber with max number of calls
        BigDecimal costFromPhoneNumberWithMaxCalls = mapOfCostPerNumber.get(mostCalledNumber);
        log.debug("In finalCostProcessing() FinalSum: " + sum  + " - " + costFromPhoneNumberWithMaxCalls);
        return sum.subtract(costFromPhoneNumberWithMaxCalls);
    }
}

