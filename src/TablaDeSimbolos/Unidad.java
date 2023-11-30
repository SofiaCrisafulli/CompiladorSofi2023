package TablaDeSimbolos;

import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;

public interface Unidad {
    public Parametro getParametro(Token nombre);
}
