///exitosamente
class A{
    private String s;

    void m1(){
        s = "metodo 1 gana";
    }

    String m2(){
        m1();
        return s;
    }

}


class Init{
    static void main()
    {
       var a = new A();
       var c = a.m2();
        System.printSln(c);


    }
}