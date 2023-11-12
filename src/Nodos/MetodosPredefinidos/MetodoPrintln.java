package Nodos.MetodosPredefinidos;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.*;
import Tipo.Tipo;

import java.util.ArrayList;

public class MetodoPrintln extends Metodo {
    public MetodoPrintln(Token tk, ArrayList<Parametro> listaP, Tipo tipoR, boolean metEs, Token claseA, Clase miClase) {
        super(tk, listaP, tipoR, metEs, claseA, miClase);
    }

    public void generar() {
        TablaDeSimbolos.gen("LOADFP");
        TablaDeSimbolos.gen("LOADSP");
        TablaDeSimbolos.gen("STOREFP");

        TablaDeSimbolos.gen("PRNLN");

        TablaDeSimbolos.gen("STOREFP");
        TablaDeSimbolos.gen("RET " + 0 + "; +0");
        TablaDeSimbolos.gen("\n");
    }
}
