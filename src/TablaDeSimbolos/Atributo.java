package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.Tipo;

public class Atributo {

    private String nombre;
    private Tipo tipo;
    private Token token;

    private Clase clase;
    private int offset;
    private boolean esEstatico;

    public Atributo(Token n, Tipo t, Clase c, boolean es) {
        nombre = n.getLexema();
        tipo = t;
        this.token = n;
        clase = c;
        offset = -1;
        esEstatico = es;
    }

    public void estaBienDeclarado() throws ExcepcionSemantica {
        if (tipo.getTokenTipo().getTipoDeToken() == TipoDeToken.id_clase) {
            if ((TablaDeSimbolos.getInstance().getInterfaz(tipo.getNombreTipo()) == null) && TablaDeSimbolos.getInstance().getClase(tipo.getNombreTipo()) == null) {
                throw new ExcepcionSemantica(tipo.getTokenTipo(), "no existe el tipo " + tipo.getTokenTipo().getLexema() + " de atributo");
            }

        }
    }

    public Token getToken() {
        return token;
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Clase getMiClase() {
        return clase;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isEsEstatico() {
        return esEstatico;
    }
}