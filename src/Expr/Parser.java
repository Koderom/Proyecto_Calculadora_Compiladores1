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
        System.out.println("entro a : expr");
        float a = termino();
        float b = p2();
        return a + b;
    }
    private float p2(){
        System.out.println("entro a : P2");
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
        System.out.println("entro a : P1");
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
        System.out.println("entro a : termino");
        float a = factor();
        float b = p4();
        return a * b;
    }
    private float p4(){
        System.out.println("entro a : P4");
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
        System.out.println("entro a : P3");
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
        System.out.println("entro a : factor");
        int nom  = analex.Preanalisis().getNom();
        float res = 0;
        switch (nom){
            case 9://num
                res = analex.Preanalisis().getAtr();
                match(analex.Preanalisis().getNom());
            break;
            case 5://menos
                match(analex.Preanalisis().getNom());
                res = -factor();
            break;
            case 4://mas
                match(analex.Preanalisis().getNom());
                res = factor();
            break;
            case 2://PA
                match(analex.Preanalisis().getNom());
                res = expr();
                match(analex.Preanalisis().getNom());//)
            break;
            case 10://IDFUNC
//                float parm = analex.Preanalisis().getAtr();//numero de parametros de la funcion
                String func = analex.lexema().toLowerCase();
                match(10);//match a idfunc
                match(2);//match a (
                float aux[] = parm();
                switch(func){
                    case "logb":
                        res = (float)(Math.log10((double)aux[0])/Math.log10((double)aux[1]));//num base
                    break;
                    case "sen":
                        res = (float)Math.sin((float)aux[0]);
                    break;
                    case "cos":
                        res = (float)Math.cos((float)aux[0]);
                    break;
                    case "sqrt":
                        res = (float)Math.sqrt((float)aux[0]);
                    break;
                }
                
            break;
            
        }
        match(3);//match a )
        return res;
    }
    public float[] parm(){
        float[] res = new float[2];
        res[0] = expr();
        res[1] = p5();
        return res;   
    }
    public float p5(){
        String lxm  = analex.Preanalisis().getLexem();
        if(lxm == ","){
            match(analex.Preanalisis().getNom());
            return expr();
        }
        return 0;
    }
    private void match(int nomToken){       
        match(nomToken, "Error de Sintaxis");
    }
    
    private void match(int nomToken, String msj){
        if (analex.Preanalisis().getNom() == nomToken){
            System.out.println("se reconcio : "+nomToken);
            analex.avanzar();   
        }
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
