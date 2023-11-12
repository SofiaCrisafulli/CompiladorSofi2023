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

    public NodoBloque(Token token) {
        tokenSentencia = token;
        listaSentencias = new ArrayList<>();
        varLocales = new HashMap<>();
    }

    @Override
    public void chequear() throws ExcepcionSemantica {
        TablaDeSimbolos.getInstance().addBloqueIni(this);
        for (NodoSentencia nodoSentencia : listaSentencias) {
            nodoSentencia.chequear();
            if (nodoSentencia.esRetornante())
                esRetornante = true;
        }
        TablaDeSimbolos.getInstance().eliminarBloque();
    }

    @Override
    public void generar() {
        TablaDeSimbolos.addBloqueIni(this);
        for (NodoSentencia nodoSentencia : listaSentencias)
            nodoSentencia.generar();
        TablaDeSimbolos.removeBloque();
        TablaDeSimbolos.gen("FMEM " + varLocales.size() + " ; libero el espacio para las variables locles, se liberaron " + varLocales.size() + "espacios de memoria");
    }


    public boolean addVarLocal(NodoVarLocal nodoVarLocal) {
        boolean esta = false;
        NodoVarLocal nodo = varLocales.put(nodoVarLocal.tokenSentencia.getLexema(), nodoVarLocal);
        if (nodo != null)
            esta = true;
        return esta;
    }

    public NodoVarLocal getVarLocal(String lexema) {
        System.out.println("Cant variables locales: " + varLocales.size());
        return varLocales.get(lexema);
    }

    public void setListaSentencias(ArrayList<NodoSentencia> listaS) {
        listaSentencias = listaS;
    }

    public boolean getEsRetornante() {
        return esRetornante;
    }
}
