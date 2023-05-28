public class Location implements Cloneable {
    String city;
    String school;

    public Location(String city, String school) {
        this.city = city;
        this.school = school;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
