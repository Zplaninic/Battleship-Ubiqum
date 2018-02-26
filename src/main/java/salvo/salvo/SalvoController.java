package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

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

    @Autowired ShipRepository shipRepository;

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
    public ResponseEntity<Map<String, Object>>  findById(Authentication authentication, @PathVariable Long id) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(id);
        Game game = gamePlayer.getGame();
        if(authentication.getName().equals(gamePlayer.getCompetitor().getUserName())) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("id", gamePlayer.getId());
            dto.put("created", gamePlayer.getDate());
            dto.put("player", makeNewJsonPlayer(gamePlayer.getCompetitor()));
            dto.put("gameplayers", game.getGamePlayers().stream().map(gameplayer -> makeNewJsonGamePlayer(gameplayer)).collect(Collectors.toList()));
            dto.put("ship", gamePlayer.getShips().stream().map(ship -> makeJsonShip(ship)).collect(Collectors.toList()));
            dto.put("salvoes", game.getGamePlayers().stream().map(gameplayer -> makeGamePlayersSalvo(gameplayer)).collect(Collectors.toList()));
            return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(makeMap("Error", "Dont cheat!!"), HttpStatus.UNAUTHORIZED);
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

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String username, @RequestParam String password) {
        List<Player> players = playerRepository.findByUserName(username);

        if (username.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }else if(password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No password"), HttpStatus.FORBIDDEN);
        }else if (!players.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.FORBIDDEN);
        }else {

            playerRepository.save(new Player(username, password));

            return new ResponseEntity<>(makeMap("userName", username), HttpStatus.CREATED);
        }

    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {

        if(authentication.getName().isEmpty()) {
            return new ResponseEntity<>(makeMap("Error", "You are not logged in!"), HttpStatus.UNAUTHORIZED);
        } else {

            Player newPlayer = playerRepository.findByUserName(authentication.getName()).get(0);

            Game newGame = new Game();
            gameRepository.save(newGame);

            GamePlayer newGamePlayer = new GamePlayer(newGame, newPlayer);
            gamePlayerRepository.save(newGamePlayer);

            return new ResponseEntity<>(makeMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @RequestMapping(path = "game/{id}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication, @PathVariable Long id) {
        Player newPlayer = playerRepository.findByUserName(authentication.getName()).get(0);
        Game game = gameRepository.findOne(id);

        if(authentication.getName().isEmpty())  {
            return new ResponseEntity<>(makeMap("Error", "You are not logged in!"), HttpStatus.UNAUTHORIZED);

        }else if(game == null) {
            return new ResponseEntity<>(makeMap("Error", "No such game"), HttpStatus.FORBIDDEN);

        }else if (game.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(makeMap("Error", "Game is full"), HttpStatus.FORBIDDEN);

        }else {
            GamePlayer newGamePlayer = new GamePlayer(game, newPlayer);
            gamePlayerRepository.save(newGamePlayer);

            return new ResponseEntity<>(makeMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createShips(Authentication authentication, @PathVariable Long gamePlayerId, @RequestBody List<Ship> ships) {
        Player currentPlayer = playerRepository.findByUserName(authentication.getName()).get(0);
        GamePlayer gamePlayer = gamePlayerRepository.findOne( gamePlayerId);

        if(authentication.getName().isEmpty()) {
            return new ResponseEntity<>(makeMap("Error", "You are not logged in!"), HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer == null) {
            return new ResponseEntity<>(makeMap("Error", "This gameplayer doesn't exist!"), HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer.getCompetitor().getId() != currentPlayer.getId()) {
            return new ResponseEntity<>(makeMap("Error", "You are not the same gameplayer!"), HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("Error", "You already have a ships!"), HttpStatus.FORBIDDEN);

        }else {
            for(Ship ship : ships) {
                Ship newShip = new Ship(ship.getShipType(), ship.getShipLocation());
                newShip.setGamePlayer(gamePlayer);
                shipRepository.save(newShip);
            }
            return new ResponseEntity<>(makeMap("ships", gamePlayer.getShips() ), HttpStatus.CREATED);
        }
    }
}