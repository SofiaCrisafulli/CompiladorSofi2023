package AnalizadorSemantico;

import AnalilzadorLexico.Token;

public class ExcepcionSemantica extends Exception {
    public ExcepcionSemantica(Token tokenActual, String mensaje) {
        super(mensajeCompleto(tokenActual,mensaje));
    }

    private static String mensajeCompleto(Token tokenActual, String mensaje){
        return "Error semántico en la línea " + tokenActual.getNroLinea() + ": " + mensaje + "\n\n[Error:" + tokenActual.getLexema() + "|" + tokenActual.getNroLinea() + "]";
    }
}
