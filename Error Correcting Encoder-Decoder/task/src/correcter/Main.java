package correcter;

import java.io.*;
import java.util.*;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String command = scanner.nextLine();
        switch (command) {
            case "encode":
                encode();
                break;
            case "send":
                send();
                break;
            case "decode":
                decode();
        }
    }

    private static void decode() throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream("received.txt"));
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("decoded.txt"))
        ) {
            Queue<Boolean> queue = new LinkedList<>();
            while (true) {
                int oldSize;
                while ((oldSize = queue.size()) < 8) {
                    addDecodingToQueue(queue, inputStream);
                    if (queue.size() == oldSize) {
                        return;
                    }
                }

                int outputByte = 0;
                for (int i = 7; i >= 0; i--) {
                    outputByte = (outputByte << 1) + getBit(queue.poll());
                }
                outputStream.write((byte) outputByte);
            }
        }
    }

    private static void send() throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream("encoded.txt"));
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("received.txt"))
        ) {
            int code;
            while ((code = inputStream.read()) != -1) {
                outputStream.write(addError((byte) code));
            }
        }
    }

    private static void encode() throws IOException {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream("send.txt"));
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("encoded.txt"))
        ) {
            Queue<Boolean> queue = new LinkedList<>();
            while (true) {
                if (queue.size() < 4) {
                    addEncodingToQueue(queue, inputStream);
                    if (queue.isEmpty()) {
                        break;
                    }
                }

                // c1 c2 d1 c3 d2 d3 d4 0
                // c - check bit, d - data bit
                // c1     +     +     +
                //    c2  +        +  +
                //          c3  +  +  +
                final int d1 = getBit(queue.poll());
                final int d2 = getBit(queue.poll());
                final int d3 = getBit(queue.poll());
                final int d4 = getBit(queue.poll());
                final int c1 = d1 ^ d2 ^ d4;
                final int c2 = d1 ^ d3 ^ d4;
                final int c3 = d2 ^ d3 ^ d4;
                int outputByte = (c1 << 7) + (c2 << 6);
                outputByte += (d1 << 5) + (c3 << 4);
                outputByte += (d2 << 3) + (d3 << 2);
                outputByte += d4 << 1;
                outputStream.write((byte) outputByte);
            }
        }
    }

    private static int getBit(Boolean bit) {
        return bit != null && bit ? 1 : 0;
    }

    private static int getBit(int code, int pos) {
        return (code & (0x1 << pos)) > 0 ? 1 : 0;
    }

    private static void addEncodingToQueue(Queue<Boolean> queue, InputStream inputStream) throws IOException {
        int code = inputStream.read();
        if (code == -1) {
            return;
        }
        for (int i = 7; i >= 0; i--) {
            queue.add((code & (0x1 << i)) > 0);
        }
    }

    private static void addDecodingToQueue(Queue<Boolean> queue, InputStream inputStream) throws IOException {
        int code = inputStream.read();
        if (code == -1) {
            return;
        }

        // c1 c2 d1 c3 d2 d3 d4 0
        // c - check bit, d - data bit
        // c1     +     +     +
        //    c2  +        +  +
        //          c3  +  +  +
        int c1 = getBit(code, 7);
        int c2 = getBit(code, 6);
        int d1 = getBit(code, 5);
        int c3 = getBit(code, 4);
        int d2 = getBit(code, 3);
        int d3 = getBit(code, 2);
        int d4 = getBit(code, 1);
        int c4 = getBit(code, 0);

        final int changedC1 = c1 ^ (d1 ^ d2 ^ d4);
        final int changedC2 = c2 ^ (d1 ^ d3 ^ d4);
        final int changedC3 = c3 ^ (d2 ^ d3 ^ d4);
        d1 ^= changedC1 & changedC2 & (changedC3 ^ 0x1);
        d2 ^= changedC1 & changedC3 & (changedC2 ^ 0x1);
        d3 ^= changedC2 & changedC3 & (changedC1 ^ 0x1);
        d4 ^= changedC1 & changedC2 & changedC3;

        queue.add(d1 > 0);
        queue.add(d2 > 0);
        queue.add(d3 > 0);
        queue.add(d4 > 0);
    }


    private static byte addError(byte code) {
        byte pos = (byte) random.nextInt(8);
        return (byte) (code ^ (1 << pos));
    }

}
