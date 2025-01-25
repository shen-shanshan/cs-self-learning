package modules;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 系统内部模块-3-扣税
 */
public class ModuleThree {
    /**
     * @description: 扣税
     * @author: SSS
     * @date: 2023/11/12 11:13
     * @param: 个人职级
     * @return: 扣税比例
     **/
    public double getWage(int level) {
        if (level == 14) {
            return 0.9;
        }
        if (level == 15) {
            return 0.8;
        }
        return 0;
    }
}
