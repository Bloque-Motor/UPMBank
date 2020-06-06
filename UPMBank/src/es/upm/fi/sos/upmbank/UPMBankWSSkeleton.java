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
import java.util.*;

/**
 * UPMBankWSSkeleton java skeleton for the axisService
 */
public class UPMBankWSSkeleton {

    private static HashMap<String, Integer> sesiones = new HashMap<>();                     //Username, Nº Sesiones
    private static HashMap<String, ArrayList<String>> listaCuentas = new HashMap<>();         //Username, [IBAN]
    private static HashMap<String, Double> cuentas = new HashMap<>();                            //IBAN, Balance
    private static HashMap<String, ArrayList<Movement>> movimientos = new HashMap<>();                  //Username, Queue<Movimientos>
    private static UPMAuthenticationAuthorizationWSSkeletonStub stub;
    User superuser;
    private String usuarioActual;
    private boolean conectado;
    private Random random;

    public UPMBankWSSkeleton() throws AxisFault {
        superuser = new User();
        superuser.setName("admin");
        superuser.setPwd("admin");
        usuarioActual = null;
        stub = new UPMAuthenticationAuthorizationWSSkeletonStub();
        random = new Random();
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

        if (!conectado) {
            response.setResult(false);
            response.setIBAN("");
        } else {
            boolean existe = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                username.setName(usuarioActual);
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                existUser.setUsername(username);
                existe = stub.existUser(existUser).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (existe) {
                Double quantity = addBankAcc.getArgs0().getQuantity();
                String iban = Integer.toString(random.nextInt(1000000));

                ArrayList<String> aux;
                if (listaCuentas.containsKey(usuarioActual)) {
                    aux = listaCuentas.get(usuarioActual);
                } else aux = new ArrayList<>();
                aux.add(iban);
                listaCuentas.put(usuarioActual, aux);
                cuentas.put(iban, quantity);

                response.setResult(true);
                response.setIBAN(iban);
            }
        }

        AddBankAccResponse res = new AddBankAccResponse();
        res.set_return(response);

        return res;
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
        BankAccount cuenta = closeBankAcc.getArgs0();
        String iban = cuenta.getIBAN();

        if (!conectado) {
            response.setResponse(false);
        } else {
            boolean existe = false;
            try {
                String usuario = usuarioActual;
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                username.setName(usuario);
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                existUser.setUsername(username);
                existe = stub.existUser(existUser).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if(cuentas.get(iban).equals(0) && existe){
                cuentas.remove(iban);
                response.setResponse(true);
            }
            else if (!existe) {
                response.setResponse(false);
            }
        }

        CloseBankAccResponse res = new CloseBankAccResponse();
        res.set_return(response);

        return res;
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
        if(conectado){
            int numberOfSessions = sesiones.get(usuarioActual);

           if(numberOfSessions == 1){
                sesiones.remove(usuarioActual);
                conectado = false;
                usuarioActual = null;
           }
           else if(numberOfSessions > 1){
               numberOfSessions--;
               sesiones.put(usuarioActual,numberOfSessions);
           }
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

        Username username = removeUser.getArgs0();
        String usuario = username.getUsername();
        ArrayList<String> cuentas = listaCuentas.get(usuario);

        if (!conectado) {
            response.setResponse(false);
        } else {
            String onlineUser = usuarioActual;
            boolean existe = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                username1.setName(usuario);
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                existUser.setUsername(username1);
                existe = stub.existUser(existUser).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (existe && onlineUser.equals("admin") && (cuentas == null || cuentas.isEmpty()) && !usuario.equals("admin")) {
                try {
                    UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser removeUser1 = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser();
                    UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE removeUserE = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE();
                    removeUser1.setName(usuario);
                    removeUserE.setRemoveUser(removeUser1);
                    response.setResponse(stub.removeUser(removeUserE).get_return().getResult());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        RemoveUserResponse res = new RemoveUserResponse();
        res.set_return(response);

        return res;
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

        if (!conectado) {
            response.setResult(false);
            response.setBalance(0);
        } else {
            Movement movimiento = addWithdrawal.getArgs0();
            String iban = movimiento.getIBAN();
            Double cantidad = movimiento.getQuantity();

            boolean existe = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username userCheck = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                userCheck.setName(usuarioActual);
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser userExist = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                userExist.setUsername(userCheck);
                existe = stub.existUser(userExist).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(existe && cuentas.containsKey(iban) && cuentas.get(iban) >= cantidad){
                double resultado = cuentas.get(iban) - cantidad;
                cuentas.put(iban, resultado);

                ArrayList<Movement> mov = movimientos.get(usuarioActual);
                Movement var = new Movement();
                if (mov == null) mov = new ArrayList<>();
                var.setIBAN(iban);
                var.setQuantity(cantidad);
                if (mov.size() >= 10){
                    mov.remove(0);
                }
                mov.add(var);
                movimientos.put(usuarioActual, mov);
                response.setResult(true);
                response.setBalance(resultado);
            }
        }

        AddWithdrawalResponse res = new AddWithdrawalResponse();
        res.set_return(response);

        return res;

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

        if (conectado && usuarioActual.equals("admin")) {
            String usuario = addUser.getArgs0().getUsername();
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.AddUser addUser1 = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUser();
                UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd userBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd();
                userBackEnd.setName(usuario);
                addUser1.setUser(userBackEnd);
                UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponseBackEnd aReturn = stub.addUser(addUser1).get_return();
                response.setResponse(aReturn.getResult());
                response.setPwd(aReturn.getPassword());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else {
            response.setResponse(false);
            response.setPwd("");
        }

        AddUserResponse res = new AddUserResponse();
        res.set_return(response);
        return res;

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
        AddMovementResponse response = new AddMovementResponse();

        if (!conectado) {
            response.setResult(false);
            response.setBalance(0);
        } else {
            Movement movimiento = addIncome.getArgs0();
            String iban = movimiento.getIBAN();
            Double cantidad = movimiento.getQuantity();

            boolean exist = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username userCheck = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                userCheck.setName(usuarioActual);
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser userExist = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                userExist.setUsername(userCheck);
                exist = stub.existUser(userExist).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (exist && cuentas.containsKey(iban)) {
                double resultado = cuentas.get(iban) + cantidad;
                cuentas.put(iban, resultado);

                ArrayList<Movement> mov = movimientos.get(usuarioActual);
                Movement var = new Movement();
                if (mov == null) mov = new ArrayList<>();
                var.setQuantity(cantidad);
                var.setIBAN(iban);
                while (mov.size() >= 10) {
                    mov.remove(0);
                }
                mov.add(var);
                movimientos.put(usuarioActual, mov);
                response.setBalance(resultado);
                response.setResult(true);
            }
        }

        AddIncomeResponse res = new AddIncomeResponse();
        res.set_return(response);
        return res;
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

        User usuario = login.getArgs0();
        String username = usuario.getName();
        String password = usuario.getPwd();

        Response response = new Response();
        response.setResponse(false);

        if (username.equals("admin") && password.equals(superuser.getPwd())) {
            response.setResponse(true);
        }
        else {
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Login login1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Login();
                UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd loginBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd();
                loginBackEnd.setName(username);
                loginBackEnd.setPassword(password);
                login1.setLogin(loginBackEnd);
                response.setResponse(stub.login(login1).get_return().getResult());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(response.getResponse()){
            if(conectado){
                int numeroSesiones = sesiones.get(username);
                numeroSesiones++;
                sesiones.put(username,numeroSesiones);
            }
            else if(!conectado){
                sesiones.put(username,1);
                conectado = true;
                usuarioActual = username;
            }

        }

        LoginResponse res = new LoginResponse();
        res.set_return(response);

    return res;
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
        MovementList response = new MovementList();
        double[] res = new double[10];

        boolean exist = false;

        if(conectado) {
            String username = usuarioActual.getName();
            UPMAuthenticationAuthorizationWSSkeletonStub.Username userCheck = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
            userCheck.setName(username);
            UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser userExist = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
            userExist.setUsername(userCheck);

            try {
                exist = stub.existUser(userExist).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (exist && conectado && movimientos.containsKey(usuarioActual.getName())) {
            Queue<Movement> mov = new LinkedList<>(movimientos.get(usuarioActual.getName()));
            int size = mov.size();
            for (int i = 0; i < size; i++) {
                int v = 9-i;
                res[v] = mov.remove().getQuantity();
            }
            response.setResult(true);
            response.setMovementQuantities(res);
        }
        else {
            response.setResult(false);
        }

        GetMyMovementsResponse endResponse = new GetMyMovementsResponse();
        endResponse.set_return(response);
        return endResponse;
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
        boolean exist = false;

        if (conectado) {
            String username = usuarioActual.getName();
            String oldPwd = changePassword.getArgs0().getOldpwd();
            String newPwd = changePassword.getArgs0().getNewpwd();
            UPMAuthenticationAuthorizationWSSkeletonStub.Username userCheck = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
            userCheck.setName(username);
            UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser userExist = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
            userExist.setUsername(userCheck);

            try {
                exist = stub.existUser(userExist).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if(username.equals("admin")){

                if(superuser.getPwd().equals(oldPwd)) {
                    superuser.setPwd(newPwd);
                    response.setResponse(true);
                }else{
                    response.setResponse(false);
                }

            }

            else if (exist && !username.equals("admin") && !oldPwd.equals(newPwd)){

                UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword changePasswordService = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
                UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd changePasswordBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd();
                changePasswordBackEnd.setName(username);
                changePasswordBackEnd.setNewpwd(newPwd);
                changePasswordBackEnd.setOldpwd(oldPwd);
                changePasswordService.setChangePassword(changePasswordBackEnd);

                try {
                    response.setResponse(stub.changePassword(changePasswordService).get_return().getResult());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

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
    