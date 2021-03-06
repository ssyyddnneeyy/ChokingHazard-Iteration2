package FestivalMiniGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import Models.JavaPlayer;
import Models.PalaceCard;

public class HoldFestivalModel {
	private ArrayList<JavaFestivalPlayer> players;
	private HashMap<String, Integer> famePointsAwarded;
	private int indexOfCurrentPlayer;
	private PalaceCard festivalCard;
	private int valueOfPalaceCity;
	private int highestBid;
	
	public HoldFestivalModel(ArrayList<JavaFestivalPlayer> festivalPlayers, PalaceCard festCard, int valueOfPalaceCity){
		this.players = festivalPlayers;
		this.indexOfCurrentPlayer = getBeginningPlayerIndex();
		this.festivalCard = festCard;
		this.valueOfPalaceCity = valueOfPalaceCity;
		this.famePointsAwarded = new HashMap<String, Integer>(10);
		initFamePointsHashMap();
		this.highestBid = 0;
	}
	
	public int getCurrentPlayer(){
		return this.indexOfCurrentPlayer;
	}
	
	public int getCurrentPlayerNumOfPalaceCards(){
		return this.players.get(indexOfCurrentPlayer).getNumPalaceCards();
	}
	
	public int getCurrentPlayerBid(){
		return this.players.get(indexOfCurrentPlayer).getFestivalBid();
	}
	
	public int getCurrentPlayerTabCount(){
		return this.players.get(indexOfCurrentPlayer).getTabCount();
	}
	
	public String getTabbedHashKey(){
		return this.players.get(indexOfCurrentPlayer).getTabbedImageString();
	}
	
	public PalaceCard getFestivalCard(){
		return this.festivalCard;
	}
	
	public int getBeginningPlayerIndex(){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).startedFestival()) return i;
		}
		return -1;
	}
	
	public boolean canEndTurn(){
		if(highestBid == 0)
			return false;
		return highestBid <= players.get(indexOfCurrentPlayer).getFestivalBid();
	}
	
	public int endTurn(){
		players.get(indexOfCurrentPlayer).endTurn();
		incrementPlayer();
		while(!players.get(indexOfCurrentPlayer).checkIfInFestival())
			incrementPlayer();
		return indexOfCurrentPlayer;
	}
	
	public boolean ifHadFullCycleTurnCheck(){
		if(players.get(indexOfCurrentPlayer).startedFestival()){
			if(highestBid != 0){
				highestBid = 0;
				return true;
			}
		}
		return false;
	}
	
	public PalaceCard tabThroughPalaceCards(){
		return players.get(indexOfCurrentPlayer).getTabbedPalaceCard();
	}
	
	public PalaceCard selectPalaceCard(){
		JavaFestivalPlayer player = players.get(indexOfCurrentPlayer);
		
		PalaceCard selectedCard = player.getSelectedPalaceCard();
		if(selectedCard == null)
			return null;
		//increment the player's festival bid
		player.addFestivalBid(selectedCard.compareForPoints(festivalCard));
		//increment the highest bid if appropriate
		int currentBid = player.getFestivalBid();
		if(currentBid > highestBid) highestBid = currentBid;
		return selectedCard;
	}
	
	public void endTabbing(){
		players.get(indexOfCurrentPlayer).cancelTabbing();
	}
	
	private void incrementPlayer(){
		indexOfCurrentPlayer = (indexOfCurrentPlayer + 1) % players.size();
	}
	
	public int dropCurrentPlayer(){
		players.get(indexOfCurrentPlayer).dropPlayerFromFestival();
		if(players.get(indexOfCurrentPlayer).startedFestival()){
			//the player who has started the festival dropped out, like a dumb ass
			//set as the next available person to be the started festival person
			int i = indexOfCurrentPlayer;
			i = (i + 1) % players.size();
			while(i != indexOfCurrentPlayer){
				if(players.get(i).checkIfInFestival()){
					//get the next player who is in the festival
					players.get(i).changeStartedFestivalStatus(true);
				}
				i = (i + 1) % players.size();
			}
		}
		return endTurn();
	}
	
	public int getNumPlayersInFestival(){
		int numPlayers = 0;
		for(JavaFestivalPlayer player : players){
			if(player.checkIfInFestival())
				numPlayers++;
		}
		return numPlayers;
	}
	
	public JavaFestivalPlayer getLastPersonInFestival(){
		for(JavaFestivalPlayer player : players){
			if(player.checkIfInFestival())
				return player;
		}
		return null;
	}
	
	public boolean checkIfEveryoneIsOutOfCards(){
		//returns true if everyone is out of cards
		//if everyone is out of cards then the game ends and a winner is calculated
		for(JavaFestivalPlayer player : players){
			if(player.checkIfInFestival()){
				if(player.getNumPalaceCards() > 0)
					return false;
			}
		}
		return true;
	}
	
	public int getNumWinners(){
		//checks for a tie
		calculateHighestBid();
		int numWinningPlayers = 0;
		for(JavaFestivalPlayer player : players){
			if(player.checkIfInFestival() && (player.getFestivalBid() == highestBid)){
					numWinningPlayers++;
			}
		}
		return numWinningPlayers;
	}
	
	private void calculateHighestBid(){
		for(JavaFestivalPlayer player : players){
			//check if they are in the festival
			if(player.checkIfInFestival()){
				if(highestBid < player.getFestivalBid()){
					highestBid = player.getFestivalBid();
				}
			}
		}
	}
	
	public ArrayList<JavaFestivalPlayer> getWinners(){
		ArrayList<JavaFestivalPlayer> winners = new ArrayList<JavaFestivalPlayer>();
		if(getNumPlayersInFestival() == 1){
			winners.add(getLastPersonInFestival());
			return winners;
		}
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getFestivalBid() == highestBid)
				winners.add(players.get(i));
		}
		return winners;
	}
	
	public void endFestival(boolean ifTie){
		ArrayList<JavaFestivalPlayer> winners = getWinners();
		int pointsToAward = getFamePointsWon(ifTie);
		for(JavaFestivalPlayer winner : winners){
			winner.awardFamePoints(pointsToAward);
		}
	}
	
	public HashMap<JavaPlayer, ArrayList<PalaceCard>> getDiscardedPalaceCards(){
		HashMap<JavaPlayer, ArrayList<PalaceCard>> hash = new HashMap<JavaPlayer, ArrayList<PalaceCard>>();
		for(JavaFestivalPlayer player : players){
			hash.put(player.getJavaPlayer(), player.getDiscardedCards());
		}
		return hash;
	}
	
	public HashMap<JavaPlayer, Integer> getFamePointsWon(){
		HashMap<JavaPlayer, Integer> hash = new HashMap<JavaPlayer, Integer>();
		for(JavaFestivalPlayer player : players){
			hash.put(player.getJavaPlayer(), new Integer(player.getFamePoints()));
		}
		return hash;
	}
	
	public int getFamePointsWon(boolean ifTie){
		if(ifTie){
			return famePointsAwarded.get(valueOfPalaceCity+"_tie");
		}
		return famePointsAwarded.get(valueOfPalaceCity+"");
	}
	
	private void initFamePointsHashMap(){
		famePointsAwarded.put("2", new Integer(1));
		famePointsAwarded.put("4", new Integer(2));
		famePointsAwarded.put("6", new Integer(3));
		famePointsAwarded.put("8", new Integer(4));
		famePointsAwarded.put("10", new Integer(5));
		famePointsAwarded.put("2_tie", new Integer(0));
		famePointsAwarded.put("4_tie", new Integer(1));
		famePointsAwarded.put("6_tie", new Integer(2));
		famePointsAwarded.put("8_tie", new Integer(2));
		famePointsAwarded.put("10_tie", new Integer(3));
	}

}
