package cs125.healthhelper;

/**
 * Created by Jacqueline on 3/11/2018.
 */

public class Exercise {

    public String exerciseName;
    public int durationMins;
    public int caloriesBurned;

    public void Exercise()
    {

    }

    public void Exercise(String name, int duration, int calories)
    {
        this.exerciseName = name;
        this.durationMins = duration;
        this.caloriesBurned = calories;
    }
}
