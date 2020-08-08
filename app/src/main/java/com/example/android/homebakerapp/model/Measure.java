package com.example.android.homebakerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "measures",
        foreignKeys ={
                @ForeignKey(entity = Ingredient.class,
                        parentColumns = "local_id",
                        childColumns = "ingredient_id",
                        onDelete = CASCADE)
        },
        indices = {
                @Index(value = {"ingredient_id"})
                // If removing this row, when building below warning would show:
                // warning: recipe_id column references a foreign key but it is not part of an index.
                // This may trigger full table scans whenever parent table is modified so you are highly advised to create an index that covers this column.
        })
public class Measure implements MeasurementSystem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    private int localId;
    @ColumnInfo(name = "ingredient_id")
    private int ingredientId;
    @ColumnInfo(name = "measurement_value")
    private double measurementValue;
    @ColumnInfo(name = "measurement_unit")
    private String measurementUnit;
    @ColumnInfo(name = " measurement_value_ref_unit")
    private double measurementValueRefUnit; // value in reference unit
    @ColumnInfo(name = "measurement_ref_unit")
    private String measurementRefUnit;

    @Ignore
    MeasurementSystem.measurementLocalSystem measurementLocalSystem = null; // Check interface for details
    @Ignore
    MeasurementSystem.measurementType measurementType = null; // Check interface for details

    @Ignore
    private static double ounceToGram = 28.35; // 1 ounce = 28.35 gr
    @Ignore
    private static double fluidOunceToML = 29.57; // 1 fluid ounce = 29.57 ml

    @Ignore
    private static String refUnitUsVol = "flOZ";
    @Ignore
    private static String refUnitUsWeight = "OZ";
    @Ignore
    private static String refUnitMetrVol = "ml";
    @Ignore
    private static String refUnitMetrWeight = "G";

    @Ignore
    private static List<String> measurementUnitUsVolType = Arrays.asList("TSP", "TBLSP", "flOZ", "CUP", "pint");
    @Ignore
    private static List<Double> measurementUnitUsVolInFlOz = Arrays.asList(0.17, 0.5, 1.0, 8.0, 16.0); // flOz is taken as reference unit

    @Ignore
    private static HashMap<String, Double> measurementUnitUsVol = buildMeasHashMap(measurementUnitUsVolType, measurementUnitUsVolInFlOz);

    @Ignore
    private static List<String> measurementUnitUsWeightType = Arrays.asList("OZ", "lb");
    @Ignore
    private static List<Double> measurementUnitUsWeightInOz = Arrays.asList(1.0, 16.0); // Oz is taken as reference unit

    @Ignore
    private static HashMap<String, Double> measurementUnitUsWeight = buildMeasHashMap(measurementUnitUsWeightType, measurementUnitUsWeightInOz);

    @Ignore
    private static List<String> measurementUnitMetrVolType = Arrays.asList("ml", "lt");
    @Ignore
    private static List<Double> measurementUnitMetrVolInMl = Arrays.asList(1.0, 1000.0); // ml is taken as reference unit

    @Ignore
    private static HashMap<String, Double> measurementUnitMetrVol = buildMeasHashMap(measurementUnitMetrVolType, measurementUnitMetrVolInMl);

    @Ignore
    private static List<String> measurementUnitMetrWeightType = Arrays.asList("G", "K");
    @Ignore
    private static List<Double> measurementUnitMetrWeightInGr = Arrays.asList(1.0, 1000.0); // gr is taken as reference unit

    @Ignore
    private static HashMap<String, Double> measurementUnitMetrWeight = buildMeasHashMap(measurementUnitMetrWeightType, measurementUnitMetrWeightInGr);

    @Ignore
    private static List<String> measurementUnitGeneric = Arrays.asList("UNIT");

    @Ignore
    private static List<List<String>> allMeasurementUnits = Arrays.asList(measurementUnitUsVolType, measurementUnitUsWeightType, measurementUnitMetrVolType, measurementUnitMetrWeightType, measurementUnitGeneric);

    @Ignore
    private static List<String> allMeasurementUnitsAsList = mergeAllMeasurementUnits();

    @Ignore
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
                this.setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem.uscs);
                this.measurementValueRefUnit = measurementUnitUsVol.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitUsVol;
            } else if (measurementUnitUsWeightType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.weight);
                this.setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem.uscs);
                this.measurementValueRefUnit = measurementUnitUsWeight.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitUsWeight;
            } else if (measurementUnitMetrVolType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.volume);
                this.setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem.metric);
                this.measurementValueRefUnit = measurementUnitMetrVol.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitMetrVol;
            } else if (measurementUnitMetrWeightType.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.weight);
                this.setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem.metric);
                this.measurementValueRefUnit = measurementUnitMetrWeight.get(measurementUnit)*measurementValue;
                this.measurementRefUnit = refUnitMetrWeight;
            } else if (measurementUnitGeneric.contains(measurementUnit)) {
                this.setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType.generic);
                this.setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem.generic);
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
    public void setMeasurementLocalSystem(com.example.android.homebakerapp.model.MeasurementSystem.measurementLocalSystem measurementLocalSystem) {
        this.measurementLocalSystem = measurementLocalSystem;
    }


    // interface method
    @Override
    public void setMeasurementType(com.example.android.homebakerapp.model.MeasurementSystem.measurementType measurementType) {
        this.measurementType = measurementType;
    }


    public MeasurementSystem.measurementLocalSystem getMeasurementLocalSystem() {
        return measurementLocalSystem;
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

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

}
