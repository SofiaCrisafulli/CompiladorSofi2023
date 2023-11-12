package Nodos.Expresion.Operando.NodosAccesos;

import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import Tipo.Tipo;

public class NodoAccesoParentizada extends NodoAcceso {
    NodoExpresion nodoExpresion;

    public NodoAccesoParentizada(NodoExpresion ne) {
        nodoExpresion = ne;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        nodoExpresion.setEsAsignacion(esAsignacion);
        Tipo tipo = nodoExpresion.chequear();
        if(nodoEncadenado != null) {
            nodoEncadenado.setEsAsignacion(esAsignacion);
            tipo = nodoEncadenado.chequear(tipo, operador);
        }
        return tipo;
    }

    @Override
    public void generar() {
        nodoExpresion.generar();
        if(nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }

    @Override
    public boolean esAsignable() {
        boolean asignable = true;
        if(nodoEncadenado != null)
            asignable = nodoEncadenado.esAsignable();
        return asignable;
    }

    @Override
    public boolean esLlamada() {
        boolean es;
        if(nodoEncadenado != null)
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
