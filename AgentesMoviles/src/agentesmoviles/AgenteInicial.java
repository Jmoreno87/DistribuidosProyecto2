/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;

import static agentesmoviles.AgenteMovil.dormir;
import java.rmi.Naming;
import java.util.ArrayList;

/**
 *
 * @author Angel Fabricio
 */
public class AgenteInicial extends AgenteMovil {
        
    ArrayList<ClaveValor> BDData;
    
    public AgenteInicial(String miNombre, ArrayList<Nodo> laListaNodos, Nodo NodoPadre ){
       
        super(miNombre, laListaNodos);

        this.nodoPadre = NodoPadre;
    }
    
    @Override
    public void ejecutar( Nodo NodoActual, String Ruta ){

        //si volvio al nodo padre
        if ( NodoActual.Igual(nodoPadre) ){ ///
            
            System.out.println("Agente: Volvi de mi viaje!");            
            DBManager.EscribirCV(BDData, Ruta);            
            System.out.println("Agente: BD Obtenida y Archivo Actualizado!");
        }
        else{
                    
            dormir(2); //pausa para poder visualizarlo
            System.out.println("Agente: Aquí el agente Inicial 007 Ejecutandose!");
            dormir(2); //pausa para poder visualizarlo     
              
            indiceNodoActual++; //aumenta el contador - ACA SE USARA BUSCAR SIGUIENTE.

            
            //si hay más computadoras que visitar
            if( indiceNodoActual < listaNodos.size() ){

                while( indiceNodoActual < listaNodos.size() ){
                                    
                        //busca el siguiente nodo a visitar
                        String NodoSiguiente = listaNodos.get(indiceNodoActual).getNodo();
                        dormir(2);//pausa para poder visualizarlo
                        
                        try{
                            
                            InterfazServidorAM NextServer =(InterfazServidorAM) Naming.lookup("//" + NodoSiguiente); //busca el server con el nombre dado por el siguiente nodo del itinerario
                            System.out.println("Agente: Siguiente - "+ NodoSiguiente );
                            dormir(5);//pausa para poder visualizarlo
                            NextServer.RecibirAgente(this); //indica al siguiente servidor que reciba a este agente.

                        }
                        //fin try... No logro conectarse al siguiente
                        catch(Exception e){
                            indiceNodoActual++;
                            System.out.println("Agente: Siguiente nodo caido, intentando con el siguiente...");
                            continue;
                        }
                        
                        break;
                }
                
            }//fin if
            if( indiceNodoActual >= listaNodos.size() ){ //si se han hecho todas las paradas
                
                dormir(2);//pausa para poder visualizarlo
                System.out.println("Agente: Se acabo el recorrido. Volviendo al nodo Padre... ");
                dormir(2);//pausa para poder visualizarlo
                
                 try{

                    InterfazServidorAM NextServer =(InterfazServidorAM) Naming.lookup("//" + nodoPadre.getNodo()); //busca el server con el nombre dado por el siguiente nodo del itinerario
                    System.out.println("Agente: Siguiente - "+ nodoPadre.getNodo() );
                    dormir(2);//pausa para poder visualizarlo
                    NextServer.RecibirAgente(this); //indica al siguiente servidor que reciba a este agente.

                }//fin try... no se encontro al nodo padre
                catch(Exception e){
                    System.out.println
                    ("Agente: Excepción en el ejecuta del Agente:" + e);
                }
            }
            
        }
        
    }
    
}
