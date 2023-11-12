package Tipo;


import AnalilzadorLexico.Token;

public class TipoBoolean extends Tipo {

    public TipoBoolean(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "boolean";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoBoolean tipoBoolean){
        return true;
    }

    public boolean soySubtipo(TipoNull tipoNull) {
        return false;
    }
}
