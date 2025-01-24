package templates;

/**
 * @Project: design-pattern
 * @Author: SSS
 * @CreateTime: 2023-11-05
 * @Description: 学生B的试卷
 */
public class TestPaperB extends TestPaper {
    public TestPaperB(String name) {
        super(name);
    }

    @Override
    public String getAnswer1() {
        return "A";
    }

    @Override
    public String getAnswer2() {
        return "B";
    }

    @Override
    public String getAnswer3() {
        return "C";
    }
}
