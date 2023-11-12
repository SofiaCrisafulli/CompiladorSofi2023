package Nodos.Expresion.Operando.NodosAccesos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.Clase;
import TablaDeSimbolos.*;
import Tipo.*;

import java.util.ArrayList;
import java.util.Iterator;

public class NodoAccesoNew extends NodoAcceso {

    ArrayList<NodoExpresion> parametros;

    public NodoAccesoNew(Token token) {
        operador = token;
        parametros = new ArrayList<>();
    }


    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        if (!esLlamada() && !esAsignable())
            throw new ExcepcionSemantica(operador, "no es posible realizar una llamada a un new ");
        Tipo tipo = null;
        if (esLlamada() || esAsignable()) {
            Clase clase = TablaDeSimbolos.getInstance().getClase(operador.getLexema());
            if (clase == null)
                throw new ExcepcionSemantica(operador, "la clase no existe");
           Constructor constructor = clase.getConstructor();
           chequerParametros(constructor);
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
        ClaseConcreta claseConcreta = TablaDeSimbolos.getClaseActual();
        TablaDeSimbolos.gen("RMEM 1 ; reservo para el malloc");
        TablaDeSimbolos.gen("PUSH " + claseConcreta.getAtributos().size() + 1  + "; apilo los atributos");
        TablaDeSimbolos.gen("PUSH simple_malloc ; rutina de heap");
        TablaDeSimbolos.gen("CALL");
        TablaDeSimbolos.gen("DUP ; no perder el nuevo CiR");
        TablaDeSimbolos.gen("PUSH " + claseConcreta.labelVT() + "; apilo el comienzo de la vt");
        TablaDeSimbolos.gen("STOREREF 0 ");

        if(nodoEncadenado != null) {
            nodoEncadenado.setEsLadoIzq(esLadoIzq);
            nodoEncadenado.generar();
        }
    }

    private void chequerParametros(Constructor constructor) throws ExcepcionSemantica {
        boolean fallo = false;
        ArrayList<Parametro> listaParametros = constructor.getParametros();
        if (listaParametros.size() == parametros.size()) {
            Iterator<Parametro> iterador = listaParametros.iterator();
            Iterator<NodoExpresion> nodoExpresionIterator = parametros.iterator();
            while (iterador.hasNext() && nodoExpresionIterator.hasNext() && !fallo) {
                Parametro p = iterador.next();
                NodoExpresion nodoExpresion = nodoExpresionIterator.next();
                fallo = !(nodoExpresion.chequear().esSubtipo(p.getTipo()));
            }
        } else
            fallo = true;
        if (fallo)
            throw new ExcepcionSemantica(operador, "error en los parámetros del constructor");
    }

    @Override
    public boolean esAsignable() {
        boolean es = false;
        if (nodoEncadenado != null)
            es = nodoEncadenado.esAsignable();
        return es;
    }

    @Override
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

    public void setParametros(ArrayList<NodoExpresion> parametros) {
        this.parametros = parametros;
    }
}