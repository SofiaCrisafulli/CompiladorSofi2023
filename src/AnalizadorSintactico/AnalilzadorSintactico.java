package AnalizadorSintactico;

import AnalilzadorLexico.AnalizadorLexico;
import AnalilzadorLexico.ExcepcionLexica;
import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.AnalizadorSemantico;
import AnalizadorSemantico.ExcepcionSemantica;
import Nodos.Encadenado.NodoEncadenado;
import Nodos.Encadenado.NodoLlamadaEncadenada;
import Nodos.Encadenado.NodoVarEncadenada;
import Nodos.Expresion.*;
import Nodos.Expresion.ExpresionesBianarias.*;
import Nodos.Expresion.Operando.Literales.*;
import Nodos.Expresion.Operando.NodoOperando;
import Nodos.Expresion.Operando.NodosAccesos.*;
import Nodos.Sentencia.*;
import TablaDeSimbolos.*;
import Tipo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static AnalilzadorLexico.TipoDeToken.*;

public class AnalilzadorSintactico {
    AnalizadorLexico analizadorLexico;
    Token tokenActual;

    public AnalilzadorSintactico(AnalizadorLexico ALex) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        analizadorLexico = ALex;
        tokenActual = analizadorLexico.proximoToken();
    }

    public void inicial() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        TablaDeSimbolos.getInstance().limpiar();
        listaClases();
        Token eof = tokenActual;
        TablaDeSimbolos.getInstance().addEOF(eof);
        match("eof", EOF);
        AnalizadorSemantico.addEOF(eof);
        TablaDeSimbolos.getInstance().estaBienDeclarado();
        TablaDeSimbolos.getInstance().consolidar();
    }

    private void listaClases() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_class || tokenActual.getTipoDeToken() == TipoDeToken.pr_interface) {
            clase();
            listaClases();
        }
    }

    private void clase() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_class)
            claseConcreta();
        else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_interface)
            metInterface();
        else
            throw new ExcepcionSintactica(tokenActual, " clase | interface");
    }

    private void claseConcreta() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("class", TipoDeToken.pr_class);
        Token nombre = tokenActual;
        match("id_clase", TipoDeToken.id_clase);
        ClaseConcreta c = new ClaseConcreta(nombre);
        TablaDeSimbolos.claseActual = c;
        Token nomAncestro = herenciaOpcional();
        if (nomAncestro == null)
            TablaDeSimbolos.getInstance().claseActual.setHereda(new Token(Object, Object.name(), tokenActual.getNroLinea()));
        match("{", TipoDeToken.simb_llave_que_abre);
        listaMiembros();
        match("}", TipoDeToken.simb_llave_que_cierra);
        c.getConstructor();
        TablaDeSimbolos.getInstance().insertarClase(nombre, TablaDeSimbolos.claseActual);
    }

    private Token heredaDe() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token nombre = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_extends) {
            match("extends", TipoDeToken.pr_extends);
            nombre = tokenActual;
            TablaDeSimbolos.getInstance().claseActual.setHereda(new Token(nombre.getTipoDeToken(), nombre.getLexema(), nombre.getNroLinea()));
            match("id_class", TipoDeToken.id_clase);
            TablaDeSimbolos.getInstance().claseActual.setHereda(nombre);
        } else
            throw new ExcepcionSintactica(tokenActual, "extends");
        return nombre;
    }

    private void metInterface() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("interface", TipoDeToken.pr_interface);
        Interface i = new Interface(tokenActual);
        TablaDeSimbolos.getInstance().setInterfaceActual(i);
        match("id_clase", TipoDeToken.id_clase);
        Token token = extiendeOpcional();
        i.setExtiende(token);
        match("{", TipoDeToken.simb_llave_que_abre);
        listaEncabezadosInterfaz();
        match("}", TipoDeToken.simb_llave_que_cierra);
        TablaDeSimbolos.getInstance().insertarInterfaz(i.getToken(), i);
    }

    private void listaEncabezadosInterfaz() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_static || isTipo()) {
            encabezadoInterfaz();
            listaEncabezadosInterfaz();
        }
    }

    private Token extiendeOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_extends) {
            match("extends", TipoDeToken.pr_extends);
            Token nombre = tokenActual;
            match("id_class", TipoDeToken.id_clase);
            return nombre;
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.simb_llave_que_abre)
            return null;
        else
            throw new ExcepcionSintactica(tokenActual, "extends");
    }

    private Token herenciaOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Token nombre = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_extends)
            nombre = heredaDe();
        else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_implements)
            implementaA();
        return nombre;
    }

    private void implementaA() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token nombre = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_implements) {
            match("implements", TipoDeToken.pr_implements);
            nombre = tokenActual;
            TablaDeSimbolos.getInstance().claseActual.setImplementa(new Token(nombre.getTipoDeToken(), nombre.getLexema(), nombre.getNroLinea()));
            match("id_class", TipoDeToken.id_clase);
            TablaDeSimbolos.getInstance().claseActual.setImplementa(nombre);
        } else
            throw new ExcepcionSintactica(tokenActual, "implements");
    }

    private void listaMiembros() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_public || tokenActual.getTipoDeToken() == TipoDeToken.pr_static || isTipo()) {
            miembro();
            listaMiembros();
        }
    }

    private void miembro() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_static)
            atributoOMetodo();
        else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_public)
            constructor();
        else
            atributoOMetodo();
    }

    private void atributoOMetodo() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        boolean esEstatico = estaticoOpcional();
        Tipo tipo = tipoMiembro();
        Token nombre = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        atributoOMetodoFactorizada(tipo, nombre, esEstatico);
    }

    private void atributoOMetodoFactorizada(Tipo tipo, Token token, boolean es) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_parentesis_que_abre) {
            ArrayList<Parametro> parametros = argsFormales();
            Metodo m = new Metodo(token, parametros, tipo, es, new Token(token.getTipoDeToken(), token.getLexema(), token.getNroLinea()), TablaDeSimbolos.getClaseActual());
            TablaDeSimbolos.getInstance().getClaseActual().setMetodoActual(m);
            NodoBloque bloque = bloque();
            ((Metodo)TablaDeSimbolos.getInstance().getClaseActual().getMetodoActual()).setBloque(bloque);
            if (!TablaDeSimbolos.getInstance().getClaseActual().addMetodo(m))
                throw new ExcepcionSemantica(token, "el método esta repetido");
        } else {
            Atributo a = new Atributo(token, tipo, TablaDeSimbolos.getClaseActual(), es);
            match(";", TipoDeToken.simb_punto_y_coma);
            TablaDeSimbolos.getInstance().getClaseActual().addAtributo(a);
        }
    }

    private void constructor() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("public", TipoDeToken.pr_public);
        Token nombre = tokenActual;
        match("id_clase", TipoDeToken.id_clase);
        Constructor constructor = new Constructor(nombre);
        TablaDeSimbolos.getInstance().getClaseActual().setConstructor(constructor);
        ArrayList<Parametro> p = argsFormales();
        TablaDeSimbolos.getInstance().getClaseActual().getConstructor().setParametros(p);
        constructor.setBloque(bloque());
    }

    private void encabezadoMetodo() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Tipo tipo1 = tipoMiembro();
        Token nombre = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        ArrayList<Parametro> p = argsFormales();
        match(";", TipoDeToken.simb_punto_y_coma);
    }

    public void encabezadoInterfaz() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Tipo tipo1 = tipoMiembro();
        Token nombre = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        ArrayList<Parametro> p = argsFormales();
        Metodo m = new Metodo(nombre, p, tipo1, false, new Token(tokenActual.getTipoDeToken(), tokenActual.getLexema(), tokenActual.getNroLinea()), TablaDeSimbolos.getClaseActual());
        match(";", TipoDeToken.simb_punto_y_coma);
        if (!TablaDeSimbolos.getInstance().getInterfazActual().addMetodo(m))
            throw new ExcepcionSemantica(nombre, "el metodo esta repetido");
    }

    private Tipo tipoMiembro() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token token = tokenActual;
        Tipo tipo = Utils.getTipo(token);
        if (isTipo() && tokenActual.getTipoDeToken() != TipoDeToken.pr_void)
            tipo();
        else
            match("void", TipoDeToken.pr_void);
        return tipo;
    }

    private boolean estaticoOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_static) {
            match("static", TipoDeToken.pr_static);
            return true;
        }
        return false;
    }

    private ArrayList<Parametro> argsFormales() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("(", TipoDeToken.simb_parentesis_que_abre);
        ArrayList<Parametro> parametros = listaArgsFormalesOpcional();
        match(")", TipoDeToken.simb_parentesis_que_cierra);
        return parametros;
    }

    private ArrayList<Parametro> listaArgsFormalesOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_boolean || tokenActual.getTipoDeToken() == TipoDeToken.pr_char || tokenActual.getTipoDeToken() == TipoDeToken.pr_int || tokenActual.getTipoDeToken() == TipoDeToken.id_clase || tokenActual.getTipoDeToken() == TipoDeToken.pr_float)
            return listaArgsFormales(new ArrayList<Parametro>());
        else {
            return new ArrayList<Parametro>();
        }
    }

    private Token tipo() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token tipo;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_boolean || tokenActual.getTipoDeToken() == TipoDeToken.pr_char || tokenActual.getTipoDeToken() == TipoDeToken.pr_int || tokenActual.getTipoDeToken() == TipoDeToken.pr_float)
            tipo = tipoPrimitivo();
        else {
            tipo = tokenActual;
            match("id_clase", TipoDeToken.id_clase);
        }
        return tipo;
    }

    private Token tipoPrimitivo() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token tipo = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_boolean) {
            tipo = tokenActual;
            match("boolean", TipoDeToken.pr_boolean);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_char) {
            tipo = tokenActual;
            match("char", TipoDeToken.pr_char);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_int) {
            tipo = tokenActual;
            match("int", TipoDeToken.pr_int);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_float)
            match("float", TipoDeToken.pr_float);
        return tipo;
    }

    private ArrayList<Parametro> listaArgsFormales(ArrayList<Parametro> parametros) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Parametro p = argFormal();
        for (Parametro p1 : parametros) {
            if (p1.getTokenParametro().getLexema().equals(p.getTokenParametro().getLexema()))
                throw new ExcepcionSemantica(p1.getTokenParametro(), "Ya existe otro parámetro con el mismo nombre");
        }
        p.setOffset(parametros.size());
        parametros.add(p);
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_coma) {
            match(",", TipoDeToken.simb_coma);
            if (isTipo() && tokenActual.getTipoDeToken() != TipoDeToken.pr_void)
                listaArgsFormalesFactorizada(parametros);
            else
                throw new ExcepcionSintactica(tokenActual, "boolean | char | int | id_clase");
        } else if (tokenActual.getTipoDeToken() != TipoDeToken.simb_parentesis_que_cierra) {
            throw new ExcepcionSintactica(tokenActual, tokenActual.getLexema());
        }
        return parametros;
    }

    private void listaArgsFormalesFactorizada(ArrayList<Parametro> p) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (isTipo())
            listaArgsFormales(p);
    }

    private boolean isTipo() {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_char || tokenActual.getTipoDeToken() == TipoDeToken.pr_boolean
                || tokenActual.getTipoDeToken() == TipoDeToken.pr_int || tokenActual.getTipoDeToken() == TipoDeToken.id_clase
                || tokenActual.getTipoDeToken() == TipoDeToken.pr_void || tokenActual.getTipoDeToken() == TipoDeToken.pr_float;
    }

    private Parametro argFormal() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        Token tipo = tipo();
        Tipo tipo1 = Utils.getTipo(tipo);
        Parametro parametro = new Parametro(tokenActual, tipo1);
        match("id_met_var", TipoDeToken.id_met_var);
        return parametro;
    }

    private NodoBloque bloque() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoBloque nodoBloque = new NodoBloque(tokenActual);
        match("{", TipoDeToken.simb_llave_que_abre);
        ArrayList<NodoSentencia> listaS = new ArrayList<>();
        sentenciasBloque(listaS);
        match("}", TipoDeToken.simb_llave_que_cierra);
        nodoBloque.setListaSentencias(listaS);
        return nodoBloque;
    }

    private ArrayList<NodoSentencia> sentenciasBloque(ArrayList<NodoSentencia> listaS) throws ExcepcionLexica, ExcepcionSemantica, IOException, ExcepcionSintactica {
        if(isSentencia()) {
            NodoSentencia ns = sentencia();
            listaS.add(ns);
            return sentenciasBloque(listaS);
        }
        else if(tokenActual.getTipoDeToken() == simb_llave_que_cierra)
            return listaS;
        else
            throw new ExcepcionSintactica(tokenActual, "token invalido");
    }

    private boolean isSentencia() {
        return tokenActual.getTipoDeToken() == TipoDeToken.simb_punto_y_coma || tokenActual.getTipoDeToken() == TipoDeToken.pr_var
                || tokenActual.getTipoDeToken() == TipoDeToken.pr_return || tokenActual.getTipoDeToken() == TipoDeToken.pr_if
                || tokenActual.getTipoDeToken() == TipoDeToken.pr_while || tokenActual.getTipoDeToken() == TipoDeToken.simb_llave_que_abre
                || primerosPrimario();
    }


    private NodoSentencia listaSentencias(ArrayList<NodoSentencia> lista) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoSentencia ns = null;
        if (primerosSentencia(tokenActual)) {
            ns = sentencia();
            lista.add(ns);
            listaSentencias(lista);
        }
        return ns;
    }

    private boolean primerosSentencia(Token token) {
        return tokenActual.getTipoDeToken() == TipoDeToken.simb_punto_y_coma || primerosOperadorUnario() || isLiteral() ||
                primerosPrimario() || tokenActual.getTipoDeToken() == TipoDeToken.pr_var || primerosReturn(tokenActual) || primerosReturn(tokenActual)
                || primerosIf(tokenActual) || primerosWhile(tokenActual) || primerosBloque(tokenActual);
    }

    private NodoSentencia sentencia() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoSentencia nodoSentencia = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_punto_y_coma) {
            nodoSentencia = new NodoSentenciaVacia();
            match(";", TipoDeToken.simb_punto_y_coma);
        } else if (primerosOperadorUnario() || isLiteral() || primerosPrimario()) {
            nodoSentencia = asignacionOLlamada();
            match(";", TipoDeToken.simb_punto_y_coma);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_var) {
            nodoSentencia = varLocal();
            match(";", TipoDeToken.simb_punto_y_coma);
        } else if (primerosReturn(tokenActual)) {
            nodoSentencia = metReturn();
            match(";", TipoDeToken.simb_punto_y_coma);
        } else if (primerosIf(tokenActual)) {
            nodoSentencia = metIf();
        } else if (primerosWhile(tokenActual)) {
            nodoSentencia = metWhile();
        } else if (primerosBloque(tokenActual)) {
            nodoSentencia = bloque();
        } else if (primerosPrimario()) {
            NodoAcceso nodoAcceso = acceso();
            NodoLlamadaOAsignacion asignacion = asignacionOLlamada();
            if (asignacion == null)
                nodoSentencia = new NodoSentenciaAcceso(tokenActual, nodoAcceso);
        } else
            throw new ExcepcionSintactica(tokenActual, "; | + | - | ! | null | true | false |  initLiteral | charLiteral | " +
                    "stringLiteral | return | if | while |  {");
        return nodoSentencia;
    }

    private boolean primerosOperadorUnario() {
        return tokenActual.getTipoDeToken() == TipoDeToken.op_mas || tokenActual.getTipoDeToken() == TipoDeToken.op_menos ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_negacion;
    }

    private boolean primerosPrimario() {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_this || tokenActual.getTipoDeToken() == TipoDeToken.id_met_var
                || tokenActual.getTipoDeToken() == TipoDeToken.pr_new ||
                tokenActual.getTipoDeToken() == TipoDeToken.simb_parentesis_que_abre ||
                tokenActual.getTipoDeToken() == TipoDeToken.id_clase;
    }

    private boolean primerosReturn(Token token) {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_return;
    }

    private boolean primerosIf(Token token) {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_if;
    }

    private boolean primerosWhile(Token token) {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_while;
    }

    private boolean primerosBloque(Token tokenActual) {
        return tokenActual.getTipoDeToken() == TipoDeToken.simb_llave_que_abre;
    }

    private NodoLlamadaOAsignacion asignacionOLlamada() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresion ne = expresion();
        NodoLlamadaOAsignacion nodoSentenciaLlamadaAsignacion = new NodoLlamadaOAsignacion(tokenActual, ne);
        return nodoSentenciaLlamadaAsignacion;
    }

    private NodoVarLocal varLocal() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("var", TipoDeToken.pr_var);
        Token token = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        Token token2;
        match("=", TipoDeToken.op_asignacion);
        NodoExpresion ne = expresionCompuesta();
        return new NodoVarLocal(token, ne);
    }

    private NodoExpresion expresion() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresionCompuesta ne = expresionCompuesta();
        Token token = tokenActual;
        NodoExpresionAsignacion nea = expresionFactorizada(ne);
        if (nea != null) {
            nea.setLadoIzq(ne);
            nea.setOperador(token);
            return nea;
        } else {
            NodoExpresionSentencia nodoExpresionSentencia = new NodoExpresionSentencia(tokenActual, ne);
            nodoExpresionSentencia.setOperador(token);
            return nodoExpresionSentencia;
        }
    }

    private NodoExpresionAsignacion expresionFactorizada(NodoExpresionCompuesta nodoExpresionCompuesta) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresionAsignacion nodoExpresionAsignacion = null;
        NodoExpresion nodoExpresion = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.op_asignacion) {
            nodoExpresionAsignacion = new NodoExpresionAsignacion(tokenActual, nodoExpresionCompuesta);
            match("=", TipoDeToken.op_asignacion);
            nodoExpresion = expresion();
            nodoExpresionAsignacion.setLadoDer(nodoExpresion);
        }
        return nodoExpresionAsignacion;
    }

    private NodoExpresionCompuesta expresionCompuesta() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresionCompuesta nodoExpresionCompuesta;
        NodoExpresionCompuesta nodoExpresionUnaria = expresionBasica();
        nodoExpresionCompuesta = nodoExpresionUnaria;
        NodoExpresionBinaria nodoExpresionBinaria = expresionCompuestaPrima(nodoExpresionUnaria);
        if (nodoExpresionBinaria != null)
            nodoExpresionCompuesta = nodoExpresionBinaria;
        return nodoExpresionCompuesta;
    }

    private NodoExpresionBinaria expresionCompuestaPrima(NodoExpresion nodoExpresion) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresionBinaria nodoExpresionBinaria = null;
        if (primerosOperadorBinario()) {
            nodoExpresionBinaria = operadorBinario();
            NodoExpresion ne = expresionBasica();
            NodoExpresionBinaria nodoExpresionBinariaAux = expresionCompuestaPrima(ne);

            if (nodoExpresionBinariaAux == null)
                nodoExpresionBinaria.setExpresiones(nodoExpresion, ne);
            else
                nodoExpresionBinaria.setExpresiones(nodoExpresion, nodoExpresionBinariaAux);
        }

        return nodoExpresionBinaria;
    }

    private boolean primerosOperadorBinario() {
        return tokenActual.getTipoDeToken() == TipoDeToken.op_or || tokenActual.getTipoDeToken() == TipoDeToken.op_and ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_igual || tokenActual.getTipoDeToken() == TipoDeToken.op_distinto ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_menor || tokenActual.getTipoDeToken() == TipoDeToken.op_mayor ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_menor_igual || tokenActual.getTipoDeToken() == TipoDeToken.op_mayor_igual ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_mas || tokenActual.getTipoDeToken() == TipoDeToken.op_menos ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_multiplicacion || tokenActual.getTipoDeToken() == TipoDeToken.op_division ||
                tokenActual.getTipoDeToken() == TipoDeToken.op_modulo;
    }

    private NodoWhile metWhile() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("while", TipoDeToken.pr_while);
        match("(", TipoDeToken.simb_parentesis_que_abre);
        Token token = tokenActual;
        NodoExpresion ne = expresion();
        match(")", TipoDeToken.simb_parentesis_que_cierra);
        NodoSentencia ns = sentencia();
        return new NodoWhile(token, ne, ns);
    }

    private NodoExpresionBinaria operadorBinario() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Token operador = tokenActual;
        NodoExpresionBinaria nodoExpresionBinaria;
        if (tokenActual.getTipoDeToken() == TipoDeToken.op_or) {
            match("||", TipoDeToken.op_or);
            nodoExpresionBinaria = new NodoEBOr(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_and) {
            match("&&", TipoDeToken.op_and);
            nodoExpresionBinaria = new NodoEBAnd(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_igual) {
            match("==", TipoDeToken.op_igual);
            nodoExpresionBinaria = new NodoEBIgualdad(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_distinto) {
            match("!=", TipoDeToken.op_distinto);
            nodoExpresionBinaria = new NodoEBDistinto(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_menor) {
            match("<", TipoDeToken.op_menor);
            nodoExpresionBinaria = new NodoEBMenor(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_mayor) {
            match(">", TipoDeToken.op_mayor);
            nodoExpresionBinaria = new NodoEBMayor(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_menor_igual) {
            match("<=", TipoDeToken.op_menor_igual);
            nodoExpresionBinaria = new NodoEBMenorIgual(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_mayor_igual) {
            match(">=", TipoDeToken.op_mayor_igual);
            nodoExpresionBinaria = new NodoEBMayorIgual(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_mas) {
            match("+", TipoDeToken.op_mas);
            nodoExpresionBinaria = new NodoEBMas(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_menos) {
            match("-", TipoDeToken.op_menos);
            nodoExpresionBinaria = new NodoEBMenos(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_multiplicacion) {
            match("*", TipoDeToken.op_multiplicacion);
            nodoExpresionBinaria = new NodoEBMultiplicacion(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_division) {
            match("/", TipoDeToken.op_division);
            nodoExpresionBinaria = new NodoEBDivision(operador);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_modulo) {
            match("%", TipoDeToken.op_modulo);
            nodoExpresionBinaria = new NodoEBModulo(operador);
        } else
            throw new ExcepcionSintactica(tokenActual, "|| | && | == | != | < | > | <= | >= | + | - | * | / | %");
        return nodoExpresionBinaria;
    }

    private Token operadorUnario() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Token token;
        if (tokenActual.getTipoDeToken() == TipoDeToken.op_mas) {
            token = tokenActual;
            match("+", TipoDeToken.op_mas);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_menos) {
            token = tokenActual;
            match("-", TipoDeToken.op_menos);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.op_negacion) {
            token = tokenActual;
            match("!", TipoDeToken.op_negacion);
        } else
            throw new ExcepcionSintactica(tokenActual, "+ | - | !");
        return token;
    }

    private NodoLiteral literal() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        NodoLiteral literal = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_null) {
            match("null", TipoDeToken.pr_null);
            literal = new NodoLiteralNull();
            literal.setOperador(tokenActual);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_true) {
            match("true", TipoDeToken.pr_true);
            literal = new NodoLiteralTrue(tokenActual);
            literal.setOperador(tokenActual);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_false) {
            match("false", TipoDeToken.pr_false);
            literal = new NodoLiteralFalse(tokenActual);
            literal.setOperador(tokenActual);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.num_int) {
            Token token = tokenActual;
            match("initLiteral", TipoDeToken.num_int);
            literal = new NodoLiteralInt(token);
            literal.setOperador(token);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.car_char) {
            Token token = tokenActual;
            match("charLiteral", TipoDeToken.car_char);
            literal = new NodoLiteralChar();
            literal.setOperador(token);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.stringLiteral) {
            Token token = tokenActual;
            match("stringLiteral", TipoDeToken.stringLiteral);
            literal = new NodoLiteralString();
            literal.setOperador(token);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_float)
            match("float", TipoDeToken.pr_float);
        return literal;
    }

    private NodoAcceso expresionParentizada() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoAcceso ne;
        match("(", TipoDeToken.simb_parentesis_que_abre);
        ne = new NodoAccesoParentizada(expresion());
        match(")", TipoDeToken.simb_parentesis_que_cierra);
        return ne;
    }


    private ArrayList<NodoExpresion> argsActuales() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("(", TipoDeToken.simb_parentesis_que_abre);
        ArrayList<NodoExpresion> listaParametros = new ArrayList<>();
        listaExpsOpcional(listaParametros);
        match(")", TipoDeToken.simb_parentesis_que_cierra);
        return listaParametros;
    }

    private ArrayList<NodoExpresion> listaExpsOpcional(ArrayList<NodoExpresion> listaParametros) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (primerosOperadorUnario() || isLiteral() || primerosPrimario())
            listaExps(listaParametros);
        return listaParametros;
    }

    private void listaExps(ArrayList<NodoExpresion> listaParametros) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        listaParametros.add(expresion());
        listaExpsFactorizada(listaParametros);
    }

    private void listaExpsFactorizada(ArrayList<NodoExpresion> listaParametros) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == simb_coma) {
            match(",", simb_coma);
            listaExps(listaParametros);
        }
    }

    private NodoSentencia metIf() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Token tokenIf = tokenActual;
        match("if", TipoDeToken.pr_if);
        match("(", TipoDeToken.simb_parentesis_que_abre);
        NodoExpresion ne = expresion();
        match(")", TipoDeToken.simb_parentesis_que_cierra);
        NodoSentencia ns = sentencia();
        NodoSentencia sentenciaElse = metIfFactorizada();
        return new NodoIf(tokenIf, ne, ns, sentenciaElse);
    }

    private NodoSentencia metIfFactorizada() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoSentencia ns = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_else) {
            match("else", TipoDeToken.pr_else);
            ns = sentencia();
        }
        return ns;
    }

    private void listaEncabezados() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_static || isTipo()) {
            encabezadoMetodo();
            listaEncabezados();
        }
    }

    private NodoOperando operando() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoOperando op;
        if (isLiteral()) {
            op = literal();
        } else if (primerosPrimario()) {
            op = acceso();
        } else
            throw new ExcepcionSintactica(tokenActual, "null | true | false | initLiteral | charLiteral | " +
                    "stringLiteral | this | idMetVar | new | ( | idClase");
        return op;
    }

    private NodoExpresionCompuesta expresionBasica() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoOperando ne;
        NodoExpresionCompuesta nodoExpresionUnaria;
        if (primerosOperadorUnario()) {
            Token token = operadorUnario();
            ne = operando();
            nodoExpresionUnaria = new NodoExpresionUnaria(token, ne);
        } else if (isLiteral() || primerosPrimario()) {
            ne = operando();
            nodoExpresionUnaria = ne;
        } else
            throw new ExcepcionSintactica(tokenActual, "null | true | false | initLiteral | charLiteral | " +
                    "stringLiteral | this | idMetVar | new | ( | idClase");
        return nodoExpresionUnaria;
    }

    private NodoAcceso acceso() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoAcceso toRet = primario();
        NodoEncadenado nodoEncadenado = encadenadoOpcional();
        toRet.setNodoEncadenado(nodoEncadenado);
        return toRet;
    }

    private NodoAcceso primario() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoAcceso primario = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.pr_this) {
            primario = new NodoAccesoThis(TablaDeSimbolos.getClaseActual().getToken(), tokenActual);
            accThis();
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.pr_new) {
            primario = accConstructor();
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.id_clase) {
            primario = new NodoAccesoIdClase(tokenActual);
            NodoEncadenado ne = accMetEstatico();
            primario.setNodoEncadenado(ne);
        } else if (tokenActual.getTipoDeToken() == TipoDeToken.simb_parentesis_que_abre)
            primario = expresionParentizada();
        else if (tokenActual.getTipoDeToken() == TipoDeToken.id_met_var) {
            Token tokenAux = tokenActual;
            primario = accVarOMetodo(tokenAux);
        } else
            throw new ExcepcionSintactica(tokenActual, "this | new | idClase | ( | id_met_var");
        return primario;
    }

    private void accThis() throws ExcepcionLexica, IOException, ExcepcionSintactica {
        match("this", TipoDeToken.pr_this);
    }

    private NodoAccesoNew accConstructor() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoAccesoNew constructor = new NodoAccesoNew(tokenActual);
        match("new", TipoDeToken.pr_new);
        Token tokenConstructor = tokenActual;
        match("id_clase", TipoDeToken.id_clase);
        ArrayList<NodoExpresion> parametros = argsActuales();
        constructor.setOperador(tokenConstructor);
        constructor.setParametros(parametros);
        return constructor;
    }

    private NodoEncadenado accMetEstatico() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match("id_clase", TipoDeToken.id_clase);
        match(".", TipoDeToken.simb_punto);
        Token token = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        ArrayList<NodoExpresion> p = argsActuales();
        NodoEncadenado nodoEncadenado = new NodoLlamadaEncadenada(token, p);
        return nodoEncadenado;
    }

    private NodoAcceso accVarOMetodo(Token tokenVar) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoAcceso nodoAcceso = null;
        match("id_met_var", TipoDeToken.id_met_var);
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_parentesis_que_abre) {
            NodoAccesoMetodo nodoAccesoMetodo = new NodoAccesoMetodo(tokenVar);
            ArrayList<NodoExpresion> args = argsActuales();
            nodoAccesoMetodo.setListaParametros(args);
            nodoAcceso = nodoAccesoMetodo;
        } else
            nodoAcceso = new NodoAccesoVariable(tokenVar);
        return nodoAcceso;
    }


    private NodoEncadenado encadenadoOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoEncadenado nodoEncadenado = null;
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_punto)
            nodoEncadenado = varOMetodoEncadenado();
        return nodoEncadenado;
    }

    private NodoEncadenado varOMetodoEncadenado() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        match(".", TipoDeToken.simb_punto);
        Token token = tokenActual;
        match("id_met_var", TipoDeToken.id_met_var);
        NodoEncadenado nodoEncadenado = varOMetodoEncadenadoFactorizado(token);
        return nodoEncadenado;
    }

    private NodoEncadenado varOMetodoEncadenadoFactorizado(Token token) throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        if (tokenActual.getTipoDeToken() == TipoDeToken.simb_parentesis_que_abre) {
            ArrayList<NodoExpresion> lista = argsActuales();
            NodoEncadenado nodoEncadenado = encadenadoOpcional();
            return new NodoLlamadaEncadenada(token, lista, nodoEncadenado);
        } else {
            NodoEncadenado nodoEncadenado = encadenadoOpcional();
            return new NodoVarEncadenada(token, nodoEncadenado);
        }
    }

    private boolean isLiteral() {
        return tokenActual.getTipoDeToken() == TipoDeToken.pr_null ||
                tokenActual.getTipoDeToken() == TipoDeToken.pr_true ||
                tokenActual.getTipoDeToken() == TipoDeToken.pr_false ||
                tokenActual.getTipoDeToken() == TipoDeToken.num_int ||
                tokenActual.getTipoDeToken() == TipoDeToken.car_char ||
                tokenActual.getTipoDeToken() == TipoDeToken.stringLiteral ||
                tokenActual.getTipoDeToken() == TipoDeToken.pr_float;
    }

    private NodoReturn metReturn() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        Token token = tokenActual;
        match("return", TipoDeToken.pr_return);
        NodoExpresion nodoExpresion = expresionOpcional();
        NodoReturn nr = new NodoReturn(token, nodoExpresion);
        return nr;
    }

    private NodoExpresion expresionOpcional() throws ExcepcionLexica, IOException, ExcepcionSintactica, ExcepcionSemantica {
        NodoExpresion ne = new NodoExpresionVacia();
        if (isLiteral() || primerosOperadorUnario() || primerosPrimario())
            ne = expresion();
        return ne;
    }

    void match(String nombreToken, TipoDeToken tokenEsperado) throws ExcepcionLexica, IOException, ExcepcionSintactica {
        if (tokenEsperado == tokenActual.getTipoDeToken())
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica(tokenActual, nombreToken);
    }
}