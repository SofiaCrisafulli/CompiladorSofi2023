package ModuloPrincipal;

import AnalilzadorLexico.*;
import ManejadorDeArchivo.GestorDeArchivo;
import TablaDeSimbolos.*;
import AnalizadorSemantico.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        String fileName;
        String archivoDeSalida;
        TablaEstatica.startTabla();
        if (args.length > 0) {
            fileName = args[0];
            archivoDeSalida = args[1];
            String[] nameIntermedio = fileName.split("/");
            String name = "Compilado_" + nameIntermedio[nameIntermedio.length - 1];
            if (args.length > 1)
                name = args[1];
            File file = new File(archivoDeSalida);
            AnalizadorSemantico analizador = null;

            try {
                AnalizadorLexico analizadorLexico = new AnalizadorLexico(new GestorDeArchivo(fileName));
                analizador = new AnalizadorSemantico(analizadorLexico);
                analizador.start();
                System.out.println("Compilación Exitosa" + System.lineSeparator() + "[SinErrores]");
                TablaDeSimbolos.getInstance().generar();
                crearArchivo(file);
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: no se encontró el archivo");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private static void crearArchivo(File file) throws IOException {
        ArrayList<String> instrucciones = TablaDeSimbolos.listaInstrucciones;
        FileWriter fileWriter = new FileWriter(file);
        for (String s : instrucciones)
            fileWriter.write(s + "\n");
        fileWriter.close();
    }
}