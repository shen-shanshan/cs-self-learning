public class Student {
    public void sduty(LearnStrategy ls) {
        ls.study();
    }

    public static void main(String[] args) {
        new Student().sduty(new EnglishLearnStrategy()); // 朗读
        new Student().sduty(new MathLearnStrategy()); // 刷题
    }
}
