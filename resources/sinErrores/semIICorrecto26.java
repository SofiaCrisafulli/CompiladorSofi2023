///12&50&exitosamente
class B{

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


