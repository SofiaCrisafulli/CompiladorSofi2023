///exitosamente
interface D{

    int m2();
}

interface C{

    int m2();
}
interface A extends C {

    void m1();
}

class E {
    void m3(){
        System.printS("HOLAS");
    }
}

class B implements A{

    void m1(){
        System.printI(12);
    }

    int m2(){
        System.printI(50);
        return 24;
    }
}

class D{
    static A c(){
        return new B();
    }
}

class Init{
    static void main(){     //no anda bien pero me quede sin ideas
        var a= D.c();
        a.m2();
    }
}


