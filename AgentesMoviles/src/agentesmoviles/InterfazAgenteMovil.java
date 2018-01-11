package agentesmoviles;

import java.io.Serializable;
import java.util.ArrayList;

public interface InterfazAgenteMovil extends Serializable{
    void ejecutar( Nodo NodoActual, String Ruta );
}