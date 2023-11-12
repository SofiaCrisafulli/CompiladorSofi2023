package Nodos.Expresion;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.*;

public class NodoExpresionVacia extends NodoExpresion {

    public NodoExpresionVacia() {
        operador = new Token(TipoDeToken.exp_vacia, "", 0);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        return new TipoVoid(operador);
    }

    @Override
    public void generar() {

    }


    @Override
    public boolean esAsignable() {
        return false;
    }

    @Override
    public boolean esLlamada() {
        return false;
    }

    @Override
    public boolean esAsignacion() {
        return false;
    }
}
