/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-07
 * @Description: 迭代器模式
 */
public class Main {
    public static void main(String[] args) {
        BookShelf bs = new BookShelf();
        bs.appendBook(new Book("aaa"));
        bs.appendBook(new Book("bbb"));
        bs.appendBook(new Book("ccc"));
        System.out.println(bs.getLength());

        Iterator it = bs.iterator();
        while (it.hasNext()) {
            Book tmp = (Book) it.next();
            System.out.println(tmp.getName());
        }
    }
}
