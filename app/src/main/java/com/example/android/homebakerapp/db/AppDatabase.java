package com.example.android.homebakerapp.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android.homebakerapp.model.Author;
import com.example.android.homebakerapp.model.Ingredient;
import com.example.android.homebakerapp.model.Measure;
import com.example.android.homebakerapp.model.Recipe;
import com.example.android.homebakerapp.model.Step;

// REF. https://developer.android.com/reference/android/arch/persistence/room/Database.html
@Database(entities = {Recipe.class, Step.class, Ingredient.class, Author.class, Measure.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "homebaker";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2) // REF. https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientDao();
    public abstract AuthorDao authorDao();
    public abstract MeasureDao measureDao();

    // REF. https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE recipes ADD COLUMN notes VARCHAR(65535)");
        }
    };
}
