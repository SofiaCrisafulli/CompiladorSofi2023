package Nodos.Encadenado;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.*;
import Tipo.*;
import jdk.jshell.execution.Util;

public class NodoVarEncadenada extends NodoEncadenado {

    Atributo atributoEnTs;

    public NodoVarEncadenada(Token token, NodoEncadenado nodo) {
        nombre = token;
        this.nodoEncadenado = nodo;
    }

    @Override
    public Tipo chequear(Tipo t, Token token) throws ExcepcionSemantica {
        Tipo tipo = null;
        if(!esAsignacion && esAsignacion())
            throw new ExcepcionSemantica(getUltimoToken(), "no es posible tener una asignacion en una variable encadenada");
        if (!esLlamada() && !esAsignable())
            throw new ExcepcionSemantica(nombre, "no es posible tener una variable encadenada");
        if (!esAsignable() && esAsignacion())
            throw new ExcepcionSemantica(nombre, "no es posible hacer una asignacion en una variable encadenada");
        Clase clase = TablaDeSimbolos.getClase(t.getNombreTipo());
        if (clase != null) {
            ClaseConcreta claseConcreta = TablaDeSimbolos.getClase(clase.getToken().getLexema());
            atributoEnTs = claseConcreta.getAtributos().get(nombre.getLexema());
            if (atributoEnTs != null)
                tipo = Utils.getTipo(atributoEnTs.getTipo().getTokenTipo());
            else
                throw new ExcepcionSemantica(nombre, "el atributo no esta en la clase");
        } else
            throw new ExcepcionSemantica(token, "la clase no existe");
        if (nodoEncadenado != null) {
            nodoEncadenado.setEsAsignacion(esAsignacion);
            tipo = nodoEncadenado.chequear(tipo, nombre);
        } else
            tipo = atributoEnTs.getTipo();
        return tipo;
    }

    @Override
    public boolean esAsignable() {
        boolean asignable = true;
        if (nodoEncadenado != null)
            asignable = nodoEncadenado.esAsignable();
        return asignable;
    }

    @Override
    public boolean esLlamada() {
        if (nodoEncadenado != null)
            return nodoEncadenado.esLlamada();
        else
            return true;
    }

    @Override
    public boolean esAsignacion() {
        if(nodoEncadenado == null)
            return false;
        else
            return nodoEncadenado.esAsignacion();
    }

    @Override
    public void generar() {
        if(!esLadoIzq || nodoEncadenado != null)
            TablaDeSimbolos.gen("LOADREF " + atributoEnTs.getOffset());
        else {
            TablaDeSimbolos.gen("SWAP");
            TablaDeSimbolos.gen("STOREREF" + atributoEnTs.getOffset());
        }
        if(nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }
}