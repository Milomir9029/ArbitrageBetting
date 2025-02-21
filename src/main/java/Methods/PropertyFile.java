package Methods;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile {
    public String getProperty(String property) throws IOException {
        FileReader reader = new FileReader("src/main/java/Data.properties");
        Properties props = new Properties();
        props.load(reader);
        return props.getProperty(property);
    }
}

