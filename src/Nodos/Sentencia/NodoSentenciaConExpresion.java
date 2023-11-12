package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;

public class NodoSentenciaConExpresion extends NodoSentencia{
    NodoExpresion nodoExpresion;


    public NodoSentenciaConExpresion(Token token, NodoExpresion ne) {
        tokenSentencia = token;
        nodoExpresion = ne;
    }
    @Override
    public void chequear() throws ExcepcionSemantica {
        nodoExpresion.chequear();
    }

    @Override
    public void generar() {
        nodoExpresion.generar();
    }
}
