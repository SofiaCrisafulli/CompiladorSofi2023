interface A{

    void m1();
    int m2();
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

class Init{
    static void main(){
        var a = new B();
        a.m1();
        a.m2();
    }
}


