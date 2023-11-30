package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Expresion.NodoExpresion;
import TablaDeSimbolos.TablaDeSimbolos;
import Tipo.*;

public class NodoWhile extends NodoSentencia {

    Token tokenWhile;
    NodoExpresion condicion;
    NodoSentencia sentencia;
    int numEtiquetaSentencia;
    int numEtiquetaFinWhile;

    public NodoWhile(Token token, NodoExpresion ne, NodoSentencia ns) {
        tokenWhile = token;
        condicion = ne;
        sentencia = ns;
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        Tipo tipo = condicion.chequear();
        if(tipo.mismoTipo(new TipoBoolean(tokenWhile))) {
            NodoBloque nodoBloque = new NodoBloque(tokenWhile);
            TablaDeSimbolos.addBloqueIni(nodoBloque);
            sentencia.chequear();
            TablaDeSimbolos.removeBloque();
        }
        else
            throw new ExcepcionSemantica(tokenWhile, "la condicion tiene que ser un boolean");
    }

    @Override
    public void generar() {
        String etiquetaThen = nuevaEtiquetaThen();
        String etiquetaFinWhile = nuevaEtiquetaFinWhile();
        TablaDeSimbolos.gen(etiquetaThen + ":");
        condicion.generar();
        TablaDeSimbolos.gen("BF " + etiquetaFinWhile);
        sentencia.generar();
        TablaDeSimbolos.gen("JUMP " + etiquetaThen);
        TablaDeSimbolos.gen(etiquetaFinWhile + ": NOP");

    }

    private String nuevaEtiquetaThen() {
        numEtiquetaSentencia = TablaDeSimbolos.getInstance().getNumeroEtiqueta();
        String nuevaEtiquetaThen = "labelThen" + numEtiquetaSentencia;
        return nuevaEtiquetaThen;
    }
    private String nuevaEtiquetaFinWhile() {
        numEtiquetaFinWhile = TablaDeSimbolos.getInstance().getNumeroEtiqueta();
        String nuevaEtiquetaFinWhile = "labelFin" + numEtiquetaFinWhile;
        return nuevaEtiquetaFinWhile;
    }


}
