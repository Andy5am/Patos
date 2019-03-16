import Clases.Funcion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author Marco Fuentes 18188, Andy Castillo 18040, Cristina Bautista 161260
 * @version 15/03/2019
 * Clase del main, se encuentran varios metodos en esta clase tambien
 */
public class Main {

    public static void main (String [] args){

        String archivo = "prueba.txt";
        String cadena;
        String code = "";
        //para leer el archivo
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                code = code + cadena;
            }
            b.close();

            //arraylist con todas las palabras importantes, para distinguirlas
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

    /**
     * @param codigo es el arraylist de lineas de codigo
     * @return Un arraylist donde las instrucciones estan separadas
     */
    public static ArrayList<ArrayList<String>> splitInst (ArrayList<String> codigo){
        //inst referencia a las instrucciones
        ArrayList<String> inst = new ArrayList<>();
        //referencia a temporal
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        int parentesis = 0;
        String str = "";
        //para recorrer toda la parte del string donde empieza y termina
        for (int i = 0; i<codigo.size(); i++){
            if (codigo.get(i).equals("(")){
                parentesis++;
                str+= codigo.get(i);
            }else if (codigo.get(i).equals(")")){
                parentesis--;
                if (parentesis==0){
                    str+= codigo.get(i);
                    //se agrega instruccion al arraylist
                    inst.add(str);
                    str ="";
                }else{
                    str+=codigo.get(i);
                }
                //espacios
            }else {
                if (!codigo.get(i+1).equals("(")||!codigo.get(i+1).equals(")")||!codigo.get(i+1).equals("+")||!codigo.get(i+1).equals("-")||!codigo.get(i+1).equals("*")||!codigo.get(i+1).equals("/")){
                    str += " ";
                }
                str+= codigo.get(i);
            }
        }
        //ya no habra espacios " " en el arraylist
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
     * Recibe un arraylist de un arraylist de strings
     */
    public static void executeFun(ArrayList<ArrayList<String>> operaciones, HashMap<String, Funcion> funciones){
        //diferenciar entre operadores y predicados
        ArrayList<String> operadoresA = new ArrayList<>();
        operadoresA.add("+");
        operadoresA.add("-");
        operadoresA.add("*");
        operadoresA.add("/");

        ArrayList<String> predicados = new ArrayList<>();
        predicados.add("ATOM");
        predicados.add("LIST");
        predicados.add("EQUAL");

        //si encuentra un defun entonces usa el metodo y hace lo correspondiente al metodo
        for (int i = 0; i<operaciones.size(); i++){
            if (operaciones.get(i).get(1).toUpperCase().equals("DEFUN")){
                defun(operaciones.get(i), funciones);
            }
        }


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
                System.out.println(condicion(operaciones.get(i),funciones));
            }
            //Busqueda de predicados
            else if(predicados.contains(operaciones.get(i).get(1).toUpperCase())){
                System.out.println(evaluarPredicados(convertir(operaciones.get(i))));
                //TODO no esta del todo completo, creo que no es del todo recursivo
            }
            //Busqueda de llamadas de funciones
            else if (funciones.keySet().contains(operaciones.get(i).get(1))){
                Funcion f = funciones.get(operaciones.get(i).get(1));
                ArrayList<String> params = new ArrayList<>();
                int j = 3;
                while (!operaciones.get(i).get(j).equals(")")){
                    params.add(operaciones.get(i).get(j));
                    j++;
                }
                if (f.initParam(params)){
                    f.replaceParams();
                    System.out.println(executeFunSingle(f.getInst(),funciones));
                }

            }else{
                //Si ninguna es True, no pasa nada
            }
        }
    }

    /**
     * @param operaciones es una arraylist de Strings
     * @param funciones es el hashmap de funciones del programa
     * @return la respuesta
     * En cambio este recibe solo un arraylist de strings y retorna la respuesta
     */
    public static String executeFunSingle(ArrayList<String> operaciones, HashMap<String, Funcion> funciones){
        //se vuelven a diferenciar entre operadores y predicados
        ArrayList<String> operadoresA = new ArrayList<>();
        String res = "";
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


        for (int i = 0; i<operaciones.size();i++){
            //Busca de operadores
            if (operadoresA.contains(operaciones.get(i))){
                String [] prueba = convertir(operaciones);
                if (operaciones.size()>0){
                    res = Double.toString(evaluarParentesis(prueba, funciones)[0]);
                }
            }
            //Busqueda de condicionales
            else if(operaciones.get(i).toUpperCase().equals("COND")){
                res = condicion(operaciones,funciones);
            }
            //Busqueda de predicados
            else if(predicados.contains(operaciones.get(i).toUpperCase())){
                res = (evaluarPredicados(convertir(operaciones)));
                //TODO no esta del todo completo, creo que no es del todo recursivo
            }
            //Busqueda de llamadas de funciones
            else if (funciones.keySet().contains(operaciones.get(i))){
                Funcion f = funciones.get(operaciones.get(i));
                ArrayList<String> params = new ArrayList<>();
                int j = 3;
                while (!operaciones.get(j).equals(")")){
                    params.add(operaciones.get(j));
                    j++;
                }
                if (f.initParam(params)){
                    f.replaceParams();
                    res = executeFunSingle(f.getInst(),funciones);
                }

            }else{
                //Si ninguna es True, no pasa nada
            }
        }
        return res;
    }

    /**
     * @param lista es un arraylist de string
     * @param funciones es el hashmap de las funciones
     * Metodo para que se interprete las definiciones de funciones de lisp
     */
    //Recibe de parametro un arraylist desde el (DEFUN...)
    public static void defun (ArrayList<String> lista, HashMap<String, Funcion> funciones){
        int parentesis = 0;
        int c = 0;
        //se instancia la clase funcion
        Funcion f = new Funcion();
        while (!lista.get(c).equals(")")){
            if (lista.get(c).equals("(")){
                parentesis++;
                //se agrega el nombre de la funcion
            }else if (parentesis == 1 && !lista.get(c).toUpperCase().equals("DEFUN") && !lista.get(c).toUpperCase().equals(")")){
                f.setNombre(lista.get(c));
            }else if(parentesis == 2 && !lista.get(c).equals(")")){
                //se van agregando los parametros
                while (!lista.get(c).equals(")")){
                    f.addParam(lista.get(c));
                    c++;
                }
                //agrega instrucciones
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
        //agrega al hash map
        funciones.put(f.getNombre(), f);
    }

    /**
     * @param values Es la matriz que contiene los elementos dentro del parentesis
     * @return En la primera posicion, el resultado de la operacion aritmetica. En el segundo, el contador interno
     * Este metodo las operaciones aritmeticas guarda los operandos y operandores por parentesis
     */
    public static double[] evaluarParentesis(String [] values, HashMap<String, Funcion> funciones){
        //del hashmap
        Set set = funciones.keySet();
        String op = "";
        //valores
        ArrayList<Double> val = new ArrayList<>();
        double r = 0;
        int contador = 1;
        //mientras no termine un parentesis
        while (!values [contador].equals(")")) {
            if (esNum(values[contador])){
                val.add(Double.parseDouble(values[contador]));
                //los operandos
            }else if (values[contador].equals("+") || values[contador].equals("-") || values[contador].equals("*") || values[contador].equals("/")){
                op = values[contador];
                //cuando empieza un parentesis
            }else if (values[contador].equals("(")){
                //matriz nueva
                String val0[] = new String[values.length-(contador+1)];
                for (int c  = contador;c<values.length-1;c++){
                    val0[c-(contador)] = values[c];
                }
                double respuesta[] = evaluarParentesis(val0, funciones);
                val.add(respuesta[0]);
                contador = (int) respuesta[1]+contador;

            }else if (set.contains(values[contador])) {
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
        return res;
    }

    /**
     * @param n String
     * @return Retorna un booleano de true o false que se utiliza en evaluarParentesis
     * Para saber si donde estan los operadores que se utilizan en el metodo de evaluarParentesis
     */
    public static Boolean esNum(String n){
        boolean b;
        try{
            Double.parseDouble(n);
            b = true;
        }catch(Exception e){
            b = false;
        }
        return b;
    }

    /**
     * @param op es el operador
     * @param num es el numero
     * @return el valor de la operacion
     * Esta hace las operaciones que estan puestas en formato lisp y retorna el resultado de esa
     * operacion aritmetica
     */
    public static double operacionAritmetica(String op, ArrayList<Double> num){
        //tipo double mas precision que un float
        double res = num.get(0);
        //para los 4 operadores que se pueden usar
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
            //precaucion con la division entre cero
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
        //retorna el double, resultado de la operacion
        return res;
    }

    /**
     * @param s es un string
     * @return un arraylist de strings
     * Este metodo es para quitar los espacios
     */
    public static ArrayList<String> split (String s){
        //nueva arraylist
        ArrayList<String> str = new ArrayList<>();
        //para que siempre este en mayuscula
        s=s.toUpperCase();
        //matriz de strings
        String[] matriz = s.split("");
        //for (int i = 0 ; i< s.length(); i++)
        for(int i = 0; i<s.length();i++){

            if (matriz[i].equals("'") || matriz[i].equals("(") ||matriz[i].equals(")") ||matriz[i].equals("+") ||matriz[i].equals("-") ||matriz[i].equals("*") ||matriz[i].equals("/")){
                //se agrega al arraylist de strings
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
                //al arraylist se le agrega un String
                str.add(st);
                i+=contador-1;
            }

        }
        //el arraylist de strings
        return str;
    }

    /**
     * @param vals un arraylist de strings
     * @return una matriz de strings
     * Se utiliza en executeFun, executeFunSingle y evaluarParentesis
     */
    public static String[] convertir (ArrayList<String> vals){
        String st = "";
        for (int a = 0; a<vals.size(); a++){
            st+= vals.get(a)+",";
        }
        //en la comilla se hace split
        String [] values = st.split(",");
        //matriz de strings
        return values;
    }

    /**
     * @param matriz es una matriz de String
     * @return un t o nil. t si es un predicado y nil si no es un predicado
     */
    public static String evaluarPredicados(String[] matriz){
        //TODO recursividad
        ArrayList<String> operadoresA = new ArrayList<>();
        operadoresA.add("+");
        operadoresA.add("-");
        operadoresA.add("*");
        operadoresA.add("/");
        int i = 1;
        String str = "";
        //para que este toda esta parte en mayusculas
        //cada uno de los case es cada una de las formas de evaluar si es predicado en lisp
        switch (matriz[i].toUpperCase()) {
            //forma de evaluar atom
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
                //forma de evaluar list
            case "LIST":
                if (matriz[i+1].equals("(") && matriz[i+2].equals("'")){
                    return "T";
                }else if(matriz[i+1].equals("'")){
                    return "T";
                }else{
                    return "nil";
                }
                //forma de evaluar equal
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
                //forma de evaluar menor que
            case "<":
                ArrayList<Double> numeros = new ArrayList<Double>();
                //Tiene que soportar que le metan funciones, operaciones o numeros
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

    /**
     * @param inst es un arraylist de instrucciones
     * @param funciones hashmap de las funciones del programa
     * @return Un string
     * Evalua las condiciones en las instrucciones de lisp
     */
    public static String condicion (ArrayList<String> inst,HashMap<String,Funcion> funciones){
        int contador = 0;
        //arraylist de condiciones
        ArrayList<String > condiciones = new ArrayList<>();
        //arraylist de acciones
        ArrayList<String> acciones = new ArrayList<>();
        String cond = "";
        String accion = "";
        Boolean condicion = false;
        Boolean act = false;
        for (int i = 0;i<inst.size();i++){
            if (inst.get(i).equals("(")){
                if (inst.get(i+1).equals("(")){
                    condicion = true;
                    contador = i+1;
                    //anadir la condicion
                    while (condicion){

                        if (!inst.get(contador).equals(")") && !inst.get(contador+1).equals("(")){
                            cond+= inst.get(contador)+",";
                            contador++;
                        }else {
                            cond+= inst.get(contador);
                            condiciones.add(cond);
                            cond= "";
                            condicion= false;
                            i = contador;
                        }
                    }
                }
            }
            //evaluar opciones
            if (i+1!= inst.size()) {
                if (inst.get(i).equals(")")) {
                    if (inst.get(i + 1).equals("(")) {
                        act = true;
                        contador = i + 1;
                        while (act) {
                            if (inst.get(contador).equals(")") && inst.get(contador + 1).equals(")")) {
                                accion += inst.get(contador + 1)+",";
                                acciones.add(accion);
                                accion ="";
                                act = false;
                                i = contador +1;

                            } else {
                                accion += inst.get(contador)+",";
                                contador++;
                            }
                        }
                    }
                }
            }
        }
            //ejecutar cada condicion
        for (int a = 0; a< condiciones.size(); a++){
         String eval = "";
            ArrayList codi = new ArrayList();
            String[] matriz = condiciones.get(a).split(",");
            if (evaluarPredicados(matriz).equals("T")){
                String[] mat = acciones.get(a).split(",");
                ArrayList action = new ArrayList();
                for ( int b = 0; b<mat.length; b++){
                    action.add(mat[b]);
                }
                eval = executeFunSingle(action,funciones);
                return eval;
            }
         }
        return "No se cumplio ninguna condicion";

    }
}