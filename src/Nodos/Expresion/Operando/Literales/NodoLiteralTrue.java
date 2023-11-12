package Nodos.Expresion.Operando.Literales;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.TablaDeSimbolos;

public class NodoLiteralTrue extends NodoLiteralBoolean {

    public NodoLiteralTrue(Token token){
        operador = token;
    }

    public void generar() {
        TablaDeSimbolos.gen("PUSH 1 ; apilo true");
    }
}
