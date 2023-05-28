public class Car implements Movable {
    @Override
    public void move() {
        System.out.println("开车！");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
