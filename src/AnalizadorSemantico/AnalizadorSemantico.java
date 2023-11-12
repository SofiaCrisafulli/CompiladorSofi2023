package AnalizadorSemantico;

import AnalilzadorLexico.AnalizadorLexico;
import AnalilzadorLexico.ExcepcionLexica;
import AnalilzadorLexico.Token;
import AnalizadorSintactico.AnalilzadorSintactico;
import AnalizadorSintactico.ExcepcionSintactica;
import TablaDeSimbolos.*;

import java.io.IOException;

public class AnalizadorSemantico {

    private AnalilzadorSintactico analilzadorSintactico;

    public AnalizadorSemantico(AnalizadorLexico ALex) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        analilzadorSintactico = new AnalilzadorSintactico(ALex);
    }

    public void start() throws ExcepcionSemantica, ExcepcionLexica, IOException, ExcepcionSintactica {
        analilzadorSintactico.inicial();
        TablaDeSimbolos.chequeoDeSentencias();
    }

    public static void addEOF(Token eof) {
        TablaDeSimbolos.getInstance().addEOF(eof);
    }

}
