/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentesmoviles;


/**
 *
 * @author Jose Moreno, Angel Peña, Milad Zaghab
 */
public class Nodo implements InterfazNodo {
    
    private static final long serialVersionUID = 1L;
    private String IDNodo;
    private String ip;

    public Nodo(String IDNodo, String ip) {
        this.IDNodo = IDNodo;
        this.ip = ip;
    }

    public String getIDNodo() {
        return IDNodo;
    }

    public void setIDNodo(String IDNodo) {
        this.IDNodo = IDNodo;
    }

    public String getIp() {
        return ip;
    }

    public void setSocket(String ip) {
        this.ip = ip;
    }
    
    public String getNodo(){
        return ip + "/" + IDNodo;
    }
    
    public Boolean Igual ( Nodo c ){
        if ( this.getNodo().equals(c.getNodo())){
            return true;
        }
        else{
            return false;
        }
    }
    
    
}
