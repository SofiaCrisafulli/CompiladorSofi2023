package Nodos.Encadenado;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.*;
import Tipo.*;

import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaEncadenada extends NodoEncadenado {

    ArrayList<NodoExpresion> parametros;
    ArrayList<NodoExpresion> paramParaCodigo;
    Clase clase;

    public NodoLlamadaEncadenada(Token token, ArrayList<NodoExpresion> p, NodoEncadenado nodo) {
        nombre = token;
        parametros = p;
        nodoEncadenado = nodo;
        paramParaCodigo = new ArrayList<>();
    }

    public NodoLlamadaEncadenada(Token token, ArrayList<NodoExpresion> p) {
        nombre = token;
        parametros = p;
        paramParaCodigo = new ArrayList<>();
    }

    @Override
    public Tipo chequear(Tipo t, Token token) throws ExcepcionSemantica {
        Tipo tipo;
        if (!esAsignacion && esAsignacion())
            throw new ExcepcionSemantica(getUltimoToken(), "no es posible tener una asignacion en una llamada encadenada");
        if (esAsignable() && !esAsignacion())
            throw new ExcepcionSemantica(new Token(TipoDeToken.op_asignacion, "=", nombre.getNroLinea()), "no es posible hacer una asignacion");
        clase = TablaDeSimbolos.getClase(t.getNombreTipo());
        if (clase == null)
            clase = TablaDeSimbolos.getInterface(t.getNombreTipo());
        if (clase != null) {
            Metodo metodo = clase.getMetodo(nombre.getLexema());
            if (metodo != null)
                tipo = metodo.getTipo();
            else
                throw new ExcepcionSemantica(nombre, "el metodo no existe");
            chequeoParametros(metodo);
        } else
            throw new ExcepcionSemantica(token, "la clase no existe en la llamada encadenada");
        if (nodoEncadenado != null) {
            nodoEncadenado.setEsAsignacion(esAsignacion);
            tipo = nodoEncadenado.chequear(tipo, nombre);
        }
        return tipo;
    }

    private void chequeoParametros(Metodo metodo) throws ExcepcionSemantica {
        boolean fallo = false;
        ArrayList<Parametro> lista = metodo.getListaParametros();
        if (lista.size() == parametros.size()) {
            Iterator<Parametro> iterator = lista.iterator();
            Iterator<NodoExpresion> iteradorNodoEx = parametros.iterator();
            while (iterator.hasNext() && !fallo) {
                Parametro p = iterator.next();
                NodoExpresion ne = iteradorNodoEx.next();
                fallo = !(ne.chequear().esSubtipo(p.getTipo()));
                paramParaCodigo.add(0, ne);
            }
        } else
            fallo = true;
        if (fallo)
            throw new ExcepcionSemantica(nombre, "los parametros estan mal");
    }

    @Override
    public boolean esAsignable() {
        boolean asignable = false;
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
        if (nodoEncadenado == null)
            return false;
        else
            return nodoEncadenado.esAsignacion();
    }

    @Override
    public void generar() {
        Metodo metodo = clase.getMetodo(nombre.getLexema());
        if (metodo.getMetodoEstatico()) {
            TablaDeSimbolos.gen("POP ; como es estatico no me sirve la referencia anterior");
            if (!metodo.getTipo().mismoTipo(new TipoVoid(new Token(TipoDeToken.pr_void, "", 0))))
                TablaDeSimbolos.gen("RMEM 1 ; reservo lugar para el return");
            for (NodoExpresion ne : paramParaCodigo)
                ne.generar();
            String label = metodo.stringLabel();
            TablaDeSimbolos.gen("PUSH " + metodo.stringLabel() + "; etiqueta");
            TablaDeSimbolos.gen("CALL");
        } else {
            if (!metodo.getTipo().mismoTipo(new TipoVoid(new Token(TipoDeToken.pr_void, "", 0)))) {
                TablaDeSimbolos.gen("RMEM 1 ; reservo lugar para el return");
                TablaDeSimbolos.gen("SWAP");
            }
            for (NodoExpresion ne : paramParaCodigo) {
                ne.generar();
                TablaDeSimbolos.gen("SWAP");
            }
            TablaDeSimbolos.gen("DUP ; no perder el this");
            TablaDeSimbolos.gen("LOADREF 0 ; cargo la vt");
            TablaDeSimbolos.gen("LOADREF " + metodo.getOffset());
            TablaDeSimbolos.gen("CALL");
        }
        if (nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }
}
