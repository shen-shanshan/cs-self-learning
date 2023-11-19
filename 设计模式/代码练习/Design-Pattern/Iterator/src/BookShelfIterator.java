/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-07
 * @Description: 迭代器模式 - 迭代器
 */
public class BookShelfIterator implements Iterator {

    private BookShelf bs;

    private int index;

    public BookShelfIterator(BookShelf bs) {
        this.bs = bs;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        if (index < bs.getLength()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object next() {
        Book book = (Book) bs.getBookAt(index);
        index++;
        return book;
    }

}
