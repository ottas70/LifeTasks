package ottas70.lifetasks;

import android.location.Location;
import android.widget.ImageView;

import java.util.ArrayList;

public class Task {

    private int id;
    private String nazev;
    private String informations;
    private int difficulty;
    private String mainPhotoUrl;
    private int upperTask_id;
    private ArrayList<ImageView> photos = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private boolean done;
    private Adress adress;
    private int points;
    private float distance;
    private double popularity;
    private int views;
    private boolean inProgress;
    private String ownersName;
    private double latitude;
    private double longtitude;

    public Task(int id,String nazev,String informations,int difficulty, String mainPhotoUrl,Adress adress, int upperTask_id,int points,int views,String ownersName,double latitude, double longtitude) {
        this.id = id;
        this.nazev = nazev;
        this.informations = informations;
        this.difficulty = difficulty;
        this.mainPhotoUrl = mainPhotoUrl;
        this.adress = adress;
        this.upperTask_id = upperTask_id;
        this.points = points;
        this.views = views;
        this.ownersName = ownersName;
        this.latitude = latitude;
        this.longtitude = longtitude;

    }

    public Task(String nazev,String informations,int difficulty, String mainPhotoUrl,Adress adress, int upperTask_id,int points,int views,String ownersName,double latitude, double longtitude) {
        this.nazev = nazev;
        this.informations = informations;
        this.difficulty = difficulty;
        this.mainPhotoUrl = mainPhotoUrl;
        this.adress = adress;
        this.upperTask_id = upperTask_id;
        this.points = points;
        this.views = views;
        this.ownersName = ownersName;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getInformations() {
        return informations;
    }

    public String getNazev() {
        return nazev;
    }

    public String getMainPhotoUrl() {
        return mainPhotoUrl;
    }

    public void setMainPhotoUrl(String mainPhotoUrl) {
        this.mainPhotoUrl = mainPhotoUrl;
    }

    public ArrayList<ImageView> getPhotos() {
        return photos;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Adress getAdress() {
        return adress;
    }

    public void setAdress(Adress adress) {
        this.adress = adress;
    }

    public int getUpperTask_id() {
        return upperTask_id;
    }

    public int getPoints() {
        return points;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = LifeTasks.round(popularity,1);
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public String getOwnersName() {
        return ownersName;
    }

    public void setOwnersName(String ownersName) {
        this.ownersName = ownersName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
