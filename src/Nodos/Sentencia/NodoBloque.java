package Nodos.Sentencia;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import TablaDeSimbolos.TablaDeSimbolos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NodoBloque extends NodoSentencia {

    ArrayList<NodoSentencia> listaSentencias;
    Map<String, NodoVarLocal> varLocales;
    int cantVarLocalBloque = 0;
    int offset;

    public NodoBloque(Token token) {
        tokenSentencia = token;
        listaSentencias = new ArrayList<>();
        varLocales = new HashMap<>();
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        establecerOffset();
        TablaDeSimbolos.getInstance().addBloqueIni(this);
        for (NodoSentencia nodoSentencia : listaSentencias) {
            nodoSentencia.chequear();
            if (nodoSentencia.esRetornante())
                esRetornante = true;
        }
        TablaDeSimbolos.getInstance().eliminarBloque();
    }

    private void establecerOffset() {
        if(TablaDeSimbolos.getInstance().getBloqueActual() == null)
            offset = 0;
        else
            offset = TablaDeSimbolos.getInstance().getBloqueActual().offset;
    }

    @Override
    public void generar() {
        TablaDeSimbolos.addBloqueIni(this);
        for (NodoSentencia nodoSentencia : listaSentencias)
            nodoSentencia.generar();
        TablaDeSimbolos.gen("FMEM " + cantVarLocalBloque);
        TablaDeSimbolos.removeBloque();
    }


    public boolean addVarLocal(NodoVarLocal nodoVarLocal) {
        boolean esta = false;
        NodoVarLocal nodo = varLocales.put(nodoVarLocal.tokenSentencia.getLexema(), nodoVarLocal);
        if (nodo != null)
            esta = true;
        nodoVarLocal.setOffset(offset);
        cantVarLocalBloque++;
        offset--;
        return esta;
    }

    public NodoVarLocal getVarLocal(String lexema) {
        return varLocales.get(lexema);
    }

    public void setListaSentencias(ArrayList<NodoSentencia> listaS) {
        listaSentencias = listaS;
    }

    public boolean getEsRetornante() {
        return esRetornante;
    }

    public Map<String, NodoVarLocal> getVarLocales() {
        return varLocales;
    }
}
