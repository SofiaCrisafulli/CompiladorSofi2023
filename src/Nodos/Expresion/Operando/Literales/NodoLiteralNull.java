package Nodos.Expresion.Operando.Literales;

import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoLiteralNull extends NodoLiteral {
    @Override
    public Tipo chequear() {
        return new TipoNull(operador);
    }

    public void generar() {
        TablaDeSimbolos.gen("PUSH 0 ; apilo null");
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
