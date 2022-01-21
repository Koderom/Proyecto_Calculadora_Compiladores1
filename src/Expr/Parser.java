package Expr;

public class Parser {
    private PError error; 
    private Cinta cinta;
    private Analex analex;             
    
    public Parser(){
        error  = new PError();
        cinta  = new Cinta();
        analex = new Analex(cinta, error);
    }
    
    public boolean hayError(){
        return error.hayError();
    }
    
    public String getErrorMsj(){
        return error.getErrorMsj();
    }
    
    public float evaluar(String expresion){
        error.init();
        cinta.init(expresion);
        analex.init();
        if (analex.preNom() == Token.FIN)  return 0;    //La expresion está vacía.
        
        
        return expr();      //Llamar al símbolo inicial.
    }
    
    
    private float expr(){  //Expr ->...  Símbolo inicial. Devuelve el resultado de la expresion.
        return 0;
    }
    
    private float termino(){    //Termino ->
        return 0;
    }
    
    private float factor(){     //Factor ->
        return 1;
    }
       
    private void match(int nomToken){       
        match(nomToken, "Error de Sintaxis");
    }
    
    private void match(int nomToken, String msj){
        if (analex.Preanalisis().getNom() == nomToken)
            analex.avanzar();   
        else
            setError(msj);
    }
    //e.g. match(Token.PC, "Se espera )");
       
    private void setError(String msj){
        error.setError(msj, analex.getPosLexema(), analex.lexema());
    }
    
    
//----------------- Para resaltar el Error en el Form --------------------------    
      public void comunicarError(){
        if (hayError())
            comunicarError(getErrorMsj(), error.getPosLexema(), error.getLexema());
    }
      
    public void comunicarError(String errorMsj, int pos, String lexema){
        //Overridable.
    }
}
