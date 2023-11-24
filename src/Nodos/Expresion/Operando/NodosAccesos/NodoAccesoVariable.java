package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Sentencia.NodoVarLocal;
import TablaDeSimbolos.*;
import Tipo.*;

public class NodoAccesoVariable extends NodoAcceso {
    Atributo atributo;
    NodoVarLocal nodoVarLocal;
    Parametro parametro;

    public NodoAccesoVariable(Token token) {
        operador = token;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        if (!esLlamada() && !esAsignable())
            throw new ExcepcionSemantica(operador, "no es posible realizar una llamada a una variable");
        if (esAsignacion() && !esAsignable())
            throw new ExcepcionSemantica(operador, "no es posible realizar una asignacion");
        Tipo tipo = null;
        if (esLlamada() || esAsignable()) {
            nodoVarLocal = TablaDeSimbolos.getInstance().getVarLocal(operador.getLexema());
            if (nodoVarLocal != null) {
                tipo = nodoVarLocal.getTipo();
            } else {
                parametro = TablaDeSimbolos.getClaseActual().getMetodoActual().getParametro(operador);
                if (parametro != null)
                    tipo = parametro.getTipo();
                else {
                    atributo = TablaDeSimbolos.getClase(TablaDeSimbolos.getClaseActual().getToken().getLexema()).getAtributos().get(operador.getLexema());
                    if (atributo != null) {
                        ClaseConcreta clase = TablaDeSimbolos.getClase(TablaDeSimbolos.getClaseActual().getToken().getLexema());
                        if (clase.getMetodoActual().getMetodoEstatico() && !atributo.isEsEstatico())
                            throw new ExcepcionSemantica(operador, "no es posible acceder a una variable de instancia en un metodo estatico");
                        tipo = atributo.getTipo();
                    } else
                        throw new ExcepcionSemantica(operador, "la variable local " + operador.getLexema() + " no existe");
                }
            }

            if (nodoEncadenado != null) {
                nodoEncadenado.setEsAsignacion(esAsignacion);
                tipo = nodoEncadenado.chequear(tipo, operador);
            }
        }
        return tipo;
    }

    @Override
    public void generar() {
        if (atributo != null) {
            TablaDeSimbolos.gen("LOAD 3");
            if (!esLadoIzq || nodoEncadenado != null)
                TablaDeSimbolos.gen("LOADREF " + atributo.getOffset() + " ; offset atributo");
            else {
                TablaDeSimbolos.gen("SWAP");
                TablaDeSimbolos.gen("STOREREF " + atributo.getOffset());
            }
        } else if (parametro != null || nodoVarLocal != null) {
            int offset = genOffset();
            if (!esLadoIzq || nodoEncadenado != null)
                TablaDeSimbolos.gen("LOAD " + offset);
            else
                TablaDeSimbolos.gen("STORE " + offset);
        } else if (atributo != null && atributo.getOffset() == -1) {
            System.out.println("Nodo acceso variable: " + TablaDeSimbolos.getClaseActual().labelVT());
            TablaDeSimbolos.gen("PUSH " + TablaDeSimbolos.getClaseActual().labelVT());
            if (!esLadoIzq || nodoEncadenado != null && nodoVarLocal != null)
                TablaDeSimbolos.gen("LOAD " + nodoVarLocal.getOffset() + " ; apilo variable local");
            else
                TablaDeSimbolos.gen("STORE " + nodoVarLocal.getOffset() + " ; apilo variable local");
        }
        if (nodoEncadenado != null) {
            nodoEncadenado.setMismoLado(esLadoIzq);
            nodoEncadenado.generar();
        }
    }

    private int genOffset() {
        int i = 0;
        if (parametro != null)
            i = parametro.getOffset();
        else if (nodoVarLocal != null)
            i = nodoVarLocal.getOffset();
        return i;
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
        boolean es;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esLlamada();
        else
            es = false;
        return es;
    }

    @Override
    public boolean esAsignacion() {
        return false;
    }


}