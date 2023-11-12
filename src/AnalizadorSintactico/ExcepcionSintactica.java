package AnalizadorSintactico;

import AnalilzadorLexico.Token;

public class ExcepcionSintactica extends Exception{

    public ExcepcionSintactica(Token tokenActual, String token) {
        super(mensajeCompleto(token, tokenActual));
    }

    private static String mensajeCompleto(String token, Token tokenActual){
        return "Error sintactico en linea " + tokenActual.getNroLinea() +": se esperaba " + token + " y se encontro " + tokenActual.getLexema() + " \n \n[Error:"+tokenActual.getLexema() + "|"+tokenActual.getNroLinea()+"]";
    }
}
