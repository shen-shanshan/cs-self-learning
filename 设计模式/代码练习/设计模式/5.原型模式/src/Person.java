public class Person implements Cloneable {
    int age = 10;
    int score = 100;

    Location loc = new Location("北京", "北京交通大学");

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Person p = (Person) super.clone();
        p.loc = (Location) loc.clone();
        return p;
    }
}
