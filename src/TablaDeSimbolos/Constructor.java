package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Sentencia.NodoBloque;

import java.util.ArrayList;
import java.util.Iterator;

public class Constructor implements Unidad{
    private Token tokenConstructor;
    private ArrayList<Parametro> parametros;
    private NodoBloque bloque;

    public Constructor(Token token) {
        assert token != null;
        tokenConstructor = token;
        parametros = new ArrayList<>();
        bloque = new NodoBloque(new Token(TipoDeToken.simb_llave_que_abre, "{", 0));
    }

    public void estaBienDeclarado() throws ExcepcionSemantica {
        if (TablaDeSimbolos.getInstance().getClase(tokenConstructor.getLexema()) == null) {
            throw new ExcepcionSemantica(tokenConstructor, "No existe clase para el constructor " + tokenConstructor.getLexema());
        }
    }

    public void setParametros(ArrayList<Parametro> p) {
        parametros = p;
    }

    public ArrayList<Parametro> getParametros() {
        return parametros;
    }

    public void chequear() throws ExcepcionSemantica {
        if (bloque.getEsRetornante())
            throw new ExcepcionSemantica(tokenConstructor, "no es posible retornar un valor en un constructor");
        if (!tokenConstructor.getLexema().equals(TablaDeSimbolos.getClaseActual().getToken().getLexema()))
            throw new ExcepcionSemantica(tokenConstructor, "el construcutor debe ser del mismo nombre que la clase");
        //bloque.chequear();
    }

    public void chequeoSentencias() throws ExcepcionSemantica {
        TablaDeSimbolos.getClaseActual().setMetodoActual(this);
        bloque.chequear();
    }

    public void generar() {
        TablaDeSimbolos.gen("\n\n");
        String label = "lblConstructor@" + tokenConstructor.getLexema() + ":";
        TablaDeSimbolos.gen(label);
        TablaDeSimbolos.gen( "LOADFP ; Apila el valor del registro fp");
        TablaDeSimbolos.gen("LOADSP ; Apila el valor del registro sp");
        TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
        bloque.generar();
        TablaDeSimbolos.gen("FMEM " + bloque.getVarLocales().size());
        TablaDeSimbolos.gen("STOREFP ;AF Almacena el tope de la pila en el registro fp");
        TablaDeSimbolos.gen("RET " + (parametros.size() + 1));
    }

    private boolean chequearParametros(Constructor metodo) {
        boolean seCumple = true;
        Iterator<Parametro> iterator = metodo.getParametros().iterator();
        if (parametros.size() == metodo.getParametros().size()) {
            for (Parametro p : parametros) {
                if (iterator.hasNext()) {
                    if (!(iterator.next().getTipo().getNombreTipo().equals(p.getTipo().getNombreTipo())))
                        seCumple = false;
                }
            }
        } else
            seCumple = false;
        return seCumple;
    }

    public NodoBloque getBloque() {
        return bloque;
    }

    public void setBloque(NodoBloque bloque) {
        this.bloque = bloque;
    }

    @Override
    public Parametro getParametro(Token nombre) {
        Parametro parametro = null;
        for (Parametro p : parametros) {
            if (nombre.getLexema().equals(p.getTokenParametro().getLexema()))
                parametro = p;
        }
        return parametro;
    }
}