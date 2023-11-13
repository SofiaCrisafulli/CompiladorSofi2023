class Init{
    static void main(){
        var a = System.read();
        var b = new B();
        var c = new C();

        if(a < 5){
            b.b1 = 7;
        }else{
            b.b1 = 5;
        }

        System.printIln(b.b1);
    }
}

class B{
     int b1;
}

class C{
     int c1;
}