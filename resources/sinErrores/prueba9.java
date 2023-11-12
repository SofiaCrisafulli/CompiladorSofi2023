///1234&33&exitosamente

class A{
    int x;

    public A(int y){
        x=y;
        debugPrint(y);
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


