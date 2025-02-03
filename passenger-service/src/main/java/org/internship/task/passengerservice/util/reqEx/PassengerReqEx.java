package org.internship.task.passengerservice.util.reqEx;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PassengerReqEx {
    public final static String PHONE_NUMBER = "^\\+375[\\- ]?\\(?\\d{2}\\)?[\\- ]?\\d{7,9}$";
}
