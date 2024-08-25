package modules;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 系统内部模块-1-基本工资
 */
public class ModuleOne {
    /**
     * @description: 发放基本工资
     * @author: SSS
     * @date: 2023/11/12 11:07
     * @param: 个人职级
     * @return: 基本工资
     **/
    public int getSalary(int level) {
        if (level == 14) {
            return 20000;
        }
        if (level == 15) {
            return 25000;
        }
        return 0;
    }
}
