package Nodos.Expresion;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.Operando.NodosAccesos.NodoAcceso;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.Tipo;

public class NodoExpresionAsignacion extends NodoExpresion {

    Token operador;
    NodoExpresionCompuesta ladoIzq;
    NodoExpresion ladoDer;

    public NodoExpresionAsignacion(Token token, NodoExpresionCompuesta li) {
        operador = token;
        ladoIzq = li;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemantica {
        ladoDer.setEsAsignacion(true);
        ladoIzq.setEsAsignacion(true);
        Tipo tipoDer = ladoDer.chequear();
        Tipo tipoIzq = ladoIzq.chequear();
        if (tipoIzq == null || tipoDer == null)
            throw new ExcepcionSemantica(operador, "asignación inválida");
        if (!esAsignable())
            throw new ExcepcionSemantica(operador, "el tipo izquierdo no es asignable");
        if (!(tipoDer.esSubtipo(tipoIzq)))
            throw new ExcepcionSemantica(operador, "el lado derecho tiene que ser subtipo del lado izquierdo");
        return tipoIzq;
    }

    @Override
    public void generar() {
        ladoDer.generar();
        TablaDeSimbolos.gen("DUP");
        ladoIzq.generar();
    }


    @Override
    public boolean esAsignable() {
        if (ladoIzq.esAsignable())
            return true;
        else
            return false;
    }

    @Override
    public boolean esLlamada() {
        return false;
    }

    @Override
    public boolean esAsignacion() {
        return true;
    }

    public void setLadoIzq(NodoExpresionCompuesta ladoIzq) {
        this.ladoIzq = ladoIzq;
    }

    public void setLadoDer(NodoExpresion ladoDer) {
        this.ladoDer = ladoDer;
    }

}