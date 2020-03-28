package objects;

import java.util.ArrayList;

public class DataContainer {

    public DataContainer(ArrayList<Food> branded, ArrayList<Food> self, ArrayList<Food> commmon) {
        this.branded = branded;
        this.self = self;
        this.common = common;
    }

    public ArrayList<Food> getBranded() {
        return branded;
    }
    public ArrayList<Food> getSelf() {
        return self;
    }
    public ArrayList<Food> getCommon() {
        return common;
    }
    private ArrayList<Food> branded;
    private ArrayList<Food> self;
    private ArrayList<Food> common;
}
