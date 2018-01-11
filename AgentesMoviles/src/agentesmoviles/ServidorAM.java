package agentesmoviles;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class ServidorAM extends UnicastRemoteObject implements InterfazServidorAM{

    private static final long serialVersionUID = 1L;
    private static InterfazServidorAM AgentsServer;
    private static String ServerName;
    private static ArrayList<Nodo> itinerario;
    private static String IPaddress;
    private static Nodo MyNodo;
    private static String RutaArchivoBD;
    private static ArrayList <ClaveValor> BD;
    private static ManejoArchivos FileHandler = new ManejoArchivos();
    
    protected ServidorAM() throws RemoteException{ 
        super();
    }
    
    //==================METODO DE RECEPCION=====================================
    @Override
    public void RecibirAgente(AgenteMovil agente) throws RemoteException{

        System.out.println ("\nServer: el Agente "+agente.nombre+" ha llegado al servidor - "+ ServerName);
              
        //cuando llega un agente a este server, 
        //el agente compara su itinerario con el del server en caso de que le falte un nodo
        if(  !agente.nodoPadre.Igual(MyNodo)){
                  agente.VerificarItinerario(itinerario);                 
        }
            
        //======================================================================
            //si llega un agente inicial desde otro servidor, se debe guardar los datos de su servidor padre.
            //y darle la BD al Agente Movil.
            if( agente.getClass() == AgenteInicial.class && !agente.nodoPadre.Igual(MyNodo)){

                //Guardar datos del Servidor padre del agente.
                itinerario.add(agente.nodoPadre);
                System.out.println("Servidor: Llego un Agente Inicial! se ha agregado a la lista de nodos -> " + itinerario.get(itinerario.size()-1).getNodo());

                //compartir BD.
                try { BD = FileHandler.leerArchivosCV(RutaArchivoBD); } catch(Exception e){ }
                ((AgenteInicial) agente).BDData = BD;
                System.out.println("Server: He compartido mi BD con el Agente Inicial.");
            }
        //======================================================================
               
        agente.ejecutar( MyNodo, RutaArchivoBD );
        

    }
    //===========================================================================

    public static void main(String[] args){

        try {        
            //===============INICIALIZACION DEL SERVIDOR DE RMI======================
            
                ServerName = JOptionPane.showInputDialog("Cual es el nombre del servidor?");  //Se obtiene el nombre del servidor             
                IPaddress = InetAddress.getLocalHost().getHostAddress(); //obtengo mi direccion IP local

                
                MyNodo = new Nodo(ServerName, IPaddress);

                Naming.rebind( "//"+MyNodo.getNodo() , new ServidorAM() ); //se registra el este servidor con el nombre especificado
                System.out.println("Server: Server ready - " + MyNodo.getNodo() );

                 String[] Conectados = Naming.list("rmi://"+MyNodo.getNodo());
 
            
            //===============FIN DE INICIALIZACION DE SERVIDOR========================
            
            //==============INICIO DE LA FASE DE CLIENTE=============================
                      
                    RutaArchivoBD = "D:\\Documentos\\NetBeansProjects\\AgentesMoviles\\CV3.txt";

                    itinerario = FileHandler.leerArchivosCF("D:\\Documentos\\NetBeansProjects\\AgentesMoviles\\ConfigFile.txt");
                  
                  
                    if ( itinerario.size() > 0 ){   

                       //busca el primer server de la lista
                       System.out.println("Enviando Agente para anunciar mi llegada y pedir la BD..."); 
                        
                            int nextServerOn = 0;
                            while(true){
                                
                                try{
                                    AgentsServer = (InterfazServidorAM) Naming.lookup("//" + itinerario.get(nextServerOn).getNodo());                          
                                    AgenteMovil agente = new AgenteInicial("007", itinerario, MyNodo); //crea el agente
                                    AgentsServer.RecibirAgente(agente); //manda el agente al primer servidor
                                }catch(Exception e){
                                    nextServerOn++;
                                    continue ;  
                                }
                                break;
                            }
                       

                    }
                    
                 
            //==============FIN DE INICIALIZACION DE CLIENTE==========================
                
            //==============INICIO DE LOOP DE CLIENTE=================================
                    
                    while( true ){
                        
                        System.out.println("\nQue Operacion Desea Realizar?");
                                               
                            Scanner sc = new Scanner(System.in);
                            String Input = sc.nextLine();//lee la opcion
                            Input = Input.toLowerCase();
 
                        
                       
                            String[] comando = Input.split(" |="); //descompone la string en palabras (separadas por ' ' o por '=')
                            String operacion = comando[0];
                        
                        
                        //==========================GET====================================
                        if ( operacion.equals("get") && comando.length == 2 ){
                            
                                System.out.println("buscar - "+ comando[1] );
                                ClaveValor Busqueda =  FileHandler.BuscarNCV( comando[1] , RutaArchivoBD );

                                if ( Busqueda != null ){
                                    System.out.println(Busqueda.getClaveValor());
                                }
                                else{
                                    System.out.println("No se encontro el valor en la BD. Mandando Agente para buscar en las replicas...");

                                    
                                    AgenteMovil agente = new AgenteGet("008", itinerario, MyNodo, comando[1]); //crea el agente
                                    sendAgent(agente);

                                }
                            
                        }
                        //==========================SET====================================
                        else if ( operacion.equals("set") && comando.length == 3 ){
                            
                                System.out.println("insertar - "+ comando[1] + ": "+ comando[2]);

                                ClaveValor Busqueda =  FileHandler.BuscarNCV( comando[1] , RutaArchivoBD );

                                if ( Busqueda != null ){
                                    
                                    //Actualizar BD
                                    System.out.println("Se encontro un valor con la misma clave en la BD. Cambiando Valor en la BD.");
                                    try { BD = FileHandler.leerArchivosCV( RutaArchivoBD ); } catch(Exception e){ BD = new ArrayList();} //sacar BD del archivo
                                    BD = FileHandler.EliminarCV(comando[1], BD, RutaArchivoBD); //eliminamos el valor existente
                                    BD.add(new ClaveValor(comando[1], comando[2])); //añadir nuevo Par a la BD
                                    FileHandler.EscribirCV(BD, RutaArchivoBD); //Guardar de nuevo la BD en el archivo.

                                    //Mandar Agente para actualizar las demas BD
                                    System.out.println("Mandando Agente a actualizar la BD de los demas nodos del sistema...");             
                                    AgenteMovil agente = new AgenteSet("009", itinerario, MyNodo, comando[1], comando[2]); //crea el agente
                                    sendAgent(agente);
                                }
                                else{
                                    
                                    //Actualizar BD
                                    System.out.println("No se encontro el valor en la BD. Agregado par Clave-Valor a la BD.");
                                    try { BD = FileHandler.leerArchivosCV( RutaArchivoBD ); } catch(Exception e){ BD = new ArrayList();} //sacar BD del archivo
                                    BD.add(new ClaveValor(comando[1], comando[2])); //añadir par la BD
                                    FileHandler.EscribirCV(BD, RutaArchivoBD); //Guardar de nuevo la BD en el archivo.

                                    //Mandar Agente para actualizar las demas BD
                                    System.out.println("Mandando Agente a actualizar la BD de los demas nodos del sistema...");             
                                    AgenteMovil agente = new AgenteSet("009", itinerario, MyNodo, comando[1], comando[2]); //crea el agente
                                    sendAgent(agente);
                                }
                            
                        }
                        //=========================DEL====================================
                        else if ( operacion.equals("del") && comando.length == 2 ){
                            
                            System.out.println("eliminar - "+ comando[1] );
                            ClaveValor Busqueda =  FileHandler.BuscarNCV( comando[1] , RutaArchivoBD );

                                if ( Busqueda != null ){
                                    
                                    //Actualizar BD
                                    System.out.println("Se encontro un valor con la misma clave en la BD y ha sido eliminado.");
                                    try { BD = FileHandler.leerArchivosCV( RutaArchivoBD ); } catch(Exception e){ BD = new ArrayList();} //sacar BD del archivo
                                    BD = FileHandler.EliminarCV(comando[1], BD, RutaArchivoBD); //eliminamos el valor existente
                                    FileHandler.EscribirCV(BD, RutaArchivoBD); //Guardar de nuevo la BD en el archivo.

                                    //Mandar Agente para actualizar las demas BD
                                    System.out.println("Mandando Agente a actualizar la BD de los demas nodos del sistema...");              
                                    AgenteMovil agente = new AgenteDel("010", itinerario, MyNodo, comando[1]); //crea el agente
                                    sendAgent(agente);

                                }
                                else{
                                    
                                    System.out.println("No se encontro el valor en la BD.");

                                    //Mandar Agente para actualizar las demas BD
                                    System.out.println("Mandando Agente a actualizar la BD de los demas nodos del sistema...");                                   
                                    AgenteMovil agente = new AgenteDel("010", itinerario, MyNodo, comando[1]); //crea el agente
                                    sendAgent(agente);

                                }
                            
                        }
                        //==========================ERROR====================================
                        else {
                            
                            System.out.println("Comando incorrecto o incompleto");
                            
                        }
   
                    }
            //==============FIN DE LOOP DE CLIENTE=================================
                

        } catch (Exception e) {
            
            System.err.println("Server: Server exception: " + e.toString());
            e.printStackTrace();

        }

    }

    public static void sendAgent(AgenteMovil agente){
         int nextServerOn = 0;
                            while(true){
                                
                                try{
                                    
                                    AgentsServer = (InterfazServidorAM) Naming.lookup("//" + itinerario.get(nextServerOn).getNodo());                          
                                    AgentsServer.RecibirAgente(agente); //manda el agente al primer servidor
                                }catch(Exception e){
                                    System.out.println("Agente: Siguiente nodo caido, intentando con el siguiente...");
                                    nextServerOn++;
                                    continue ;  
                                }
                                break;
                            }
    }
}