package Tipo;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.Clase;

public abstract class Tipo {

    Token tokenTipo;

    public Tipo(Token token) {
        tokenTipo = token;
    }
    public abstract String getNombreTipo();

    public abstract boolean esSubtipo(Tipo tipoDelAncestro);

    public boolean mismoTipo(Tipo tipo){
        return getNombreTipo().equals(tipo.getNombreTipo());
    }
    public boolean mismoTipo(TipoClase tipo){
        return false;
    }

    public boolean soySubtipo(TipoClase subtipo){
        return false;
    }

    public boolean soySubtipo(TipoBoolean subtipo){
        return false;
    }

    public boolean soySubtipo(TipoChar subtipo){
        return false;
    }

    public boolean soySubtipo(TipoInt subtipo){
        return false;
    }

    public boolean soySubtipo(TipoVoid subtipo){
        return false;
    }

    public boolean soySubtipo(TipoNull subtipo){
        return false;
    }

    public boolean soySubtipo(TipoString tipoString){
        return false;
    }


    public Token getTokenTipo() {
        return tokenTipo;
    }
}