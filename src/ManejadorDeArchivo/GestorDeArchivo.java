package ManejadorDeArchivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GestorDeArchivo {

    private BufferedReader lectorDeArchivo;
    private int pos;
    private static int nroLinea;
    private static boolean siguienteLinea;
    private String lineaActual;
    private String lineaAnterior;
    private int nroCol;

    public GestorDeArchivo(String archivo) throws IOException {
        siguienteLinea = false;
        nroLinea = 1;
        pos = 0;
        lectorDeArchivo = new BufferedReader(new FileReader(archivo));
        lineaAnterior = null;
        lineaActual = lectorDeArchivo.readLine();
        nroCol = 1;
    }

    public char getProxCaracter() throws IOException {
        char x = '\u001a';
        siguienteLinea = false;
        if (lineaActual != null) {
            if (pos < lineaActual.length()) {
                x = lineaActual.charAt(pos);
                pos++;
                nroCol = pos;
            } else {
                lineaAnterior = lineaActual;
                lineaActual = lectorDeArchivo.readLine();
                siguienteLinea = true;
                nroLinea++;
                if (lineaActual != null) {
                    pos = 0;
                    x = '\n';
                }
            }
        }
        if (esEOF(x)) {
            lectorDeArchivo.close();
        }
        return x;
    }

    public String devolverAnterior() {
        if (siguienteLinea)
            return lineaAnterior;
        else
            return lineaActual;
    }

    public static int getNroLinea() {
        if (siguienteLinea) {
            return nroLinea - 1;
        } else {
            return nroLinea;
        }
    }

    public boolean esEOF(char x) {
        return x == '\u001a';
    }

    public int getNroCol() {
        return nroCol;
    }

    public void setNroCol(int nroCol) {
        this.nroCol = nroCol;
    }
}
