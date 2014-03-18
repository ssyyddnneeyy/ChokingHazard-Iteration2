package Models;

import Helpers.Json;
import Helpers.JsonObject;
import Models.Tile.TileType;

public class JavaCell implements Serializable<JavaCell> {
   private int elevation;
   private String cellType;
   private boolean hasDeveLoper;
   private int cellId;
   public int xVal;
   public int yVal;
   public JavaCell(int x, int y, String cellType, int cellId, int elevation) {
	   this.xVal = x;
	   this.yVal = y;
	   this.cellType = cellType;
	   this.cellId = cellId;
	   this.elevation = elevation;
   }
   
   public JavaCell(int x, int y, String cellType, int cellId) {
	   this.xVal = x;
	   this.yVal = y;
	   elevation = 0;
	   this.cellType = cellType;
	   this.cellId = cellId;
   }
   
   public JavaCell(int x, int y, int cellId) {
	  this.xVal = x;
	  this.yVal = y;
	  elevation = 0;
	  cellType = "blank";
	  this.cellId = cellId;
   }


	public int getX() {
		return xVal;
	}

	public int getY() {
		return yVal;
	}
	
   public int getCellId() {
      return cellId;
   }
   
   public void setCellId(int cellId) {
	   this.cellId = cellId;
   }
   
   public int getElevation() {
      return elevation;
   }
   
   public String getCellType() {
      return cellType;
   }
   
   public void setElevation(int e) {
      elevation = e;
   }
   
   public void setCellType(TileType tileCells) {
      cellType = tileCells.toString();
   }
   
   public void setDeveloper() {
	   this.hasDeveLoper = true;
   }
   
   public void removeDeveloper() {
	   this.hasDeveLoper = false;
   }
   
   public boolean hasDeveloper() {
	   return hasDeveLoper;
   }
   
   public boolean isBorder() {
	   return false; //TODO
   }
   
   public boolean hasAdjacentLandSpaceTile() {
	   //TODO: finish brett
	   /* 
	   int x = this.getX();
	   int y = this.getY();
	   if (x + 1 == 14)
	   {
		   if (map.isTileOrLand(x+1,y))
			   return true;
	   }
	   
	   if (x- 1 == 0)
	   {
		   if (board.isTileOrLand(x-1,y))
			   return true;
	   }
	   if (y + 1 == 14)
	   {
		   if (board.isTileOrLand(x,y+1))
			   return true;
	   }
	   if (y - 1 == 0)
	   {
		   if (board.isTileOrLand(x,y-1))
			   return true;
	   }
	   */ 
	   return false;   
   }
	
	@Override
	public String serialize() {
		return Json.jsonObject(Json.jsonMembers(
			Json.jsonPair("elevation", Json.jsonValue(elevation + "")),
			Json.jsonPair("cellType", Json.jsonValue(cellType + "")),
			Json.jsonPair("hasDeveLoper", Json.jsonValue(hasDeveLoper + "")),
			Json.jsonPair("cellId", Json.jsonValue(cellId + "")),
			Json.jsonPair("xVal", Json.jsonValue(xVal + "")),
			Json.jsonPair("yVal", Json.jsonValue(yVal + ""))
		));
	}
	
	@Override
	public JavaCell loadObject(JsonObject json) {
		elevation = Integer.parseInt(json.getString("elevation"));
		cellType = json.getString("cellType");
		hasDeveLoper = Boolean.parseBoolean(json.getString("hasDeveLoper"));
		cellId = Integer.parseInt(json.getString("cellId"));
		xVal = Integer.parseInt(json.getString("xVal"));
		yVal = Integer.parseInt(json.getString("yVal"));
		return this;
	}
}