
import Clases.Funcion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main (String [] args){

        String archivo = "prueba.txt";
        String cadena;
        String code = "";
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                code = code + cadena;
            }
            b.close();

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

            executeFun(splitInst(split(code)), funciones);

        }catch(Exception e){
            System.out.println("No se pudo abrir el archivo :(");
        }
    }

    private static ArrayList<ArrayList<String>> splitInst (ArrayList<String> codigo){
        ArrayList<String> inst = new ArrayList<>();
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        int parentesis = 0;
        String str = "";
        for (int i = 0; i<codigo.size(); i++){
            if (codigo.get(i).equals("(")){
                parentesis++;
                str+= codigo.get(i);
            }else if (codigo.get(i).equals(")")){
                parentesis--;
                if (parentesis==0){
                    str+= codigo.get(i);
                    inst.add(str);
                    str ="";
                }else{
                    str+=codigo.get(i);
                }
            }else {
                if (!codigo.get(i+1).equals("(")||!codigo.get(i+1).equals(")")||!codigo.get(i+1).equals("+")||!codigo.get(i+1).equals("-")||!codigo.get(i+1).equals("*")||!codigo.get(i+1).equals("/")){
                    str += " ";
                }
                str+= codigo.get(i);
            }
        }
        for (int a = 0; a<inst.size(); a++){
            ArrayList<String> ins = split(inst.get(a));
            temp.add(ins);
        }
        return temp;
    }

    /**
     * @param funciones Es el hashmap de Funciones del programa
     * @param operaciones  Es un ArrayList
     * Cada instruccion es del tipo (ALGO ASDKCI HOLA SALU2 (/ 5 9) HAHA)
     */
    public static void executeFun(ArrayList<ArrayList<String>> operaciones, HashMap<String, Funcion> funciones){
        ArrayList<String> operadoresA = new ArrayList<>();
        operadoresA.add("+");
        operadoresA.add("-");
        operadoresA.add("*");
        operadoresA.add("/");

        ArrayList<String> predicados = new ArrayList<>();
        predicados.add("ATOM");
        predicados.add("LIST");
        predicados.add("EQUAL");

        for (int i = 0; i<operaciones.size(); i++){
            if (operaciones.get(i).get(1).toUpperCase().equals("DEFUN")){
                defun(operaciones.get(i), funciones);
            }
        }

        System.out.println(funciones);

        for (int i = 0; i<operaciones.size();i++){
            //Busca de operadores
            if (operadoresA.contains(operaciones.get(i).get(1))){
                String [] prueba = convertir(operaciones.get(i));
                if (operaciones.get(i).size()>0){
                    System.out.println(evaluarParentesis(prueba, funciones)[0]);
                }
            }
            //Busqueda de condicionales
            else if(operaciones.get(i).get(1).toUpperCase().equals("COND")){
                //todo Parte de Andy
            }
            //Busqueda de predicados
            else if(predicados.contains(operaciones.get(i).get(1).toUpperCase())){
                System.out.println(evaluarPredicados(convertir(operaciones.get(i))));
                //TODO no esta del todo completo, creo que no es del todo recursivo
            }
            //Busqueda de llamadas de funciones
            else if (funciones.keySet().contains(operaciones.get(i).get(1))){
                System.out.println("Encontramos una funcion en "+ (i+1));
                Funcion f = funciones.get(operaciones.get(i).get(1));
                ArrayList<String> params = new ArrayList<>();
                int j = 3;
                while (!operaciones.get(i).get(j).equals(")")){
                    params.add(operaciones.get(i).get(j));
                    j++;
                }
                if (f.initParam(params)){
                    f.replaceParams();
                    //System.out.println(evaluarParentesis(convertir(f.getInst()), funciones)[0]);
                    executeFunSingle(f.getInst(),funciones);
                }

            }else{
                //Si ninguna es True, no pasa nada
            }
        }
    }

    public static void executeFunSingle(ArrayList<String> operaciones, HashMap<String, Funcion> funciones){
        ArrayList<String> operadoresA = new ArrayList<>();
        operadoresA.add("+");
        operadoresA.add("-");
        operadoresA.add("*");
        operadoresA.add("/");

        ArrayList<String> predicados = new ArrayList<>();
        predicados.add("ATOM");
        predicados.add("LIST");
        predicados.add("EQUAL");

//        for (int i = 0; i<operaciones.size(); i++){
//            if (operaciones.get(i).get(1).toUpperCase().equals("DEFUN")){
//                defun(operaciones.get(i), funciones);
//            }
//        }

        System.out.println(funciones);

        for (int i = 0; i<operaciones.size();i++){
            //Busca de operadores
            if (operadoresA.contains(operaciones.get(i))){
                String [] prueba = convertir(operaciones);
                if (operaciones.size()>0){
                    System.out.println(evaluarParentesis(prueba, funciones)[0]);
                }
            }
            //Busqueda de condicionales
            else if(operaciones.get(i).toUpperCase().equals("COND")){
                //todo Parte de Andy
            }
            //Busqueda de predicados
            else if(predicados.contains(operaciones.get(i).toUpperCase())){
                System.out.println(evaluarPredicados(convertir(operaciones)));
                //TODO no esta del todo completo, creo que no es del todo recursivo
            }
            //Busqueda de llamadas de funciones
            else if (funciones.keySet().contains(operaciones.get(i))){
                System.out.println("Encontramos una funcion en "+ (i+1));
                Funcion f = funciones.get(operaciones.get(i));
                ArrayList<String> params = new ArrayList<>();
                int j = 3;
                while (!operaciones.get(i).equals(")")){
                    params.add(operaciones.get(i));
                    j++;
                }
                if (f.initParam(params)){
                    f.replaceParams();
                    //System.out.println(evaluarParentesis(convertir(f.getInst()), funciones)[0]);
                    executeFunSingle(f.getInst(),funciones);
                }

            }else{
                //Si ninguna es True, no pasa nada
            }
        }
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

    /**
     * @param values Es la matriz que contiene los elementos dentro del parentesis
     * @return En la primera posicion, el resultado de la operacion aritmetica. En el segundo, el contador interno
     */
    private static double[] evaluarParentesis(String [] values, HashMap<String, Funcion> funciones){
        Set set = funciones.keySet();
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
                double respuesta[] = evaluarParentesis(val0, funciones);
                val.add(respuesta[0]);
                contador = (int) respuesta[1]+contador;

            }else if (set.contains(values[contador])) {
                System.out.println("WENAS");
                Funcion f = funciones.get(values[contador]);
                if (values[contador+1].equals("(")){
                    contador = contador + 2;
                    ArrayList<String> params = new ArrayList<>();
                    while (!values[contador].equals(")")){
                        params.add(values[contador]);
                        contador ++;
                    }
                    if (f.initParam(params)){
                        f.replaceParams();
                        val.add(evaluarParentesis(convertir(f.getInst()), funciones)[0]);
                    }
                }
            }
            contador++;
        }
        r = operacionAritmetica(op, val);
        double res [] = new double [2];
        res [0] =r;
        res [1] = (double) contador;
        //System.out.println(res[0]);
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

    private static ArrayList<String> split (String s){
        ArrayList<String> str = new ArrayList<>();
        s=s.toUpperCase();
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

    public static String evaluarPredicados(String[] matriz){
        //TODO recursividad
        ArrayList<String> operadoresA = new ArrayList<>();
        operadoresA.add("+");
        operadoresA.add("-");
        operadoresA.add("*");
        operadoresA.add("/");
        int i = 1;
        String str = "";
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
                ArrayList<Double> numeros = new ArrayList<Double>();
                try{
                    //Tiene que soportar que le metan funciones, operaciones o numeros
                    if(matriz[i+1].equals("(") && operadoresA.contains(matriz[i+2])){
                        for (int j = i; j<matriz.length;j++){

                        }
                        //numeros.add(evaluarParentesis(Arrays.copyOfRange(matriz,i+2,matriz.length) , funciones));
                    }
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

}