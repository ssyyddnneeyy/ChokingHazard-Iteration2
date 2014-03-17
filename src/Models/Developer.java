package Models;

import Helpers.Json;
import Helpers.JsonObject;

public class Developer implements Serializable<Developer>
{
   private Cell location;
   private JavaPlayer owner;
   
   public Developer(JavaPlayer p)
   {
      //developer not on the board yet, so location is null
      location = null;
      owner = p;
   }
   
   public void setLocation(Cell c)
   {
      location = c;
   }
   
   public void setOwner(JavaPlayer p)
   {
      owner = p;
   }
   
   public Cell getLocation()
   {
      return location;
   }
   
   public JavaPlayer getOwner()
   {
      return owner;
   }

	@Override
	public String serialize() {
		return Json.jsonObject(Json.jsonMembers(
			Json.jsonPair("location-x", Json.jsonValue(location.xVal + "")),
			Json.jsonPair("location-y", Json.jsonValue(location.yVal + "")),
			Json.jsonPair("owner", Json.jsonValue(owner.name + ""))
		));
	}
	
	@Override
	public Developer loadObject(JsonObject json) {
		if(owner == null)
			// TODO Auto-generated method stub
			System.out.println("FJDSKL");
		return this;
	}
}