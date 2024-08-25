package modules;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 系统内部模块-2-奖金
 */
public class ModuleTwo {
    /**
     * @description: 发放年终奖
     * @author: SSS
     * @date: 2023/11/12 11:10
     * @param: 个人职级
     * @return: 年终奖
     **/
    public int getBonus(int level) {
        if (level == 14) {
            return 50000;
        }
        if (level == 15) {
            return 70000;
        }
        return 0;
    }
}
