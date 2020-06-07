/**
 * UPMBankWSSkeleton.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 19, 2012 (07:45:31 IST)
 */
package es.upm.fi.sos.upmbank;

import es.upm.fi.sos.upmbank.xsd.*;
import org.apache.axis2.AxisFault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * UPMBankWSSkeleton java skeleton for the axisService
 */
public class UPMBankWSSkeleton {

    private static HashMap<String, Integer> sesiones = new HashMap<>();
    private static HashMap<String, Double> cuentas = new HashMap<>();
    private static HashMap<String, ArrayList<String>> listaCuentas = new HashMap<>();
    private static HashMap<String, ArrayList<Movement>> movimientos = new HashMap<>();
    private static UPMAuthenticationAuthorizationWSSkeletonStub stub;

    User superuser;
    private Random random;
    private String usuarioActual;
    private boolean conectado;

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
            boolean comprobacionUsuario = false;
            try {

                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                username.setName(usuarioActual);
                existUser.setUsername(username);
                comprobacionUsuario = stub.existUser(existUser).get_return().getResult();

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (comprobacionUsuario) {
                Double cantidad = addBankAcc.getArgs0().getQuantity();
                String iban = Integer.toString(random.nextInt(1000000));

                ArrayList<String> aux;
                if (listaCuentas.containsKey(usuarioActual)) {
                    aux = listaCuentas.get(usuarioActual);
                } else aux = new ArrayList<>();
                aux.add(iban);
                cuentas.put(iban, cantidad);
                listaCuentas.put(usuarioActual, aux);

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
  
            if (cuentas.get(iban).equals(0)) {
                response.setResponse(true);
                cuentas.remove(iban);
            } else {
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
        if (conectado) {

            int numSesiones = sesiones.get(usuarioActual);

            if (numSesiones == 1) {
                sesiones.remove(usuarioActual);
                usuarioActual = null;
                conectado = false;
            } else if (numSesiones > 1) {
                numSesiones--;
                sesiones.put(usuarioActual, numSesiones);
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
            String usuarioOnline = usuarioActual;
            boolean comprobacionUsuario = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                username1.setName(usuario);
                existUser.setUsername(username1);
                comprobacionUsuario = stub.existUser(existUser).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (usuarioOnline.equals("admin") && comprobacionUsuario && (cuentas == null || cuentas.isEmpty()) && !usuario.equals("admin")) {
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

            boolean comprobacionUsuario = false;
            try {
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existeUsuario = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                username1.setName(usuarioActual);
                existeUsuario.setUsername(username1);
                comprobacionUsuario = stub.existUser(existeUsuario).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (comprobacionUsuario && cuentas.containsKey(iban) && cuentas.get(iban) >= cantidad) {

                double resultado = cuentas.get(iban) - cantidad;
                cuentas.put(iban, resultado);

                ArrayList<Movement> movimientosAux = movimientos.get(usuarioActual);
                Movement var = new Movement();
                if (movimientosAux == null) movimientosAux = new ArrayList<>();
                var.setIBAN(iban);
                var.setQuantity(cantidad);
                if (movimientosAux.size() >= 10) {
                    movimientosAux.remove(0);
                }
                movimientosAux.add(var);
                movimientos.put(usuarioActual, movimientosAux);
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
        } else {
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
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existeUsuario = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                username1.setName(usuarioActual);
                existeUsuario.setUsername(username1);
                exist = stub.existUser(existeUsuario).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (exist && cuentas.containsKey(iban)) {
                double resultado = cuentas.get(iban) + cantidad;
                cuentas.put(iban, resultado);

                ArrayList<Movement> movimientosAux = movimientos.get(usuarioActual);
                Movement var = new Movement();
                if (movimientosAux == null) movimientosAux = new ArrayList<>();
                var.setQuantity(cantidad);
                var.setIBAN(iban);
                while (movimientosAux.size() >= 10) {
                    movimientosAux.remove(0);
                }
                movimientosAux.add(var);
                movimientos.put(usuarioActual, movimientosAux);
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
        } else {
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
        if (response.getResponse()) {
            if (conectado) {
                int numeroSesiones = sesiones.get(username);
                numeroSesiones++;
                sesiones.put(username, numeroSesiones);
            } else {
                sesiones.put(username, 1);
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

        if (!conectado) {
            response.setResult(false);
        } else {
            boolean comprobacionUsuario = false;
            try {
                String username = usuarioActual;
                UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existeUsuario = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                username1.setName(username);
                existeUsuario.setUsername(username1);
                comprobacionUsuario = stub.existUser(existeUsuario).get_return().getResult();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (movimientos.containsKey(usuarioActual) && comprobacionUsuario) {
                double[] res = new double[10];
                ArrayList<Movement> movimientosAux = new ArrayList<>(movimientos.get(usuarioActual));
                int size = movimientosAux.size();
                for (int i = 0; i < size; i++) {
                    int v = 9 - i;
                    res[v] = movimientosAux.remove(0).getQuantity();
                }
                response.setResult(true);
                response.setMovementQuantities(res);
            }
        }

        GetMyMovementsResponse res = new GetMyMovementsResponse();
        res.set_return(response);

        return res;
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

        if (!conectado) {
            response.setResponse(false);
        } else {
            String oldPwd = changePassword.getArgs0().getOldpwd();
            String newPwd = changePassword.getArgs0().getNewpwd();
            if (usuarioActual.equals("admin")) {

                if (superuser.getPwd().equals(oldPwd)) {
                    superuser.setPwd(newPwd);
                    response.setResponse(true);
                } else {
                    response.setResponse(false);
                }
            } else {
                boolean comprobacionUsuario = false;
                try {
                    UPMAuthenticationAuthorizationWSSkeletonStub.Username username1 = new UPMAuthenticationAuthorizationWSSkeletonStub.Username();
                    UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser existUser = new UPMAuthenticationAuthorizationWSSkeletonStub.ExistUser();
                    username1.setName(usuarioActual);
                    existUser.setUsername(username1);
                    comprobacionUsuario = stub.existUser(existUser).get_return().getResult();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (!oldPwd.equals(newPwd) && comprobacionUsuario) {
                    try {
                        UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword changePassword1 = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
                        UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd passwordBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd();
                        passwordBackEnd.setName(usuarioActual);
                        passwordBackEnd.setNewpwd(newPwd);
                        passwordBackEnd.setOldpwd(oldPwd);
                        changePassword1.setChangePassword(passwordBackEnd);
                        response.setResponse(stub.changePassword(changePassword1).get_return().getResult());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        ChangePasswordResponse endResponse = new ChangePasswordResponse();
        endResponse.set_return(response);

        return endResponse;
    }
}
    