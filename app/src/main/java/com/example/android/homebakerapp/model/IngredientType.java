package com.example.android.homebakerapp.model;

/**
 * @author matbe
 * defines common types/categories of ingredients in baking,
 * as they will undergo different types of calculations
 */
public interface IngredientType {

    // GUIDELINES ON INGREDIENT TYPES:
    // liquid can be water or milk etc.,
    // fat can be oil or butter or cream cheese or melted chocolate etc.,
    // yeastOrLeaveningAgent can be sourdough starter or baking powder etc.,
    // sweetener can be sugar etc., seasoning can be salt or pepper etc.,
    // flavoring can be cocoa powder or vanilla extract
    enum ingredientCategory { liquid, fat, yeastOrLeaveningAgent, flourOrSolid, sweetener, seasoning, flavoring, egg, other; };

    public void setIngredientCategory(IngredientType.ingredientCategory ingredientCategory);

}
