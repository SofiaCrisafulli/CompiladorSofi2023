package Nodos.Expresion.Operando.Literales;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.*;

public class NodoLiteralFalse extends NodoLiteralBoolean {

    public NodoLiteralFalse(Token token) {
        operador = token;
    }

    public void generar() {
        TablaDeSimbolos.gen("PUSH 0 ; apilo false");
    }
}
