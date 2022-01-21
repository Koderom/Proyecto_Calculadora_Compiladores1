package Expr;

public class Main {
    private static void pruebaAnalex(){
        String ProgFuente = "99 3. .25 32.78 , ( ) + - * / pi e sen cos sqrt logb";
        Cinta C = new Cinta(ProgFuente);
        
        PError E = new PError();        
        Analex A = new Analex(C, E);
        do{
            System.out.println( A.Preanalisis()+ "  Lexem="+(char)34+A.lexema()+(char)34);  
            A.avanzar();
        }while (A.preNom() != Token.FIN);
        
        if (E.hayError())
            System.out.println(E.getErrorMsj());
    }
    
    
    public static void main(String[] args) { 
       //pruebaAnalex();
        
       Parser parser = new Parser();
       
       float r = parser.evaluar("25 * sin(23.6+e)- logB(10, sen(pi)) ");
       
        if (parser.hayError())
           System.out.println(parser.getErrorMsj());
        else
           System.out.println("El valor de la expresión es " + r);
 
    }
    
}
