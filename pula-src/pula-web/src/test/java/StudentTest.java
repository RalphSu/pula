import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

public class StudentTest {

    @Test
    public void test() throws Exception {
        List<String> content = Files.readAllLines(
                Paths.get(new URI("file://"+StudentTest.class.getResource("/test.json").getPath())), Charset.forName("GBK"));

        String cont = content.get(0);
        
        System.out.println(cont);
        
        Charset set = Charset.forName("GBK");
        System.out.println(new String(cont.getBytes(set), set));
    }
}
