package Nodos.Expresion;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.Tipo;

public class NodoExpresionSentencia extends NodoExpresion {
    Token token;
    NodoExpresion nodoExpresion;

    public NodoExpresionSentencia(Token token, NodoExpresion nea) {
        this.token = token;
        nodoExpresion = nea;
        esAsignacion = false;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        nodoExpresion.setEsAsignacion(esAsignacion);
        return nodoExpresion.chequear();
    }

    @Override
    public void generar() {
        nodoExpresion.generar();
    }

    @Override
    public boolean esAsignable() {
        return nodoExpresion.esAsignable();
    }

    @Override
    public boolean esLlamada() {
        return nodoExpresion.esLlamada();
    }

    @Override
    public boolean esAsignacion() {
        return nodoExpresion.esAsignacion();
    }

    public Token getToken() {
        return token;
    }
}
