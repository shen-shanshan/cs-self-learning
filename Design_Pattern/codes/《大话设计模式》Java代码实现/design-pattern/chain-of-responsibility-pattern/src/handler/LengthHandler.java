package handler;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-26
 * @Description: 判断长度是否合法（具体处理类）
 */
public class LengthHandler extends Handler {
    public LengthHandler() {
    }

    public LengthHandler(Handler next) {
        super(next);
    }

    @Override
    public boolean handle(String request) {
        if (request.length() != 9) {
            System.out.println(request + "：工号长度不合法");
            return false;
        }
        // 传递给链条的下一个节点进行处理
        if (next != null) {
            return next.handle(request);
        }
        return true;
    }
}
