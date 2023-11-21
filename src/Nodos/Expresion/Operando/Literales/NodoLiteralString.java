package Nodos.Expresion.Operando.Literales;

import Tipo.*;
import TablaDeSimbolos.*;

public class NodoLiteralString extends NodoLiteral {

    @Override
    public Tipo chequear() {
        return new TipoString(operador);
    }

    public void generar() {

        String string = operador.getLexema();
        string = string.substring(1, string.length() - 1);

        TablaDeSimbolos.gen("RMEM 1 ; puntero al String en .HEAP");
        TablaDeSimbolos.gen("PUSH " + (string.length() + 1));
        TablaDeSimbolos.gen("PUSH simple_malloc");
        TablaDeSimbolos.gen("CALL");
        for(int i = 0; i < string.length(); i++){
            TablaDeSimbolos.gen("DUP ; Duplico la referencia del comienzo del String ya que STOREREF la consume");
            TablaDeSimbolos.gen("PUSH " + '\''+string.charAt(i)+'\'' +" ");
            TablaDeSimbolos.gen("STOREREF "+ i +" ; Guardo la letra apilada");
        }
        TablaDeSimbolos.gen("DUP ; Duplico la referencia del comienzo del String ya que STOREREF la consume");
        TablaDeSimbolos.gen("PUSH " + 0 + " ; Apilo terminador");
        TablaDeSimbolos.gen("STOREREF " + string.length());
    }

    @Override
    public boolean esAsignable() {
        return false;
    }

    @Override
    public boolean esLlamada() {
        return false;
    }

    @Override
    public boolean esAsignacion() {
        return false;
    }
}
