public class Plane implements Movable {
    @Override
    public void move() {
        System.out.println("起飞！");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
