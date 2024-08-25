import java.io.IOException;

/**
 * @Project: Design Pattern
 * @Author: SSS
 * @CreateTime: 2023-03-07
 * @Description: 适配器模式 - Target
 */
public interface FileIO {

    public void readFromFile(String filename) throws IOException;

    public void writeToFile(String filename) throws IOException;

    public void setValue(String key, String value);

    public String getValue(String key);

}
