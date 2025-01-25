public class InterDemo {
    public static void main(String[] args) {
        Inter<String> i1 = new InterImpl<>();
        i1.show("100");
        Inter<Integer> i2 = new InterImpl<>();
        i2.show(100);

        Inter<?> i3 = new InterImpl<>();
        // i3.show(100);
    }
}

// 泛型通配符
// ？ ：表示任意类型
// ？ extends E ：向下限定，表示 E 及其子类
// ？ super E ：向上限定，表示 E 及其父类


