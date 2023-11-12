package Nodos.MetodosPredefinidos;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.*;
import Tipo.Tipo;

import java.util.ArrayList;

public class MetodoRead extends Metodo {
    public MetodoRead(Token tk, ArrayList<Parametro> listaP, Tipo tipoR, boolean metEs, Token claseA, Clase miClase) {
        super(tk, listaP, tipoR, metEs, claseA, miClase);
    }

    public void generar() {
        TablaDeSimbolos.gen("LOADFP");
        TablaDeSimbolos.gen("LOADSP");
        TablaDeSimbolos.gen("STOREFP");

        TablaDeSimbolos.gen("READ ; lectura de un digito entero");
        TablaDeSimbolos.gen("PUSH 48 ; apilo el 48 para restarselo al ASCII del digito paa obtener el numero");
        TablaDeSimbolos.gen("SUB");
        TablaDeSimbolos.gen("STORE 3 ; pongo el tope de la pila en la locacion reservada");

        TablaDeSimbolos.gen("STOREFP");
        TablaDeSimbolos.gen("RET " + 0 + "; +0");
        TablaDeSimbolos.gen("\n");
    }
}
