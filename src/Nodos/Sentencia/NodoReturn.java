package Nodos.Sentencia;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import Nodos.Expresion.NodoExpresionVacia;
import TablaDeSimbolos.*;
import Tipo.*;

public class NodoReturn extends NodoSentencia {
    Token tokenReturn;
    NodoExpresion retorno;
    int cantVarLocales;

    public NodoReturn(Token token, NodoExpresion ne) {
        tokenReturn = token;
        retorno = ne;
    }


    @Override
    public void chequear() throws ExcepcionSemantica {
        if (TablaDeSimbolos.getClaseActual().getMetodoActual() instanceof Constructor) {
            if(retorno != null)
                throw new ExcepcionSemantica(tokenReturn, "es un constructor, no deberia tener un retorno");
            //aca tenes que controlar que no tengas expresion en el nodo return;;;
        }
        else { //luego si es metodo dejas todo como tenias para metodo
            if (retorno != null)
                retorno.setEsAsignacion(true);
            Tipo tipoExpresion = retorno.chequear();
            Tipo tipoRetorno = null;

            tipoRetorno = ((Metodo) TablaDeSimbolos.getClaseActual().getMetodoActual()).getTipoRetorno();

            if (tipoRetorno.mismoTipo(new TipoVoid(tokenReturn)) && !(retorno instanceof NodoExpresionVacia))
                throw new ExcepcionSemantica(tokenReturn, "no es posible retornar algo ya que el método es void");
            if (retorno == null) {
                if (!tipoExpresion.mismoTipo(new TipoVoid(tokenReturn)) && tipoRetorno.mismoTipo(new TipoVoid(tokenReturn)) && !retorno.equals(new NodoExpresionVacia()))
                    throw new ExcepcionSemantica(tokenReturn, "se deberia retornar algo de tipo " + tipoRetorno.getNombreTipo());
            } else {
                if (tipoRetorno.mismoTipo(new TipoVoid(tokenReturn)) && !tipoExpresion.mismoTipo(Utils.getTipo(new Token(TipoDeToken.pr_void, "void", 0))))
                    throw new ExcepcionSemantica(tokenReturn, "solo se puede retornar una sentencia vacia ya que el metodo es void");
                if (!tipoExpresion.esSubtipo(tipoRetorno) && !tipoRetorno.mismoTipo(new TipoVoid(tokenReturn)))
                    throw new ExcepcionSemantica(tokenReturn, "el tipo de la expresión de retorno no es subtipo del retorno del metodo");
            }
        }
    }

    @Override
    public void generar() {
        TablaDeSimbolos.gen("FMEM " + cantVarLocales);
        Metodo m = (Metodo) TablaDeSimbolos.getClaseActual().getMetodoActual();
        if (m.getTipo().mismoTipo(new TipoVoid(tokenReturn))) {
            TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
            TablaDeSimbolos.gen("RET " + m.getOffsetLinea());
        } else {
            retorno.generar();
            TablaDeSimbolos.gen("STORE " + m.getOffsetRetorno());
            TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
            TablaDeSimbolos.gen("RET " + m.getOffsetLinea());
        }
        TablaDeSimbolos.gen("\n\n");
    }

    public boolean esRetornante() {
        return true;
    }
}