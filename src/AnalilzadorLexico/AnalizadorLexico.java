package AnalilzadorLexico;

import ManejadorDeArchivo.GestorDeArchivo;

import java.io.EOFException;
import java.io.IOException;


public class AnalizadorLexico {

    String lexema;
    char caracterActual;
    GestorDeArchivo gestorDeFuente;
    String lineaComentarioMultilinea;
    int nroFilaComentarioMul;
    int nroColComentarioMul;
    PalabrasReservadas palabrasReservadas;
    float minFloat = Float.MIN_VALUE;
    float maxFloat = Float.MAX_VALUE;

    public AnalizadorLexico(GestorDeArchivo gestor) throws IOException {
        palabrasReservadas = new PalabrasReservadas();
        gestorDeFuente = gestor;
        actualizarCaracterActual();
    }

    public Token proximoToken() throws IOException, ExcepcionLexica {
        lexema = "";
        return e0();
    }

    private void actualizarLexema() {
        lexema = lexema + caracterActual;
    }

    public void actualizarCaracterActual() throws IOException {
        caracterActual = gestorDeFuente.getProxCaracter();
    }

    private Token e0() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return digito();
        } else if (Character.isUpperCase(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return mayuscula();
        } else if (Character.isLowerCase(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return minuscula();
        } else if (caracterActual == '>') {
            actualizarLexema();
            actualizarCaracterActual();
            return mayor();
        } else if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return asignacion();
        } else if (gestorDeFuente.esEOF(caracterActual)) {
            return eof();
        } else if (caracterActual == '<') {
            actualizarLexema();
            actualizarCaracterActual();
            return menor();
        } else if (caracterActual == '!') {
            actualizarLexema();
            actualizarCaracterActual();
            return negacion();
        } else if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return asignacion();
        } else if (caracterActual == '+') {
            actualizarLexema();
            actualizarCaracterActual();
            return mas();
        } else if (caracterActual == '-') {
            actualizarLexema();
            actualizarCaracterActual();
            return menos();
        } else if (caracterActual == '*') {
            actualizarLexema();
            actualizarCaracterActual();
            return multiplicacion();
        } else if (caracterActual == '/') {
            actualizarLexema();
            actualizarCaracterActual();
            return division();
        } else if (caracterActual == '&') {
            actualizarLexema();
            actualizarCaracterActual();
            return and();
        } else if (caracterActual == '|') {
            actualizarLexema();
            actualizarCaracterActual();
            return or();
        } else if (caracterActual == '%') {
            actualizarLexema();
            actualizarCaracterActual();
            return modulo();
        } else if (caracterActual == '"') {
            actualizarLexema();
            actualizarCaracterActual();
            return strings();
        } else if (caracterActual == '(') {
            actualizarLexema();
            actualizarCaracterActual();
            return parentesisQueAbre();
        } else if (caracterActual == ')') {
            actualizarLexema();
            actualizarCaracterActual();
            return parentesisQueCierra();
        } else if (caracterActual == '{') {
            actualizarLexema();
            actualizarCaracterActual();
            return llaveQueAbre();
        } else if (caracterActual == '}') {
            actualizarLexema();
            actualizarCaracterActual();
            return llaveQueCierra();
        } else if (caracterActual == ';') {
            actualizarLexema();
            actualizarCaracterActual();
            return puntoYComa();
        } else if (caracterActual == ',') {
            actualizarLexema();
            actualizarCaracterActual();
            return coma();
        } else if (caracterActual == '.') {
            actualizarLexema();
            actualizarCaracterActual();
            return punto();
        } else if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return caracter();
        } else if (caracterActual == '\n' || caracterActual == ' ' || caracterActual == '\t') {
            actualizarCaracterActual();
            return e0();
        } else {
            actualizarLexema();
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Símbolo inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        }
    }

    private Token digito() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return digito();
        } else if (caracterActual == '.') {
            actualizarLexema();
            actualizarCaracterActual();
            return float1();
        } else if (caracterActual == 'e' || caracterActual == 'E') {
            actualizarLexema();
            actualizarCaracterActual();
            return float2();
        } else if (lexema.length() > 9)
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Dígito inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        else
            return new Token(TipoDeToken.num_int, lexema, gestorDeFuente.getNroLinea());
    }

    private Token mayuscula() throws IOException {
        if (Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual == '_') {
            actualizarLexema();
            actualizarCaracterActual();
            return mayuscula();
        } else {
            TipoDeToken tipoDeToken = palabrasReservadas.getTipoDeToken(lexema);
            if (tipoDeToken == null)
                return new Token(TipoDeToken.id_clase, lexema, gestorDeFuente.getNroLinea());
            else
                return new Token(tipoDeToken, lexema, gestorDeFuente.getNroLinea());
        }
    }

    private Token minuscula() throws IOException {
        if (Character.isDigit(caracterActual) || Character.isLetter(caracterActual) || caracterActual == '_') {
            actualizarLexema();
            actualizarCaracterActual();
            return minuscula();
        } else {
            TipoDeToken tipoDeToken = palabrasReservadas.getTipoDeToken(lexema);
            if (tipoDeToken == null)
                return new Token(TipoDeToken.id_met_var, lexema, gestorDeFuente.getNroLinea());
            else
                return new Token(tipoDeToken, lexema, gestorDeFuente.getNroLinea());
        }
    }

    private Token mayor() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return mayorIgual();
        } else
            return new Token(TipoDeToken.op_mayor, lexema, gestorDeFuente.getNroLinea());
    }

    private Token mayorIgual() {
        return new Token(TipoDeToken.op_mayor_igual, lexema, gestorDeFuente.getNroLinea());
    }

    private Token eof() {
        return new Token(TipoDeToken.EOF, lexema, gestorDeFuente.getNroLinea());
    }

    private Token menor() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return menorIgual();
        } else
            return new Token(TipoDeToken.op_menor, lexema, gestorDeFuente.getNroLinea());
    }

    private Token menorIgual() {
        return new Token(TipoDeToken.op_menor_igual, lexema, gestorDeFuente.getNroLinea());
    }

    private Token negacion() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return distinto();
        } else
            return new Token(TipoDeToken.op_negacion, lexema, gestorDeFuente.getNroLinea());
    }

    private Token distinto() {
        return new Token(TipoDeToken.op_distinto, lexema, gestorDeFuente.getNroLinea());
    }

    private Token asignacion() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return new Token(TipoDeToken.op_igual, lexema, gestorDeFuente.getNroLinea());
        } else
            return new Token(TipoDeToken.op_asignacion, lexema, gestorDeFuente.getNroLinea());
    }

    private Token mas() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return masIgual();
        } else
            return new Token(TipoDeToken.op_mas, lexema, gestorDeFuente.getNroLinea());
    }

    private Token masIgual() {
        return new Token(TipoDeToken.op_mas_igual, lexema, gestorDeFuente.getNroLinea());
    }

    private Token menos() throws IOException {
        if (caracterActual == '=') {
            actualizarLexema();
            actualizarCaracterActual();
            return menosIgual();
        } else
            return new Token(TipoDeToken.op_menos, lexema, gestorDeFuente.getNroLinea());
    }

    private Token menosIgual() {
        return new Token(TipoDeToken.op_menos_igual, lexema, gestorDeFuente.getNroLinea());
    }

    private Token multiplicacion() {
        return new Token(TipoDeToken.op_multiplicacion, lexema, gestorDeFuente.getNroLinea());
    }

    private Token division() throws IOException, ExcepcionLexica {
        if (caracterActual == '/') {
            actualizarCaracterActual();
            return comentario();
        } else if (caracterActual == '*') {
            lineaComentarioMultilinea = gestorDeFuente.devolverAnterior();
            nroFilaComentarioMul = gestorDeFuente.getNroLinea();
            nroColComentarioMul = gestorDeFuente.getNroCol();
            actualizarCaracterActual();
            return comentarioMultilinea();
        }
        return new Token(TipoDeToken.op_division, lexema, gestorDeFuente.getNroLinea());
    }

    private Token comentario() throws ExcepcionLexica, IOException {
        if (caracterActual == '\n' || gestorDeFuente.esEOF(caracterActual))
            return proximoToken();
        else {
            actualizarCaracterActual();
            return comentario();
        }
    }

    private Token comentarioMultilinea() throws IOException, ExcepcionLexica {
        if (gestorDeFuente.esEOF(caracterActual))
            throw new ExcepcionLexica(lexema + "*", nroFilaComentarioMul, " Comentario multilínea mal cerrado ", lineaComentarioMultilinea, nroColComentarioMul);
        else if (caracterActual == '\n' || caracterActual == ' ' || caracterActual != '*' || caracterActual == '\t') {
            actualizarCaracterActual();
            return comentarioMultilinea();
        } else
            return cerrarComentarioMultilinea();
    }

    private Token cerrarComentarioMultilinea() throws IOException, ExcepcionLexica {
        if (caracterActual == '*') {
            actualizarCaracterActual();
            return cerrarComentarioMultilinea();
        } else if (caracterActual == '/') {
            actualizarCaracterActual();
            return proximoToken();
        } else
            return comentarioMultilinea();
    }

    private Token and() throws IOException, ExcepcionLexica {
        if (caracterActual == '&') {
            actualizarLexema();
            actualizarCaracterActual();
            return new Token(TipoDeToken.op_and, lexema, gestorDeFuente.getNroLinea());
        } else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Operador inválido and", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token or() throws IOException, ExcepcionLexica {
        if (caracterActual == '|') {
            actualizarLexema();
            actualizarCaracterActual();
            return new Token(TipoDeToken.op_or, lexema, gestorDeFuente.getNroLinea());
        } else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Operador inválido or", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token modulo() {
        return new Token(TipoDeToken.op_modulo, lexema, gestorDeFuente.getNroLinea());
    }

    private Token strings() throws IOException, ExcepcionLexica {
        if (caracterActual == '"') {
            if (lexema.endsWith("\\")) {
                actualizarLexema();
                actualizarCaracterActual();
                return strings();
            } else {
                actualizarLexema();
                actualizarCaracterActual();
                return stringCierre();
            }
        } else if (caracterActual == '\n' || gestorDeFuente.esEOF(caracterActual))
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "String mal cerrado", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        else {
            actualizarLexema();
            actualizarCaracterActual();
            return strings();
        }
    }

    private Token stringCierre() {
        return new Token(TipoDeToken.stringLiteral, lexema, gestorDeFuente.getNroLinea());
    }


    private Token parentesisQueAbre() throws IOException {
        return new Token(TipoDeToken.simb_parentesis_que_abre, lexema, gestorDeFuente.getNroLinea());
    }

    private Token parentesisQueCierra() {
        return new Token(TipoDeToken.simb_parentesis_que_cierra, lexema, gestorDeFuente.getNroLinea());
    }

    private Token llaveQueAbre() {
        return new Token(TipoDeToken.simb_llave_que_abre, lexema, gestorDeFuente.getNroLinea());
    }

    private Token llaveQueCierra() {
        return new Token(TipoDeToken.simb_llave_que_cierra, lexema, gestorDeFuente.getNroLinea());
    }

    private Token puntoYComa() {
        return new Token(TipoDeToken.simb_punto_y_coma, lexema, gestorDeFuente.getNroLinea());
    }

    private Token coma() {
        return new Token(TipoDeToken.simb_coma, lexema, gestorDeFuente.getNroLinea());
    }

    private Token punto() throws IOException {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
        }
        return new Token(TipoDeToken.simb_punto, lexema, gestorDeFuente.getNroLinea());
    }

    private Token float1() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return float1();
        } else if (caracterActual == 'e' || caracterActual == 'E') {
            actualizarLexema();
            actualizarCaracterActual();
            return float2();
        } else if (Float.valueOf(lexema) > minFloat && Float.valueOf(lexema) < maxFloat)
            return new Token(TipoDeToken.pr_float, lexema, gestorDeFuente.getNroLinea());
        else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Float inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token float2() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return float3();
        } else if (caracterActual == '+' || caracterActual == '-' || Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return float4();
        } else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Float inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token float3() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return float3();
        } else if (Float.valueOf(lexema) > minFloat && Float.valueOf(lexema) < maxFloat)
            return new Token(TipoDeToken.pr_float, lexema, gestorDeFuente.getNroLinea());
        else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Float inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token float4() throws IOException, ExcepcionLexica {
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return float2();
        } else {
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Float inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        }
    }

    private Token caracter() throws IOException, ExcepcionLexica {
        if (caracterActual == '\n' || gestorDeFuente.esEOF(caracterActual) || caracterActual == '\'')
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Caracter inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        else if (caracterActual == '\\') {
            actualizarLexema();
            actualizarCaracterActual();
            return caracterConBarra();
        } else {
            actualizarLexema();
            actualizarCaracterActual();
            if (caracterActual == '\'') {
                actualizarLexema();
                actualizarCaracterActual();
                return cierreCaracter();
            } else
                throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Caracter inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        }
    }

    private Token caracterConBarra() throws ExcepcionLexica, IOException {
        if (caracterActual == '\n' || gestorDeFuente.esEOF(caracterActual))
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Caracter inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            if (caracterActual == '\'') {
                actualizarLexema();
                actualizarCaracterActual();
                return cierreCaracter();
            } else
                throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Caracter inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
        }
        actualizarLexema();
        actualizarCaracterActual();
        if (caracterActual == '\'') {
            actualizarLexema();
            actualizarCaracterActual();
            return cierreCaracter();
        } else
            throw new ExcepcionLexica(lexema, gestorDeFuente.getNroLinea(), "Caracter inválido", gestorDeFuente.devolverAnterior(), gestorDeFuente.getNroCol());
    }

    private Token cierreCaracter() {
        return new Token(TipoDeToken.car_char, lexema, gestorDeFuente.getNroLinea());
    }
}