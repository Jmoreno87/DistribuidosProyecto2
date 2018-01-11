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

public class AgenteGet extends AgenteMovil {
    
    private String Clave;
    private ClaveValor Resultado = null;
    
    public AgenteGet(String miNombre, ArrayList<Nodo> laListaNodos, Nodo NodoPadre, String Clave ){
       
        super(miNombre, laListaNodos);

        this.Clave = Clave;
        this.nodoPadre = NodoPadre;
    }
    
    
    @Override
    public void ejecutar( Nodo NodoActual, String Ruta ){

        //si volvio al nodo padre
        if ( NodoActual.Igual(nodoPadre) ){
            
                System.out.println("Agente: Volvi.");            

                if( Resultado != null ){

                    System.out.println("Agente: El valor que busca es: (" + Resultado.getClaveValor() + ")" );

                    //actualizar BD de Padre
                    ArrayList<ClaveValor> BD;
                    try { BD = DBManager.leerArchivosCV(Ruta); } catch(Exception e){ BD = new ArrayList();} //sacar BD del archivo
                    BD.add(Resultado); //añadir resultado de la busqueda a la BD
                    DBManager.EscribirCV(BD, Ruta); //Guardar de nuevo la BD en el archivo.
                    
                    System.out.println("BD Actualizada con el valor encontrado!");

                }
                else{

                    System.out.println("Agente: No encontre lo que buscaba.");

                }            
        }
        //no esta en el padre, sino que esta en cualquier otro nodo del sistema
        else{
                    
            dormir(2); //pausa para poder visualizarlo
            System.out.println("Agente: Aquí el agente Get 008! Buscando...");
            dormir(2); //pausa para poder visualizarlo  
            Resultado =  DBManager.BuscarNCV( Clave , Ruta );
            
            //se encontro el par en este nodo
            if( Resultado != null ){
                       
                       try{// vamos al nodo padre

                            InterfazServidorAM NextServer =(InterfazServidorAM) Naming.lookup("//" + nodoPadre.getNodo()); //busca el server con el nombre dado por el siguiente nodo del itinerario
                            System.out.println("Agente: Encontre lo que buscaba. Volviendo al nodo Padre..." );
                            dormir(2);//pausa para poder visualizarlo
                            NextServer.RecibirAgente(this); //indica al siguiente servidor que reciba a este agente.

                        }//fin try... no se encontro al nodo padre
                        catch(Exception e){
                            System.out.println
                            ("Agente: No se encuentra el nodo Padre:" + e);
                        }
            }
            //no se encontro el par.. tratamos en el siguiente nodo
            else{
                      
                indiceNodoActual++; //aumenta el contador

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
                    System.out.println("Agente: No encontre lo que buscaba y no hay mas nodos que visitar. Volviendo al nodo Padre...");
                    dormir(2);//pausa para poder visualizarlo

                     try{

                        InterfazServidorAM NextServer =(InterfazServidorAM) Naming.lookup("//" + nodoPadre.getNodo()); //busca el server con el nombre dado por el siguiente nodo del itinerario
                        System.out.println("Agente: Siguiente - "+ nodoPadre.getNodo() );
                        dormir(2);//pausa para poder visualizarlo
                        NextServer.RecibirAgente(this); //indica al siguiente servidor que reciba a este agente.

                    }//fin try... no se encontro al nodo padre
                    catch(Exception e){
                        System.out.println
                        ("Agente: No se encuentra el nodo Padre:" + e);
                    }
                }
            
            } 
        }
        
    }
}
