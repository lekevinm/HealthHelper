package cs125.healthhelper;

import java.util.List;

/**
 * Created by Jacqueline on 3/6/2018.
 */

public class UserProfile {

    public int height;
    public int weight;
    public List allergies;

    public UserProfile()
    {

    }

    public UserProfile(int height, int weight, List allergies)
    {
        this.height = height;
        this.weight = weight;
        this.allergies = allergies;
    }
}
