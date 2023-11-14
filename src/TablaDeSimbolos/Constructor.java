package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Sentencia.NodoBloque;

import java.util.ArrayList;

public class Constructor {
    private Token tokenConstructor;
    private ArrayList<Parametro> parametros;
    private NodoBloque bloque;

    public Constructor(Token token) {
        tokenConstructor = token;
        parametros = new ArrayList<>();
        bloque = new NodoBloque(new Token(TipoDeToken.simb_llave_que_abre, "{", 0));
    }

    public Token getTokenConstructor() {
        return tokenConstructor;
    }

    public void setTokenConstructor(Token tokenConstructor) {
        this.tokenConstructor = tokenConstructor;
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
    }

    public void chequeoSentencias() throws ExcepcionSemantica {
        bloque.chequear();
    }

    public void generar() {
        //TablaDeSimbolos.getClaseActual().setMetodoActual(this);
        TablaDeSimbolos.gen("LOADFP ; Apila el valor del registro fp");
        TablaDeSimbolos.gen("LOADSP ; Apila el valor del registro sp");
        TablaDeSimbolos.gen("STOREFP ; Almacena el tope de la pila en el registro fp");
        bloque.generar();
        TablaDeSimbolos.gen("STOREFP ;AF Almacena el tope de la pila en el registro fp");
        System.out.println("generar metodo");
        TablaDeSimbolos.gen("RET " + (parametros.size()) + 1);
    }
}