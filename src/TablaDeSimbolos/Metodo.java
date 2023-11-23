package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Sentencia.NodoBloque;
import Tipo.Tipo;

import java.util.ArrayList;
import java.util.Iterator;


public class Metodo {

    private Token tokenMetodo;
    private Tipo tipoRetorno;
    private boolean metodoEstatico;
    private ArrayList<Parametro> listaParametros;
    private Token claseAsociada;
    private Clase miClase;
    private NodoBloque bloque;
    private int offset;
    private int offsetParametros;


    public Metodo(Token tk, ArrayList<Parametro> listaP, Tipo tipoR, boolean metEs, Token claseA, Clase miClase) {
        tokenMetodo = tk;
        listaParametros = listaP;
        tipoRetorno = tipoR;
        metodoEstatico = metEs;
        claseAsociada = claseA;
        bloque = new NodoBloque(tokenMetodo);
        this.miClase = miClase;
        offset = -1;
        if (metodoEstatico)
            offsetParametros = 3;
        else
            offsetParametros = 4;
        for(Parametro p : listaParametros) {
            p.setOffset(offsetParametros);
            offsetParametros++;
        }
    }

    public void estaBienDeclarado() throws ExcepcionSemantica {
        for (Parametro p : listaParametros)
            p.checkExistencia();
        if (tipoRetorno.getTokenTipo().getTipoDeToken() == TipoDeToken.id_clase) {
            if ((TablaDeSimbolos.getInstance().getInterfaz(tipoRetorno.getTokenTipo().getLexema()) == null) && (TablaDeSimbolos.getInstance().getClase(tipoRetorno.getTokenTipo().getLexema()) == null))
                throw new ExcepcionSemantica(tipoRetorno.getTokenTipo(), tipoRetorno.getNombreTipo() + " no existe");
        }
    }

    public boolean verificarHerencia(Metodo metodoHeredado) {
        return metodoHeredado.getTipoRetorno().getNombreTipo().equals(tipoRetorno.getNombreTipo()) &&
                (metodoHeredado.getMetodoEstatico() == metodoEstatico) && (comprobarParametros(metodoHeredado.getListaParametros()));
    }

    private boolean comprobarParametros(ArrayList<Parametro> parametros) {
        boolean correcto = parametros.size() == listaParametros.size();
        Iterator<Parametro> iteradorH = parametros.iterator();
        Iterator<Parametro> iterador = listaParametros.iterator();
        while (iteradorH.hasNext() && iterador.hasNext() && correcto) {
            Parametro heredado = iteradorH.next();
            Parametro miParametro = iterador.next();
            correcto = heredado.comprobar(miParametro);
        }
        if (iterador.hasNext() || iteradorH.hasNext())
            correcto = false;
        return correcto;
    }

    public ArrayList<Parametro> getListaParametros() {
        return listaParametros;
    }

    public boolean getMetodoEstatico() {
        return metodoEstatico;
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public Token getTokenMetodo() {
        return tokenMetodo;
    }

    public boolean esIgualElMetodo(Metodo metodo) {
        boolean estaV1 = tokenMetodo.getLexema().equals(metodo.getTokenMetodo().getLexema());
        boolean estaV2 = compararParametros(metodo.getListaParametros());
        boolean estaV3 = metodoEstatico == metodo.metodoEstatico;

        return estaV1 && estaV2 && estaV3;
    }

    private boolean compararParametros(ArrayList<Parametro> parametros2) {
        boolean sonIguales = true;
        if (listaParametros.size() != parametros2.size())
            sonIguales = false;
        else
            for (int i = 0; i < parametros2.size(); i++) {
                if (listaParametros.get(i).equals(parametros2.get(i)))
                    sonIguales = true;
                if (!sonIguales)
                    break;
            }
        return sonIguales;
    }

    public boolean chequearMetodos(Token padre) throws ExcepcionSemantica {
        boolean esIgual = true;
        ClaseConcreta clasePadre = TablaDeSimbolos.getInstance().clases.get(padre.getLexema());
        Metodo metodoPadre = clasePadre.getMetodos().get(tokenMetodo.getLexema());
        if (metodoPadre != null) {
            esIgual = true;

            if (!metodoPadre.tipoRetorno.getNombreTipo().equals(tipoRetorno.getNombreTipo())) {
                esIgual = false;
            }

            if (!chequearParametros(metodoPadre)) {
                esIgual = false;
            }

        }
        if (esIgual == false)
            throw new ExcepcionSemantica(tokenMetodo, "El método no coincide con el método de la clase padre");
        return esIgual;
    }

    private boolean chequearParametros(Metodo metodo) {
        boolean seCumple = true;
        Iterator<Parametro> iterator = metodo.getListaParametros().iterator();
        if (listaParametros.size() == metodo.getListaParametros().size()) {
            for (Parametro p : listaParametros) {
                if (iterator.hasNext()) {
                    if (!(iterator.next().getTipo().getNombreTipo().equals(p.getTipo().getNombreTipo())))
                        seCumple = false;
                }
            }
        } else
            seCumple = false;
        return seCumple;
    }


    public void setMetodoEstatico(boolean metodoEstatico) {
        this.metodoEstatico = metodoEstatico;
    }


    public Parametro getParametro(Token nombre) {
        Parametro parametro = null;
        for (Parametro p : listaParametros) {
            if (nombre.getLexema().equals(p.getTokenParametro().getLexema()))
                parametro = p;
        }
        return parametro;
    }

    public Clase getMiClase() {
        return miClase;
    }

    public Tipo getTipo() {
        return tipoRetorno;
    }

    public void setBloque(NodoBloque nuevoBloque) {
        bloque = nuevoBloque;
    }

    public void chequeoSentencias() throws ExcepcionSemantica {
        Clase clase = TablaDeSimbolos.getInstance().getClaseActual();
        if (miClase == clase)
            bloque.chequear();
    }

    public int getOffsetLinea() {
        if (metodoEstatico)
            return listaParametros.size();
        else
            return listaParametros.size() + 1;
    }

    public int getOffsetRetorno() {
        if (metodoEstatico)
            return listaParametros.size() + 3;
        else
            return listaParametros.size() + 4;
    }

    public void generar() {
        TablaDeSimbolos.getClaseActual().setMetodoActual(this);
        TablaDeSimbolos.gen("LOADFP ; Apila el valor del registro fp");
        TablaDeSimbolos.gen("LOADSP ; Apila el valor del registro sp");
        TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
        bloque.generar();
        //TablaDeSimbolos.gen("FMEM ");
        TablaDeSimbolos.gen("STOREFP ;AF Almacena el tope de la pila en el registro fp");
        if (metodoEstatico)
            TablaDeSimbolos.gen("RET " + listaParametros.size());
        else
            TablaDeSimbolos.gen("RET " + (listaParametros.size() + 1));
    }

    public String etiquetaMetodo() {
        String etiqueta = tokenMetodo.getLexema() + "$";
        for (Parametro p : listaParametros)
            etiqueta = etiqueta + p.getTipo().getTokenTipo().getLexema() + "$";
        if (etiqueta.contains("$"))
            etiqueta = etiqueta.substring(0, etiqueta.length() - 1);
        etiqueta = etiqueta + "@" + miClase.getToken().getLexema() + " ; Apila el método ";
        return etiqueta;
    }

    public int getOffset() {
        return offset;
    }

    public String stringLabel() {
        String labels = tokenMetodo.getLexema() + "$";
        for (Parametro p : listaParametros)
            labels = labels + p.getTipo().getTokenTipo().getLexema() + "$";
        if (labels.contains("$"))
            labels = labels.substring(0, labels.length() - 1);
        labels = labels + "@" + miClase.getToken().getLexema();
        return labels;
    }

    public void setListaParametros(ArrayList<Parametro> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public void setOffset(int offsetVT) {
        offset = offsetVT;
    }

}