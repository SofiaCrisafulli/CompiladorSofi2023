///1&2&exitosamente
class A {
    public A(int x, int y) {
        debugPrint(x);
        debugPrint(y);
    }
}



class Init {
    A a;
    static void main() {
        new A(1,2);
    }
}