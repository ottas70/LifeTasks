package ottas70.lifetasks;

public class Review {

    private String nadpis;
    private int stars;
    private String text;
    User user;

    public Review(String nadpis, int stars, String text, User user) {
        this.nadpis = nadpis;
        this.stars = stars;
        this.text = text;
        this.user = user;
    }

    public int getStars() {
        return stars;
    }

    public String getNadpis() { return nadpis; }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }
}
