package handler;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 判断数字个数是否合法（具体处理类）
 */
public class NumberHandler extends Handler {
    public NumberHandler() {
    }

    public NumberHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean handle(String request) {
        int count = 0;
        for (char c : request.toCharArray()) {
            if (c - '0' >= 0 && c - '0' <= 9) {
                count++;
            }
        }
        if (count != 8) {
            System.out.println(request + "：数字个数不合法");
            return false;
        }
        if (next != null) {
            next.handle(request);
        }
        return true;
    }
}
