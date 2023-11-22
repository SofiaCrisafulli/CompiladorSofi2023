package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.*;
import Tipo.*;

public class NodoAccesoIdClase extends NodoAcceso {

    public NodoAccesoIdClase(Token token) {
        operador = token;
    }

    public Tipo chequear() throws ExcepcionSemantica {
        Tipo tipo = null;
        if (!esLlamada() && !esAsignable())
            throw new ExcepcionSemantica(operador, "no es posible realizar un acceso a una clase");
        if (esLlamada() || esAsignable()) {
            Clase clase = TablaDeSimbolos.getInstance().getClase(operador.getLexema());
            if (clase == null)
                throw new ExcepcionSemantica(operador, "la clase no es válida");
            if (!esLlamada())
                throw new ExcepcionSemantica(operador, "no es posible realizar una llamada a una clase");
                tipo = new TipoClase(operador);
            if (nodoEncadenado != null) {
                nodoEncadenado.setEsAsignacion(esAsignacion);
                tipo = nodoEncadenado.chequear(tipo, operador);
            }
        }
        return tipo;
    }

    @Override
    public void generar() {
        ClaseConcreta clase = TablaDeSimbolos.getInstance().getClase(operador.getLexema());
        String label = clase.labelVT();
        TablaDeSimbolos.gen("PUSH " + clase.labelVT());
        if(nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }


    public boolean esAsignable() {
        boolean es = false;
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
}