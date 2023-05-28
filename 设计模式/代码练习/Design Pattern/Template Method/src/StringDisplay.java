/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 模板方法模式 - 具体方法 2
 */
public class StringDisplay extends AbstractDisplay {

    String str;

    public StringDisplay(String str) {
        this.str = str;
    }

    @Override
    protected void open() {
        printLine();
    }

    @Override
    protected void print() {
        System.out.println("|" + str + "|");
    }

    @Override
    protected void close() {
        printLine();
    }

    private void printLine() {
        System.out.print("+");
        for (int i = 0; i < str.length(); i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
}
