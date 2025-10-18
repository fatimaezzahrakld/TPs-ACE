package entities;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "salles")
public class Salle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    
    @OneToMany(mappedBy = "salle", fetch = FetchType.EAGER)
    private List<Machine> machines;

    // ✅ Constructeur vide (obligatoire pour JPA)
    public Salle() {}

    // ✅ Constructeur avec code (pour ton code de test)
    public Salle(String code) {
        this.code = code;
    }

    // (facultatif) Constructeur complet
    public Salle(int id, String code, List<Machine> machines) {
        this.id = id;
        this.code = code;
        this.machines = machines;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<Machine> getMachines() { return machines; }
    public void setMachines(List<Machine> machines) { this.machines = machines; }
}
