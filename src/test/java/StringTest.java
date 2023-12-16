
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTest {

  @Test
  public void lengthTest() {

 String line = "Ввод с консолkи";
 int lengthLine = line.length();
    assertTrue(lengthLine>14,  "текст меньше 15 символов");

  }
}
