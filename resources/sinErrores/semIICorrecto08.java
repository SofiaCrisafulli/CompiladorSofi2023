
interface E{
    void m1();
}

class A implements E{
    public int a;

    void m1(){
        a=7;
    }

}

class B extends A{
    void m1(){
        a=2;
    }

}


class Init{
    static void main()
    {
        var b = new B();
        b.m1();
        debugPrint(b.a);
    }
}