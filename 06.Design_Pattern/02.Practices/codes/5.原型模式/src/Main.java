public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person();
        Person p2 = (Person) p1.clone();
        System.out.println(p2.loc.city); // 北京
        p1.loc.city = "上海";
        System.out.println(p2.loc.city); // 北京
    }
}
