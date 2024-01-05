import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class MainTest {

    @Disabled
    @Test
    @Timeout(value = 27000, unit = TimeUnit.MILLISECONDS)
    void main() throws Exception {
        Main.main(new String[]{});
    }
}