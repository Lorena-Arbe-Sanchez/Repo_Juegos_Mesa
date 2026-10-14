package org.egibide.models;

import java.time.LocalDate;

public class Asociacion {

    private int id;
    private String nombre;
    private String razonSocial;
    private LocalDate fechaApertura;
    private String direccion;
    private String telefono;
    private String email;
    private String cif;

    public Asociacion() {
    }

    public Asociacion(int id, String nombre, String razonSocial, LocalDate fechaApertura, String direccion,
                      String telefono, String email, String cif) {
        this.id = id;
        this.nombre = nombre;
        this.razonSocial = razonSocial;
        this.fechaApertura = fechaApertura;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.cif = cif;
    }

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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    @Override
    public String toString() {
        return "Asociacion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", fechaApertura=" + fechaApertura +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", cif='" + cif + '\'' +
                '}';
    }
}
