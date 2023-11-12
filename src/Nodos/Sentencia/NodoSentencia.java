package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;


public abstract class NodoSentencia {

    Token tokenSentencia;
    boolean esRetornante;
    public abstract void chequear() throws ExcepcionSemantica;
    public abstract void generar();

    public boolean esRetornante() {
        return false;
    }
}
