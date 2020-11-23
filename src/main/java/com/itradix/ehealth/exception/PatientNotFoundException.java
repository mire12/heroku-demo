package com.itradix.ehealth.exception;


public class PatientNotFoundException extends Exception {
    private final String patientName;

    public static PatientNotFoundException createWith(String patientName) {
        return new PatientNotFoundException(patientName);
    }

    private PatientNotFoundException(String patientName) {
        this.patientName = patientName;
    }

    @Override
    public String getMessage() {
        return "Patient '" + patientName + "' not found";
    }
}
