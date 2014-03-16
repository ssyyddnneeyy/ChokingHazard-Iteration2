package Controllers;

import Helpers.Deck;
import Models.PalaceCard;
import Models.SharedComponentModel;
import Views.SharedComponentPanel;

public class SharedComponentController {
	private SharedComponentModel sharedModel;
	private SharedComponentPanel sharedPanel;

	public SharedComponentController(){
		//new game constructor
		this.sharedModel = new SharedComponentModel();
		this.sharedPanel = new SharedComponentPanel(sharedModel.getThreeSpaceTiles(), sharedModel.getIrrigationTiles(), 
				sharedModel.getPalaceTiles(), sharedModel.getNumberPalaceCards());
		
		//there's no festival card for this deck. so when dealPalaceCards(numPlayers) is called, it will create a festival card
	}
	public SharedComponentController(int threeTiles, int irrigationTiles, int[] palaceTiles, Deck<PalaceCard> deck, PalaceCard festivalCard){
		//load game constructor
		this.sharedModel = new SharedComponentModel(threeTiles, irrigationTiles, palaceTiles, deck, festivalCard);
		this.sharedPanel = new SharedComponentPanel(threeTiles, irrigationTiles, palaceTiles, deck.size(), festivalCard.getType());
	}
	
	public SharedComponentPanel getSharedComponentPanel(){
		return this.sharedPanel;
	}
	
	public PalaceCard drawFromDeck(){
		PalaceCard card = sharedModel.drawFromDeck();
		sharedPanel.drawFromPalaceDeck(sharedModel.getNumberPalaceCards());
		return card;
	}
	
	public PalaceCard drawFestivalCard(){
		PalaceCard card = this.sharedModel.getFestivalCard();
		sharedModel.drawFestivalCard();
		sharedPanel.drawFestivalCard(sharedModel.getNumberPalaceCards(), sharedModel.getFestivalCardType());
		return card;
	}
	
	public PalaceCard[][] dealPalaceCards(int numPlayers){
		PalaceCard[][] cards = new PalaceCard[numPlayers][3];
		for(int i = 0; i < numPlayers; i++){
			int j = 0;
			while(j < 3){
				cards[i][j] = drawFromDeck();
				j++;
			}
		}
		sharedModel.initFestivalCard();
		sharedPanel.drawFestivalCard(sharedModel.getNumberPalaceCards(), sharedModel.getFestivalCardType());
		return cards;
	}
}
