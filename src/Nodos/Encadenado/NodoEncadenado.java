package Nodos.Encadenado;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.*;

public abstract class NodoEncadenado {

    public NodoEncadenado nodoEncadenado;
    protected boolean esAsignacion = true;
    protected Token nombre;
    protected boolean esLadoIzq = false;
    public abstract Tipo chequear(Tipo t, Token token) throws ExcepcionSemantica;

    public abstract boolean esAsignable();
    public abstract boolean esLlamada();

    public abstract boolean esAsignacion();

    public void setNodoEncadenado(NodoEncadenado nodoEncadenado) {
        if (this.nodoEncadenado != null)
            this.nodoEncadenado.setNodoEncadenado(nodoEncadenado);
        else
            this.nodoEncadenado = nodoEncadenado;
    }

    public void setEsAsignacion(boolean esAsignacion) {
        this.esAsignacion = esAsignacion;
    }

    public Token getUltimoToken() {
        if(nodoEncadenado != null)
            return nodoEncadenado.getUltimoToken();
        else
            return nombre;
    }

    public void setMismoLado(boolean esLadoIzq) {
        this.esLadoIzq = esLadoIzq;
    }

    public abstract void generar();

    public void setEsLadoIzq(boolean esLadoIzq) {
        this.esLadoIzq = esLadoIzq;
    }
}
