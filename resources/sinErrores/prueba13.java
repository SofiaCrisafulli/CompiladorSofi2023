///1&2&3&2&exitosamente
class A {
    static int x;
    public A(){
        x=1;
    }

    static void setX(int newX) {
        x = newX;
    }

    int getX() {
        return x;
    }

    static void printX() {
        debugPrint(x);
    }
}

class Main {
    static void main() {
        var a = new A();

        //we check that return-giving accesses work
        //var p1 = a.getX();
        a.printX();

        //we check that non-return giving accesses work
        a.setX(2);

        var p2 = a.x;
        a.printX();
        A.setX(3);
        A.printX();

        //debugPrint(p1);
        debugPrint(p2);
    }
}