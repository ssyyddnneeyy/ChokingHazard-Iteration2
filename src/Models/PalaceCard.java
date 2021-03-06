package Models;

import java.util.*;

import Helpers.Json;
import Helpers.JsonObject;

public class PalaceCard implements Serializable<PalaceCard> {
	private ArrayList<Integer> symbols;
	private int numSymbols;
	private String cardType;
	private boolean faceUp;

	public PalaceCard(int cardType, boolean faceUp) {
		this.faceUp = faceUp;
		symbols = new ArrayList<Integer>();

		switch (cardType) {
		case 1:
			symbols.add(1);
			this.numSymbols = 1;
			this.cardType = "palaceCard_DRUM";
			break;
		case 2:
			symbols.add(2);
			this.numSymbols = 1;
			this.cardType = "palaceCard_MASK";
			break;
		case 3:
			symbols.add(3);
			this.numSymbols = 1;
			this.cardType = "palaceCard_PUPPET";
			break;
		case 4:
			symbols.add(1);
			symbols.add(2);
			this.numSymbols = 2;
			this.cardType = "palaceCard_MASK_DRUM";
			break;
		case 5:
			symbols.add(1);
			symbols.add(3);
			this.numSymbols = 2;
			this.cardType = "palaceCard_PUPPET_DRUM";
			break;
		case 6:
			symbols.add(2);
			symbols.add(3);
			this.numSymbols = 2;
			this.cardType = "palaceCard_PUPPET_MASK";
			break;
		}
	}

	public PalaceCard(ArrayList<Integer> symbo, int numSymbo, String cardType) {
		this.symbols = symbo;
		this.numSymbols = numSymbo;
		this.cardType = cardType;
	}

	public ArrayList<Integer> getSymbols() {
		return symbols;
	}

	public String getType() {
		return this.cardType;
	}

	public int getTypeNumber() {
		if(cardType.equals("palaceCard_DRUM"))
			return 1;
		else if(cardType.equals("palaceCard_MASK"))
			return 2;
		else if(cardType.equals("palaceCard_PUPPET"))
			return 3;
		else if(cardType.equals("palaceCard_MASK_DRUM"))
			return 4;
		else if(cardType.equals("palaceCard_PUPPET_DRUM"))
			return 5;
		else if(cardType.equals("palaceCard_PUPPET_MASK"))
			return 6;
		return -1;
	}

	public int getNumSymbols() {
		return this.numSymbols;
	}

	// Returns if has the shared symbols on it
	public boolean compareHasSymbols(PalaceCard card) {

		// returns true if the comparing card and this card share a symbol
		// false if they dont
		for (Integer i : card.getSymbols()) {
			if (this.symbols.contains(i))
				return true;
		}

		return false;
	}

	public boolean compareNumSymbols(PalaceCard card) {
		// returns true if the num of symbols matches the card
		if (this.numSymbols > card.getNumSymbols()) {
			return false;
		}
		return true;
	}

	public int compareForPoints(PalaceCard card) {
		int points = 0;
		for (Integer i : card.getSymbols()) {
			if (this.symbols.contains(i))
				points++;
		}
		return points;
	}

	@Override
	public String serialize() {
		return Json.jsonObject(Json.jsonMembers(
				Json.jsonPair("symbols", Json.serializeArray(symbols)),
				Json.jsonPair("numSymbols", Json.jsonValue(numSymbols + "")),
				Json.jsonPair("cardType", Json.jsonValue(cardType + ""))));
	}

	@Override
	public PalaceCard loadObject(JsonObject json) {
		symbols = new ArrayList<Integer>();
		for (String symbol : json.getStringArray("symbols"))
			symbols.add(Integer.parseInt(symbol));
		numSymbols = Integer.parseInt(json.getString("numSymbols"));
		cardType = json.getString("cardType");
		return this;
	}

	public void setFaceDown() {
		faceUp = false;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp() {
		faceUp = true;
	}
}