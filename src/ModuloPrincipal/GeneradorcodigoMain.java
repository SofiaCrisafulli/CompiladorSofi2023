package ModuloPrincipal;

import AnalilzadorLexico.AnalizadorLexico;
import ManejadorDeArchivo.GestorDeArchivo;
import TablaDeSimbolos.TablaDeSimbolos;
import TablaDeSimbolos.TablaEstatica;
import AnalizadorSemantico.*;

import java.io.File;

public class GeneradorcodigoMain {

    public static void main(String[] args) {
        for (int i = 1; i < 29; i++) {
            String name;
            if (i < 10)
                name = "0" + i;
            else
                name = "" + i;
            String file_name;
            TablaEstatica.startTabla();
            if (true || args.length > 0) {
                //file_name=args[0];
                //Para revisar

                file_name = "resources/sinErrores/semIICorrecto" + name + ".java";
                String[] nameIntermedio = file_name.split("/");
                String namee = "Compilado_" + nameIntermedio[nameIntermedio.length - 1];
                if (args.length > 1)
                    namee = args[1];
                File file = new File(file_name);
                AnalizadorSemantico analizador = null;

                try {
                    AnalizadorLexico analizadorLexico = new AnalizadorLexico(new GestorDeArchivo(file_name));
                    analizador = new AnalizadorSemantico(analizadorLexico);
                    analizador.start();
                    System.out.println("Finalizo el analisis sintactico");
                    System.out.println("Finalizo el analisis Semantico" + System.lineSeparator() + "[SinErrores]");
                    TablaDeSimbolos.generarInicial();
                } catch (Exception e) {
                    // e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}