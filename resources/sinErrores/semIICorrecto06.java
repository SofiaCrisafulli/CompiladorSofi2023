
class A {
    public int a;

    static int m1(){
        return 8 / 2;
    }

}

class B extends A{

}


class Init{
    static void main()
    {
        var b = new B();
        debugPrint(b.m1());
    }
}