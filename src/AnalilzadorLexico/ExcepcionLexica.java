package AnalilzadorLexico;

public class ExcepcionLexica extends Exception {
    public ExcepcionLexica(String lexema, int nroLinea, String mensaje, String linea, int nroCol) {
        super(mensajeCompleto(lexema, nroLinea, mensaje, linea, nroCol));
    }

    private static String mensajeCompleto(String lexema, int nroLinea, String mensaje, String linea, int nroCol) {
        String detalle = "Detalle: ";
        String mnsjeCompleto = detalle + linea;
        String puntero = "";
        for (int i = 0; i < detalle.length() + nroCol - 1; i++) {
            if (mnsjeCompleto.charAt(i) == '\t')
                puntero += '\t';
            else
                puntero += ' ';
        }
        puntero += '^';
        return "Error léxico en línea: " + nroLinea + " y en la columna: " + nroCol + "\n" + mnsjeCompleto + "\n" + puntero + "\n [Error:" + lexema + "|" + nroLinea + "]\n";
    }
}