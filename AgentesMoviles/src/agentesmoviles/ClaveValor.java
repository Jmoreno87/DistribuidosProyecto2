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
public class ClaveValor implements InterfazClaveValor{
    
    private static final long serialVersionUID = 1L;
    private String Clave;
    private String Valor;

    public ClaveValor(String Clave, String Valor) {
        this.Clave = Clave;
        this.Valor = Valor;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String Valor) {
        this.Valor = Valor;
    }
    
    public String getClaveValor(){
        return "("+Clave+": "+Valor+")";
    }
    
    
}
