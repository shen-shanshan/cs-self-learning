package facade;

import modules.ModuleOne;
import modules.ModuleThree;
import modules.ModuleTwo;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-12
 * @Description: 外观（门面）类
 */
public class Facade {
    ModuleOne moduleOne;
    ModuleTwo moduleTwo;
    ModuleThree moduleThree;

    public Facade() {
        moduleOne = new ModuleOne();
        moduleTwo = new ModuleTwo();
        moduleThree = new ModuleThree();
    }

    /**
     * @description: 计算税前工资
     * @author: SSS
     * @date: 2023/11/12 11:21
     * @param: 个人职级
     * @return: 税前工资
     **/
    public double getSalaryWithoutTax(int level) {
        if (level < 14 || level > 15) {
            return 0;
        }
        return moduleOne.getSalary(level) * 12 + moduleTwo.getBonus(level);
    }

    /**
     * @description: 计算到手工资
     * @author: SSS
     * @date: 2023/11/12 11:21
     * @param: 个人职级
     * @return: 到手工资
     **/
    public double getSalaryWithTax(int level) {
        return getSalaryWithoutTax(level) * moduleThree.getWage(level);
    }
}
