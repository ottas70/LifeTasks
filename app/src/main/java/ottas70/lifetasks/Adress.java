package ottas70.lifetasks;

/**
 * Created by Ottas on 10.1.2016.
 */
public class Adress {

    private String street = "";
    private String city = "";
    private String state = "";
    private String x;

    public Adress(String state) {
        this.state = state;
    }

    public Adress(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public Adress(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
