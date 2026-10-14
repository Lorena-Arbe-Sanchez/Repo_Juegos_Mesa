package org.egibide.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Partida {

    private int id;
    // Como una partida puede tener jugadoras socias y/o invitadas, mediante 2 atributos se registran
    private List<Socia> listaJugadorasSocias = new ArrayList<>();
    private List<Invitada> listaJugadorasInvitadas = new ArrayList<>();
    // En phpMyAdmin --> En la tabla Partida, se añade "ON DELETE CASCADE" a la clave ajena del juego si se quiere que, al borrar un juego, se borren también sus partidas y las participaciones asociadas
    private Juego juego;
    private LocalDate fecha;
    private LocalTime hora;
    private int jugadoresDeseados;
    private int plazasLibres;
    // Como hay socias y/o invitadas, así hay la posibilidad de registrar las 2 como ganadoras. Pueden ser "null"
    private List<Socia> ganadorasSocias = new ArrayList<>();
    private List<Invitada> ganadorasInvitadas = new ArrayList<>();

    public Partida() {
    }

    public Partida(int id, List<Socia> listaJugadorasSocias, List<Invitada> listaJugadorasInvitadas, Juego juego,
                   LocalDate fecha, LocalTime hora, int jugadoresDeseados, int plazasLibres,
                   List<Socia> ganadorasSocias, List<Invitada> ganadorasInvitadas) {
        this.id = id;
        this.listaJugadorasSocias = listaJugadorasSocias;
        this.listaJugadorasInvitadas = listaJugadorasInvitadas;
        this.juego = juego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.plazasLibres = plazasLibres;
        this.ganadorasSocias = ganadorasSocias;
        this.ganadorasInvitadas = ganadorasInvitadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Socia> getListaJugadorasSocias() {
        return listaJugadorasSocias;
    }

    public void setListaJugadorasSocias(List<Socia> listaJugadorasSocias) {
        this.listaJugadorasSocias = listaJugadorasSocias;
    }

    public List<Invitada> getListaJugadorasInvitadas() {
        return listaJugadorasInvitadas;
    }

    public void setListaJugadorasInvitadas(List<Invitada> listaJugadorasInvitadas) {
        this.listaJugadorasInvitadas = listaJugadorasInvitadas;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getJugadoresDeseados() {
        return jugadoresDeseados;
    }

    public void setJugadoresDeseados(int jugadoresDeseados) {
        this.jugadoresDeseados = jugadoresDeseados;
    }

    public int getPlazasLibres() {
        return plazasLibres;
    }

    public void setPlazasLibres(int plazasLibres) {
        this.plazasLibres = plazasLibres;
    }

    public List<Socia> getGanadorasSocias() {
        return ganadorasSocias;
    }

    public void setGanadorasSocias(List<Socia> ganadorasSocias) {
        this.ganadorasSocias = ganadorasSocias;
    }

    public List<Invitada> getGanadorasInvitadas() {
        return ganadorasInvitadas;
    }

    public void setGanadorasInvitadas(List<Invitada> ganadorasInvitadas) {
        this.ganadorasInvitadas = ganadorasInvitadas;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", listaJugadorasSocias=" + listaJugadorasSocias +
                ", listaJugadorasInvitadas=" + listaJugadorasInvitadas +
                ", juego=" + juego +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", jugadoresDeseados=" + jugadoresDeseados +
                ", plazasLibres=" + plazasLibres +
                ", ganadorasSocias=" + ganadorasSocias +
                ", ganadorasInvitadas=" + ganadorasInvitadas +
                '}';
    }
}
