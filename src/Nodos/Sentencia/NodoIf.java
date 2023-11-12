package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;
public class NodoIf extends NodoSentencia {
    Token tokenIf;
    NodoExpresion condicion;
    NodoSentencia sentenciaThen;
    NodoSentencia sentenciaElse;
    String numEtiquetaThen;
    String numEtiquetaElse;

    public NodoIf(Token token, NodoExpresion ne, NodoSentencia ns, NodoSentencia nodoElse) {
        tokenIf = token;
        condicion = ne;
        sentenciaThen = ns;
        sentenciaElse = nodoElse;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        Tipo tipo = condicion.chequear();
        if(tipo.mismoTipo(new TipoBoolean(tokenIf))) {
            NodoBloque nodoThen = new NodoBloque(tokenIf);
            sentenciaThen.chequear();
            if (sentenciaElse != null)
                sentenciaElse.chequear();
        }
        else
            throw new ExcepcionSemantica(tokenIf, "se esperaba un tipo boolean");
    }

    @Override
    public void generar() {
        condicion.generar();
        String etiquetaThen = nuevaEtiquetaThen();
        String etiquetaElse = nuevaEtiquetaElse();

        if(sentenciaElse == null) {
            TablaDeSimbolos.gen("BF " + etiquetaThen + " ; salta afuera del if si la condicion es falsa");
            sentenciaThen.generar();
            TablaDeSimbolos.gen(etiquetaElse + ": NOP");
        }
        else {
            TablaDeSimbolos.gen("BF " + etiquetaElse + " ; salta a la condicion else");
            sentenciaThen.generar();
            TablaDeSimbolos.gen("JUMP " + etiquetaThen + " ; salteo al else");
            TablaDeSimbolos.gen(etiquetaElse + ":");
            sentenciaElse.generar();
            TablaDeSimbolos.gen(etiquetaThen + ": NOP");
        }
    }

    private String nuevaEtiquetaThen() {
        String nuevaEtiqueta = "labelThen" + numEtiquetaThen;
        numEtiquetaThen = TablaDeSimbolos.getInstance().getNumeroEtiqueta();
        return nuevaEtiqueta;
    }

    private String nuevaEtiquetaElse() {
        String nuevaEtiqueta = "labelElse" + numEtiquetaElse;
        numEtiquetaElse = TablaDeSimbolos.getInstance().getNumeroEtiqueta();
        return nuevaEtiqueta;
    }
}
