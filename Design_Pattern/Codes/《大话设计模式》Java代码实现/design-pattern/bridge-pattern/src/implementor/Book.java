package implementor;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-19
 * @Description: 电子书（具体层次）
 */
public class Book extends Implementor {
    @Override
    public String run() {
        return "看电子书";
    }
}
