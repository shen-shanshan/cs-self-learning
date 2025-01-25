/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-08
 * @Description: 模板方法模式 - 具体方法 1
 */
public class CharDisplay extends AbstractDisplay {

    char c;

    public CharDisplay(char c) {
        this.c = c;
    }

    @Override
    protected void open() {
        System.out.print("<<");
    }

    @Override
    protected void print() {
        System.out.print(c);
    }

    @Override
    protected void close() {
        System.out.println(">>");
    }
}
