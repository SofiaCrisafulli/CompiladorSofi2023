package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoEBMenos extends NodoExpresionBinaria {

    public NodoEBMenos(Token operador) {
        super(operador);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        Tipo tipo = new TipoInt(operador);
        Tipo tipoLderecho = ladoDer.chequear();
        Tipo tipoLizquierdo = ladoIzq.chequear();
        if(!(tipoLderecho.mismoTipo(tipo) && tipoLizquierdo.mismoTipo(tipo)))
            throw new ExcepcionSemantica(operador, "el lado derecho como el izquierdo debe ser del mismo tipo");
        return tipo;
    }

    public void generar() {
        ladoIzq.generar();
        ladoDer.generar();
        TablaDeSimbolos.gen("SUB");
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
