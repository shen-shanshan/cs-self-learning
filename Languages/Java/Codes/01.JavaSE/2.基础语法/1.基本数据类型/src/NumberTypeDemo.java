public class NumberTypeDemo {
    public static void main(String[] args) {
        byte a = 3;
        int b = 4;
        int c = a + b;
        // byte c = a + b;
        // java: 不兼容的类型: 从int转换到byte可能会有损失
        System.out.println(c);
        System.out.println("--------------------");

        // 解决：强制类型转换（可能会损失精度）
        byte c2 = (byte) (a + b);
        System.out.println(c2);
        System.out.println("--------------------");

        double d = 12.345;
        // float f = d;
        // java: 不兼容的类型: 从double转换到float可能会有损失
        float f = (float) d;
        System.out.println(f);
        System.out.println("--------------------");

        byte b1 = 3;
        byte b2 = 4;
        byte b3;
        // b3 = b1 + b2;
        // java: 不兼容的类型: 从int转换到byte可能会有损失
        // 类型提升，b1，b2会先转换为int
        b3 = 3 + 4;
        // 常量：先计算结果，若在byte范围内，则不报错
        System.out.println(b3);
        System.out.println("--------------------");

        byte e = (byte) 130;
        System.out.println(e);// -126
        System.out.println("--------------------");

        System.out.println("hello" + 'a' + 1);
        System.out.println('a' + "hello" + 1);
        System.out.println('a' + 1);
        System.out.println('a' + 1 + "hello");
        System.out.println("--------------------");

        int x = 3;
        int y = 4;
        System.out.println(x / y);
        System.out.println((x * 0.1) / y);
        System.out.println("--------------------");

        int z = 4;
        int zz = (z++) + (++z) + (z * 10);
        System.out.println(zz);
        System.out.println("--------------------");
    }
}
