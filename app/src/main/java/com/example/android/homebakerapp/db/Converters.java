package com.example.android.homebakerapp.db;

import androidx.room.TypeConverter;

import com.example.android.homebakerapp.model.IngredientType;
import com.example.android.homebakerapp.model.MeasurementSystem;

// REF. https://developer.android.com/training/data-storage/room/referencing-data
public class Converters {

    /**
     * Convert IngredientCategory to String
     * also REF. https://stackoverflow.com/questions/57326789/how-to-save-enum-field-in-the-database-room
     */
    @TypeConverter
    public static String convertIngredientCategoryEnumToString(IngredientType.ingredientCategory iC) {
        return iC.name(); // REF. https://www.tutorialspoint.com/java/lang/enum_name.htm
    }

    /**
     * Convert String to IngredientCategory enum
     * also REF. https://stackoverflow.com/questions/57326789/how-to-save-enum-field-in-the-database-room
     * && https://stackoverflow.com/questions/604424/how-to-get-an-enum-value-from-a-string-value-in-java
     */
    @TypeConverter
    public static IngredientType.ingredientCategory convertStringToIngredientCategoryEnum(String iCAsString) {
        return IngredientType.ingredientCategory.valueOf(iCAsString);
    }

    /**
     * Convert MeasurementType to String
     */
    @TypeConverter
    public static String convertMeasurementTypeEnumToString(MeasurementSystem.measurementType mT) {
        return mT.name(); // REF. https://www.tutorialspoint.com/java/lang/enum_name.htm
    }

    /**
     * Convert String to MeasurementType enum
     */
    @TypeConverter
    public static MeasurementSystem.measurementType convertStringToMeasurementTypeEnum(String mTAsString) {
        return MeasurementSystem.measurementType.valueOf(mTAsString);
    }

    /**
     * Convert MeasurementLocalSystem to String
     */
    @TypeConverter
    public static String convertMeasurementLocalSystemEnumToString(MeasurementSystem.measurementLocalSystem mLS) {
        return mLS.name(); // REF. https://www.tutorialspoint.com/java/lang/enum_name.htm
    }

    /**
     * Convert String to MeasurementLocalSystem enum
     */
    @TypeConverter
    public static MeasurementSystem.measurementLocalSystem convertStringToMeasurementLocalSystemEnum(String mLSAsString) {
        return MeasurementSystem.measurementLocalSystem.valueOf(mLSAsString);
    }

}
