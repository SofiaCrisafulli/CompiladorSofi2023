package Tipo;

import AnalilzadorLexico.Token;

public class TipoChar extends Tipo {

    public TipoChar(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "char";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoChar tipoChar){
        return true;
    }

    public boolean soySubtipo(TipoNull tipoNull) {
        return false;
    }
}
