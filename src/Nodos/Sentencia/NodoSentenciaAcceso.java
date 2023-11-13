package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresionCompuesta;
import Nodos.Expresion.Operando.NodosAccesos.NodoAcceso;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoSentenciaAcceso extends NodoSentencia {
    NodoAcceso nodoAcceso;
    Tipo tipo;

    public NodoSentenciaAcceso(Token tk, NodoAcceso na) {
        tokenSentencia = tk;
        nodoAcceso = na;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        tipo = nodoAcceso.chequear();
        if(!nodoAcceso.esLlamada())
            throw new ExcepcionSemantica(tokenSentencia, "no es una llamada");
    }

    @Override
    public void generar() {
        nodoAcceso.generar();
        if(!tipo.mismoTipo(new TipoVoid(tokenSentencia)))
        TablaDeSimbolos.gen("POP");
    }

    public void setNodoAcceso(NodoAcceso nodoAcceso) {
        this.nodoAcceso = nodoAcceso;
    }
}
