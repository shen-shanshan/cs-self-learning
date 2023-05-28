public class Person {
    // 个人信息
    private int id;
    private String name;
    private int age;
    private double weight;
    private int score;
    private Location loc;

    public static class PersonBuilder {
        Person p = new Person();

        public PersonBuilder basicInfo(int id, String name, int age) {
            p.id = id;
            p.name = name;
            p.age = age;
            return this;
        }

        public PersonBuilder weight(double weight) {
            p.weight = weight;
            return this;
        }

        public PersonBuilder score(int score) {
            p.score = score;
            return this;
        }

        public PersonBuilder loc(String city, String street) {
            p.loc = new Location(city, street);
            return this;
        }

        public Person build() {
            return p;
        }
    }
}
