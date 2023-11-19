package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.*;
import Tipo.*;

import java.util.ArrayList;

public class NodoLlamadaOAsignacion extends NodoSentencia {
    Token nombre;
    NodoExpresion nodoExpresion;
    ArrayList<NodoExpresion> parametros;
    Tipo tipo;

    public NodoLlamadaOAsignacion(Token n, NodoExpresion ne) {
        nombre = n;
        parametros = new ArrayList<>();
        nodoExpresion = ne;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        tipo = nodoExpresion.chequear();
        if (!nodoExpresion.esAsignacion() && !nodoExpresion.esLlamada())
            throw new ExcepcionSemantica(nombre, "debe ser una asignación o una llamada");
    }

    @Override
    public void generar() {
        nodoExpresion.generar();
        if(!tipo.mismoTipo(new TipoVoid(nombre)))
            TablaDeSimbolos.gen("POP");
    }
}