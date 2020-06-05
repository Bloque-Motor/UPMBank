package es.upm.fi.sos.upmbank.client;

import org.apache.axis2.AxisFault;
import es.upm.fi.sos.upmbank.client.UPMBankWSStub.*;

import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws RemoteException {

        UPMBankWSStub stub = null;

        try {
            stub = new UPMBankWSStub();
            stub._getServiceClient().engageModule("addressing");
            stub._getServiceClient().getOptions().setManageSession(true);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

        // Superuser
        User admin = new User();
        admin.setName("admin");
        admin.setPwd("admin");

        AddUser testUser = new AddUser();
        Username test = new Username();
        test.setUsername("asdfasdfbgfdsh75757asdfafñlghj.,");
        System.out.println(test.getUsername());
        testUser.setArgs0(test);

        Login adminLogin = new Login();
        adminLogin.setArgs0(admin);
        Logout adminLogout = new Logout();

        stub.login(adminLogin);

        AddUserResponse response = new AddUserResponse();
        response = stub.addUser(testUser).get_return();

            System.out.println(response.getResponse());
            System.out.println(response.getPwd());


        Login user1 = new Login();
        User test1 = new User();
        test1.setPwd("null");
        test1.setName("kljhfaosudfasf15461234");

        user1.setArgs0(test1);

        try {
            System.out.println(stub.login(user1).get_return().getResponse() +  " prueba login");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
