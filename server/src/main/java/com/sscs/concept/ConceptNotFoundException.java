package com.sscs.concept;

public class ConceptNotFoundException extends RuntimeException {
    public ConceptNotFoundException(String cui) {
        super("Could not found concept cui: " + cui);
    }
}
