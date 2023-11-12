///&exitosamente
class A{
    static void main(){}
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
    int metodoB(){
        imprimir();
    }
    int metodoA(){

    }
}