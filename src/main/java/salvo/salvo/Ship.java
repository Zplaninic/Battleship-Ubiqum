package salvo.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String shipType;

    @ElementCollection
    @Column(name="shipLocation")
    private List<String> shipLocation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;

    public Ship() {}

    public Ship(String shipType, List<String> shipLocation, GamePlayer gamePlayer) {
        this.shipType = shipType;
        this.shipLocation = shipLocation;
        this.gamePlayer = gamePlayer;
    }

    public Ship(String shipType, List<String> shipLocation) {
        this.shipType = shipType;
        this.shipLocation = shipLocation;
    }
    public Long getId() {
        return id;
    }

    public String getShipType() {
        return shipType;
    }

    public List<String> getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(List<String> shipLocation) {
        this.shipLocation = shipLocation;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
}

