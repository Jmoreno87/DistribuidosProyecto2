/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazServidorAM extends Remote {
    
    public void RecibirAgente(AgenteMovil agente) throws RemoteException;

}