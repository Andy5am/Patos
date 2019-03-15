
import Clases.Funcion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main (String [] args){
        ArrayList<String> palabrasClave = new ArrayList<>();
        palabrasClave.add("ATOM");
        palabrasClave.add("LIST");
        palabrasClave.add("EQUAL");
        palabrasClave.add("<");
        palabrasClave.add(">");
        palabrasClave.add("+");
        palabrasClave.add("-");
        palabrasClave.add("*");
        palabrasClave.add("/");
        palabrasClave.add("DEFUN");
        palabrasClave.add("COND");
        HashMap funciones = new HashMap<String, Funcion>();

        String linea = "(DEFUN hola(x y)(+ x (/ y 5)))";
        defun(split(linea),funciones);
        ArrayList<String> p = new ArrayList();
        p.add("5");
        p.add("6");
        Funcion f0 = ((Funcion) funciones.get("hola"));
        if (f0.initParam(p)){
            f0.replaceParams();
            System.out.println(funciones);
        }
    }

    public void execute(){

    }

    //Recibe de parametro un arraylist desde el (DEFUN...)
    public static void defun (ArrayList<String> lista, HashMap<String, Funcion> funciones){
        int parentesis = 0;
        int c = 0;
        Funcion f = new Funcion();
        while (!lista.get(c).equals(")")){
            if (lista.get(c).equals("(")){
                parentesis++;
            }else if (parentesis == 1 && !lista.get(c).toUpperCase().equals("DEFUN") && !lista.get(c).toUpperCase().equals(")")){
                f.setNombre(lista.get(c));
            }else if(parentesis == 2 && !lista.get(c).equals(")")){
                while (!lista.get(c).equals(")")){
                    f.addParam(lista.get(c));
                    c++;
                }
            }else if (parentesis>2){
                f.addInst("(");
                int a = 0;
                for (int i =c;i<lista.size()-1;i++){
                    f.addInst(lista.get(i));
                    a++;
                }
                c = c + a - 1;
            }
            c++;
        }
        funciones.put(f.getNombre(), f);
    }

    /**public static void definirFuncion (ArrayList<String> codigo, HashMap funciones){
        int parentesis = 0;
        int parCerrado = 0;
        String nombre = "";
        ArrayList<String > parametros = new ArrayList();
        ArrayList<String> instrucciones = new ArrayList();
        for (int i = 0; i < codigo.size(); i++){
            if (codigo.get(i).equals("DEFUN")){
                nombre = codigo.get(i+1);
            }else if (codigo.get(i).equals("(")){
                parentesis++;
                if (parentesis==2){
                    while (!codigo.get(i).equals(")")){
                        if (!codigo.get(i).equals("(")) {
                            parametros.add(codigo.get(i));
                            i++;
                        }else {
                            i++;
                        }
                    }
                    parCerrado++;
                }
            }else if (parCerrado==1){
                while (!codigo.get(i).equals(")")){
                    instrucciones.add(codigo.get(i));
                    i++;
                }
            }
        }
        funciones.put(nombre, new Funcion(nombre,parametros,instrucciones));
    }**/

    /**
     * @param values Es la matriz que contiene los elementos dentro del parentesis
     * @return En la primera posicion, el resultado de la operacion aritmetica. En el segundo, el contador interno
     */
    private static double[] evaluarParentesis(String [] values){
        String op = "";
        ArrayList<Double> val = new ArrayList<>();
        double r = 0;
        int contador = 1;
        while (!values [contador].equals(")")) {
            if (esNum(values[contador])){
                val.add(Double.parseDouble(values[contador]));
            }else if (values[contador].equals("+") || values[contador].equals("-") || values[contador].equals("*") || values[contador].equals("/")){
                op = values[contador];
            }else if (values[contador].equals("(")){
                String val0[] = new String[values.length-(contador+1)];
                for (int c  = contador;c<values.length-1;c++){
                    val0[c-(contador)] = values[c];
                }
                double respuesta[] = evaluarParentesis(val0);
                //System.out.println(respuesta[0]);
                val.add(respuesta[0]);
                contador = (int) respuesta[1]+contador;
            }
            contador++;
        }
        r = operacionAritmetica(op, val);
        double res [] = new double [2];
        res[0] =r;
        res [1] = (double) contador;
        return res;
    }

    private static Boolean esNum(String n){
        boolean b;
        try{
            Double.parseDouble(n);
            b = true;
        }catch(Exception e){
            b = false;
        }
        return b;
    }

    private static double operacionAritmetica(String op, ArrayList<Double> num){
        double res = num.get(0);
        switch (op) {
            case "+":
                for (int i = 1; i < num.size(); i++) {
                    res = res + num.get(i);
                }
                break;
            case "-":
                for (int i = 1; i < num.size(); i++) {
                    res = res - num.get(i);
                }
                break;
            case "*":
                for (int i = 1; i < num.size(); i++) {
                    res = res * num.get(i);
                }
                break;
            case "/":
                if (num.get(num.size() - 1) == 0) {
                    //Error - Division entre 0
                    throw new ArithmeticException("Division entre 0");
                } else {
                    for (int i = 1; i<num.size();i++) {
                        res = res / num.get(i);
                    }
                }
                break;
        }
        return res;
    }

    private static ArrayList<String > split (String s){
        ArrayList<String> str = new ArrayList<>();
        s.toUpperCase();
        String[] matriz = s.split("");
        //for (int i = 0 ; i< s.length(); i++)
        for(int i = 0; i<s.length();i++){

            if (matriz[i].equals("'") || matriz[i].equals("(") ||matriz[i].equals(")") ||matriz[i].equals("+") ||matriz[i].equals("-") ||matriz[i].equals("*") ||matriz[i].equals("/")){
                str.add(matriz[i]);
            }else if(!matriz[i].equals(" ")){
                Boolean continuar = true;
                String st = "";
                int contador = 0;
                while (continuar){
                    st += matriz[contador+i];
                    contador++;
                    if (matriz[contador+i].equals("(") ||matriz[contador+i].equals(")") ||matriz[contador+i].equals("+") ||matriz[contador+i].equals("-") ||matriz[contador+i].equals("*") ||matriz[contador+i].equals("/") ||matriz[contador+i].equals(" ")){
                        continuar = false;
                    }
                }
                str.add(st);
                i+=contador-1;
            }

        }
        return str;
    }

    private static String[] convertir (ArrayList<String> vals){
        String st = "";
        for (int a = 0; a<vals.size(); a++){
            st+= vals.get(a)+",";
        }
        String [] values = st.split(",");

        return values;
    }

    public static String evaluarPredicados(String[] matriz, List<String> palabrasClave){
        int i = 1;
        String str = "";
        while(!matriz[i].equals(")")){
            switch (matriz[i].toUpperCase()) {
                case "ATOM":
                    if (!matriz[i+2].equals(")")){
                        if (matriz[i+2].equals("+")||matriz[i+2].equals("-")||matriz[i+2].equals("*")||matriz[i+2].equals("/")){
                            return "T";
                        }else {
                            return "Nil";
                        }
                    }else if (matriz[i+1].equals("(")){
                        if (matriz[i+2].equals("DEFUN")||matriz[i+2].equals("COND")||matriz[i+2].equals("LIST")){
                            return "Nil";
                        }
                    }else {
                        return "T";
                    }
                case "LIST":
                    if (matriz[i+1].equals("(") && matriz[i+2].equals("'")){
                        return "T";
                    }else if(matriz[i+1].equals("'")){
                        return "T";
                    }else{
                        return "nil";
                    }
                case "EQUAL":
                    try{
                        if (matriz[i+1].equals(matriz[i+2])){
                            return "T";
                        }else{
                            return "nil";
                        }
                    }catch(Exception e){
                        return "nil";
                    }
                case "<":
                    try{
                        double n1 = Double.parseDouble(matriz[i+1]);
                        double n2 = Double.parseDouble(matriz[i+2]);
                        if(n1<n2){
                            return "T";
                        }else{
                            return "nil";
                        }
                    }catch(Exception e){
                        return "nil";
                    }
                case ">":
                    try{
                        double n1 = Double.parseDouble(matriz[i+1]);
                        double n2 = Double.parseDouble(matriz[i+2]);
                        if(n1>n2){
                            return "T";
                        }else{
                            return "nil";
                        }
                    }catch(Exception e){
                        return "nil";
                    }
                default:
                    return"Ningun predicado dado";
            }
        }
        return str;
    }

}