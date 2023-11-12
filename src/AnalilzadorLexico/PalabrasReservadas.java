package AnalilzadorLexico;

import java.util.HashMap;
import java.util.Map;

public class PalabrasReservadas {

    private Map<String, TipoDeToken> map = new HashMap<String, TipoDeToken>();

    public PalabrasReservadas() {
        map.put("class", TipoDeToken.pr_class);
        map.put("interface", TipoDeToken.pr_interface);
        map.put("extends", TipoDeToken.pr_extends);
        map.put("implements", TipoDeToken.pr_implements);
        map.put("public", TipoDeToken.pr_public);
        map.put("static", TipoDeToken.pr_static);
        map.put("void", TipoDeToken.pr_void);
        map.put("boolean", TipoDeToken.pr_boolean);
        map.put("char", TipoDeToken.pr_char);
        map.put("int", TipoDeToken.pr_int);
        map.put("if", TipoDeToken.pr_if);
        map.put("else", TipoDeToken.pr_else);
        map.put("while", TipoDeToken.pr_while);
        map.put("return", TipoDeToken.pr_return);
        map.put("var", TipoDeToken.pr_var);
        map.put("this", TipoDeToken.pr_this);
        map.put("new", TipoDeToken.pr_new);
        map.put("null", TipoDeToken.pr_null);
        map.put("true", TipoDeToken.pr_true);
        map.put("false", TipoDeToken.pr_false);
        map.put("float", TipoDeToken.pr_float);
        map.put("", TipoDeToken.exp_vacia);
    }

    public TipoDeToken getTipoDeToken(String token){
        return map.get(token);
    }
}
