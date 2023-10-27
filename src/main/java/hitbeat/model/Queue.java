package hitbeat.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="queue")
public class Queue extends BaseModel{
    @Id
    @GeneratedValue
    private UUID id;

    private String type;

    private String nome;

    @ManyToMany(cascade={ CascadeType.ALL})
    @JoinTable(
        name="queue_track",
        joinColumns = {@JoinColumn(name="queue_id")},
        inverseJoinColumns = {@JoinColumn(name="track_id")}
    )
    private Set<Track> tracks = new HashSet<>();
}
