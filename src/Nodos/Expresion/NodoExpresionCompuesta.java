package Nodos.Expresion;

import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.Tipo;

public abstract class NodoExpresionCompuesta extends NodoExpresion {
    public abstract Tipo chequear() throws ExcepcionSemantica;
}