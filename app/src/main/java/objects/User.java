package objects;

public class User {
    private String name;
    private String image;

    public int getDailyCalorieGoal() {
        return dailyCalorieGoal;
    }

    public void setDailyCalorieGoal(int dailyCalorieGoal) {
        this.dailyCalorieGoal = dailyCalorieGoal;
    }

    private int dailyCalorieGoal;

    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public String getFavFood() {
        return favFood;
    }
    private String favFood;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.image = "";
        this.favFood = "";
        this.dailyCalorieGoal = 1800;
    }
}
