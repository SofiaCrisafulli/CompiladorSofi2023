package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import Tipo.*;


public class Parametro {
    private Token tokenParametro;
    private Tipo tipo;
    private int offset;


    public Parametro(Token token, Tipo t) {
        tokenParametro = token;
        tipo = t;
        offset = -1;
    }

    public boolean comprobar(Parametro p) {
        return tipo.getNombreTipo().equals(p.getTipo().getNombreTipo());
    }

    public void checkExistencia() throws ExcepcionSemantica {
        if(tipo.getTokenTipo().getTipoDeToken() == TipoDeToken.id_clase) {
            if((TablaDeSimbolos.getInstance().getInterfaz(tipo.getNombreTipo()) == null) && TablaDeSimbolos.getInstance().getClase(tipo.getNombreTipo()) == null) {
                throw new ExcepcionSemantica(tipo.getTokenTipo(),"no existe la clase ");
            }
        }
    }

    public Token getTokenParametro() {
        return tokenParametro;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTokenParametro(Token tokenParametro) {
        this.tokenParametro = tokenParametro;
    }

    public int getOffset() {
        return offset;
    }


    /*public void addParametro(Parametro p) throws ExcepcionSemantica {
        for(Parametro p1: parametros)
            if(p1.getTokenParametro().equals(p.getTokenParametro()))
                throw new ExcepcionSemantica(p.getTokenParametro(), "Ya existe un parámetro con ese nombre");
        parametros.add(p);
    }*/
}