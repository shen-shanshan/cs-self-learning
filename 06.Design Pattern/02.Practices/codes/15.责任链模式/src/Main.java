public class Main {
    public static void main(String[] args) {
        FilterChain fc = new FilterChain();
        // 链式编程，每加入一个过滤器，就返回一个新的链
        fc.add(new FirstFilter()).add(new SecondFilter());
        // 处理消息
        fc.doFilter();

        FilterChain fc2 = new FilterChain();
        // FilterChain也实现了Filter接口
        // 可以将整条链当做一种过滤器来进行添加和应用
        fc2.add(fc).add(new SecondFilter());
        fc2.doFilter();
    }
}
