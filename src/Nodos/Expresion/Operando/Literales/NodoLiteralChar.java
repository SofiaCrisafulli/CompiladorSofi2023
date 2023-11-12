package Nodos.Expresion.Operando.Literales;

import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoLiteralChar extends NodoLiteral{

    @Override
    public Tipo chequear() {
        return new TipoChar(operador);
    }

    public void generar() {
        TablaDeSimbolos.gen("PUSH" + operador.getLexema() + "; apilo caracter");
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
