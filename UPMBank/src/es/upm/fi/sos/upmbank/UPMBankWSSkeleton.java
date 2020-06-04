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
    BankAccount test = new BankAccount();
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
        Double quantity
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
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#closeBankAcc");
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
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#removeUser");
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
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#addWithdrawal");
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
        response.setResponse(false);
        response.setPwd("");


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
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName() + "#login");
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
        response.setResponse(false);

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

        ChangePasswordResponse endResponse = new ChangePasswordResponse();
        endResponse.set_return(response);

        return endResponse;
    }

}
    