///1234&exitosamente
class A{
    static void imprimir(){
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
        return 1;
    }
    void metodoC(){
        return ;
    }
}
class Init{
    static void main(){
        A.imprimir();
    }
}