class A extends System {
    static void main(){
        var v0 = "Hola";
        var v2 = 'k';
        var v1 = m1(v0, v2, 10);
        printIln(v1);
        printIln(m1("xd", v2, v1));
        printIln(m1("jjj", v2, m1("aaaA", '#', 20)));

    }

    static int m1(String p1, char p2, int p3){
        var v0 = p1;
        printSln(v0);
        var v1 = p2;
        printCln(v1);
        return p3;
    }
}