import templates.TestPaper;
import templates.TestPaperA;
import templates.TestPaperB;

public class Main {
    public static void main(String[] args) {
        TestPaper studentA = new TestPaperA("小明");
        studentA.printScore();

        TestPaper studentB = new TestPaperB("小红");
        studentB.printScore();
    }
}