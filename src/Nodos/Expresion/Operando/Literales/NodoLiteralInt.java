package Nodos.Expresion.Operando.Literales;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoLiteralInt extends NodoLiteral {

    public NodoLiteralInt(Token tokenLit) {
        operador = tokenLit;
    }

    @Override
    public Tipo chequear() {
        return new TipoInt(operador);
    }

    public void generar() {
        TablaDeSimbolos.gen("PUSH " + operador.getLexema() + "; apilo entero");
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
