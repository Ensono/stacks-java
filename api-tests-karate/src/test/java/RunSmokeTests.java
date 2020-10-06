import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RunSmokeTests {

    @Test
    void runSmokeTests() {
        Results results = Runner.path("classpath:com/xxAMIDOxx/xxSTACKSxx/api/karate").tags("~@ignore", "@Smoke").parallel(1);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
