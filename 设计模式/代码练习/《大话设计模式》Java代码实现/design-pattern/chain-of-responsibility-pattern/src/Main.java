import handler.Handler;
import handler.LengthHandler;
import handler.NumberHandler;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Handler handler = new LengthHandler(new NumberHandler());

        List<String> requests = new ArrayList<>();
        requests.add("s00845127");
        requests.add("hhh22222d");
        requests.add("333");

        for (String request : requests) {
            if (handler.handle(request)) {
                System.out.println(request + "：是合法的");
            }
        }
    }
}