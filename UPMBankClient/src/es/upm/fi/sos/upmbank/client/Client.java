package es.upm.fi.sos.upmbank.client;

import org.apache.axis2.AxisFault;
import es.upm.fi.sos.upmbank.client.UPMBankWSStub.*;

import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) {

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

        Login adminLogin = new Login();
        adminLogin.setArgs0(admin);
        try {
            System.out.println(stub.login(adminLogin).get_return().getResponse());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
