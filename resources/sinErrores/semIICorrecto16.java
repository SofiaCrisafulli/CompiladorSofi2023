
class A {

    static int m1() {
        return  17;
    }
    int m2() {
        return this.m1();
    }

}


class Init{
    static void main()
    {
        var a = new A();
        System.printIln(a.m2());
       }



    }
