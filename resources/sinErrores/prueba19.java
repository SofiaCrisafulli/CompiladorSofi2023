///100&4&2&2&exitosamente

class A{

    int a1;
    void m1(int p1){
        debugPrint(p1);
    }

    int m2(){

        return 33;
    }
}

class B extends A{
    int a2;
    public B(int a1, int a2){
        this.a1=a1;
        this.a2=a2;
    }
    int m2(){
        if(a2>0)
            return a1;
        return a2;
    }
    void m3(int p){
        var v= m2();
        m1(v);
        m1(p);
        v=p=2;
        m1(v);
        m1(p);
    }
}

class Init{
    static void main()

    {
        var x = new B(100,4);
        x.m3(4);
        //var s = new String();
        //System.printSln(s);
        var t="";
    }
}