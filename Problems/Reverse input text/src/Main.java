import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        Deque<Character> deque = new LinkedList<>();
        int ch;
        while ((ch = reader.read()) != -1) {
            deque.push((char) ch);
        }
        while (!deque.isEmpty()) {
            System.out.print(deque.poll());
        }
        reader.close();
    }
}