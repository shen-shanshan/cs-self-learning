public class Main {
    public static void main(String[] args) {
        Person p = new Person.PersonBuilder()
                .basicInfo(1, "申杉杉", 23)
                //.weight(60.5)
                //.score(99)
                .loc("北京", "上园村3号")
                .build();
    }
}
