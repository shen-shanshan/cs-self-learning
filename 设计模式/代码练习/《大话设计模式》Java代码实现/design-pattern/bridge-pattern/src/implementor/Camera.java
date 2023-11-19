package implementor;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-19
 * @Description: 手机软件（具体层次）
 */
public class Camera extends Implementor {
    @Override
    public String run() {
        return "使用相机拍照";
    }
}
