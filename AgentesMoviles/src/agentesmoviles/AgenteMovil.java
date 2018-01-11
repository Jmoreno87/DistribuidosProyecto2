/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;


//Una implementación de un agente móvil


import java.rmi.Naming;
import java.util.*;


public class AgenteMovil implements InterfazAgenteMovil{

    private static final long serialVersionUID = 1L;
    int indiceNodoActual;  //cuál es el siguiente ordenador a visitar
    
    Nodo nodoPadre;
    
    String nombre;
    ArrayList<Nodo> listaNodos; //el itinerario
    
    ManejoArchivos DBManager = new ManejoArchivos();

    public AgenteMovil(String miNombre, ArrayList<Nodo> laListaNodos){
        nombre = miNombre;
        listaNodos = laListaNodos;
        indiceNodoActual = 1;
    } 

    //Este método define las tareas que realiza el agente
    //móvil una vez que llega a un servidor.
    @Override
    public void ejecutar( Nodo NodoActual, String Ruta){
      
    }
    
    public void VerificarItinerario( ArrayList<Nodo> ItinerarioServer ){
        
        System.out.println("Agente: verificando itinerario...");
        
        ArrayList <Nodo> NodosFaltantes = DBManager.cruceTablas( this.listaNodos , ItinerarioServer);
        
        if( NodosFaltantes.size() > 0){
            System.out.println("Agente: He encontrado nodos que no estan en mi itinerario y han sido agregados!");
            listaNodos.addAll(NodosFaltantes);
        }
        
    }
    
    
    //El método dormir suspende la ejecución de este objeto
    //un número determinado de segundos.
    static void dormir(double time){
        try{
             Thread.sleep( (long) ( time * 1000.0 ) );
        }
        catch(InterruptedException e){
            System.out.println("excepción en dormir");
        }   
    }//fin dormir

}//fin class Agente



