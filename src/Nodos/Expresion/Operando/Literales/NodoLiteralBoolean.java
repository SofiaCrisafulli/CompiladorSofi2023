package Nodos.Expresion.Operando.Literales;

import Tipo.*;

public class NodoLiteralBoolean extends NodoLiteral {


    @Override
    public Tipo chequear() {
        return new TipoBoolean(operador);
    }

    @Override
    public void generar() {

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
