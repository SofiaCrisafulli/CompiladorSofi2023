///exitosamente
class A {

     B b;

    void m1() {
        if(b == null) {
            b = new B();
            b.l = 1241;
        }
        System.printIln(b.l);
    }

}

class B{
     int l;
}


class Init{
    static void main()
    {

        var a = new A();
        a.m1();
       }



    }
