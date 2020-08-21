import java.io.*;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        // implement the method
        try (CharArrayWriter writer = new CharArrayWriter()) {
            for (String word : words) {
                writer.write(word);
            }
            return writer.toCharArray();
        }
    }

    public static void main(String[] args) {
        File sampleFile = new File("sample.txt");
        byte[] content = new byte[] {'J', 'a', 'v', 'a'};

        try (OutputStream outputStream = new FileOutputStream(sampleFile, true)) {
            outputStream.write(content);
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }
}