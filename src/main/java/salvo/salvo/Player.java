package salvo.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String userName;

    @OneToMany(mappedBy="competitor", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public Player() {}

    public Long getId() {
        return id;
    }

    public Player(String email) {
        userName = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    @JsonIgnore
//    public List<Game> getGames() {
//        return gamePlayers.stream().map(gp -> gp.getGame()).collect(Collectors.toList());
//    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setCompetitor(this);
        gamePlayers.add(gamePlayer);
    }

    public String toString() {
        return "User: " + userName;
    }
}
