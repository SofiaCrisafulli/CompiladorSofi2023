package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoEBMayorIgual extends NodoExpresionBinaria {
    public NodoEBMayorIgual(Token operador) {
        super(operador);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        Tipo tipo = new TipoInt(operador);
        Tipo tipoIzq = ladoIzq.chequear();
        Tipo tipoDer = ladoDer.chequear();
        if(!(tipoIzq.mismoTipo(tipo) && tipoDer.mismoTipo(tipo)))
            throw new ExcepcionSemantica(operador, "el lado derecho como el izquierdo debe ser del mismo tipo");
        return new TipoBoolean(operador);
    }

    public void generar() {
        ladoIzq.generar();
        ladoDer.generar();
        TablaDeSimbolos.gen("GE");
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
