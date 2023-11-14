package Nodos.MetodosPredefinidos;

import AnalilzadorLexico.Token;
import Nodos.Sentencia.NodoBloque;
import TablaDeSimbolos.*;
import Tipo.Tipo;

import java.util.ArrayList;

public class MetodoPrintB extends Metodo {

    public MetodoPrintB(Token tk, ArrayList<Parametro> listaP, Tipo tipoR, boolean metEs, Token claseA, Clase miClase) {
        super(tk, listaP, tipoR, metEs, claseA, miClase);
    }

    public void generar() {
        TablaDeSimbolos.gen("LOADFP");
        TablaDeSimbolos.gen("LOADSP");
        TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");

        TablaDeSimbolos.gen("LOAD 3 ; cargo el primer parametro");
        TablaDeSimbolos.gen("BPRINT");

        TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
        TablaDeSimbolos.gen("RET " + 1 + "; +1");
        TablaDeSimbolos.gen("\n");
    }
}
