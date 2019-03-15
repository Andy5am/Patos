package Clases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Funcion {
    String nombre;//Nombre de la funcion. Es unico
    HashMap <String, String> parametros; //Lista con los parametros que recibe la funcion
    ArrayList<String> instrucciones; //Lista con las instrucciones 'en crudo'

    //Constructor
    public Funcion(String nom , ArrayList<String> param, ArrayList<String> inst){
        nombre = nom;
        instrucciones = inst;
        for (int i = 0; i<param.size();i++){
            parametros.put(param.get(i),"");
        }
    }

    public Funcion(){
        nombre = "";
        parametros = new HashMap();
        instrucciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void addParam (String param){
        parametros.put(param,"");
    }

    public boolean initParam(ArrayList<String> params){
        int contador = 0;
        if (params.size() == parametros.size()) {
            for (String a : parametros.keySet()){
                parametros.put(a,params.get(contador));
                contador++;
            }
            return true;
        }else if(params.size()<parametros.size()){
            System.out.println("ERROR - Falta de argumentos para la funcion '"+nombre+"'");
            return false;
        }else{
            System.out.println("ERROR - Demasiados argumentos para la funcion '"+nombre+"'");
            return false;
        }
    }

    public void replaceParams(){
        for (int i = 0; i<instrucciones.size();i++){
            String a = instrucciones.get(i);
            if  (parametros.keySet().contains(a)){
                instrucciones.set(i,parametros.get(a));
            }

        }
    }

    public void addInst(String i){
        instrucciones.add(i);
    }
    @Override
    public String toString() {
        return "Funcion{" +
                "nombre='" + nombre + '\'' +
                ", parametros=" + parametros +
                ", instrucciones=" + instrucciones +
                '}';
    }

    //TODO
    public String execute () {
        String str = "";
            //Aqui deberia de meter al codigo del main completo
        return str;
    }
}
