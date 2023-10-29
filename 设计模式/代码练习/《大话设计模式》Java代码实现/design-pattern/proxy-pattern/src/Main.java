import subject.Proxy;
import subject.RealSubject;
import subject.Subject;

public class Main {
    public static void main(String[] args) {
        Subject proxy = new Proxy("申杉杉", new RealSubject("申四四"));
        proxy.request();
    }
}