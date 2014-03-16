package Models;
import java.util.*;

import Helpers.Json;
import Helpers.JsonObject;

public class PalaceCard implements Serializable<PalaceCard> {
   private ArrayList<Integer> symbols;
   private int numSymbols;
   private String cardType;
   
   public PalaceCard(int cardType) {
      symbols = new ArrayList<Integer>();
      
      switch(cardType){
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
   
   public PalaceCard(ArrayList<Integer> symbo, int numSymbo, String cardType){
	   this.symbols = symbo;
	   this.numSymbols = numSymbo;
	   this.cardType = cardType;
   }
   
   public ArrayList<Integer> getSymbols(){
      return symbols;
   }
   
   public String getType(){
	   return this.cardType;
   }
   
   public int getNumSymbols(){
	   return this.numSymbols;
   }
   
   //Returns the point value of cards
   public int compare(PalaceCard card){
      int pnts = 0;
      
      for(Integer i:card.getSymbols())
      {
         if(this.symbols.contains(i))
            pnts++;
      }
      //returns if the symbol comparing to is on the card
      return pnts;
   }
   
   public PalaceCard deepCopy(){
	   return new PalaceCard(this.symbols, this.numSymbols, this.cardType);
   }

	@Override
	public String serialize() {
		return Json.jsonObject(Json.jsonMembers(
			Json.jsonPair("symbols", Json.serializeArray(symbols)),
			Json.jsonPair("numSymbols", Json.jsonValue(numSymbols + "")),
			Json.jsonPair("cardType", Json.jsonValue(cardType + ""))
		));
	}
	
	@Override
	public PalaceCard loadObject(JsonObject json) {
		// TODO Auto-generated method stub
		return null;
	}
}