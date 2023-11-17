package Nodos.Expresion;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.Tipo;

public abstract class NodoExpresion {

    public Token operador;

    protected boolean esAsignacion = true;
    protected boolean esLadoIzq = false;
    public abstract Tipo chequear() throws ExcepcionSemantica;

    public abstract void generar();
    public abstract boolean esAsignable();
    public abstract boolean esLlamada();
    public abstract boolean esAsignacion();

    public void setEsAsignacion(boolean esAsignacion) {
        this.esAsignacion = esAsignacion;
    }

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
    }

    public void setEsLadoIzq(boolean li) {
        esLadoIzq = li;
    }

}
