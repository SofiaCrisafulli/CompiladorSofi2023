///exitosamente
interface E{
    void m1();
    int getv();
}

interface T{
    void m1();
}

interface R extends T{

}

class A implements E {
     int a;

    void m1(){
        a=7;
    }

    int getv(){
        return a;
    }

}

class B {
     A elem;
    void m1(){
        elem = new A();
        elem.m1();
        debugPrint(elem.getv());
    }

}


class Init{
    static void main()
    {
        var b = new B();
        b.m1();
    }
}