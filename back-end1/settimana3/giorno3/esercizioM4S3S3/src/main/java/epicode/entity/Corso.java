package epicode.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="corsi")
public class Corso {
    @Id
    @GeneratedValue
    private int id;
    private String nome;
    @ManyToMany(mappedBy = "corsi")
    private List<Studente> studenti;
    public Corso() {
    }
    public Corso(String nome) {
        this.nome = nome;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public List<Studente> getStudenti() {
        return studenti;
    }
    public void setStudenti(List<Studente> studenti) {
        this.studenti = studenti;
    }
    @Override
    public String toString() {
        return "Corso{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
