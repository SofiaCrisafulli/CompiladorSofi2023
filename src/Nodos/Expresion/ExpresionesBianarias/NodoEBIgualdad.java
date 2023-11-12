package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoEBIgualdad extends NodoExpresionBinaria {

    public NodoEBIgualdad(Token operador) {
        super(operador);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        Tipo ladoIzquierdo = ladoIzq.chequear();
        Tipo ladoDerecho = ladoDer.chequear();
        if(!(ladoDerecho.esSubtipo(ladoIzquierdo) || ladoIzquierdo.esSubtipo(ladoDerecho)))
            throw new ExcepcionSemantica(operador, "el tipo debe ser un booleano");
        return new TipoBoolean(operador);
    }

    public void generar() {
        ladoIzq.generar();
        ladoDer.generar();
        TablaDeSimbolos.gen("EQ");
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