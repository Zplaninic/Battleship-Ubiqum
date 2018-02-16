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
    private String password;


    @OneToMany(mappedBy="competitor", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch = FetchType.EAGER)
    Set<Score> scores;

    public Player() {}

    public Long getId() {
        return id;
    }

    public Player(String email, String password) {
        this.userName = email;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    @JsonIgnore
//    public List<Game> getGames() {
//        return gamePlayers.stream().map(gp -> gp.getGame()).collect(Collectors.toList());
//    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setCompetitor(this);
        gamePlayers.add(gamePlayer);
    }

    public void setScore(Score score) {
        score.setPlayer(this);
        scores.add(score);
    }

    public Set<Score> getScores() {
        return scores;
    }

    public String toString() {
        return "User: " + userName;
    }

//    tu je problem
    public Score getScore(Game game) {
        return scores.stream().filter(score -> score.getGame().getId()==game.getId()).findFirst().orElse(null);
    }
}
