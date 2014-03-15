package Models;
import java.util.Stack;
import java.util.*;

public class BoardModel {
	private JavaCell[][] map;
	private Stack<Cell> path;
	
	public BoardModel() {
		this.map = new JavaCell [14][14];
		this.path = new Stack<Cell>();
	
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (x == 0 || x == 1) {
					map[x][y] = new JavaCell (x, y);
				}

				else if (x <= 6 && (y <= 1 || y >= 12)) {
					map[x][y] = new JavaCell (x, y);
				}

				else if (x >= 7 && (y <= 1 || y >= 12)) {
					map[x][y] = new JavaCell (x, y);
				}
				else if (x == 12 || x == 13) {
					map[x][y] = new JavaCell (x, y);
				}
				else {
					map[x][y] = new JavaCell (x,y);	//this creates Cell objects for the rest of central Java
				}
			}
		}
		
	}
	
	public boolean placeTile(Cell[][] cells, Tile tile) {
		
		return true;
	}
	
	private boolean checkValidTilePlacement(int xC, int yC, Tile tile, JavaPlayer player) {
		JavaCell[][] javaCells = createTestMap(xC, yC);
		int neededActionPoints = checkNeededActionPoints(javaCells, tile);
		
		if (checkPalacePlacement(javaCells, tile) && 
			checkTilesBelow(javaCells, tile) && 
			checkElevation(javaCells, tile) && 
			checkIrrigationPlacement(javaCells, tile) &&
			checkDeveloperOnCell(javaCells, tile) && 
			checkCityConnection(javaCells, tile) && 
			checkEdgePlacement(javaCells, tile) &&
			player.decrementNActionPoints(neededActionPoints)) {
				return true;
		}

		return false;
	}
	
	private int checkNeededActionPoints(JavaCell[][] javaCells, Tile tile) {
		return 0;
	}
	
	private boolean checkPalacePlacement(JavaCell[][] javaCells, Tile tile) {
		return true;
	}
	
	private boolean checkTilesBelow(JavaCell[][] javaCells, Tile tile) {
		return true;
	}
	
	private boolean checkElevation(JavaCell[][] javaCells, Tile tile) {
		return true;
	}
	
	private boolean checkIrrigationPlacement(JavaCell[][] javaCells, Tile tile) {
		String[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (javaCells[i][j] != null &&
					    javaCells[i][j].getCellType() != null &&
					    javaCells[i][j].getCellType() == "irrigation") {
		
							return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean checkDeveloperOnCell(JavaCell[][] javaCells, Tile tile) {
		String[][] tileCells = tile.getTileCells();
		
		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (javaCells[i][j] != null &&
					    javaCells[i][j].hasDeveloper()) {
		
							return false;
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean checkCityConnection(JavaCell[][] javaCells, Tile tile) {
		return true;
	}
	
	private boolean checkEdgePlacement(JavaCell[][] javaCells, Tile tile) {
		return true;
	}

   public JavaCell[][] getMap()
   {
      return map;
   }
   
   //Given a root cell, finds and adds to a list all adjacent cells with the same type
   //This can be used to retrieve the cells that make up a city.
   public ArrayList<JavaCell> getCityFromRootCell(JavaCell root)
   {
      ArrayList<JavaCell> connected = new ArrayList<JavaCell>();
      int x = root.getX();
      int y = root.getY();
      
		connected.add(root);

		int i = 0;
		while (i < connected.size()) {
			Cell temp = connected.get(i);
			HashSet<JavaCell> adjacent = new HashSet<JavaCell>();
			if (y < 14 && map[y + 1][x].getCellType().equals("village") || map[y + 1][x].getCellType().equals("palace"))
				adjacent.add(map[y + 1][x]);
			if (y > 0 && map[y - 1][x].getCellType().equals("village") || map[y - 1][x].getCellType().equals("palace"))
				adjacent.add(map[y - 1][x]);
			if (x < 14 && map[y][x + 1].getCellType().equals("village") || map[y][x + 1].getCellType().equals("palace"))
				adjacent.add(map[y][x + 1]);
			if (x > 0 && map[y][x - 1].getCellType().equals("village") || map[y][x - 1].getCellType().equals("palace"))
				adjacent.add(map[y][x - 1]);

			Iterator<JavaCell> it = adjacent.iterator();
			while (it.hasNext()) {
				JavaCell next = (JavaCell) it.next();
				if (!connected.contains(next))
					connected.add(next);
			}
			i++;
		}

		return connected;
   }
}
