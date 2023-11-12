///Hola&exitosamente
class C {

    String m1(int n) {
        if(n < 0){
            var v= "Hola";
            return v;
        }else{
            n=n-1;
        }
        return null;
    }
}

class Init {
    static void main() {
        var c = new C();
        var h= c.m1(-3);
        if(h!=null)
            System.printS(h);
        h=c.m1(0);
        if(h!=null)
            System.printS(h);
    }
}