package it.epidocode.lezioneU5s2s2.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Studente {
    private int matricola;
    private static int cont;
    private String nome, cognome;
    private LocalDate dataNascita;

    public Studente(String nome, String cognome, LocalDate dataNascita) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        matricola = cont++;
    }
}
