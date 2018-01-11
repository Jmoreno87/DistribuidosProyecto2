/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

    
public class ClienteAM {

	private static InterfazServidorAM AgentsServer;

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
            
                //====Creacion del itinerario de este cliente
                ArrayList<Nodo> listaNodos = new ArrayList();
             
                //=====Fin del itinerario===================

		AgentsServer = (InterfazServidorAM) Naming.lookup("//localhost/Server1"); //busca el server con el nombre dado
                
                
                AgenteMovil agente = new AgenteMovil("007",listaNodos);
                
                AgentsServer.RecibirAgente(agente);
                
                //String txt = JOptionPane.showInputDialog("What is your name?");               
		//String response = look_up.helloTo(txt); //llama al metodo remoto
		//JOptionPane.showMessageDialog(null, response);

	}

}