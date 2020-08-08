package com.example.android.homebakerapp.model;

/**
 * @author matbe
 * interface that defines allowed measurement systems
 */
public interface MeasurementSystem {
    // For enum type, ref. https://stackoverflow.com/questions/24715096/how-to-only-allow-certain-values-as-parameter-for-a-method-in-java
    enum measurementLocalSystem { uscs , metric, generic; }; // can be metric or United States customary system (USCS), ref. https://en.wikipedia.org/wiki/United_States_customary_units
    enum measurementType { volume , weight, generic; }; // can be volume or weight

    // Ref. currency example @ https://stackoverflow.com/questions/15318796/enum-implementation-inside-interface-java
    public void setMeasurementLocalSystem(MeasurementSystem.measurementLocalSystem measurementLocalSystem);
    public void setMeasurementType(MeasurementSystem.measurementType measurementType);

}
