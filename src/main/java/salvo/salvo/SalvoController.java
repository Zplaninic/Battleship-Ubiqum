package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    public SalvoController() {
    }

    @Autowired
    private GameRepository gameRepository;

    @Autowired GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeNewJsonGame(game))
                .collect(Collectors.toList());
    }

//    Take just IDs at /api/games
//    public List<Long> getId() {
//        return repository.findAll().stream().map(Game -> Game.getId()).collect(Collectors.toList());
//        //                                       Game::getId()
//    }

    private Map<String, Object> makeNewJsonGame(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gameplayers", game.getGamePlayers().stream().map(gamePlayer ->  makeNewJsonGamePlayer(gamePlayer)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeNewJsonGamePlayer(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player",makeNewJsonPlayer(gamePlayer.getCompetitor()));
        return dto;
    }

    private Map<String, Object> makeNewJsonPlayer(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("playerId", player.getId());
        dto.put("playerName", player.getUserName());
        return dto;
    }

    @RequestMapping("/game_view/{id}")
    public Map<String, Object>  findById(@PathVariable Long id) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepository.findOne(id);
        Game game = gamePlayer.getGame();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getDate());
        dto.put("player", gamePlayer.getCompetitor());
        dto.put("gameplayers", game.getGamePlayers().stream().map(gameplayer -> makeNewJsonGamePlayer(gameplayer)).collect(Collectors.toList()));
        dto.put("ship", gamePlayer.getShips().stream().map(ship -> makeJsonShip(ship)).collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getSalvos().stream().map(salvo -> makeJsonSalvo(salvo)).collect(Collectors.toList()));
        return dto;
    }
//

    private Map<String, Object> makeJsonShip(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getShipType());
        dto.put("locations", ship.getShipLocation());
        return dto;
    }

    private Map<String, Object> makeJsonSalvo(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getSalvoLocation());
        return dto;
    }


}