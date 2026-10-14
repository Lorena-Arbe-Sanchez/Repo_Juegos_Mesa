package org.egibide.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Juego {

    private int id;
    private String nombre;
    private List<String> listaAutores = new ArrayList<>();
    private String descripcion;
    private int minJugadores;
    private int maxJugadores;
    private int duracion;
    private String tematica;
    private boolean competitivo; // (competitivo o cooperativo)
    private String ubicacion;
    private String estado; // (disponible, prestado, en reparación)
    // Como un juego puede tener como propietaria a una asociación o a una socia, mediante 2 atributos se registra (1 será "null")
    private Asociacion asociacionPropietaria;
    private Socia sociaPropietaria;
    private LocalDate fechaAlta;

    // Constructor vacío
    public Juego() {
    }

    // TODO : Revisar si crear más constructores en todas las clases (igual es necesario que tengan menos datos al empezar)
    // Constructor con todos los atributos
    public Juego(int id, String nombre, List<String> listaAutores, String descripcion, int minJugadores,
                 int maxJugadores, int duracion, String tematica, boolean competitivo, String ubicacion,
                 String estado, Asociacion asociacionPropietaria, Socia sociaPropietaria, LocalDate fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.listaAutores = listaAutores;
        this.descripcion = descripcion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.tematica = tematica;
        this.competitivo = competitivo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.asociacionPropietaria = asociacionPropietaria;
        this.sociaPropietaria = sociaPropietaria;
        this.fechaAlta = fechaAlta;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getListaAutores() {
        return listaAutores;
    }

    public void setListaAutores(List<String> listaAutores) {
        this.listaAutores = listaAutores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public boolean isCompetitivo() {
        return competitivo;
    }

    public void setCompetitivo(boolean competitivo) {
        this.competitivo = competitivo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Asociacion getAsociacionPropietaria() {
        return asociacionPropietaria;
    }

    public void setAsociacionPropietaria(Asociacion asociacionPropietaria) {
        this.asociacionPropietaria = asociacionPropietaria;
    }

    public Socia getSociaPropietaria() {
        return sociaPropietaria;
    }

    public void setSociaPropietaria(Socia sociaPropietaria) {
        this.sociaPropietaria = sociaPropietaria;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // TODO : Repasar este para ver si muestra bien los datos y si no es recursivo por FK-s
    // Función "toString" para poder mostrar los datos
    @Override
    public String toString() {
        return "Juego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", listaAutores=" + listaAutores +
                ", descripcion='" + descripcion + '\'' +
                ", minJugadores=" + minJugadores +
                ", maxJugadores=" + maxJugadores +
                ", duracion=" + duracion +
                ", tematica='" + tematica + '\'' +
                ", competitivo=" + competitivo +
                ", ubicacion='" + ubicacion + '\'' +
                ", estado='" + estado + '\'' +
                ", asociacionPropietaria=" + asociacionPropietaria +
                ", sociaPropietaria=" + sociaPropietaria +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
}
