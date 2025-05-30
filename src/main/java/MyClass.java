import utils.ExcelXMLReplacer;

import java.io.File;
import java.io.IOException;

public class MyClass {
    public static void main(String[] args) throws IOException {
        File file = new File("Book1.xlsx");
        ExcelXMLReplacer.replaceUserName(file, "srcText");
    }
}
