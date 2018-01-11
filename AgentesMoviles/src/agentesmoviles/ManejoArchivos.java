/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josemoreno
 */
public class ManejoArchivos implements InterfazArchivo {

    private static final long serialVersionUID = 1L;

    public ManejoArchivos() {}

    public ArrayList<Nodo> leerArchivosCF( String Ruta) throws IOException{

           /* LEE LOS ARCHIVOS CONFIG FILE  Y GUARDA LOS DATOS EN UN ARRAYLIST */

            ArrayList pruebaT= new ArrayList();
            ArrayList<Nodo> cof= new ArrayList();


            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;

          try {
             // Apertura del fichero y creacion de BufferedReader para poder
             // hacer una lectura comoda (disponer del metodo readLine()).

             archivo = new File (Ruta);
             fr = new FileReader (archivo);
             br = new BufferedReader(fr);

             // Lectura del fichero
             String linea;
             while((linea=br.readLine())!=null)
             {
              pruebaT.add(linea);

             }
             String nodo="";
             String socket="";

              for (int h = 0; h < pruebaT.size(); h++) {

                  if(h%2==0)
                  {
                      nodo=pruebaT.get(h).toString();
                      //System.out.println("nodo: "+nodo);

                  }
                    if(h%2!=0)
                  {
                      socket=pruebaT.get(h).toString();
                      //System.out.println("Socket1: "+socket);

                      try{


                          Nodo cf = new Nodo(nodo, socket);
                          cof.add(cf);

                        nodo="";
                        socket="";

                      } 
                      catch(java.lang.NumberFormatException e){} 



                  }

              }
          }
          catch(java.lang.NumberFormatException e){

              System.out.println("verificar archivo");
          } catch (FileNotFoundException ex) {
              System.out.println(ex);
        }

          finally{
             // En el finally cerramos el fichero, para asegurarnos
             // que se cierra tanto si todo va bien como si salta 
             // una excepcion.
             try{                    
                if( null != fr ){   
                   fr.close();     
                }                  
             }catch (Exception e2){ 
                e2.printStackTrace();
             }
          }

            return cof;
      }

    public ArrayList<ClaveValor> leerArchivosCV( String Ruta ) throws IOException {
           /* LEE LOS ARCHIVOS CLAVE VALOR  Y GUARDA LOS DATOS EN UN ARRAYLIST*/
          ArrayList pruebaT= new ArrayList();
          ArrayList<ClaveValor> cv= new ArrayList();

          File archivo = null;
          FileReader fr = null;
          BufferedReader br = null;

          try {
             // Apertura del fichero y creacion de BufferedReader para poder
             // hacer una lectura comoda (disponer del metodo readLine()).

             archivo = new File (Ruta);
             fr = new FileReader (archivo);
             br = new BufferedReader(fr);


             // Lectura del fichero
             String linea;
             while((linea=br.readLine())!=null)
             {
              pruebaT.add(linea);

             }
             String cl="";
             String va="";

              for (int h = 0; h < pruebaT.size(); h++) {

                  if(h%2==0) //verifico que la posicion sea par para la clave
                  {
                      cl=pruebaT.get(h).toString();
                      //System.out.println("Clave: "+cl);

                  }
                    if(h%2!=0)// posicion impar para el valor
                  {
                      va=pruebaT.get(h).toString();
                      //System.out.println("valor: "+va);

                      // creo CLAVEVALOR  de los datos leidos en el archivo
                      ClaveValor cvx=new ClaveValor(cl, va);
                      //añado  esa clavevalor a el arreglo
                      cv.add(cvx);
                      // reseteo variables por si acaso
                      cl="";
                      va="";
                      //System.out.println("cv size "+cv.size());

                  }

              }
          }
          catch(java.lang.NumberFormatException e){

              System.out.println("verificar archivo");
          } catch (FileNotFoundException ex) {
              System.out.println(ex);
        }

          finally{
             // En el finally cerramos el fichero, para asegurarnos
             // que se cierra tanto si todo va bien como si salta 
             // una excepcion.
             try{                    
                if( null != fr ){   
                   fr.close();     
                }                  
             }catch (Exception e2){ 
                e2.printStackTrace();
             }
          }

    return cv;
    }

    public void EscribirCV(ArrayList<ClaveValor> cv, String Ruta){
            /*ESTA FUNCION  RECIBE COMO PARAMETROS UN ARREGLO DE CLAVEVALOR EL CUAL GUARDA EN UN ARCHIVO
            IDEAL PARA USARLO DESPUES DE ACTUALIZAR/ELIMINAR/AGREGAR EL ARREGLO DE CLAVES*/
            FileWriter fichero = null;
            PrintWriter pw = null;
            try
            {
                fichero = new FileWriter(Ruta);
                pw = new PrintWriter(fichero);

                for (int i = 0; i < cv.size(); i++){
                    pw.println(cv.get(i).getClave());
                    pw.println(cv.get(i).getValor());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
               try {
               // Nuevamente aprovechamos el finally para 
               // asegurarnos que se cierra el fichero.
               if (null != fichero)
                  fichero.close();
               } catch (Exception e2) {
                  e2.printStackTrace();
               }
            }

    }

    public ArrayList<ClaveValor> EliminarCV(String clave, ArrayList<ClaveValor> acv, String Ruta){

            for (int i = 0; i < acv.size(); i++) {
                
                    ClaveValor cv= acv.get(i);

                    if(clave.compareTo(cv.getClave())==0)
                    {
                        acv.remove(i);
                        return acv;
                    }  
            }

            return acv;
    }

    public boolean BuscarCV(String clave, ArrayList<ClaveValor> acv){
            for (int i = 0; i < acv.size(); i++) {
                
                    ClaveValor cv= acv.get(i);

                    if(clave.compareTo(cv.getClave())==0)
                    {
                    return true;
                    }  
            }

            return false;

    }

    public ClaveValor BuscarNCV(String clave, String Ruta ){
        
            ArrayList<ClaveValor> acv;
            
            try { acv = leerArchivosCV(Ruta); } catch(Exception e){ acv = new ArrayList(); }
                
            for (int i = 0; i < acv.size(); i++) {
                    ClaveValor cv= acv.get(i);

                    if(clave.compareTo(cv.getClave())==0)
                    {
                    return cv;
                    }  
            }

            return null;
    }

    public ArrayList<Nodo> cruceTablas(ArrayList<Nodo> tablaAgente,ArrayList<Nodo> tablaServer){

            ArrayList<Nodo> aux= new ArrayList();


            for (int i = 0; i < tablaServer.size(); i++) {

                    Nodo cfag = tablaServer.get(i);
                    // si cfag no esta contenido en el arraylist del agente lo agrega a la tabla aux
                    if(Contiene(tablaAgente, cfag)!= true){               
                        aux.add(cfag);
                    }

            }

            return aux;
    }

    
    public Boolean Contiene( ArrayList<Nodo> Lista , Nodo nodo){
        
        for( int i = 0 ; i < Lista.size() ; i++ ){
            if ( Lista.get(i).getNodo().equals(nodo.getNodo()))
                return true;
        }
        
        return false;
    }

}
