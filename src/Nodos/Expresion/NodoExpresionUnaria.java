package Nodos.Expresion;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.Operando.NodoOperando;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoExpresionUnaria extends NodoExpresionCompuesta {
    NodoOperando operando;

    public NodoExpresionUnaria(Token token, NodoOperando nodoOperando) {
        operador = token;
        operando = nodoOperando;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        Tipo tipo = null;
        if (operador != null) {
            if (operador.getTipoDeToken() == TipoDeToken.op_mas || operador.getTipoDeToken() == TipoDeToken.op_menos) {
                tipo = new TipoInt(operador);
                if (!operando.chequear().mismoTipo(tipo))
                    throw new ExcepcionSemantica(operador, "tienen que ser del mismo tipo");
            } else if (operador.getTipoDeToken() == TipoDeToken.op_negacion) {
                tipo = new TipoBoolean(operador);
                if (!operando.chequear().mismoTipo(tipo))
                    throw new ExcepcionSemantica(operador, "tienen que ser del mismo tipo");
            }
        } else
            tipo = operando.chequear();
        return tipo;
    }

    public void generar() {
        operando.generar();
        if (operador.equals("-"))
            TablaDeSimbolos.gen("NEG ; menos unario");
        else if (operador.equals("!"))
            TablaDeSimbolos.gen("NOT ; negacion unaria");
    }

    @Override
    public boolean esAsignable() {
        boolean es = false;
        if (operador.getTipoDeToken() != TipoDeToken.op_mas || operador.getTipoDeToken() != TipoDeToken.op_menos || operador.getTipoDeToken() != TipoDeToken.op_negacion)
            es = operando.esAsignable();
        return es;
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
