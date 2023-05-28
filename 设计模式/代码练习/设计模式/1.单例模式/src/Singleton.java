// 饿汉式
//public class Singleton {
//    // 创建实例
//    private static final Singleton instance = new Singleton();
//
//    // 私有构造方法
//    private Singleton() {
//    }
//
//    // 提供get方法
//    public static Singleton getInstance() {
//        return instance;
//    }
//}

// 懒汉式
//public class Singleton {
//    private volatile static Singleton instance = null;
//
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        // DCL（第一次判断）
//        if (instance == null) {
//            synchronized (Singleton.class) {
//                // DCL（第二次判断）
//                if (instance == null) {
//                    // 创建对象
//                    instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }
//}

// 静态内部类
//public class Singleton {
//    private Singleton() {
//    }
//
//    // 静态内部类
//    private static class SingletonFactory {
//        // 当内部类被调用时才创建对象
//        private static Singleton instance = new Singleton();
//    }
//
//    public static Singleton getInstance() {
//        return SingletonFactory.instance;
//    }
//}

// 枚举
//public enum Singleton {
//    Instance;
//}