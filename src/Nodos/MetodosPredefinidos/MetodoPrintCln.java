package Nodos.MetodosPredefinidos;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.*;
import Tipo.Tipo;

import java.util.ArrayList;

public class MetodoPrintCln extends Metodo {
    public MetodoPrintCln(Token tk, ArrayList<Parametro> listaP, Tipo tipoR, boolean metEs, Token claseA, Clase miClase) {
        super(tk, listaP, tipoR, metEs, claseA, miClase);
    }

    public void generar() {
        TablaDeSimbolos.gen("LOADFP");
        TablaDeSimbolos.gen("LOADSP");
        TablaDeSimbolos.gen("STOREFP");

        TablaDeSimbolos.gen("LOAD 3 ; cargo el primer parametro");
        TablaDeSimbolos.gen("CPRINT");
        TablaDeSimbolos.gen("PRNLN");

        TablaDeSimbolos.gen("STOREFP");
        TablaDeSimbolos.gen("RET " + 1 + "; +1");
        TablaDeSimbolos.gen("\n");
    }
}
