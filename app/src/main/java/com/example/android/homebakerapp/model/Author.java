package com.example.android.homebakerapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "authors",
        foreignKeys ={
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "local_id",
                        childColumns = "recipe_id",
                        onDelete = CASCADE)
        })
public class Author {

    private int id;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "author_recipe_relationship_id")
    private int authorRecipeRelationshipId;
    @ColumnInfo(name = "recipe_id")
    private int recipeId;
    private String name;
    private String website;

    public Author() {};

    public Author(String name, String website) {

        this.name = name;
        this.website = website;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorRecipeRelationshipId() {
        return authorRecipeRelationshipId;
    }

    public int getRecipeId() {
        return recipeId;
    }

}

