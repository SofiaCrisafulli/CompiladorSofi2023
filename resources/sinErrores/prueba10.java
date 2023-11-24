///1234&33&exitosamente

class A{
    int x;

    public A(int y){
        m1(y);
    }
    void m1(int y){
        x=y;
        //debugPrint(x);
        debugPrint(1);
        x = 33;
        debugPrint(x);
    }
}


class Init{
    static void main()
    {
        new A(1234);

    }
}