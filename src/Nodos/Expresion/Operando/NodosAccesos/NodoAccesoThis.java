package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoAccesoThis extends NodoAcceso {
    Token thisClase;

    public NodoAccesoThis(Token token, Token tokenActual) {
        thisClase = token;
        operador = tokenActual;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        if (!esLlamada() && !esAsignable() && !esOperacionValida())
            throw new ExcepcionSemantica(operador, "no es posible realizar una llamada o asignarle a un this");
        Tipo tipo = null;
        if (esLlamada() || esAsignable()) {
            Clase clase = TablaDeSimbolos.getInstance().getClase(operador.getLexema());
            if (operador.getLexema().equals("this"))
                clase = TablaDeSimbolos.getInstance().getClaseActual();
            if (clase == null)
                throw new ExcepcionSemantica(operador, "la clase no existe");
            if(TablaDeSimbolos.getClaseActual().getMetodoActual().getMetodoEstatico())
                throw new ExcepcionSemantica(operador, "no es posible acceder a un this en un metodo estatico");
            tipo = new TipoClase(operador);
            if (nodoEncadenado != null) {
                nodoEncadenado.setEsAsignacion(esAsignacion);
                tipo = nodoEncadenado.chequear(tipo, thisClase);
            }
        } else
            throw new ExcepcionSemantica(operador, "no es posible hacer una asignacion o una llamada");
        return tipo;
    }

    @Override
    public void generar() {
        TablaDeSimbolos.gen("LOAD 3 ; apilo el this");
        if(nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }

    @Override
    public boolean esAsignable() {
        boolean es = true;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esAsignable();
        return es;
    }

    @Override
    public boolean esLlamada() {
        boolean es = false;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esLlamada();
        return es;
    }

    @Override
    public boolean esAsignacion() {
        return false;
    }

    private boolean esOperacionValida() {
        return false;
    }
}
