package cs125.healthhelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoodRecommendation implements Serializable {
    private String recipeName;
    private String ingredients;
    private String course;
    private String source;
    private String totalTime;

    public FoodRecommendation(String recipeName, String ingredients, String course, String source, String totalTime) {
        this.recipeName = recipeName;
        this.ingredients = ingredients.replaceAll("[\"\\[\\]]", "");
        this.course = course.replaceAll("[\"\\[\\]]", "");
        this.source = source;
        this.totalTime = totalTime + " sec";
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getIngredients() { return ingredients; }

    public String getCourse() { return course; }

    public String getSource() { return source; }

    public String getTotalTime() { return totalTime; }
}