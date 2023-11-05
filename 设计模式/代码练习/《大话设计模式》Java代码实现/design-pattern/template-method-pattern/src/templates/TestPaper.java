package templates;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 模板类 - 试卷
 */
public abstract class TestPaper {
    private String name;

    private int score;

    public TestPaper(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * @description: 模板方法
     * @author: SSS
     * @date: 2023/11/5 17:34
     **/
    public void printScore() {
        int score1 = getAnswer1().equals("A") ? 30 : 0;
        score += score1;
        System.out.println(name + "第一题的得分为：" + score1);
        int score2 = getAnswer2().equals("B") ? 30 : 0;
        score += score2;
        System.out.println(name + "第二题的得分为：" + score2);
        int score3 = getAnswer3().equals("C") ? 40 : 0;
        score += score3;
        System.out.println(name + "第三题的得分为：" + score3);
        System.out.println(name + "的总分为：" + score);
    }

    public abstract String getAnswer1();

    public abstract String getAnswer2();

    public abstract String getAnswer3();
}
