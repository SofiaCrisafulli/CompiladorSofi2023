package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.*;
import Tipo.*;

import java.util.ArrayList;
import java.util.Iterator;

public class NodoAccesoMetodo extends NodoAcceso {

    ArrayList<NodoExpresion> parametros;
    ArrayList<NodoExpresion> parametrosInvertidos;
    Metodo metodo;

    public NodoAccesoMetodo(Token token) {
        operador = token;
        parametros = new ArrayList<>();
        parametrosInvertidos = new ArrayList<>();
    }

    public Tipo chequear() throws ExcepcionSemantica {
        if (!esLlamada() && !esAsignable())
            throw new ExcepcionSemantica(operador, "no es posible realizar una llamada a un metodo");
        Tipo tipoMetodo = null;
        if (esLlamada() || esAsignable()) {
            metodo = TablaDeSimbolos.getClaseActual().getMetodo(operador.getLexema());
            if (metodo == null)
                throw new ExcepcionSemantica(operador, "el metodo no existe");

            Unidad metodoOconstructor = TablaDeSimbolos.claseActual.getMetodoActual();
            if (metodoOconstructor instanceof Metodo){
                Metodo metodoActual = (Metodo)TablaDeSimbolos.claseActual.getMetodoActual();
                if (metodoActual.getMetodoEstatico() && !metodo.getMetodoEstatico())
                    throw new ExcepcionSemantica(operador, "no existe el método actual");
                chequeoParametros(metodo);
                tipoMetodo = metodo.getTipo();
                if (nodoEncadenado != null) {
                    nodoEncadenado.setEsAsignacion(esAsignacion);
                    tipoMetodo = nodoEncadenado.chequear(tipoMetodo, operador);
                }
            } else if (metodoOconstructor instanceof  Constructor) {
                Constructor metodoActual = (Constructor)TablaDeSimbolos.claseActual.getMetodoActual();
                chequeoParametros(metodo);
                tipoMetodo = metodo.getTipo();
                if (nodoEncadenado != null) {
                    nodoEncadenado.setEsAsignacion(esAsignacion);
                    tipoMetodo = nodoEncadenado.chequear(tipoMetodo, operador);
                }
            }

        }
        return tipoMetodo;
    }

    @Override
    public void generar() {
        if (metodo.getMetodoEstatico()) {
            if (!metodo.getTipo().mismoTipo(new TipoVoid(operador)))
                TablaDeSimbolos.gen("RMEM 1 ; reservo lugar para el return");
            for (NodoExpresion ne : parametrosInvertidos)
                ne.generar();
            TablaDeSimbolos.gen("PUSH " + metodo.etiquetaMetodo());
            TablaDeSimbolos.gen("CALL ; Llama al método en el tope de la pila");
        } else {
            TablaDeSimbolos.gen("LOAD 3");
            if (!metodo.getTipo().mismoTipo(new TipoVoid(operador))) {
                TablaDeSimbolos.gen("RMEM 1 ; reservo lugar para el return");
                TablaDeSimbolos.gen("SWAP");
            }
            for (NodoExpresion ne : parametrosInvertidos) {
                ne.generar();
                TablaDeSimbolos.gen("SWAP");
            }
            TablaDeSimbolos.gen("DUP");
            TablaDeSimbolos.gen("LOADREF 0 ; Cargo la VT");
            TablaDeSimbolos.gen("LOADREF " + metodo.getOffset());
            TablaDeSimbolos.gen("CALL");
        }
        if (nodoEncadenado != null) {
            nodoEncadenado.setMismoLado(esLadoIzq);
            nodoEncadenado.generar();
        }
    }

    private void chequeoParametros(Metodo metodo) throws ExcepcionSemantica {
        boolean fallo = false;
        ArrayList<Parametro> listaParametros = metodo.getListaParametros();
        if (listaParametros.size() == parametros.size()) {
            Iterator<Parametro> iterador = listaParametros.iterator();
            Iterator<NodoExpresion> nodoExpresionIterator = parametros.iterator();
            while (iterador.hasNext() && nodoExpresionIterator.hasNext() && !fallo) {
                Parametro p = iterador.next();
                NodoExpresion nodoExpresion = nodoExpresionIterator.next();
                fallo = !(nodoExpresion.chequear().esSubtipo(p.getTipo()));
                parametrosInvertidos.add(0, nodoExpresion);
            }
        } else
            fallo = true;
        if (fallo)
            throw new ExcepcionSemantica(operador, "error en los parámetros del acceso al metodo");
    }


    public void setListaParametros(ArrayList<NodoExpresion> args) {
        parametros = args;
    }

    @Override
    public boolean esAsignable() {
        boolean es = false;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esAsignable();
        return es;
    }

    public boolean esLlamada() {
        boolean es;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esLlamada();
        else
            es = true;
        return es;
    }

    @Override
    public boolean esAsignacion() {
        return false;
    }
}