
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

        HashMap funciones = new HashMap();/**
        String operador = "";
        String linea = "(DEFUN hola(x y)(+ x y))";
        String simple = "(- 5 3)";
        String [] v = linea.split("");
        System.out.println(v[6]);

        /*for (int i = 0; i<v.length;i++){
            System.out.print(v[i]+",");
        }
        try{
            System.out.println("La respuesta es: "+ evaluarParentesis(convertir(split(linea)))[0]);
        }catch(ArithmeticException e){
            System.out.println("Hay una division entre 0... ERROR MATEMATICO");
        }
        //Suponemos que todos los numeros son menores que 10
        String[] values = linea.split("");
        System.out.println(split(linea));
        definirFuncion(split(linea),funciones);
        System.out.println(funciones);
        ArrayList car = split(linea);
        if (car.get(3).equals("hola")){
            System.out.println("True");
        }else {
            System.out.println("false");
        }**/

        String prueba = "(aToM '(+ 5 1))";
        ArrayList prueba0 =split(prueba);
        String[] mero = convertir(prueba0);
        System.out.println(prueba);
        for (int i = 0;i<mero.length;i++){
            System.out.print(mero[i]+",");
        }
        System.out.println(evaluarPredicados(mero,palabrasClave));
    }

    public static void definirFuncion (ArrayList<String> codigo, HashMap funciones){
        int parentesis = 0;
        int parCerrado = 0;
        String nombre = "";
        ArrayList<String > parametros = new ArrayList();
        ArrayList<String> instrucciones = new ArrayList();
        for (int i = 0; i < codigo.size(); i++){
            if (codigo.get(i).equals("DEFUN")){
                nombre = codigo.get(i+1);
                 //String[] name = nombre.split(" ");
                //nombre= name[1];
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
    }

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

    private static List<String> arrayToList(String [] a){
        List<String> l = new ArrayList<String>();
        for (int i = 0; i< a.length; i++){
            l.add(a[i]);
        }
        return l;
    }

    private static ArrayList<String > split (String s){
        ArrayList<String> str = new ArrayList<>();
        s.toUpperCase();
        String[] matriz = s.split("");
        //for (int i = 0 ; i< s.length(); i++)
        for(int i = 0; i<s.length();i++){

            if (matriz[i].equals("(") ||matriz[i].equals(")") ||matriz[i].equals("+") ||matriz[i].equals("-") ||matriz[i].equals("*") ||matriz[i].equals("/")){
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

    public static boolean estaEnLista(String a, List<String> lista){
        for (int i = 0;i<lista.size();i++){
            if (lista.get(i).equals(a)){
                return true;
            }
        }
        return false;
    }

    public static String evaluarPredicados(String[] matriz, List<String> palabrasClave){
        int i = 1;
        String str = "";
        while(!matriz[i].equals(")")){
            switch (matriz[i].toUpperCase()) {
                case "ATOM":
                    if (matriz[i+1].equals("(")){
                        return "nil";
                    }else if(matriz[i+1].toUpperCase().equals("NIL")){
                        return "T";
                    }else if (matriz[i+1].equals("'")){
                        return "nil";
                    }else if(matriz[i+1].equals("(") && estaEnLista(matriz[i+2], palabrasClave) && !matriz[i+2].equals("DEFUN") && !matriz[i+2].equals("COND")){
                        return "T";
                    }else {
                        return "nil";
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