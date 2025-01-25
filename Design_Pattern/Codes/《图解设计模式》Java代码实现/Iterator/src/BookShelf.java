import java.util.ArrayList;
import java.util.List;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-07
 * @Description: 迭代器模式 - 集合
 */
public class BookShelf implements Aggregate {

    private List<Book> books;

    public BookShelf() {
        this.books = new ArrayList<>();
    }

    public Book getBookAt(int index) {
        return books.get(index);
    }

    public void appendBook(Book book) {
        books.add(book);
    }

    public int getLength() {
        return books.size();
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }

}
