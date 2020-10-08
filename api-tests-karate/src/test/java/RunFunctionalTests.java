import com.intuit.karate.Results;
import com.intuit.karate.Runner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


class RunFunctionalTests {

    @Test
    void runFunctionalTests() {
        Results results = Runner.path("classpath:com/xxAMIDOxx/xxSTACKSxx/api/karate").tags("~@ignore", "@Functional").parallel(1);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
