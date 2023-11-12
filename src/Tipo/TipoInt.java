package Tipo;

import AnalilzadorLexico.Token;

public class TipoInt extends Tipo {

    public TipoInt(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "int";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoInt tipoInt) {
        return true;
    }

    public boolean soySubtipo(TipoNull tipoNull) {
        return false;
    }

    public boolean esSubtipo(TipoClase tipo) {
        return false;
    }
}
