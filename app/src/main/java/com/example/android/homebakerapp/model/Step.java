package com.example.android.homebakerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "steps",
        foreignKeys ={
            @ForeignKey(entity = Recipe.class,
            parentColumns = "local_id",
            childColumns = "recipe_id",
            onDelete = CASCADE)
        },
        indices = {
                @Index(value = {"recipe_id"})
                // If removing this row, when building below warning would show:
                // warning: recipe_id column references a foreign key but it is not part of an index.
                // This may trigger full table scans whenever parent table is modified so you are highly advised to create an index that covers this column.
        })
public class Step implements Serializable {

    private int id;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    private int localId;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    @Ignore
    public Step() {}

    public Step(int id, String shortDescription) {
        super();
        this.id = id;
        this.shortDescription = shortDescription;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getShortDescription() {
        return shortDescription;
    }


    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getVideoURL() {
        return videoURL;
    }


    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }


    public String getThumbnailURL() {
        return thumbnailURL;
    }


    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getLocalId() {
        return localId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

}

