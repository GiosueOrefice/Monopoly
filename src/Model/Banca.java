package Model;

import Mediator.ConcreteMediator;
import Mediator.Mediator;
import Mediator.Player;
import Strategy.Prigione;
import Strategy.Proprieta;
/**
 *
 * @version n.n (24-02-2021)
 * @author Giosue' Orefice
 */
public class Banca {
	static Mediator mediator = new ConcreteMediator();
	private static String message;


	public Banca(Player[] players) {
			for(Player player: players) {
				mediator.addBuyer(player);
				player.setMediator(mediator);
			}	
	}

	public static void distribuisciDenaro(Player[] players) {
		int money = 1000;
		for(Player player: players) 
			player.setMoney(money  + (6-players.length)*50);
	}


	/**
		Scopo del metodo: Acquistare una proprieta
		@param player giocatore che vuole acquistare
		@param property proprieta da acquistare
		@return void
	 */
	public static void buyProprieta(Player player, Proprieta property) {
		if(player.getMoney()>= property.getCostoAcquisto())
		{
			property.setPlayer(player);
			player.setProprieta(property);
			player.setMoney(player.getMoney() - property.getCostoAcquisto()); 
			property.setAcquisto(true);
			message = player.getName() + " ha acquistato:" + property.getName() + " per " +
					property.getCostoAcquisto();
			if(!player.isCostruzione())
				player.checkCostruire(property);
		}
		else
			message = "Non puoi acquistare questa proprieta'\n";	
	}

	/**
	Scopo del metodo: Uscire di prigione pagando
	@param player giocatore in prigione
	@param prigione la prigione dove si trova il giocatore
	@return void
	 */
	public static void esciPrigione(Player player, Prigione prigione) {
		if(player.getMoney()>= prigione.getPrezzOut()) {
			player.setPrigione(false);
			message = "SEI USCITO DI PRIGIONE, POTRAI GIOCARE AL PROSSIMO TURNO";
		}
		else 
			message = "Non hai i soldi necessari per uscire di prigione";
	}


	/**
	Scopo del metodo: pagare l'affitto quando si atterra su una proprieta di un altro giocatore
	@param player giocatore che deve pagare
	@param property 
	@return void
	 */
	public static void pagaAffitto(Player player, Proprieta property) {
		player.setMoney(player.getMoney() - property.prezzoDaPagare());
		Player player2 = property.getPlayer();
		player2.setMoney(player2.getMoney() + property.prezzoDaPagare());
		message = "Hai pagato " + property.prezzoDaPagare() + " per l'affito a " + player2.getName();
	}

	/**
	Scopo del metodo: pagare il costo di una casa su una proprieta
	@param property la proprieta dove si vuole costruire
	@return void
	 */
	public static void costruisciCasa(Proprieta property) {
		riscuoti(property.getPlayer(), property.getCostoCasa());
		message = "Hai costruito una casa su " + property.getName();
	}

	/**
	Scopo del metodo: pagare il costo di un albergo su una proprieta
	@param property la proprieta dove si vuole costruire
	@return void
	 */
	public static void costruisciAlbergo(Proprieta property) {
		riscuoti(property.getPlayer(), property.getCostoAlbergo());
		message = "Hai costruito l'albergo su " + property.getName();
	}

	/**
	Scopo del metodo: riscuote da un giocatore del denaro
	@param player
	@param money
	@return void
	 */
	public static void riscuoti(Player player, int money) {
		player.setMoney(player.getMoney() - money);
		message = "La banca ha riscosso " + money + "�";
	}

	/**
	Scopo del metodo: versare a un giocatore del denaro
	@param player
	@param money
	@return void
	 */
	public static void versa(Player player, int money) {
		player.setMoney(player.getMoney() + money);
		message = "Hai ricevuto dalla banca: " + money + "�";
	}



	/**
	Scopo del metodo: effettuare un asta quando un giocatore rifiuta di acuquistare una proprieta
	@param proprieta
	@return void
	 */
	public static void asta(Proprieta proprieta) {
		mediator.eseguiAsta();
		Player winner = (Player)mediator.findHighestBidder();

		proprieta.setPlayer(winner);
		proprieta.setAcquistata(true);
		winner.setProprieta(proprieta);
		if(!winner.isCostruzione())
			winner.checkCostruire(proprieta);
		message = "";
	}


	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		Banca.message = message;
	}



}
