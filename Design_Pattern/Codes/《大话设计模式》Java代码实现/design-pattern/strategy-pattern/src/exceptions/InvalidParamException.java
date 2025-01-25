package exceptions;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-10-22
 * @Description: 非法参数异常类
 */
public class InvalidParamException extends RuntimeException {
    public InvalidParamException(String message) {
        super("您输入的参数[" + message + "]为无效值");
    }
}
