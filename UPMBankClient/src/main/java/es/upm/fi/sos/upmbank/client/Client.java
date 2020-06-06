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

        // Creación superuser
        User admin = new User();
        admin.setName("admin");
        admin.setPwd("admin");
        System.out.println("Usuario administrador creado");

        // Creación dos users para pruebas
        Username user1 = new Username();
        user1.setUsername("InigoA");
        Username user2 = new Username();
        user2.setUsername("IgnacioA");

        //login con el superuser
        System.out.println("\n\n Test 1 - el usuario admin hace login con el usuario y el password correcto");
        Login loginAdmin = new Login();
        Logout logoutAdmin = new Logout();
        loginAdmin.setArgs0(admin);
        System.out.println("\n Login admin debería ser true, el resultado es: " + stub.login(loginAdmin).get_return().getResponse());

        //login con usuario no existente
        System.out.println("\n\n Test 2 - Probamos hacer login con un user que no existe");
        User usuarioNoExiste = new User();
        usuarioNoExiste.setName("JuanB");
        usuarioNoExiste.setPwd("12345");
        Login login1 = new Login();
        login1.setArgs0(usuarioNoExiste);
        System.out.println("\n Login con un usuario que no existe deberia dar false, el resultado es: " +stub.login(login1).get_return().getResponse());

        //Añadimos usuarios al sistema
        System.out.println("\n\n Test 3 - Añadimos dos usuarios al sistema");
        loginAdmin.setArgs0(admin);
        System.out.println("\n Nos logueamos con el admin : " + stub.login(loginAdmin).get_return().getResponse());
        AddUser adduser1 = new AddUser();
        adduser1.setArgs0(user1);
        AddUserResponse respuesta1 = new AddUserResponse();
        respuesta1 = stub.addUser(adduser1).get_return();
        System.out.println("\n Añadimos al usuario 1 " + user1.getUsername() + " el resultado es: " + respuesta1.getResponse());
        AddUser addUser2 = new AddUser();
        addUser2.setArgs0(user2);
        AddUserResponse respuesta2 = new AddUserResponse();
        respuesta2 = stub.addUser(adduser1).get_return();
        System.out.println("\n Añadimos al usuario 2 " + user2.getUsername() + " el resultado es: " + respuesta2.getResponse());
        stub.logout(logoutAdmin);

        //Hacemos una operación del servicio
        System.out.println("\n\n Test 4 - Hacemos operaciones del servicio con el user1");
        User userOp1 = new User();
        userOp1.setName(user1.getUsername());
        userOp1.setPwd(respuesta1.getPwd());
        Login loginUser1 = new Login();
        loginUser1.setArgs0(userOp1);
        System.out.println("\n Hacemos login del usuario 1 " + user1.getUsername() + " el resultado es: " + stub.login(loginUser1).get_return().getResponse());
        AddBankAcc addCuenta1 = new AddBankAcc();
        Deposit dineroUser1 = new Deposit();
        dineroUser1.setQuantity(5000.0);
        addCuenta1.setArgs0(dineroUser1);
        BankAccountResponse respuestaBankAccount1 = new BankAccountResponse();
        respuestaBankAccount1 = stub.addBankAcc(addCuenta1).get_return();
        System.out.println("\n El usuario ha creado un cuenta con número de IBAN: " + respuestaBankAccount1.getIBAN() + ", creado correctamente:  " + respuestaBankAccount1.getResult());
        Logout logoutUserOp1 = new Logout();
        stub.logout(logoutUserOp1);






        /*
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
        }*/
    }
}
