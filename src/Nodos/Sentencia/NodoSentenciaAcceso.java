package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresionCompuesta;
import Nodos.Expresion.Operando.NodosAccesos.NodoAcceso;

public class NodoSentenciaAcceso extends NodoSentencia {
    NodoExpresionCompuesta nodoAcceso;

    public NodoSentenciaAcceso(Token tk, NodoExpresionCompuesta na) {
        tokenSentencia = tk;
        nodoAcceso = na;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        nodoAcceso.chequear();
    }

    @Override
    public void generar() {
        nodoAcceso.generar();
    }
}
