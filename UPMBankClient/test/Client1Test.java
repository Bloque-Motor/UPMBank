import es.upm.fi.sos.upmbank.client.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Client1Test {

    private Client client;

    @BeforeAll
    void loadClient() {client = new Client();}

    @Test
    @DisplayName("Sudo login")
    void sudoLogin1() {

    }
}
