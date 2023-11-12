package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoEBDistinto extends NodoExpresionBinaria {

    public NodoEBDistinto(Token operador) {
        super(operador);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        Tipo ladoIzquierdo = ladoIzq.chequear();
        Tipo ladoDerecho = ladoDer.chequear();
        if(!(ladoDerecho.esSubtipo(ladoIzquierdo) || ladoIzquierdo.esSubtipo(ladoDerecho)))
            throw new ExcepcionSemantica(operador, "debe ser un subtipo");
        return new TipoBoolean(operador);
    }

    public void generar() {
        ladoIzq.generar();
        ladoDer.generar();
        TablaDeSimbolos.gen("NE");
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