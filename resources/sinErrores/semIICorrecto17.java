///IGUALDAD&MENOR&MAYOR&MAIGUALDAD&MEIGUALDAD&MENOS&MAS&AND&or&negacion&DIV&modulo&MUL&Negativo&exitosamente



class Init{
    static void main()
    {
        if(1 == 1)
            System.printSln("IGUALDAD");
        if(0 < 1)
            System.printSln("MENOR");
        if(1 > 0)
            System.printSln("MAYOR");
        if(1 >= 1)
            System.printSln("MAIGUALDAD");
        if(1 <= 1)
            System.printSln("MEIGUALDAD");
        if((1-1)==0)
            System.printSln("MENOS");
        if((1+1)==2)
            System.printSln("MAS");
        if((1 == 1) && true)
            System.printSln("AND");
        if((1 != 1) || true)
            System.printSln("or");
        if(!false)
            System.printSln("negacion");
        if((2/2) == 1 )
            System.printSln("DIV");
        if((2%2) == 0 )
            System.printSln("modulo");
        if((2*3) == 6)
            System.printSln("MUL");
        var a = -2;
        if(a<0)
            System.printSln("Negativo");
       }



    }
