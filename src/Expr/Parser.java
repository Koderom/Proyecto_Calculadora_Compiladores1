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
        float a = termino();
        float b = p2();
        return a + b;
    }
    private float p2(){
        String lxm  = analex.Preanalisis().getLexem();
        String[] conj = {"+","-"};
        if (existIn(lxm,conj)){
            float a = p1();
            float b = p2();
            return a + b;
        }
        return 0;
    }
    private float p1(){
        String lxm  = analex.Preanalisis().getLexem();
        if(lxm == "+"){
            match(analex.Preanalisis().getNom());
            return termino();
        }else{
            match(analex.Preanalisis().getNom());
            return -termino();
        }
    }
    private float termino(){    //Termino ->
        float a = factor();
        float b = p4();
        return a * b;
    }
    private float p4(){
        String lxm  = analex.Preanalisis().getLexem();
        String[] conj = {"*","/"};
        if(existIn(lxm, conj)){
            float a = p3();
            float b = p4();
            return a * b;
        }
        return 1;
    }
    private float p3(){
        String lxm  = analex.Preanalisis().getLexem();
        if(lxm == "*"){
            match(analex.Preanalisis().getNom());
            return factor();
        }else{
            if(lxm == "/"){
                match(analex.Preanalisis().getNom());
                return 1/factor();
            }
        }
        return 0;
    }
    private float factor(){     //Factor ->
        int nom  = analex.Preanalisis().getNom();
        if(nom == 9){// num
            float a = analex.Preanalisis().getAtr();
            match(analex.Preanalisis().getNom());
            return a;
        }else{
            if(nom == 5){// menos
                match(analex.Preanalisis().getNom());
                return -factor();
            }else{
                if(nom == 4){//mas
                    match(analex.Preanalisis().getNom());
                    return factor();
                }else{// PA = (
                    match(analex.Preanalisis().getNom());
                    float b = expr();
                    match(analex.Preanalisis().getNom());
                    return b;
                }
            }
        }
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
    //funcion para buscar en un conjunto
    private boolean existIn(String a, String[] b){
        for (int i = 0; i < b.length; i++) {
            if(a == b[i]) return true;
        }
        return false;
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
