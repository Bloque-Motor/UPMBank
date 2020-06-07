package es.upm.fi.sos.upmbank.client;

import org.apache.axis2.AxisFault;
import es.upm.fi.sos.upmbank.client.UPMBankWSStub.*;

import java.rmi.RemoteException;
import java.util.Arrays;

public class Cliente {

    public static void main(String[] args) throws RemoteException {

        UPMBankWSStub stub = null;
        stub = new UPMBankWSStub();
        stub._getServiceClient().engageModule("addressing");
        stub._getServiceClient().getOptions().setManageSession(true);

        // Superuser
        User admin = new User();
        admin.setName("admin");
        admin.setPwd("admin");

        //Usuario que no existe
        User adminErroneo = new User();
        adminErroneo.setName("admin");
        adminErroneo.setPwd("paco");

        //Usuarios test
        User test1 = new User();
        test1.setName("PabloBDC");
        String iban1 = "";

        User test2 = new User();
        test2.setName("PabloBDC1");
        String iban2 = "";

        //Login admin
        System.out.println("\nAdmin hace login correcto");
        Login adminLogin = new Login();
        Logout adminLogout = new Logout();
        adminLogin.setArgs0(admin);
        System.out.println("Resultado admin login: " + stub.login(adminLogin).get_return().getResponse());
        stub.logout(adminLogout);

        //Login admin erroneo
        System.out.println("\nAdmin hace login contraseña incorrecta");
        adminLogin.setArgs0(adminErroneo);
        System.out.println("Resultado admin incorrecto: " + stub.login(adminLogin).get_return().getResponse());

        //Login usuarios que no existen
        System.out.println("\nHacemos login con usuarios aun no registrados");
        Login user1 = new Login();
        Login user2 = new Login();
        user1.setArgs0(test1);
        user2.setArgs0(test2);
        System.out.println("Resultado hacer login usuario 1 aun no registrado: " + stub.login(user1).get_return().getResponse());
        System.out.println("Resultado hacer login usuario 2 aun no registrado: " + stub.login(user2).get_return().getResponse());

        //Operaciones sin login
        System.out.println("\nProbamos a añadir usuario sin hacer login");
        AddUser addUserNoLogin = new AddUser();
        Username usernameNoLogin = new Username();
        usernameNoLogin.setUsername("Hola");
        addUserNoLogin.setArgs0(usernameNoLogin);
        AddUserResponse addUserResponseNoLogin = stub.addUser(addUserNoLogin).get_return();
        System.out.println("Add user sin login: " + addUserResponseNoLogin.getResponse());

        //Login admin y crear usuarios test
        System.out.println("\nHacemos login con el admin y añadimos los dos usuarios del test");
        adminLogin.setArgs0(admin);
        System.out.println("Login admin: " + stub.login(adminLogin).get_return().getResponse());
        AddUser adduser1 = new AddUser();
        Username username1 = new Username();
        username1.setUsername(test1.getName());
        adduser1.setArgs0(username1);
        AddUser adduser2 = new AddUser();
        Username username2 = new Username();
        username2.setUsername(test2.getName());
        adduser2.setArgs0(username2);
        AddUserResponse addUserResponse1 = stub.addUser(adduser1).get_return();
        System.out.println("Registrar usuario1: " + addUserResponse1.getResponse());
        if (addUserResponse1.getResponse()) test1.setPwd(addUserResponse1.getPwd());
        AddUserResponse addUserResponse2 = stub.addUser(adduser2).get_return();
        System.out.println("Registrar al usuario2: " + addUserResponse2.getResponse());
        if (addUserResponse2.getResponse()) test2.setPwd(addUserResponse2.getPwd());

        //Login user1 sin cerrar sesion admin
        System.out.println("\nHacemos login con los nuevos usuarios sin cerrar la sesion del admin");
        Login loginUser1 = new Login();
        loginUser1.setArgs0(test1);
        System.out.println("Respuesta de login user1: " + stub.login(loginUser1).get_return().getResponse());
        Login loginUser2 = new Login();
        loginUser2.setArgs0(test2);
        System.out.println("Respuesta de login user2: " + stub.login(loginUser2).get_return().getResponse());

        //Creamos cuentas para los dos usuarios
        System.out.println("\nCreamos una cuenta bancaria para cada uno de los usuarios");
        AddBankAcc addBankAcc1 = new AddBankAcc();
        AddBankAcc addBankAcc2 = new AddBankAcc();
        Deposit deposit1 = new Deposit();
        Deposit deposit2 = new Deposit();
        deposit1.setQuantity(1000);
        deposit2.setQuantity(2000);
        addBankAcc1.setArgs0(deposit1);
        addBankAcc2.setArgs0(deposit2);
        BankAccountResponse bankAccountResponse1 = stub.addBankAcc(addBankAcc1).get_return();
        BankAccountResponse bankAccountResponse2 = stub.addBankAcc(addBankAcc2).get_return();
        System.out.println("Respuesta de añadir cuenta para usuario1: " + bankAccountResponse1.getResult());
        System.out.println("Respuesta de añadir cuenta para usuario2: " + bankAccountResponse2.getResult());
        iban1 = bankAccountResponse1.getIBAN();
        iban2 = bankAccountResponse2.getIBAN();

        //Añadimos dinero a la cuenta del usuario1 y quitamos de usuario2
        System.out.println("\nAñadimos dinero a la cuenta del usuario1 y se lo quitamos a la cuenta de usuario2");
        Movement movement1 = new Movement();
        Movement movement2 = new Movement();
        movement1.setQuantity(1000);
        movement1.setIBAN(iban1);
        movement2.setQuantity(2000);
        movement2.setIBAN(iban2);
        AddIncome addIncome1 = new AddIncome();
        AddWithdrawal addWithdrawal2 = new AddWithdrawal();
        addIncome1.setArgs0(movement1);
        addWithdrawal2.setArgs0(movement2);
        AddIncomeResponse addIncomeResponse1 = stub.addIncome(addIncome1);
        AddWithdrawalResponse addWithdrawalResponse2 = stub.addWithdrawal(addWithdrawal2);
        System.out.println("Resultado añadir dinero al usuario1: " + addIncomeResponse1.get_return().getResult() + ". Balance: " + addIncomeResponse1.get_return().getBalance());
        System.out.println("Resultado de retirar dinero al usuario2: " + addWithdrawalResponse2.get_return().getResult() + ". Balance: " + addWithdrawalResponse2.get_return().getBalance());

        //Borramos cuentas
        System.out.println("\nBorramos cuenta usuario2 e intentamos borrar cuenta usuario1");
        CloseBankAcc closeBankAcc1 = new CloseBankAcc();
        CloseBankAcc closeBankAcc2 = new CloseBankAcc();
        BankAccount bankAccount1 = new BankAccount();
        BankAccount bankAccount2 = new BankAccount();
        bankAccount1.setIBAN(iban1);
        bankAccount2.setIBAN(iban2);
        closeBankAcc1.setArgs0(bankAccount1);
        closeBankAcc2.setArgs0(bankAccount2);
        CloseBankAccResponse closeBankAccResponse1 = stub.closeBankAcc(closeBankAcc1);
        CloseBankAccResponse closeBankAccResponse2 = stub.closeBankAcc(closeBankAcc2);
        System.out.println("Respuesta de cerrar cuenta usuario1: " + closeBankAccResponse1.get_return().getResponse());
        System.out.println("Respuesta de cerrar cuenta usuario2: " + closeBankAccResponse2.get_return().getResponse());

        //Cambiamos contraseña usuarios
        System.out.println("\nProbamos a cambiar contraseña usuario1 y cambiamos contraseña usuario2");
        ChangePassword changePassword1 = new ChangePassword();
        ChangePassword changePassword2 = new ChangePassword();
        PasswordPair passwordPair1 = new PasswordPair();
        PasswordPair passwordPair2 = new PasswordPair();
        passwordPair1.setOldpwd(test1.getPwd());
        passwordPair1.setNewpwd("1111");
        passwordPair2.setOldpwd("1111");
        passwordPair2.setNewpwd("2222");
        changePassword1.setArgs0(passwordPair1);
        changePassword2.setArgs0(passwordPair2);
        ChangePasswordResponse changePasswordResponse1 = stub.changePassword(changePassword1);
        ChangePasswordResponse changePasswordResponse2 = stub.changePassword(changePassword2);
        System.out.println("Respuesta de intentar cambiar usuario1: " + changePasswordResponse1.get_return().getResponse());
        System.out.println("Respuesta de intentar cambiar usuario2: " + changePasswordResponse2.get_return().getResponse());
        passwordPair1.setNewpwd(test1.getPwd());
        passwordPair1.setOldpwd("1111");
        changePassword1.setArgs0(passwordPair1);
        changePasswordResponse1 = stub.changePassword(changePassword1);
        System.out.println("Devolvemos la contraseña del usuario1 a su estado inicial: " + changePasswordResponse1.get_return().getResponse());

        //GetMovements
        System.out.println("Solicitamos la lista de movimientos del usuario1");
        GetMyMovements getMyMovements = new GetMyMovements();
        GetMyMovementsResponse getMyMovementsResponse = stub.getMyMovements(getMyMovements);
        System.out.println("Ultimos movimientos: " + Arrays.toString(getMyMovementsResponse.get_return().getMovementQuantities()));
    }
}
