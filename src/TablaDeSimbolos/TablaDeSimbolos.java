package TablaDeSimbolos;

import AnalilzadorLexico.TipoDeToken;
import AnalilzadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSintactico.ExcepcionSintactica;
import Nodos.MetodosPredefinidos.*;
import Nodos.Sentencia.NodoBloque;
import Nodos.Sentencia.NodoVarLocal;
import Tipo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolos {

    public static TablaDeSimbolos instance;
    public static Map<String, ClaseConcreta> clases;
    public static Map<String, Interface> interfaces;
    public static Map<String, Metodo> metodos;

    public static Metodo main;
    public static ClaseConcreta claseActual;
    public static Interface interfaceActual;
    public static boolean hayMain;
    public Token tokenEof;
    public static ArrayList<NodoBloque> bloques;
    public static ArrayList<String> listaInstrucciones;
    public int numeroEtiqueta;


    public TablaDeSimbolos() {
        limpiar();
    }

    public static void setClaseActual(ClaseConcreta c) {
        claseActual = c;
    }

    public static void limpiar() {
        System.out.println("Limpiar");
        clases = new HashMap<String, ClaseConcreta>();
        interfaces = new HashMap<String, Interface>();
        metodos = new HashMap<String, Metodo>();
        hayMain = false;
        bloques = new ArrayList<>();
        listaInstrucciones = new ArrayList<>();
        crearObject();
        crearSystem();
        crearString();
    }

    public static TablaDeSimbolos getInstance() {
        if (instance == null)
            instance = new TablaDeSimbolos();
        return instance;
    }

    private static void crearString() {
        ClaseConcreta string = new ClaseConcreta(new Token(TipoDeToken.stringLiteral, "String", 0));
        clases.put(string.getToken().getLexema(), string);
        string.setConsolidar(true);
        string.setHayHerenciaCircular(true);
    }

    private static void crearSystem() {
        ClaseConcreta system = new ClaseConcreta(new Token(TipoDeToken.id_clase, "System", 0));
        system.setHereda(new Token(TipoDeToken.id_clase, "Object", 0));
        TablaDeSimbolos.setClaseActual(system);
        Metodo met = new MetodoRead(new Token(TipoDeToken.id_met_var, "read", 0), new ArrayList<Parametro>(), new TipoInt(new Token(TipoDeToken.id_met_var, "read", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosBoolean = new ArrayList<>();
        parametrosBoolean.add(new Parametro(new Token(TipoDeToken.pr_boolean, "boolean", 0), new TipoBoolean(new Token(TipoDeToken.pr_boolean, "boolean", 0))));
        met = new MetodoPrintB(new Token(TipoDeToken.id_met_var, "printB", 0), parametrosBoolean, new TipoVoid(new Token(TipoDeToken.id_met_var, "printB", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosChar = new ArrayList<>();
        parametrosChar.add(new Parametro(new Token(TipoDeToken.pr_char, "char", 0), new TipoBoolean((new Token(TipoDeToken.pr_boolean, "boolean", 0)))));
        met = new MetodoPrintC(new Token(TipoDeToken.id_met_var, "printC", 0), parametrosChar, new TipoVoid(new Token(TipoDeToken.id_met_var, "printC", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosInt = new ArrayList<>();
        parametrosInt.add(new Parametro(new Token(TipoDeToken.pr_int, "int", 0), new TipoInt(new Token(TipoDeToken.num_int, "int", 0))));
        met = new MetodoPrintI(new Token(TipoDeToken.id_met_var, "printI", 0), parametrosInt, new TipoVoid(new Token(TipoDeToken.id_met_var, "printI", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosString = new ArrayList<>();
        parametrosString.add(new Parametro(new Token(TipoDeToken.stringLiteral, "string", 0), new TipoString(new Token(TipoDeToken.stringLiteral, "string", 0))));
        met = new MetodoPrintS(new Token(TipoDeToken.id_met_var, "printS", 0), parametrosString, new TipoVoid(new Token(TipoDeToken.id_met_var, "printS", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);


        met = new MetodoPrintln(new Token(TipoDeToken.id_met_var, "println", 0), new ArrayList<Parametro>(), new TipoVoid(new Token(TipoDeToken.id_met_var, "println", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosBln = new ArrayList<>();
        parametrosBln.add(new Parametro(new Token(TipoDeToken.pr_boolean, "boolean", 0), new TipoBoolean((new Token(TipoDeToken.pr_boolean, "boolean", 0)))));
        met = new MetodoPrintBln(new Token(TipoDeToken.id_met_var, "printBln", 0), parametrosBln, new TipoVoid(new Token(TipoDeToken.id_met_var, "printBln", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosPrintCln = new ArrayList<Parametro>();
        parametrosPrintCln.add(new Parametro(new Token(TipoDeToken.pr_char, "char", 0), new TipoChar((new Token(TipoDeToken.pr_char, "char", 0)))));
        met = new MetodoPrintCln(new Token(TipoDeToken.id_met_var, "printCln", 0), parametrosPrintCln, new TipoVoid(new Token(TipoDeToken.id_met_var, "printCln", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosPrintIln = new ArrayList<Parametro>();
        parametrosPrintIln.add(new Parametro(new Token(TipoDeToken.pr_int, "printIln", 0), new TipoInt(new Token(TipoDeToken.num_int, "int", 0))));
        met = new MetodoPrintIln(new Token(TipoDeToken.id_met_var, "printIln", 0), parametrosPrintIln, new TipoVoid(new Token(TipoDeToken.id_met_var, "printIln", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        ArrayList<Parametro> parametrosPrintSln = new ArrayList<>();
        parametrosPrintSln.add(new Parametro(new Token(TipoDeToken.id_met_var, "string", 0), new TipoString(new Token(TipoDeToken.stringLiteral, "string", 0))));
        met = new MetodoPrintSln(new Token(TipoDeToken.id_met_var, "printSln", 0), parametrosPrintSln, new TipoVoid(new Token(TipoDeToken.id_met_var, "printSln", 0)), true, system.getToken(), system);
        met.setMetodoEstatico(true);
        system.addMetodo(met);

        clases.put(system.getToken().getLexema(), system);
        system.setHayHerenciaCircular(true);
        system.setConsolidar(true);
    }

    private static void crearObject() {
        ClaseConcreta objeto = new ClaseConcreta(new Token(TipoDeToken.id_clase, "Object", 0));
        TablaDeSimbolos.setClaseActual(objeto);
        ArrayList<Parametro> parametrosDebugPrint = new ArrayList<>();
        parametrosDebugPrint.add(new Parametro(new Token(TipoDeToken.id_met_var, "debugPrint", 0), new TipoInt(new Token(TipoDeToken.pr_int, "int", 0))));
        Metodo met = new MetodoDebugPrint(new Token(TipoDeToken.id_met_var, "debugPrint", 0), parametrosDebugPrint, new TipoVoid(new Token(TipoDeToken.id_met_var, "debugPrint", 0)), true, new Token(TipoDeToken.id_met_var, "debugPrint", 0), claseActual);
        met.setMetodoEstatico(true);
        objeto.addMetodo(met);
        clases.put(objeto.getToken().getLexema(), objeto);
        objeto.setHayHerenciaCircular(true);
        objeto.setConsolidar(true);
    }

    public static void chequeoDeSentencias() throws ExcepcionSemantica {
        for (ClaseConcreta clase : clases.values()) {
            claseActual = clase;
            clase.chequear();
            clase.chequeoDeSentencias();
        }
    }

    public static ClaseConcreta getClase(String nombre) {
        if (nombre.equals("this"))
            return claseActual;
        return clases.get(nombre);
    }

    public Interface getInterfaz(String nombre) {
        return interfaces.get(nombre);
    }


    public static ClaseConcreta getClaseActual() {
        return claseActual;
    }

    public static void setInterfaceActual(Interface i) {
        interfaceActual = i;
    }


    public Map<String, ClaseConcreta> getClases() {
        return clases;
    }

    public static Map<String, Interface> getInterfaces() {
        return interfaces;
    }

    public static Interface getInterface(String i) {
        return interfaces.get(i);
    }

    public static void addBloqueIni(NodoBloque b) {
        bloques.add(0, b);
    }

    public static void removeBloque() {
        bloques.remove(0);
    }

    public static NodoBloque getBloqueActual() {
        return bloques.get(0);
    }

    public void eliminarBloque() {
        bloques.remove(0);
    }

    public boolean existeClase(String nombreClase) {
        ClaseConcreta clase = clases.get(nombreClase);
        if (clase == null)
            return false;
        else
            return true;
    }

    public void estaBienDeclarado() throws ExcepcionSemantica {
        for (Interface interfaz : interfaces.values())
            interfaz.estaBienDeclarada();
        for (ClaseConcreta claseConcreta : clases.values()) {
            claseConcreta.estaBienDeclarada();
        }
    }

    public void consolidar() throws ExcepcionSemantica, ExcepcionSintactica {
        for (Interface interfaz : interfaces.values()) {
            interfaz.consolidar();
        }
        for (ClaseConcreta claseConcreta : clases.values()) {
            if (!claseConcreta.getEstaConsolidada()) {
                claseConcreta.consolidarMetodos();
                claseConcreta.consolidarAtributos();
                claseConcreta.consolidarImplements();
                claseConcreta.setConsolidar(true);
            }
            Metodo m = claseConcreta.getMetodos().get("main");
            if (hayMain && m != null)
                throw new ExcepcionSemantica(m.getTokenMetodo(), "hay mas de un main");
            if (m != null && m.getMetodoEstatico()) {
                hayMain = true;
                main = m;
            }
        }
        if (!hayMain) {
            tokenEof.setLexema("EOF");
            throw new ExcepcionSemantica(tokenEof, "no se encuentra el metodo main");
        }
    }


    public boolean insertarClase(Token token, ClaseConcreta claseActual) throws ExcepcionSemantica {
        boolean esta = false;
        if (interfaces.get(token.getLexema()) != null)
            esta = true;
        else {
            if (clases.get(token.getLexema()) == null)
                clases.put(token.getLexema(), claseActual);
            else
                esta = true;
        }
        if (esta)
            throw new ExcepcionSemantica(token, "Ya existe una clase o interfaz con ese nombre");
        return esta;
    }


    public boolean insertarInterfaz(Token token, Interface i) throws ExcepcionSemantica {
        boolean esta = false;
        if (clases.get(token.getLexema()) != null)
            esta = true;
        else {
            if (interfaces.get(token.getLexema()) == null)
                interfaces.put(token.getLexema(), interfaceActual);
            else
                esta = true;
        }
        if (esta)
            throw new ExcepcionSemantica(token, "Ya existe una clase o interfaz con ese nombre");
        return esta;
    }

    public boolean existeInterfaz(String nombre) {
        Interface i = interfaces.get(nombre);
        if (i == null)
            return false;
        else
            return true;
    }

    public static NodoVarLocal getVarLocal(String lexema) {
        NodoVarLocal nodoVarLocal = null;
        for (NodoBloque bloque : bloques) {
            if (nodoVarLocal == null)
                nodoVarLocal = bloque.getVarLocal(lexema);
        }
        return nodoVarLocal;
    }

    public static NodoVarLocal getVarlocalAndParam(String lexema) {

        NodoVarLocal nodoVarLocal = getVarLocal(lexema);

        if (nodoVarLocal == null) {
            for (Parametro p : getClaseActual().getMetodoActual().getListaParametros()) {
                if (nodoVarLocal == null && lexema.equals(p.getTokenParametro().getLexema()))
                    nodoVarLocal = new NodoVarLocal(p.getTokenParametro(), p.getTipo());
            }
        }
        return nodoVarLocal;
    }


    public static NodoBloque getNodoBloque() {
        return bloques.get(0);
    }


    public Interface getInterfazActual() {
        return interfaceActual;
    }

    public void addEOF(Token eof) {
        tokenEof = eof;
    }


    public void generar() throws ExcepcionSemantica {
        generarInicial();
        for (ClaseConcreta clase : clases.values())
            clase.generar();
    }

    public static void generarInicial() {
        // Code
        listaInstrucciones.add(".CODE");
        listaInstrucciones.add("PUSH simple_heap_init");
        listaInstrucciones.add("CALL");
        listaInstrucciones.add("PUSH " + main.getTokenMetodo().getLexema() + "@" + main.getMiClase().getToken().getLexema());
        listaInstrucciones.add("CALL");
        listaInstrucciones.add("HALT");
        listaInstrucciones.add("");


        listaInstrucciones.add("simple_heap_init:");
        listaInstrucciones.add("RET 0");
        listaInstrucciones.add("");

        //malloc
        listaInstrucciones.add("simple_malloc:");
        listaInstrucciones.add("LOADFP ; Inicialización unidad");
        listaInstrucciones.add("LOADSP");
        listaInstrucciones.add("STOREFP ; Finaliza inicialización del RA");
        listaInstrucciones.add("LOADHL ; hl");
        listaInstrucciones.add("DUP ; hl");
        listaInstrucciones.add("PUSH 1 ; 1");
        listaInstrucciones.add("ADD ; hl + 1");
        listaInstrucciones.add("STORE 4 ; Guarda resultado (puntero a base del bloque)");
        listaInstrucciones.add("LOAD 3 ; Carga cantidad de celdas a alojar (parámetro)");
        listaInstrucciones.add("ADD");
        listaInstrucciones.add("STOREHL ; Mueve el heap limit (hl)");
        listaInstrucciones.add("STOREFP");
        listaInstrucciones.add("RET 1 ; Retorna eliminando el parámetro");
        listaInstrucciones.add("");
    }


    public static void gen(String intruccion) {
        listaInstrucciones.add(intruccion);
    }

    public String getNumeroEtiqueta() {
        return "E" + numeroEtiqueta++;
    }
}
