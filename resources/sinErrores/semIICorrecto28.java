///exitosamente
interface D{

    int m2();
}

interface C{

    int m2();
}
interface A extends D{

    void m1();
}

class G {
    void m3(){
        System.printS("HOLAS");
    }
}

class B implements C {

    void m1(){
        System.printI(12);
    }

    int m2(){
        System.printI(50);
        return 24;
    }
}

class Init{
    static void main(){
        var a = new B();
        a.m1();
        a.m2();
    }
}


