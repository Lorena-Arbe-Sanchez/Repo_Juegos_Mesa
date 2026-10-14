package org.egibide.models;

import java.util.ArrayList;
import java.util.List;

public class Socia {

    private String documento; // (DNI/NIE/Pasaporte) (se utilizará como ID)
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private int edad;
    private String email;
    private List<Juego> listaJuegos = new ArrayList<>();
    private List<Partida> listaPartidas = new ArrayList<>();

    public Socia() {
    }

    public Socia(String documento, String nombre, String apellido1, String apellido2, String telefono, int edad,
                 String email, List<Juego> listaJuegos, List<Partida> listaPartidas) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telefono = telefono;
        this.edad = edad;
        this.email = email;
        this.listaJuegos = listaJuegos;
        this.listaPartidas = listaPartidas;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Juego> getListaJuegos() {
        return listaJuegos;
    }

    public void setListaJuegos(List<Juego> listaJuegos) {
        this.listaJuegos = listaJuegos;
    }

    public List<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public void setListaPartidas(List<Partida> listaPartidas) {
        this.listaPartidas = listaPartidas;
    }

    @Override
    public String toString() {
        return "Socia{" +
                "documento='" + documento + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", telefono='" + telefono + '\'' +
                ", edad=" + edad +
                ", email='" + email + '\'' +
                ", listaJuegos=" + listaJuegos +
                ", listaPartidas=" + listaPartidas +
                '}';
    }
}
