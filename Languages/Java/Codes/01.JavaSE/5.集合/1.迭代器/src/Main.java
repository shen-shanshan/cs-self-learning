import java.util.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> c = new ArrayList<>();
        c.add("hello");
        c.add("world");
        c.add("java");
        // 通过集合获取迭代器对象
        ListIterator<String> lit = c.listIterator();
        while (lit.hasNext()) {
            String s = (String) lit.next();
            // System.out.println(s);
            lit.add("hhh");
        }
    }
}

