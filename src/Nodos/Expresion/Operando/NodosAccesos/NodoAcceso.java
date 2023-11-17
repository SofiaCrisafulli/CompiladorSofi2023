package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import Nodos.Encadenado.NodoEncadenado;
import Nodos.Expresion.Operando.NodoOperando;

public abstract class NodoAcceso extends NodoOperando {
    NodoEncadenado nodoEncadenado;

    public abstract boolean esAsignable();

    public void setOperador(Token tokenC) {
        operador = tokenC;
    }

    public void setNodoEncadenado(NodoEncadenado nodoEncadenado) {
        if (this.nodoEncadenado != null)
            this.nodoEncadenado.setNodoEncadenado(nodoEncadenado);
        else
            this.nodoEncadenado = nodoEncadenado;
    }

}
