package es.upm.fi.sos.upmbank.client;

import com.github.javafaker.Faker;
import org.apache.axis2.AxisFault;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class jUnitClientTest {

    private jUnitClient client;
    private static Faker faker;
    private static HashMap<String, String> usedUsers;
    private static final int NUMUSERS = 5;
    private static final String ADMIN = "admin";

    @BeforeAll
    static void loadFaker() {
        faker = new Faker();
        usedUsers = new HashMap<>();
    }

    @BeforeEach
    void loadClient() throws AxisFault {
        client = new jUnitClient();
    }


    @Test
    @DisplayName("Sudo Login")
    void sudoLogin() {
        client.setUsername(ADMIN);
        client.setPassword(ADMIN);
        try {
            assertTrue(client.login());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogin() {
    }

    @Test
    void testLogout() {
    }
    @ParameterizedTest
    @MethodSource("generateUsername")
    @DisplayName("AddUserAsSudo")
    void testAddUserAsSudo(String input) {
        client.setUsername(ADMIN);
        client.setPassword(ADMIN);
        try {
            if (client.login()) {
                UPMBankWSStub.AddUserResponse response = client.addUser(input);
                assertTrue(response.getResponse());
                usedUsers.put(input, response.getPwd());
                System.out.println(input + ": " + response.getPwd());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRemoveUser() {
    }

    @Test
    void testChangePassword() {
    }

    @Test
    void testAddBankAcc() {
    }

    @Test
    void testCloseBankAcc() {
    }

    @Test
    void testAddMovement() {
    }

    @Test
    void testAddWithdrawal() {
    }

    @Test
    void testGetMyMovements() {
    }

    private static Stream<Arguments> argumentsStream() {
        return Stream.of(

        );
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SudoActions {
        private jUnitClient client;

        @BeforeAll
        void loadClient() throws AxisFault {
            this.client = new jUnitClient();
        }

        @Test
        @DisplayName("SudoLogin")
        @Order(0)
        void sudoLogin() {
            this.client.setUsername(ADMIN);
            this.client.setPassword(ADMIN);
            try {
                assertTrue(client.login());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @ParameterizedTest
        @DisplayName("Sudo Add Users")
        @MethodSource("es.upm.fi.sos.upmbank.client.jUnitClientTest#generateUsername")
        @Order(2)
        void addUsers(String input) {
            try {
                UPMBankWSStub.AddUserResponse response = client.addUser(input);
                assertTrue(response.getResponse());
                usedUsers.put(input, response.getPwd());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("Remove one user")
        @Order(3)
        void removeUser() {
            Object[] keys = usedUsers.keySet().toArray();
            Object key = keys[new Random().nextInt(keys.length)];
            String username = key.toString();
            usedUsers.remove(username);
            try {
                UPMBankWSStub.Response response = client.removeUser(username);
                assertTrue(response.getResponse());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    static Stream<String> generateUsername() {
        String[] array = new String[NUMUSERS];
        for (int i = 0; i < NUMUSERS; i++) {
            String var = faker.regexify("[A-Z]{2}+[0-9]{2}");
            array[i] = var;
        }
        return Stream.of(array);
    }
}