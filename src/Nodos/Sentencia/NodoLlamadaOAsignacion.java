package Nodos.Sentencia;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import Nodos.Expresion.NodoExpresionCompuesta;
import TablaDeSimbolos.Metodo;
import TablaDeSimbolos.*;
import Tipo.*;

import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaOAsignacion extends NodoSentencia {
    Token nombre;
    NodoExpresion nodoExpresion;
    ArrayList<NodoExpresion> parametros;

    public NodoLlamadaOAsignacion(Token n, NodoExpresion ne) {
        nombre = n;
        parametros = new ArrayList<>();
        nodoExpresion = ne;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        nodoExpresion.chequear();
        if (!nodoExpresion.esAsignacion() && !nodoExpresion.esLlamada())
            throw new ExcepcionSemantica(nombre, "debe ser una asignación o una llamada");
    }

    @Override
    public void generar() {
        nodoExpresion.generar();
    }
}