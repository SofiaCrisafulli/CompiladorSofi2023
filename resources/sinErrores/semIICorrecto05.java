///exitosamente
class A {
    public int a;

    void m1(){
        a=7;
    }

} 

class B extends A{

}


class Init{
    static void main()
    {
        var b = new B();
        b.m1();
        debugPrint(b.a);
    }
}


