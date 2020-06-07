package es.upm.fi.sos.upmbank.client;

import org.apache.axis2.AxisFault;
import es.upm.fi.sos.upmbank.client.UPMBankWSStub.*;

import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.Arrays;

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

        // Creación admin
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

        //Creacion admin con contraseña mal
        System.out.println("\n\n Test 2 - el usuario admin hace login con el usuario y el password incorrecto");
        User adminMal = new User();
        adminMal.setName("admin");
        adminMal.setPwd("nopass");
        Login loginAdminMal = new Login();
        loginAdminMal.setArgs0(adminMal);
        System.out.println("\n Login admin debería ser false, el resultado es: " + stub.login(loginAdminMal).get_return().getResponse());

        //login con usuario no existente
        System.out.println("\n\n Test 3 - Probamos hacer login con un user que no existe");
        User usuarioNoExiste = new User();
        usuarioNoExiste.setName("JuanB");
        usuarioNoExiste.setPwd("12345");
        Login login1 = new Login();
        login1.setArgs0(usuarioNoExiste);
        System.out.println("\n Login con un usuario que no existe deberia dar false, el resultado es: " +stub.login(login1).get_return().getResponse());

        //Añadimos usuarios al sistema
        System.out.println("\n\n Test 4 - Añadimos dos usuarios al sistema");
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

        //Añadimos usuario ya existente al sistema
        System.out.println("\n\n Test 5 - Intentamos añadir un usuario ya existente, el resultado deberia ser false");
        loginAdmin.setArgs0(admin);
        AddUser adduser11 = new AddUser();
        adduser11.setArgs0(user1);
        System.out.println("\n Añadimos al usuario 1 " + user1.getUsername() + " el resultado es: " + stub.addUser(adduser11).get_return().getResponse());
        Logout logoutAddUserAdmin = new Logout();
        stub.logout(logoutAddUserAdmin);

        //Hacemos una operación del servicio
        System.out.println("\n\n Test 6 - Hacemos operaciones del servicio con el user1");
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
        System.out.println("\n El usuario ha creado un cuenta con número de IBAN " + respuestaBankAccount1.getIBAN() + ", creado correctamente:  " + respuestaBankAccount1.getResult());
        AddIncome addIncomeUser1 = new AddIncome();
        Movement movUser1 = new Movement();
        movUser1.setIBAN(respuestaBankAccount1.getIBAN());
        movUser1.setQuantity(1500.0);
        addIncomeUser1.setArgs0(movUser1);
        AddMovementResponse movResponse1 = new AddMovementResponse();
        movResponse1 = stub.addIncome(addIncomeUser1).get_return();
        System.out.println("\n Realiza un ingreso en la cuenta " + respuestaBankAccount1.getIBAN() + " de " + movUser1.getQuantity() + " con resultado: " + movResponse1.getResult());
        System.out.println("\n Le quedan actualmente en la cuenta: " +  movResponse1.getBalance() + "€");
        MovementList getMovUser1 = new MovementList();
        GetMyMovements getMov1 = new GetMyMovements();
        getMovUser1 = stub.getMyMovements(getMov1).get_return();
        System.out.println("\n Su historial de operaciones es : " + Arrays.toString(getMovUser1.getMovementQuantities()));
        Logout logoutUserOp1 = new Logout();
        stub.logout(logoutUserOp1);

        //Hacemos una operacion del servicio sin loguearnos
        System.out.println("\n\n Test 7 - Realizamos una operación en el sistema con un usuario que no ha hecho login previo, el resultado debería ser false");
        AddIncome addIncomeUser11 = new AddIncome();
        Movement movUser11 = new Movement();
        movUser1.setIBAN(respuestaBankAccount1.getIBAN());
        movUser1.setQuantity(2500.0);
        addIncomeUser1.setArgs0(movUser11);
        System.out.println("\n Realiza un ingreso en la cuenta, resultado: " + stub.addIncome(addIncomeUser11).get_return().getResult());

        //Un usuario sin cuentas abiertas que no es el admin intenta eliminarse
        System.out.println("\n\n Test 8 - Un usuario intenta eliminarse, debería dar false");
        RemoveUser removeUserNotAllowed = new RemoveUser();
        removeUserNotAllowed.setArgs0(user2);
        User userOp2 = new User();
        userOp2.setName(user2.getUsername());
        userOp2.setPwd(respuesta2.getPwd());
        Login loginUser2Remove = new Login();
        loginUser2Remove.setArgs0(userOp2);
        System.out.println("Hacemos login con el user 2: " + stub.login(loginUser2Remove));
        System.out.println("El usuario se intenta eliminar, resultado: " + stub.removeUser(removeUserNotAllowed).get_return().getResponse());
        Logout logoutUser2 = new Logout();
        stub.logout(logoutUser2);

        //Intentamos quitar un usuario con cuentas abiertas
        RemoveUser removeUser1 = new RemoveUser();
        removeUser1.setArgs0(user1);
        System.out.println("\n\n Test 9 - Intentamos borrar un usuario con cuentas abiertas, debería ser false");
        loginAdmin.setArgs0(admin);
        System.out.println("Hacemos login con el admin: " + stub.login(loginAdmin));
        System.out.println("El resultado de borrar un usuario con cuentas abiertas es: " + stub.removeUser(removeUser1).get_return().getResponse());

        //Intentamos quitar un usuario sin cuentas abiertas
        RemoveUser removeUser2 = new RemoveUser();
        removeUser2.setArgs0(user2);
        System.out.println("\n\n Test 10 - Intentamos borrar un usuario sin cuentas abiertas, debería ser true");
        System.out.println("El resultado de borrar un usuario sin cuentas abiertas es: " + stub.removeUser(removeUser2).get_return().getResponse());

        //El usuario admin intenta autoeliminarse
        RemoveUser removeAdmin = new RemoveUser();
        Username userAdmin = new Username();
        userAdmin.setUsername("admin");
        removeAdmin.setArgs0(userAdmin);
        System.out.println("\n\n Test 11 - El usuario Admin intenta autoeliminarse, debería ser false");
        System.out.println("El resultado de autoeliminarse el admin es: " + stub.removeUser(removeAdmin));
        Logout logoutAdminRemove = new Logout();
        stub.logout(logoutAdminRemove);

        //Cambiamos la contraseña al admin sin haber hecho login previo
        System.out.println("\n\n Test 12 - Cambiamos la contraseña de admin sin haber hecho login previo, debería dar false");
        ChangePassword changeAdminNoLogin = new ChangePassword();
        PasswordPair passAdminNoLogin = new PasswordPair();
        passAdminNoLogin.setOldpwd("admin");
        passAdminNoLogin.setNewpwd("superuser");
        changeAdminNoLogin.setArgs0(passAdminNoLogin);
        System.out.println("El resultado de cambiar la contraseña ha sido: " + stub.changePassword(changeAdminNoLogin).get_return().getResponse());

        //Cambiamos la contraseña al admin introduciendo incorrectamente la oldpsw
        Login loginAdminPassIncorrect = new Login();
        loginAdminPassIncorrect.setArgs0(admin);
        System.out.println("\n\n Test 13 - Cambiamos la contraseña de admin introduciendo mal la oldpsw, debería dar false");
        System.out.println("Hacemos login con el usuario admin, resultado: " + stub.login(loginAdminPassIncorrect));
        ChangePassword changeAdminIncorrect = new ChangePassword();
        PasswordPair passAdminIncorrect = new PasswordPair();
        passAdminIncorrect.setOldpwd("passIncorrect");
        passAdminIncorrect.setNewpwd("superuser");
        changeAdminIncorrect.setArgs0(passAdminIncorrect);
        System.out.println("El resultado de cambiar la contraseña ha sido: " + stub.changePassword(changeAdminIncorrect).get_return().getResponse());

        //Cambiamos la contraseña al admin introduciendo correctamente la oldpsw
        System.out.println("\n\n Test 14 - Cambiamos la contraseña de admin introduciendo las psw correctamente, debería ser true");
        ChangePassword changeAdmin = new ChangePassword();
        PasswordPair passAdmin = new PasswordPair();
        passAdmin.setOldpwd("admin");
        passAdmin.setNewpwd("superuser");
        changeAdmin.setArgs0(passAdmin);
        System.out.println("El resultado de cambiar la contraseña ha sido: " + stub.changePassword(changeAdmin).get_return().getResponse());






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
