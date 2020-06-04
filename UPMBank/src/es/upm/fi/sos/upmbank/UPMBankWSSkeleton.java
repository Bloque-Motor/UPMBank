/**
 * UPMBankWSSkeleton.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package es.upm.fi.sos.upmbank;

import UPMAuthenticationAuthorization.UPMAuthenticationAuthorizationWSSkeletonStub;
import es.upm.fi.sos.upmbank.xsd.*;
import org.apache.axis2.AxisFault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;

/**
 * UPMBankWSSkeleton java skeleton for the axisService
 */
public class UPMBankWSSkeleton {

    private static HashMap<String, User> listaUsuarios;
    private static HashMap<String, Integer> usuariosOnline;                     //Username, Nº Sesiones
    private static HashMap<String, ArrayList<BankAccount>> accountList;         //Username, [BankAccount]
    private static HashMap<String, Double> accounts;                           //IBAN, Balance
    private static HashMap<String, Queue<Movement>> movements;                  //Username, Queue<Movimientos>
    private static UPMAuthenticationAuthorizationWSSkeletonStub AuthClient;
    User admin;
    private User sesionActual;
    private boolean online;



    public UPMBankWSSkeleton() {
        if (listaUsuarios == null) {
            listaUsuarios = new HashMap<String, User>();
        }
        if (usuariosOnline == null) {
            usuariosOnline = new HashMap<String, Integer>();
        }
        admin = new User();
        admin.setName("admin");
        admin.setPwd("admin");
        listaUsuarios.put("admin", admin);
        sesionActual = null;

        try {
            AuthClient = new UPMAuthenticationAuthorizationWSSkeletonStub();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }


    /**
     * Auto generated method signature
     *
     * @param addBankAcc
     * @return addBankAccResponse
     */

    public es.upm.fi.sos.upmbank.AddBankAccResponse addBankAcc
    (
            es.upm.fi.sos.upmbank.AddBankAcc addBankAcc
    ) {
        BankAccountResponse response = new BankAccountResponse();

        if (online) {
            Double quantity = addBankAcc.getArgs0().getQuantity();
            Random random = new Random();
            String ibanInt = String.format("%04d", random.nextInt(10000));
            String IBAN =  sesionActual.getName() + ibanInt;

            BankAccount account = new BankAccount();
            account.setIBAN(IBAN);

            ArrayList<BankAccount> aux;
            if (accountList.containsKey(sesionActual.getName())) {
                aux = accountList.get(sesionActual.getName());
            }
            else {
                aux = new ArrayList<>();
            }
            aux.add(account);
            accountList.put(sesionActual.getName(), aux);
            accounts.put(IBAN, quantity);

            response.setResult(true);
            response.setIBAN(IBAN);
        }
        else {
            response.setResult(false);
            response.setIBAN("");
        }

        AddBankAccResponse endResponse = new AddBankAccResponse();
        endResponse.set_return(response);

        return endResponse;

    }


    /**
     * Auto generated method signature
     *
     * @param closeBankAcc
     * @return closeBankAccResponse
     */

    public es.upm.fi.sos.upmbank.CloseBankAccResponse closeBankAcc
    (
            es.upm.fi.sos.upmbank.CloseBankAcc closeBankAcc
    ) {

        Response response = new Response();
        BankAccount userBank = closeBankAcc.getArgs0();
        String userIban = userBank.getIBAN();

        if(accounts.get(userIban).equals(0) && online){

            accounts.remove(userIban);
            response.setResponse(true);

        } else {response.setResponse(false);}

        CloseBankAccResponse endResponse = new CloseBankAccResponse();
        endResponse.set_return(response);

        return endResponse;
    }


    /**
     * Auto generated method signature
     *
     * @param logout
     * @return
     */

    public void logout
    (
            es.upm.fi.sos.upmbank.Logout logout
    ) {
        //TODO : fill this with the necessary business logic

        if(online == true){
            int numberOfSessions = usuariosOnline.get(sesionActual.getName());

            while(numberOfSessions >= 1){
                if(numberOfSessions == 1){
                    usuariosOnline.remove(sesionActual.getName());
                    online = false;
                    sesionActual = null;
                }
                numberOfSessions--;
                usuariosOnline.put(sesionActual.getName(),numberOfSessions);
            }

           /* if(numberOfSessions == 1){
                usuariosOnline.remove(sesionActual.getName());
                online = false;
                sesionActual = null;
            }

            else if(numberOfSessions > 1){
                numberOfSessions--;
                usuariosOnline.put(sesionActual.getName(),numberOfSessions);
            }*/
        }
    }


    /**
     * Auto generated method signature
     *
     * @param removeUser
     * @return removeUserResponse
     */

    public es.upm.fi.sos.upmbank.RemoveUserResponse removeUser
    (
            es.upm.fi.sos.upmbank.RemoveUser removeUser
    ) {

        Response response = new Response();

        UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser userRemoveService = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser();
        UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE userRemoved = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE();

        Username user = removeUser.getArgs0();
        String username = user.getUsername();

        String onlineUser = sesionActual.getName();
        ArrayList numeroCuentas = accountList.get(username);


        if (onlineUser.equals("admin") && online && numeroCuentas.size() > 0) {

                userRemoveService.setName(username);
                userRemoved.setRemoveUser(userRemoveService);

                try {
                    response.setResponse(AuthClient.removeUser(userRemoved).get_return().getResult());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

        }else{
            response.setResponse(false);
        }

        RemoveUserResponse endResponse = new RemoveUserResponse();
        endResponse.set_return(response);

        return endResponse;
    }

    /**
     * Auto generated method signature
     *
     * @param addWithdrawal
     * @return addWithdrawalResponse
     */

    public es.upm.fi.sos.upmbank.AddWithdrawalResponse addWithdrawal
    (
            es.upm.fi.sos.upmbank.AddWithdrawal addWithdrawal
    ) {

        AddMovementResponse response = new AddMovementResponse();

        Movement info = addWithdrawal.getArgs0();
        String ibanNumber = info.getIBAN();
        Double quantityNumber = info.getQuantity();

        if(accounts.containsKey(ibanNumber) && online){
        if(accounts.get(ibanNumber) >= quantityNumber) {

            Double newQuantity = accounts.get(ibanNumber) - quantityNumber;
            accounts.put(ibanNumber, newQuantity);

            Queue<Movement> mov = movements.get(sesionActual.getName());
            Movement var = new Movement();
            var.setIBAN(ibanNumber);
            var.setQuantity(quantityNumber);

            if (mov.size() >= 10){
                mov.remove();
            }

            mov.add(var);

            movements.put(sesionActual.getName(), mov);
            response.setResult(true);
            response.setBalance(newQuantity);

        }
        }else{
            response.setResult(false);
            response.setBalance(0);
        }

        AddWithdrawalResponse endResponse = new AddWithdrawalResponse();
        endResponse.set_return(response);

        return endResponse;

       }


    /**
     * Auto generated method signature
     *
     * @param addUser
     * @return addUserResponse
     */

    public es.upm.fi.sos.upmbank.AddUserResponse addUser
    (
            es.upm.fi.sos.upmbank.AddUser addUser
    ) {
        es.upm.fi.sos.upmbank.xsd.AddUserResponse response = new es.upm.fi.sos.upmbank.xsd.AddUserResponse();


        if (online && sesionActual.getName().equals("admin")) {
            String username = addUser.getArgs0().getUsername();

            UPMAuthenticationAuthorizationWSSkeletonStub.AddUser addUserService = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUser();
            UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd userBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd();

            userBackEnd.setName(username);
            addUserService.setUser(userBackEnd);

            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponseBackEnd userResponseBackEnd = AuthClient.addUser(addUserService).get_return();
                response.setResponse(userResponseBackEnd.getResult());
                response.setPwd(userResponseBackEnd.getPassword());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else {
            response.setResponse(false);
            response.setPwd("");
        }

        AddUserResponse endResponse = new AddUserResponse();
        endResponse.set_return(response);
        return endResponse;

    }


    /**
     * Auto generated method signature
     *
     * @param addIncome
     * @return addIncomeResponse
     */

    public es.upm.fi.sos.upmbank.AddIncomeResponse addIncome
    (
            es.upm.fi.sos.upmbank.AddIncome addIncome
    ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#addIncome");
    }


    /**
     * Auto generated method signature
     *
     * @param login
     * @return loginResponse
     */

    public es.upm.fi.sos.upmbank.LoginResponse login
    (
            es.upm.fi.sos.upmbank.Login login
    ) {

        User user = login.getArgs0();
        String username = user.getName();
        String password = user.getPwd();

        Response response = new Response();
        response.setResponse(false);

        UPMAuthenticationAuthorizationWSSkeletonStub.Login loginService = new UPMAuthenticationAuthorizationWSSkeletonStub.Login();
        UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd loginBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd();

        loginBackEnd.setName(username);
        loginBackEnd.setPassword(password);
        loginService.setLogin(loginBackEnd);

        try {
            response.setResponse(AuthClient.login(loginService).get_return().getResult());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(response.getResponse() == true){
            if(online){
                int numberOfSessions = usuariosOnline.get(username);
                usuariosOnline.put(username,numberOfSessions++);

            }

            else if(!online){
                usuariosOnline.put(username,1);
                online = true;
            }

        }

        LoginResponse endResponse = new LoginResponse();
        endResponse.set_return(response);

    return endResponse;
    }


    /**
     * Auto generated method signature
     *
     * @param getMyMovements
     * @return getMyMovementsResponse
     */

    public es.upm.fi.sos.upmbank.GetMyMovementsResponse getMyMovements
    (
            es.upm.fi.sos.upmbank.GetMyMovements getMyMovements
    ) {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#getMyMovements");
    }


    /**
     * Auto generated method signature
     *
     * @param changePassword
     * @return changePasswordResponse
     */

    public es.upm.fi.sos.upmbank.ChangePasswordResponse changePassword
    (
            es.upm.fi.sos.upmbank.ChangePassword changePassword
    ) {
        Response response = new Response();

        if (online) {
            String username = sesionActual.getName();
            String oldPwd = changePassword.getArgs0().getOldpwd();
            String newPwd = changePassword.getArgs0().getNewpwd();

            UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword changePasswordService = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
            UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd changePasswordBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd();
            changePasswordBackEnd.setName(username);
            changePasswordBackEnd.setNewpwd(newPwd);
            changePasswordBackEnd.setOldpwd(oldPwd);
            changePasswordService.setChangePassword(changePasswordBackEnd);

            try {
                response.setResponse(AuthClient.changePassword(changePasswordService).get_return().getResult());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        else {
            response.setResponse(false);
        }

        ChangePasswordResponse endResponse = new ChangePasswordResponse();
        endResponse.set_return(response);

        return endResponse;
    }

}
    