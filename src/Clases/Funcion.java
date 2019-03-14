package Clases;

import java.util.ArrayList;

public class Funcion {
    String nombre;//Nombre de la funcion. Es unico
    ArrayList<String> parametros; //Lista con los parametros que recibe la funcion
    ArrayList<String> instrucciones; //Lista con las instrucciones 'en crudo'

    //Constructor
    public Funcion(String nom , ArrayList<String> param, ArrayList<String> inst){
        nombre = nom;
        parametros = param;
        instrucciones = inst;
    }

    public Funcion(){
        nombre = "";
        parametros = new ArrayList<>();
        instrucciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void addParam (String param){
        parametros.add(param);
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
}
