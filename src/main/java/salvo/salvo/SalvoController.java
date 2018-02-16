package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    public SalvoController() {
    }

    @Autowired
    private GameRepository gameRepository;

    @Autowired GamePlayerRepository gamePlayerRepository;

    @Autowired PlayerRepository playerRepository;

    @RequestMapping("/games")
    public Map<String, Object> returnGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if(!isGuest(authentication)) {
            dto.put("player", makeNewJsonPlayer(playerRepository.findByUserName(authentication.getName()).get(0)));
        }
        dto.put("games", getAllGames());
        return dto;
    }
    private List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeNewJsonGame(game))
                .collect(Collectors.toList());

    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


//    Take just IDs at /api/games
//    public List<Long> getId()
//        return repository.findAll().stream().map(Game -> Game.getId()).collect(Collectors.toList());
//        //                                       Game::getId()
//    }


    private Map<String, Object> makeNewJsonGame(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gameplayers", game.getGamePlayers().stream().map(gamePlayer ->  makeJsonGamePlayerScore(gamePlayer)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeNewJsonGamePlayer(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player",makeNewJsonPlayer(gamePlayer.getCompetitor()));
        return dto;
    }

    private Map<String, Object> makeJsonGamePlayerScore(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player",makeNewJsonPlayer(gamePlayer.getCompetitor()));
        Score score = gamePlayer.getScore();
        dto.put("score", score == null ? "No score" : score.getScore());
        return dto;
    }

    private Map<String, Object> makeNewJsonPlayer(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("playerId", player.getId());
        dto.put("playerName", player.getUserName());
//        dto.put("scores", player.getScores().stream().map(score -> makeJsonScore(score)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeJsonShip(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getShipType());
        dto.put("locations", ship.getShipLocation());
        return dto;
    }

    @RequestMapping("/game_view/{id}")
    public Map<String, Object>  findById(@PathVariable Long id) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepository.findOne(id);
        Game game = gamePlayer.getGame();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getDate());
        dto.put("player", makeNewJsonPlayer(gamePlayer.getCompetitor()));
        dto.put("gameplayers", game.getGamePlayers().stream().map(gameplayer -> makeNewJsonGamePlayer(gameplayer)).collect(Collectors.toList()));
        dto.put("ship", gamePlayer.getShips().stream().map(ship -> makeJsonShip(ship)).collect(Collectors.toList()));
        dto.put("salvoes", game.getGamePlayers().stream().map(gameplayer ->  makeGamePlayersSalvo(gameplayer)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeJsonSalvo(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id", salvo.getGamePlayer().getId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getSalvoLocation());
        return dto;
    }

    private List<Object> makeGamePlayersSalvo(GamePlayer gamePlayer) {
        return gamePlayer.getSalvos().stream().map(salvo -> makeJsonSalvo(salvo)).collect(Collectors.toList());
    }



//    private List<Player> makeAuthenticationPlayer (Authentication authentication) {
//        return playerRepository.findByUserName(authentication.getName());
//    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String username, @RequestParam String password) {
        List<Player> players = playerRepository.findByUserName(username);

        if (username.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }

        if (!players.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(username, password));

        return new ResponseEntity<>(makeMap("userName", username), HttpStatus.CREATED);

    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        map.put(key, value);
        return map;
    }

}