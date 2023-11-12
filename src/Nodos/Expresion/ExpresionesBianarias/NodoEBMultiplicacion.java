package Nodos.Expresion.ExpresionesBianarias;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoEBMultiplicacion extends NodoExpresionBinaria {

    public NodoEBMultiplicacion(Token operador) {
        super(operador);
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        if(esLlamada() || esAsignacion())
            throw new ExcepcionSemantica(operador, "no es posible realizar una llamada o una asignacion");
        Tipo tipo = new TipoInt(operador);
        Tipo tipoIzq = ladoIzq.chequear();
        Tipo tipoDer = ladoDer.chequear();
        if(!(tipoIzq.mismoTipo(tipo) && tipoDer.mismoTipo(tipo)))
            throw new ExcepcionSemantica(operador, "el lado derecho y el lado izquierdo deben ser del mismo tipo");
        return tipo;
    }

    public void generar() {
        ladoIzq.generar();
        ladoDer.generar();
        TablaDeSimbolos.gen("MUL");
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
