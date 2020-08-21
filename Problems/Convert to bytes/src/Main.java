import java.io.InputStream;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;
        int b;
        while ((b = inputStream.read()) != -1) {
            System.out.print(b);
        }
    }
}