package Tipo;

import AnalilzadorLexico.Token;

public class TipoVoid extends Tipo {
    public TipoVoid(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "void";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoString tipoVoid){
        return true;
    }

    public boolean soySubtipo(TipoNull tipoNull) {
        return false;
    }
}
