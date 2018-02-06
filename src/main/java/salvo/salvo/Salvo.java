package salvo.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer turn;

    @ElementCollection
    @Column(name = "salvoLocation")
    private List<String> salvoLocation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameplayer_id")
    private GamePlayer gamePlayer;

    public Salvo() {}

    public Salvo(Integer turn, List<String> salvoLocation, GamePlayer gamePlayer) {
        this.turn = turn;
        this.salvoLocation = salvoLocation;
        this.gamePlayer = gamePlayer;
    }

    public Long getId() {
        return id;
    }

    public Integer getTurn() {
        return turn;
    }

    public List<String> getSalvoLocation() {
        return salvoLocation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public void setSalvoLocation(List<String> salvoLocation) {
        this.salvoLocation = salvoLocation;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
