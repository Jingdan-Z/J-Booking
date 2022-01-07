package com.travel.staybooking.exception;

public class InvalidStayAddressException extends RuntimeException {
    public InvalidStayAddressException(String message) {
        super(message);
    }
}
