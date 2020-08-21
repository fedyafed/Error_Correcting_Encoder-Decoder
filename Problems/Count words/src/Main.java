import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // start coding here
        int count = 0;
        boolean isWord = false;
        int ch;
        while ((ch = reader.read()) != -1) {
            if (Character.isLetter(ch)) {
                if (!isWord) {
                    isWord = true;
                    count++;
                }
            } else {
                isWord = false;
            }
        }
        System.out.println(count);
        reader.close();
    }
}
