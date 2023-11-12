package Tipo;

import AnalilzadorLexico.Token;
import TablaDeSimbolos.*;

public class TipoClase extends Tipo {

    public TipoClase(Token tokenClase) {
        super(tokenClase);
    }

    @Override
    public String getNombreTipo() {
        return tokenTipo.getLexema();
    }

    @Override
    public boolean esSubtipo(Tipo tipoDelAncestro) {
        return tipoDelAncestro.soySubtipo(this);
    }

    public boolean soySubtipo(TipoChar tipoChar) {
        return true;
    }

    public boolean soySubtipo(TipoNull tipoNull) {
        return true;
    }

    @Override
    public boolean mismoTipo(TipoClase tipo) {
        return tokenTipo.getLexema().equals(tipo.getTokenClase().getLexema());
    }

    @Override
    public boolean soySubtipo(TipoClase subtipo) {
        boolean valorDeVerdad = false;
        if (mismoTipo(subtipo))
            valorDeVerdad = true;
        else if (tokenTipo.getLexema().equals("Object"))
            valorDeVerdad = true;
        else {
            ClaseConcreta clase = TablaDeSimbolos.getClase(subtipo.getTokenClase().getLexema());
            if (clase != null) {
                Token token = clase.getHereda();
                if (token != null && !(token.getLexema().equals("Object")))
                    valorDeVerdad = soySubtipo(new TipoClase(token));

                if (!valorDeVerdad) {        //reviso interfaces
                    Token implementa = clase.getImplementa();
                    if (implementa != null && !valorDeVerdad)
                        valorDeVerdad = soySubtipo(new TipoClase(implementa));
                }
            }
            Interface interfac = TablaDeSimbolos.getInterface(subtipo.getTokenClase().getLexema());
            if (interfac != null && interfac.getExtiende() != null) {
                Interface interface1 = TablaDeSimbolos.getInterface(interfac.getExtiende().getLexema());
                if (!valorDeVerdad)
                    valorDeVerdad = soySubtipo(new TipoClase(interface1.getToken()));
            }
        }
        return valorDeVerdad;
    }

    private Token getTokenClase() {
        return tokenTipo;
    }
}