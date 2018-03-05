package cs125.healthhelper;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {
    private String mRecommended;
    private Integer amount; // to be implemented later at your discretion

    public Recommendation(String recommended) {
        mRecommended = recommended;
    }

    public String getRecommended() {
        return mRecommended;
    }
}