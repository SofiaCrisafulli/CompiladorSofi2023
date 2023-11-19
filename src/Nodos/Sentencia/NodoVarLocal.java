package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoVarLocal extends NodoSentencia {
    NodoExpresion nodoExpresion;
    Tipo tipo;
    int offset;

    public NodoVarLocal(Token token, NodoExpresion nodoExpresion) {
        tokenSentencia = token;
        this.nodoExpresion = nodoExpresion;
        offset = -1;
    }

    public NodoVarLocal(Token token, Tipo t) {
        tokenSentencia = token;
        tipo = t;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        tipo = nodoExpresion.chequear();

        if (tipo.mismoTipo(new TipoNull(tokenSentencia)))
            throw new ExcepcionSemantica(tokenSentencia, "la variable es nula");
        if(tipo.mismoTipo(new TipoVoid(tokenSentencia)))
            throw new ExcepcionSemantica(tokenSentencia, "el método es void, no es posible retornar un valor");
        NodoVarLocal nodoVarLocal = TablaDeSimbolos.getVarlocalAndParam(tokenSentencia.getLexema());
        if (nodoVarLocal == null) {
            TablaDeSimbolos.getNodoBloque().addVarLocal(this);
            nodoVarLocal = this;
        } else
            throw new ExcepcionSemantica(tokenSentencia, "la variable esta repetida");
        if (!nodoVarLocal.getTipo().esSubtipo(tipo))
            throw new ExcepcionSemantica(tokenSentencia, nodoVarLocal.getTipo().getNombreTipo() + " no es subtipo de " + tipo.getTokenTipo().getLexema());
    }

    @Override
    public void generar() {
        TablaDeSimbolos.gen("RMEM 1 ; guardo lugar para la variable local " + tokenSentencia.getLexema());
        if(nodoExpresion != null) {
            nodoExpresion.generar();
            TablaDeSimbolos.gen("STORE " + offset);
        }
    }

    public Tipo getTipo() throws ExcepcionSemantica {
        Tipo toRet = tipo;
        if (toRet == null) {
            tipo = nodoExpresion.chequear();
        }
        return toRet;
    }

    public void setTipo(Tipo t) {
        tipo = t;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}