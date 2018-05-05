package netPractice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class GetUrlContents {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.baidu.com/");
            InputStream inputStream = url.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            char[] chars = new char[1024];
            while (inputStreamReader.read(chars) > 0) {
                System.out.print(chars);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
