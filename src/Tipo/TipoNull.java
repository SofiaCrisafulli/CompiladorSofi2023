package Tipo;

import AnalilzadorLexico.Token;

public class TipoNull extends Tipo {
    public TipoNull(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "null";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoClase subtipo){
        return true;
    }

    public boolean soySubtipo(TipoString tipoString){
        return true;
    }

    @Override
    public boolean soySubtipo(TipoNull subtipo) {
        return true;
    }
}
