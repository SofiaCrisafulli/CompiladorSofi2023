///1234&exitosamente
class A{

    public A(){
        debugPrint(1234);
    }
    void imprimir(){
        debugPrint(1234);
    }
    int metodoA(){

    }
}
class B extends A{
    static void main(){}
    int metodoB(){
        imprimir();
    }
    int metodoA(){

    }
}