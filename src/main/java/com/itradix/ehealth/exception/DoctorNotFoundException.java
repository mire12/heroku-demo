package com.itradix.ehealth.exception;


public class DoctorNotFoundException extends Exception {
    private final String doctorName;

    public static DoctorNotFoundException createWith(String doctorName) {
        return new DoctorNotFoundException(doctorName);
    }

    private DoctorNotFoundException(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String getMessage() {
        return "doctor '" + doctorName + "' not found";
    }
}
