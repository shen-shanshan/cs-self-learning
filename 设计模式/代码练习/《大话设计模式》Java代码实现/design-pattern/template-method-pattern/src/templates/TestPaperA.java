package templates;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 学生A的试卷
 */
public class TestPaperA extends TestPaper {
    public TestPaperA(String name) {
        super(name);
    }

    @Override
    public String getAnswer1() {
        return "C";
    }

    @Override
    public String getAnswer2() {
        return "B";
    }

    @Override
    public String getAnswer3() {
        return "A";
    }
}
