
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main (String [] args){
        ArrayList<Double> numeros = new ArrayList<>();
        String operador = "";

        String linea = "(+ 5(* 3 2)2(/ 8 1))";
        String simple = "(- 5 3)";
        String [] v = linea.split("");
        for (int i = 0; i<v.length;i++){
            System.out.print(v[i]+",");
        }
        try{
            System.out.println("La respuesta es: "+evaluarParentesis(v)[0]);
        }catch(ArithmeticException e){
            System.out.println("Hay una division entre 0... ERROR MATEMATICO");
        }

        //Suponemos que todos los numeros son menores que 10
        String[] values = linea.split("");

    }

    private static double[] evaluarParentesis(String [] values){
        String op = "";
        ArrayList<Double> val = new ArrayList<>();
        double r = 0;
        int contador = 1;
        while (!values [contador].equals(")")){
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
                System.out.println(respuesta[0]);
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
}
