package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import Nodos.Expresion.NodoExpresion;
import Nodos.Expresion.NodoExpresionCompuesta;

public abstract class NodoExpresionBinaria extends NodoExpresionCompuesta {
    NodoExpresion ladoIzq;
    NodoExpresion ladoDer;
    Token operador;

    public NodoExpresionBinaria(Token operador) {
        System.out.println("Operador en neb" + operador.getLexema());
        this.operador = operador;
    }

    public boolean esAsignable() {
        return false;
    }

    public void setExpresiones(NodoExpresion ladoI, NodoExpresion ladoD) {
        ladoIzq = ladoI;
        ladoDer = ladoD;
    }
}
