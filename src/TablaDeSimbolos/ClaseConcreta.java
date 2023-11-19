package TablaDeSimbolos;


import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSintactico.ExcepcionSintactica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaseConcreta extends Clase {

    private Token tokenClase;
    private Map<String, Parametro> parametros;
    private Token implementa;
    private Token hereda;
    private boolean estaConsolidada;
    private boolean hayHerenciaCircular;
    private Constructor constructorClase;
    private int offsetCiR;
    private int offsetVT;
    private HashMap<Integer, Atributo> atributoOffset;
    private HashMap<Integer, Metodo> metodosOffset;

    public ClaseConcreta(Token idClase) {
        tokenClase = idClase;
        atributos = new HashMap<String, Atributo>();
        metodos = new HashMap<String, Metodo>();
        parametros = new HashMap<String, Parametro>();
        atributos = new HashMap<>();
        hereda = new Token(TipoDeToken.id_clase, tokenClase.getLexema(), tokenClase.getNroLinea());
        constructorClase = new Constructor(idClase);
        hayHerenciaCircular = false;
        atributoOffset = new HashMap<Integer, Atributo>();
        metodosOffset = new HashMap<>();
    }

    public Token getToken() {
        return tokenClase;
    }

    public void setToken(Token tokenClase) {
        this.tokenClase = tokenClase;
    }

    public Token getHereda() {
        return hereda;
    }


    public ArrayList<String> ancestros() {
        ArrayList<String> ancestros = new ArrayList<>();
        ancestros.add(tokenClase.getLexema());

        ancestros.add(String.valueOf(implementa));
        if (hereda != null) {
            ancestros.add(hereda.getLexema());
            if (hereda.getLexema().equals("Object"))
                return ancestros;
            else {
                ancestros.addAll(TablaDeSimbolos.getInstance().getClase(hereda.getLexema()).ancestros());
                return ancestros;
            }
        }
        return ancestros;
    }

    public Map<String, Metodo> getMetodos() {
        return metodos;
    }

    public void estaBienDeclarada() throws ExcepcionSemantica {
        if (TablaDeSimbolos.getInstance().clases.containsValue(tokenClase.getLexema()))
            throw new ExcepcionSemantica(tokenClase, "La clase ya existe");
        if (!tokenClase.getLexema().equals("Object")) {
            if (TablaDeSimbolos.getInstance().existeClase(hereda.getLexema())) {
                for (Metodo metodo : metodos.values()) {
                    metodo.estaBienDeclarado();
                    metodo.chequearMetodos(hereda);
                }
                for (Atributo atributo : atributos.values())
                    atributo.estaBienDeclarado();
                constructorClase.estaBienDeclarado();
            } else
                throw new ExcepcionSemantica(hereda, "La clase " + tokenClase.getLexema() + " intenta heredar de una clase no declarada -->" + hereda.getLexema());
        }
    }


    private void chequearExtends() throws ExcepcionSemantica {
        for (ClaseConcreta c : TablaDeSimbolos.getInstance().clases.values())
            if (TablaDeSimbolos.getInstance().getClase(hereda.getLexema()) == null)
                throw new ExcepcionSemantica(hereda, "la clase " + hereda.getLexema() + " que " + tokenClase.getLexema() + " debe extender no existe");
    }

    private void chequearImplements() throws ExcepcionSemantica {
        if (!TablaDeSimbolos.getInstance().existeInterfaz(implementa.getLexema())) {
            throw new ExcepcionSemantica(implementa, "la interfaz " + implementa.getLexema() + " que " + tokenClase.getLexema() + " debe implementar no existe");
        } else {
            for (Metodo m : TablaDeSimbolos.getInstance().getInterfaz(implementa.getLexema()).getMetodos().values()) {
                Metodo metodo = metodos.get(m.getTokenMetodo().getLexema());
                if (metodo == null)
                    throw new ExcepcionSemantica(m.getTokenMetodo(), "el método de la interfaz implementada no existe");
                else {
                    if (!metodo.verificarHerencia(m))
                        throw new ExcepcionSemantica(metodo.getTokenMetodo(), "el método sobrecargado es distinto");
                }
            }
        }
    }

    private void verificarHerenciaCircular(ArrayList<ClaseConcreta> clasesPadre) throws ExcepcionSintactica {
        if (!hayHerenciaCircular) {
            if (clasesPadre.contains(this)) {
                hayHerenciaCircular = true;
                throw new ExcepcionSintactica(hereda, "la clase " + this.getToken().getLexema() + " posee herencia circular");
            } else {
                clasesPadre.add(this);
                ClaseConcreta claseConcreta = TablaDeSimbolos.getInstance().getClase(hereda.getLexema());
                if (claseConcreta != null) {
                    claseConcreta.verificarHerenciaCircular(clasesPadre);
                }
            }
        }
        clasesPadre.remove(this);
    }

    public boolean addMetodo(Metodo m) {
        boolean esta = false;
        Metodo metodo = metodos.put(m.getTokenMetodo().getLexema(), m);
        if (metodo == null)
            esta = true;
        return esta;
    }


    public void consolidarMetodos() throws ExcepcionSemantica, ExcepcionSintactica {
        verificarHerenciaCircular(new ArrayList<ClaseConcreta>());
        if (hayHerenciaCircualar)
            throw new ExcepcionSemantica(hereda, "Hay herencia circular");
        if (tokenClase.getTipoDeToken() == TipoDeToken.pr_implements)
            verificarMetodosDeInterfaces();
        ClaseConcreta clasePadre = TablaDeSimbolos.getInstance().getClase(hereda.getLexema());
        if (!clasePadre.estaConsolidada) {
            clasePadre.consolidarAtributos();
            clasePadre.consolidarMetodos();
            clasePadre.consolidarImplements();
            clasePadre.setConsolidar(true);
        }

        for (Metodo metodoHeredado : clasePadre.metodos.values()) {
            if (metodos.get(metodoHeredado.getTokenMetodo().getLexema()) == null) {
                addMetodo(metodoHeredado);
            } else {
                if (!metodoHeredado.verificarHerencia(metodos.get(metodoHeredado.getTokenMetodo().getLexema())))
                    throw new ExcepcionSemantica(metodos.get(metodoHeredado.getTokenMetodo().getLexema()).getTokenMetodo(), "El metodo " + metodoHeredado.getTokenMetodo().getLexema() + " no coincide con el metodo de tu padre");
                else {
                    Metodo metodo = metodos.get(metodoHeredado.getTokenMetodo().getLexema());
                    metodo.setOffset(metodoActual.getOffset());
                }
            }
        }
        int offsetClasePadre = clasePadre.getOffsetVt();
        offsetVT = offsetClasePadre;
        setMetodosOffset();


        //offsetCiR = TablaDeSimbolos.getInstance().getClase(hereda.getLexema()).getOffsetCiR();
        //offsetVT = TablaDeSimbolos.getInstance().getClase(hereda.getLexema()).getOffsetVt();
        //setMetodosOffset();
        //setAtributoOffset(clasePadre);
    }


    public void consolidarAtributos() throws ExcepcionSemantica {
        ClaseConcreta clasePadre = TablaDeSimbolos.getInstance().getClase(hereda.getLexema());
        for (Atributo atributoHeredado : clasePadre.atributos.values()) {
            if (atributos.get(atributoHeredado.getNombre()) == null)
                atributos.put(atributoHeredado.getNombre(), atributoHeredado);
            else
                throw new ExcepcionSemantica(atributos.get(atributoHeredado.getNombre()).getToken(), "Ya existe un atributo con ese nombre");
        }
        genOffsetAtributos();
    }


    public void setHayHerenciaCircular(boolean b) {
        hayHerenciaCircular = b;
    }

    private void verificarMetodosDeInterfaces() throws ExcepcionSemantica {
        for (Metodo m : metodos.values())
            if (metodos.get(m.getTokenMetodo().getLexema()) != null)
                if (!m.verificarHerencia(metodos.get(m.getTokenMetodo().getLexema())))
                    throw new ExcepcionSemantica(tokenClase, "Hay herencia circular");
                else
                    throw new ExcepcionSemantica(tokenClase, "El método" + metodos.get(m.getTokenMetodo().getLexema() + "no esta implementado"));
    }

    public void setHereda(Token nombre) {
        if (!hayHerenciaCircular)
            hereda = nombre;
    }

    public void setImplementa(Token i) {
        implementa = i;
    }

    public void setConsolidar(boolean consolidada) {
        estaConsolidada = consolidada;
    }

    public boolean getEstaConsolidada() {
        return estaConsolidada;
    }

    public Token getImplementa() {
        return implementa;
    }

    public void consolidarImplements() throws ExcepcionSemantica {
        if (implementa != null)
            chequearImplements();
        chequearExtends();
    }

    public Map<String, Atributo> getAtributos() {
        return atributos;
    }

    public void chequeoDeSentencias() throws ExcepcionSemantica {
        for (Metodo m : metodos.values()) {
            metodoActual = m;
            m.chequeoSentencias();
        }
        constructorClase.chequeoSentencias();
    }

    public void chequear() throws ExcepcionSemantica {
        if (constructorClase == null) {
            constructorClase = new Constructor(tokenClase);
            constructorClase.setParametros(new ArrayList<Parametro>());
        }
        constructorClase.chequear();
    }

    private void genOffsetAtributos() {
        offsetCiR = TablaDeSimbolos.getClase(hereda.getLexema()).getOffsetCiR();
        for (Atributo atributo : atributos.values()) {
            if (atributo.getOffset() == -1 && !atributo.isEsEstatico()) {
                atributo.setOffset(offsetCiR);
                offsetCiR++;
            }

            atributoOffset.put(atributo.getOffset(), atributo);
        }
    }

    private int getOffsetCiR() {
        return offsetCiR;
    }

    @Override
    public void generar() {
        TablaDeSimbolos.gen("\n\n");
        TablaDeSimbolos.gen(".DATA");
        String labels;
        if (metodosOffset.size() > 0) {
            labels = "VT_" + tokenClase.getLexema() + ": DW ";
            for (int i = 0; i != metodosOffset.size(); i++) {
                Metodo metodo = metodosOffset.get(i);
                labels = labels + metodo.stringLabel() + ",";
            }
            labels = labels.substring(0, labels.length() - 1);
        } else
            labels = "VT_" + tokenClase.getLexema() + ": NOP";
        TablaDeSimbolos.gen(labels + "\n");
        TablaDeSimbolos.gen(".CODE");
        labels = "";
        for (Metodo m : metodos.values()) {
            if (m.getMiClase() == this) {
                labels = m.stringLabel() + ":";
                TablaDeSimbolos.gen(labels);
                metodoActual = m;
                m.generar();
            }
        }
        if (constructor != null)
            constructor.generar();
    }


    public void setMetodosOffset() {
        //offsetVT = TablaDeSimbolos.getClase(hereda.getLexema()).getOffsetVt();
        for (Metodo m : metodos.values()) {
            if (!m.getMetodoEstatico()) {
                if (m.getOffset() == -1) {
                    m.setOffset(offsetVT);
                    offsetVT++;
                }
                System.out.println("Offset metodo: " + m.getOffset());
                System.out.println("Metodo: " + m.getTokenMetodo().getLexema());
                metodosOffset.put(m.getOffset(), m);
            }
        }
    }

    public void setAtributoOffset(ClaseConcreta claseConcreta) {
        offsetCiR = claseConcreta.offsetCiR;
        for (Atributo a : atributos.values()) {
            if (a.getOffset() == -1) {
                a.setOffset(offsetCiR);
                offsetCiR++;
            }
            atributoOffset.put(a.getOffset(), a);
        }
    }

    public void setOffsetCiR(int offset) {
        offsetCiR = offset;
    }

    public void setConstructorClase(Constructor constructorClase) {
        this.constructorClase = constructorClase;
    }

    public String labelVT() {
        return "VT_" + tokenClase.getLexema();
    }

    private int getOffsetVt() {
        return offsetVT;
    }
}