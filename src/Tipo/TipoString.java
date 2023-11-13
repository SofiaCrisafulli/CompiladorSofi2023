package Tipo;

import AnalilzadorLexico.Token;

public class TipoString extends Tipo {


    public TipoString(Token token) {
        super(token);
    }

    @Override
    public String getNombreTipo() {
        return "String";
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoString tipoString){
        return true;
    }
    public boolean soySubtipo(TipoNull tipoNull) {
        return true;
    }
}