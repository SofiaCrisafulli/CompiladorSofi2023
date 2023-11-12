///1&2&1&2&exitosamente
class A {
    int x;
    int y;

    public A(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void imprimir() {
        while ((x - y) > 0) {
            System.printSln(getPalabra(x - y));
            x = -x + 1;
        }
    }

    String getPalabra(int c) {
        var v= "hola";
        if(c==1)
            return v;
        else
            return "chau";
    }
}

class Main {
    static void main() {
        var a = new A(2, 1);
        a.imprimir();
    }
}