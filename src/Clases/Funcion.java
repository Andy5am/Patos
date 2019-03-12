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

    public String getNombre() {
        return nombre;
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
