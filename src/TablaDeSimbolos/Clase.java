package TablaDeSimbolos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSintactico.ExcepcionSintactica;
import Tipo.Tipo;

import java.util.ArrayList;
import java.util.Map;

public abstract class Clase {

    protected Token tokenClase;
    protected Map<String, Metodo> metodos;
    protected ArrayList<Metodo> metodosDeclaradosEnClase;
    protected Map<String, Atributo> atributos;
    protected Metodo metodoActual;
    protected Metodo metodoChequeado;
    protected boolean hayHerenciaCircualar;

    protected Constructor constructor;

    protected Token tipo;

    public Token getToken() {
        return tokenClase;
    }

    public abstract void guardarAtributo(Atributo atributo) throws ExcepcionSemantica;

    public abstract void guardarMetodo(Metodo metodo) throws ExcepcionSemantica ;

    public abstract void estaBienDeclarada() throws ExcepcionSemantica, ExcepcionSintactica;


    public Map<String, Metodo> getMetodos() {
        return metodos;
    }

    public boolean addMetodo(Metodo m) {
        boolean esta;
        Metodo m2 = metodos.put(m.getTokenMetodo().getLexema(), m);
        esta = m2 == null;
        return esta;
    }

    public void addAtributo(Atributo a) throws ExcepcionSemantica {
        System.out.println("addAtributo");
        if(atributos.get(a.getNombre()) == null)
            atributos.put(a.getNombre(), a);
        else
            throw new ExcepcionSemantica(a.getToken(), "el atributo ya existe");
    }

    public Metodo getMetodoActual() {
        return metodoActual;
    }

    public abstract ArrayList<String> ancestros() throws ExcepcionSemantica;

    public void setMetodoActual(Metodo m) {
        metodoActual = m;
    }

    public void setConstructor(Constructor c) throws ExcepcionSemantica {
        if(constructor == null)
            constructor = c;
        else
            throw new ExcepcionSemantica(tokenClase, "hay mas de un constructor");
    }

    public Constructor getConstructor() {
        if(constructor == null) {
            constructor = new Constructor(tokenClase);
            constructor.setParametros(new ArrayList<Parametro>());
        }
        return constructor;
    }

    public Token getTipo() {
        return tipo;
    }

    public boolean esAtributo(String lexema) {
        boolean es = false;
        for(Atributo a : atributos.values())
            if(a.getToken().getLexema().equals(lexema))
                es = true;
        return es;
    }

    public Metodo getMetodo(String lexema) {
        return metodos.get(lexema);
    }

    public Atributo getAtributo(String lexema) {
        Atributo atributo = null;
        for(Atributo a: atributos.values())
            if(a.equals(lexema))
                atributo = a;
        return atributo;
    }

    public void chequear() throws ExcepcionSemantica {
        for(Metodo m : metodos.values())
            m.chequeoSentencias();
        constructor.chequeoSentencias();
    }

    public abstract void generar();


}