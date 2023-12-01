package TablaDeSimbolos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interface extends Clase {
    private Token tokenInterface;
    private Token extiende;
    private boolean estaConsolidada;
    private boolean noTieneExtensionCircular;

    public Interface(Token token) {
        tokenInterface = token;
        metodos = new HashMap<>();
        estaConsolidada = false;
        noTieneExtensionCircular = false;
    }

    public Token getToken() {
        return tokenInterface;
    }


    public void estaBienDeclarada() throws ExcepcionSemantica {
        if (extiende != null)
            chequearImplements();

        for (Metodo metodo : metodos.values()) {
            if (metodo.getMetodoEstatico())
                throw new ExcepcionSemantica(metodo.getTokenMetodo(), " los metodos en las interfaces no pueden ser estaticos, el metodo " + metodo.getTokenMetodo().getLexema() + " lo es");
            metodo.estaBienDeclarado();
        }
    }

    private void chequearImplements() throws ExcepcionSemantica {
        if (!TablaDeSimbolos.getInstance().existeInterfaz(extiende.getLexema()))
            throw new ExcepcionSemantica(extiende, "la interfaz " + extiende.getLexema() + " no existe");
    }

    public void consolidar() throws ExcepcionSemantica {
        if (TablaDeSimbolos.getInstance().existeInterfaz(tokenInterface.getLexema())) {
            if (!estaConsolidada && extiende != null) {
                chequearExtensionCircular(new ArrayList<>());
                Interface interfaceExt = TablaDeSimbolos.getInterfaces().get(extiende.getLexema());
                if (!interfaceExt.estaConsolidada)
                    interfaceExt.consolidar();
                for (Metodo metExt : interfaceExt.metodos.values()) {
                    if (metodos.get(metExt.getTokenMetodo().getLexema()) == null)
                        metodos.put(metExt.getTokenMetodo().getLexema(), metExt);
                    else if (!metodos.get(metExt.getTokenMetodo().getLexema()).esIgualElMetodo(metExt))
                        throw new ExcepcionSemantica(metodos.get(metExt.getTokenMetodo().getLexema()).getTokenMetodo(), "el método " + metExt.getTokenMetodo().getLexema() + " fue declarado con distinta signatura en " + tokenInterface.getLexema());
                }
                estaConsolidada = true;
            }
        }
    }

    private void chequearExtensionCircular(ArrayList<Token> interfacesAVerificar) throws ExcepcionSemantica {
        if (!noTieneExtensionCircular && extiende != null) {
            if (interfacesAVerificar.contains(extiende))
                throw new ExcepcionSemantica(extiende, "la interface " + tokenInterface.getLexema() + " tiene extensión circular");
            else {
                interfacesAVerificar.add(extiende);
                Interface claseAncestro = TablaDeSimbolos.getInterfaces().get(extiende.getLexema());
                claseAncestro.chequearExtensionCircular(interfacesAVerificar);
            }
            noTieneExtensionCircular = true;
        }
    }

    public Map<String, Metodo> getMetodos() {
        return metodos;
    }


    public boolean addMetodo(Metodo m) {
        boolean esta;
        Metodo m2 = metodos.put(m.getTokenMetodo().getLexema(), m);
        esta = m2 == null;
        return esta;
    }


    public void setExtiende(Token e) {
        extiende = e;
    }

    public ArrayList<String> ancestros() throws ExcepcionSemantica {
        ArrayList<String> ancestros = new ArrayList<>();
        ancestros.add(tokenInterface.getLexema());

        ancestros.addAll(TablaDeSimbolos.getInstance().getInterfaz(extiende.getLexema()).ancestros());
        return ancestros;
    }


    public void generar() {

    }

    public Token getExtiende() {
        return extiende;
    }
}
