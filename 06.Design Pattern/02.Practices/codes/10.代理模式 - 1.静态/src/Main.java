public class Main {
    public static void main(String[] args) {
        new CarLogProxy(new CarTimeProxy(new Car())).move();
        // 开车
        // time = 3s
        // 停车
    }
}
