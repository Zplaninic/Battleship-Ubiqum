package salvo.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication

public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
    public CommandLineRunner initData(PlayerRepository playerRepository,
                                      GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository,
                                      SalvoRepository salvoRepository) {
	    return (args) -> {

			Player jBauer = new Player("j.bauer@ctu.gov");
			Player cObrian = new Player("c.obrian@ctu.gov");
			Player tAlmeida = new Player("t.almeida@ctu.gov");
			Player kBauer = new Player("kim_bauer@gmail.com");

			playerRepository.save(jBauer);
			playerRepository.save(cObrian);
            playerRepository.save(kBauer);
			playerRepository.save(tAlmeida);


	        //Task 2.1 --- Add more hours
			Game firstGame = new Game();
			gameRepository.save(firstGame);

			Game secondGame = new Game();
			Date secondDate = Date.from(firstGame.getDate().toInstant().plusSeconds(3600));
			secondGame.setDate(secondDate);
			gameRepository.save(secondGame);

			Game thirdGame = new Game();
			Date thirdDate = Date.from(firstGame.getDate().toInstant().plusSeconds(7200));
			thirdGame.setDate(thirdDate);
			gameRepository.save(thirdGame);

			Game forthGame = new Game();
			Date forthDate = Date.from(firstGame.getDate().toInstant().plusSeconds(10800));
			forthGame.setDate(forthDate);
			gameRepository.save(forthGame);

			Game fifthGame = new Game();
			Date fifthDate = Date.from(firstGame.getDate().toInstant().plusSeconds(14400));
			fifthGame.setDate(fifthDate);
			gameRepository.save(fifthGame);

			Game sixtGame = new Game();
			Date sixtDate = Date.from(firstGame.getDate().toInstant().plusSeconds(18000));
			sixtGame.setDate(sixtDate);
			gameRepository.save(sixtGame);

			Game seventhGame = new Game();
			Date seventhDate = Date.from(firstGame.getDate().toInstant().plusSeconds(22400));
			seventhGame.setDate(seventhDate);
			gameRepository.save(seventhGame);

			Game eightGame = new Game();
			Date eightDate = Date.from(firstGame.getDate().toInstant().plusSeconds(26000));
			eightGame.setDate(eightDate);
			gameRepository.save(eightGame);

			//Task 2.2 ---Create a GamePlayer RestRepository
			GamePlayer firstMatch1 = new GamePlayer(firstGame, jBauer);
			gamePlayerRepository.save(firstMatch1);
			GamePlayer firstMatch2= new GamePlayer(firstGame, cObrian);
			gamePlayerRepository.save(firstMatch2);

			GamePlayer secondMatch1 = new GamePlayer(secondGame, jBauer);
			gamePlayerRepository.save(secondMatch1);
			GamePlayer secondMatch2 = new GamePlayer(secondGame, cObrian);
			gamePlayerRepository.save(secondMatch2);

			GamePlayer thirdMatch1 = new GamePlayer(thirdGame, cObrian);
			gamePlayerRepository.save(thirdMatch1);
			GamePlayer thirdMatch2 = new GamePlayer(thirdGame, tAlmeida);
			gamePlayerRepository.save(thirdMatch2);

			GamePlayer forthMatch1 = new GamePlayer(forthGame, cObrian);
			gamePlayerRepository.save(forthMatch1);
			GamePlayer forthMatch2 = new GamePlayer(forthGame, jBauer);
			gamePlayerRepository.save(forthMatch2);

			GamePlayer fifthMatch1 = new GamePlayer(fifthGame, tAlmeida);
			gamePlayerRepository.save(fifthMatch1);
			GamePlayer fifthMatch2 = new GamePlayer(fifthGame, jBauer);
			gamePlayerRepository.save(fifthMatch2);

			GamePlayer sixtMatch1 = new GamePlayer(sixtGame, kBauer);
			gamePlayerRepository.save(sixtMatch1);

			GamePlayer seventhMatch1 = new GamePlayer(seventhGame, tAlmeida);
			gamePlayerRepository.save(seventhMatch1);

			GamePlayer eightMatch1 = new GamePlayer(eightGame, kBauer);
			gamePlayerRepository.save(eightMatch1);
			GamePlayer eightMatch2 = new GamePlayer(eightGame, tAlmeida);
			gamePlayerRepository.save(eightMatch2);

			//Task 3.1. --- Create a ship
			Ship Destroyer11 = new Ship("Destroyer", Arrays.asList("H2", "H3", "H4"), firstMatch1);
			shipRepository.save(Destroyer11);
			Ship Submarine11 = new Ship("Submarine", Arrays.asList("E1", "F1", "G1"), firstMatch1);
			shipRepository.save(Submarine11);
			Ship PatrolBoat11 = new Ship("Patrol Boat", Arrays.asList("B4", "B5"), firstMatch1);
			shipRepository.save(PatrolBoat11);
			Ship Destroyer21 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), firstMatch2);
			shipRepository.save(Destroyer21);
			Ship PatrolBoat21 = new Ship("Patrol Boat", Arrays.asList("F1", "F2"), firstMatch2);
			shipRepository.save(PatrolBoat21);

			Ship Destroyer12 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), secondMatch1);
			shipRepository.save(Destroyer12);
			Ship PatrolBoat12 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), secondMatch1);
			shipRepository.save(PatrolBoat12);
			Ship Submarine12 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), secondMatch2);
			shipRepository.save(Submarine12);
			Ship PatrolBoat22 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"), secondMatch2);
			shipRepository.save(PatrolBoat22);

			Ship Destroyer31 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), thirdMatch1);
			shipRepository.save(Destroyer31);
			Ship PatrolBoat31 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), thirdMatch1);
			shipRepository.save(PatrolBoat31);
			Ship Submarine31 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), thirdMatch2);
			shipRepository.save(Submarine31);
			Ship PatrolBoat32 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"), thirdMatch2);
			shipRepository.save(PatrolBoat32);

			Ship Destroyer41 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), forthMatch1);
			shipRepository.save(Destroyer41);
			Ship PatrolBoat41 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), forthMatch1);
			shipRepository.save(PatrolBoat41);
			Ship Submarine41 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), forthMatch2);
			shipRepository.save(Submarine41);
			Ship PatrolBoat42 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"), forthMatch2);
			shipRepository.save(PatrolBoat42);

			Ship Destroyer51 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), fifthMatch1);
			shipRepository.save(Destroyer51);
			Ship PatrolBoat51 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), fifthMatch1);
			shipRepository.save(PatrolBoat51);
			Ship Submarine51 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), fifthMatch2);
			shipRepository.save(Submarine51);
			Ship PatrolBoat52 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"), fifthMatch2);
			shipRepository.save(PatrolBoat52);

			Ship Destroyer61 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), sixtMatch1);
			shipRepository.save(Destroyer61);
			Ship PatrolBoat61 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), sixtMatch1);
			shipRepository.save(PatrolBoat61);

			Ship Destroyer81 = new Ship("Destroyer", Arrays.asList("B5", "C5", "D5"), eightMatch1);
			shipRepository.save(Destroyer81);
			Ship PatrolBoat81 = new Ship("Patrol Boat", Arrays.asList("C6", "C7"), eightMatch1);
			shipRepository.save(PatrolBoat81);
			Ship Submarine81 = new Ship("Submarine", Arrays.asList("A2", "A3", "A4"), eightMatch2);
			shipRepository.save(Submarine81);
			Ship PatrolBoat82 = new Ship("Patrol Boat", Arrays.asList("G6", "H6"), eightMatch2);
			shipRepository.save(PatrolBoat82);

//			--Task 4.1 Make Salvos
//            salvo111 = first game, first player, first turn
            Salvo salvo111 = new Salvo(1, Arrays.asList("B5", "C5" , "F1"), firstMatch1);
            salvoRepository.save(salvo111);
            Salvo salvo121 = new Salvo(1, Arrays.asList("B4", "B5" , "B6"), firstMatch2);
            salvoRepository.save(salvo121);
            Salvo salvo112 = new Salvo(2, Arrays.asList("F2", "D5"), firstMatch1);
            salvoRepository.save(salvo112);
            Salvo salvo122 = new Salvo(2, Arrays.asList("E1", "H3" , "A2"), firstMatch2);
            salvoRepository.save(salvo122);

            Salvo salvo211 = new Salvo(1, Arrays.asList("A2", "A4" , "G6"), secondMatch1);
            salvoRepository.save(salvo211);
            Salvo salvo221 = new Salvo(1, Arrays.asList("B5", "D5" , "C7"), secondMatch2);
            salvoRepository.save(salvo221);
            Salvo salvo212 = new Salvo(2, Arrays.asList("A3", "H6"), secondMatch1);
            salvoRepository.save(salvo212);
            Salvo salvo222 = new Salvo(2, Arrays.asList("C5", "C6"), secondMatch2);
            salvoRepository.save(salvo222);

            Salvo salvo311 = new Salvo(1, Arrays.asList("G6", "H6" , "A4"), thirdMatch1);
            salvoRepository.save(salvo311);
            Salvo salvo321 = new Salvo(1, Arrays.asList("H1", "H2" , "H3"), thirdMatch2);
            salvoRepository.save(salvo321);
            Salvo salvo312 = new Salvo(2, Arrays.asList("A2", "A3" , "D8"), thirdMatch1);
            salvoRepository.save(salvo312);
            Salvo salvo322 = new Salvo(2, Arrays.asList("E1", "F2" , "G3"), thirdMatch2);
            salvoRepository.save(salvo322);




        };

    }

}
