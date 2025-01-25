import components.Component;
import components.Person;
import decorators.Cloth;
import decorators.Pant;
import decorators.Shoe;

public class Main {
    public static void main(String[] args) {
        Component person = new Person("申杉杉");
        Component decoratedPerson = new Shoe(new Pant(new Cloth(person)));
        decoratedPerson.show();
    }
}