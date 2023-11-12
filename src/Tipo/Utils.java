package Tipo;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import Nodos.Expresion.NodoExpresion;


public class Utils {
    public static Tipo getTipo(Token token) {
        Tipo tipo = null;
        switch (token.getTipoDeToken()) {
            case pr_int:
                tipo = new TipoInt(token);
                break;
            case pr_boolean:
                tipo = new TipoBoolean(token);
                break;
            case pr_char:
                tipo = new TipoChar(token);
                break;
            case pr_void:
                tipo = new TipoVoid(token);
                break;
            case id_clase:
                if (token.getTipoDeToken() == TipoDeToken.stringLiteral)
                    tipo = new TipoString(token);
                else if (token.getLexema().equals("String"))
                    tipo = new TipoString(token);
                else if (token.getTipoDeToken() == TipoDeToken.id_clase)
                    tipo = new TipoClase(token);
                break;
            case stringLiteral:
                tipo = new TipoString(token);
                break;
        }
        return tipo;
    }
}