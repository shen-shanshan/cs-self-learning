public class FirstFilter implements Filter {
    @Override
    public void doFilter() {
        // 处理消息中的非法字符
    }
}

class SecondFilter implements Filter {
    @Override
    public void doFilter() {
        // 将消息中的字母都换为小写
    }
}