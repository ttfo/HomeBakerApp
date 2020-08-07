package com.example.android.homebakerapp.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Measure implements MeasurementSystem {

    MeasurementSystem.measurmentLocalSystem measurmentLocalSystem = null; // Check interface for details
    MeasurementSystem.measurementType measurementType = null; // Check interface for details

    private static double ounceToGram = 28.35; // 1 ounce = 28.35 gr
    private static double fluidOunceToML = 29.57; // 1 fluid ounce = 29.57 ml

    private static String refUnitUsVol = "flOZ";
    private static String refUnitUsWeight = "OZ";
    private static String refUnitMetrVol = "ml";
    private static String refUnitMetrWeight = "G";

    private static List<String> measurementUnitUsVolType = Arrays.asList("TSP", "TBLSP", "flOZ", "CUP", "pint");
    private static List<Double> measurementUnitUsVolInFlOz = Arrays.asList(0.17, 0.5, 1.0, 8.0, 16.0); // flOz is taken as reference unit

    private static HashMap<String, Double> measurementUnitUsVol = buildMeasHashMap(measurementUnitUsVolType, measurementUnitUsVolInFlOz);

    private static List<String> measurementUnitUsWeightType = Arrays.asList("OZ", "lb");
    private static List<Double> measurementUnitUsWeightInOz = Arrays.asList(1.0, 16.0); // Oz is taken as reference unit

    private static HashMap<String, Double> measurementUnitUsWeight = buildMeasHashMap(measurementUnitUsWeightType, measurementUnitUsWeightInOz);

    private static List<String> measurementUnitMetrVolType = Arrays.asList("ml", "lt");
    private static List<Double> measurementUnitMetrVolInMl = Arrays.asList(1.0, 1000.0); // ml is taken as reference unit

    private static HashMap<String, Double> measurementUnitMetrVol = buildMeasHashMap(measurementUnitMetrVolType, measurementUnitMetrVolInMl);

    private static List<String> measurementUnitMetrWeightType = Arrays.asList("G", "K");
    private static List<Double> measurementUnitMetrWeightInGr = Arrays.asList(1.0, 1000.0); // gr is taken as reference unit

    private static HashMap<String, Double> measurementUnitMetrWeight = buildMeasHashMap(measurementUnitMetrWeightType, measurementUnitMetrWeightInGr);

    private static List<String> measurementUnitGeneric = Arrays.asList("UNIT");

    private static List<List<String>> allMeasurementUnits = Arrays.asList(measurementUnitUsVolType, measurementUnitUsWeightType, measurementUnitMetrVolType, measurementUnitMetrWeightType, measurementUnitGeneric);

    private static List<String> allMeasurementUnitsAsList = mergeAllMeasurementUnits();

    private double measurementValue;
    private String measurementUnit;
    private double measurementValueRefUnit; // value in reference unit
    private String measurementRefUnit;


    public Measure() {}

    // Constructor
    public Measure(double measurementValue, String measurementUnit) throws InvalidParameterException {
        this.measurementValue = measurementValue;

        // verify that measurementUnit is valid
        if (allMeasurementUnitsAsList.contains(measurementUnit)) {
            this.measurementUnit = measurementUnit;

            // defining what measurement system is being used, based on measurement unit provided
            if (measurementUnitUsVolType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.volume);
                this.setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem.uscs);
                this.measurementValueRefUnit = measurementUnitUsVol.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitUsVol;
            } else if (measurementUnitUsWeightType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.weight);
                this.setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem.uscs);
                this.measurementValueRefUnit = measurementUnitUsWeight.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitUsWeight;
            } else if (measurementUnitMetrVolType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.volume);
                this.setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem.metric);
                this.measurementValueRefUnit = measurementUnitMetrVol.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitMetrVol;
            } else if (measurementUnitMetrWeightType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.weight);
                this.setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem.metric);
                this.measurementValueRefUnit = measurementUnitMetrWeight.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitMetrWeight;
            } else if (measurementUnitGeneric.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.generic);
                this.setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem.generic);
            }

            return;
        } else {
            throw new InvalidParameterException();
        }

    }

    // TODO create methods to convert from US to metric

    // Helper method to build HashMap of measurement type and multiplier to reference unit
    private static HashMap<String, Double> buildMeasHashMap(List<String> ls, List<Double> ld) {

        Map<String, Double> measurementUnitUsVol = new HashMap<String, Double>();

        for (int i = 0; i < ls.size(); i++) {

            measurementUnitUsVol.put(ls.get(i), ld.get(i)); // populate HashMap with name of unit and factor to minimal unit in system
            // teaspoon (tsp) is 1/6 of fluid ounce
        };

        return (HashMap<String, Double>) measurementUnitUsVol;

    }


    public static List<String> mergeAllMeasurementUnits() {

        List<String> myMeasurementUnits = new ArrayList<String>();

        for (List<String> measList : allMeasurementUnits) {
            for (String meas : measList) {
                myMeasurementUnits.add(meas);
            }
        }

        return myMeasurementUnits;

    }

    public void setMeasurementUnit(String measurementUnit) throws InvalidParameterException {

        // 'measurementUnit' is the unit provided by the user
        // check if it matches with any of the available units

        if (allMeasurementUnitsAsList.contains(measurementUnit)) {
            this.measurementUnit = measurementUnit;
        } else {
            throw new InvalidParameterException();
        }

    }


    public void setMeasurementValue (double measurementValue) {
        this.measurementValue = measurementValue;
    }


    // interface method
    @Override
    public void setMeasurmentLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurmentLocalSystem measurmentLocalSystem) {
        this.measurmentLocalSystem = measurmentLocalSystem;
    }


    // interface method
    @Override
    public void setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType measurementType) {
        this.measurementType = measurementType;
    }


    public MeasurementSystem.measurmentLocalSystem getMeasurmentLocalSystem() {
        return measurmentLocalSystem;
    }


    public MeasurementSystem.measurementType getMeasurementType() {
        return measurementType;
    }


    public double getMeasurementValue() {
        return measurementValue;
    }


    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public double getMeasurementValueRefUnit() {
        return measurementValueRefUnit;
    }

    public void setMeasurementValueRefUnit(double measurementValueRefUnit) {
        this.measurementValueRefUnit = measurementValueRefUnit;
    }

    public String getMeasurementRefUnit() {
        return measurementRefUnit;
    }

    public void setMeasurementRefUnit(String measurementRefUnit) {
        this.measurementRefUnit = measurementRefUnit;
    }

}
