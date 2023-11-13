



class Init{
    static void main()
    {
       var r = true;
       var e =0;
       while(r){
           if(r){
               e = e+1;
               System.printIln(e);
           }else{   //nunca se va a ejecutar, solo para testear
               System.printSln("FIN");
           }
           if(e>12){
               r =false;
           }

       }



    }
}