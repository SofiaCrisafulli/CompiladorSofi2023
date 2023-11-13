///exitosamente
class A {

    static void fibonacci(int tope ,int actual, int anterior){
        var anteriorAux = anterior;
        if(tope > actual) {
            System.printIln(actual);
            if (actual == 0)
                actual = actual + 1;
            else {
                anterior = actual;
                actual = anteriorAux+actual;
            }
            fibonacci(tope,actual,anterior);
        }

    }



}



class Init{
    static void main() {
        System.printSln("Ingresa un valor menor a 10");
        var a = System.read();
        if (a < 0) {
            System.printSln("Error");
        } else {
            if (a != 0) {
                System.printS("Se eligio:");
                System.printI(a);
                System.println();

                A.fibonacci(a, 0, 0);
            }
            else
                System.printI(a);
        }
    }


    }
