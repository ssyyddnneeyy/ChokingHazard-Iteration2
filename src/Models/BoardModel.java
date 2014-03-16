package Models;

import java.util.Stack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class BoardModel {
	private JavaCell[][] map;
	private Stack<JavaCell> path;
	private ArrayList<JavaCell> connectedPalaces = new ArrayList<JavaCell>();

	public BoardModel() {
		this.map = new JavaCell[14][14];
		this.path = new Stack<JavaCell>();

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (x == 0 || x == 1) {
					map[x][y] = new JavaCell(x, y, 0);
				}

				else if (x <= 6 && (y <= 1 || y >= 12)) {
					map[x][y] = new JavaCell(x, y, 0);
				}

				else if (x >= 7 && (y <= 1 || y >= 12)) {
					map[x][y] = new JavaCell(x, y, 0);
				} else if (x == 12 || x == 13) {
					map[x][y] = new JavaCell(x, y, 0);
				} else {
					map[x][y] = new JavaCell(x, y, 0); // this creates Cell
														// objects for the rest
														// of central Java
				}
			}
		}

	}

	public boolean placeTile(int xC, int yC, Tile tile, JavaPlayer player) {
		if (checkValidTilePlacement(xC, yC, tile, player)) {

			return true;
		}

		return false;
	}

	private boolean checkValidTilePlacement(int xC, int yC, Tile tile,
			JavaPlayer player) {

		JavaCell[][] miniMap = createTestMap(xC, yC); // creating a small map
														// with the cells we
														// need to compare

		int neededActionPoints = checkNeededActionPoints(miniMap, tile);

		if (checkPalacePlacement(miniMap, tile)
				&& checkTilesBelow(miniMap, tile)
				&& checkElevation(miniMap, tile, xC, yC)
				&& checkIrrigationPlacement(miniMap, tile)
				&& checkDeveloperOnCell(miniMap, tile)
				&& checkCityConnection(miniMap, tile)
				&& checkEdgePlacement(miniMap, tile)
				&& player.decrementNActionPoints(neededActionPoints)) {
			return true;
		}

		return false;
	}

	private JavaCell[][] createTestMap(int xC, int yC) {

		JavaCell[][] testingMap = new JavaCell[3][3];

		for (int i = 0, x = xC - 1; i < 3; i++, x++)
			for (int j = 0, y = yC - 1; j < 3; j++, y++)
				testingMap[i][j] = map[x][y];

		return testingMap;
	}

	private int checkNeededActionPoints(JavaCell[][] miniMap, Tile tile) {
		int outsideCount = 1;
		int mapRowLength = map.length;
		int mapColumnSize = map[0].length;
		String[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (miniMap[i][j] != null
							&& ((i == mapRowLength)
									|| (i == mapColumnSize)
									|| ((i != 0) && (i != mapColumnSize - 1) && (j == 0)) || ((i != 0)
									&& (i != mapColumnSize) && (j == mapRowLength)))) {

						outsideCount++;
					}
				}
			}
		}

		return outsideCount;
	}

	private boolean checkPalacePlacement(JavaCell[][] miniMap, Tile tile) {
		String[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (miniMap[i][j] != null &&
						miniMap[i][j].getCellType() != null &&
						miniMap[i][j].getCellType() == "irrigation") {

						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean checkTilesBelow(JavaCell[][] miniMap, Tile tile) {

		return true;
	}

	private boolean checkElevation(JavaCell[][] miniMap, Tile tile, int xC,
			int yC) {
		String[][] tileCells = tile.getTileCells();

		int elevation = map[xC][yC].getElevation();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null
						&& miniMap[i][j].getElevation() != elevation) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean checkIrrigationPlacement(JavaCell[][] miniMap, Tile tile) {
		String[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (miniMap[i][j] != null
							&& miniMap[i][j].getCellType() != null
							&& miniMap[i][j].getCellType() == "irrigation") {

						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean checkDeveloperOnCell(JavaCell[][] miniMap, Tile tile) {
		String[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (miniMap[i][j] != null && miniMap[i][j].hasDeveloper()) {

						return false;
					}
				}
			}
		}

		return true;
	}

	// cannot place a village next to 2 palaces
	private boolean checkCityConnection(JavaCell[][] miniMap, Tile tile) {
		JavaCell[][] mapCopy = new JavaCell[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapCopy[i][j] = map[i][j];
			}
		}
		
		String[][] tileCells = tile.getTileCells();
		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[0].length; j++) {
				if (tileCells[i][j] != null &&
					tileCells[i][j] == "village" &&
					miniMap[i][j] != null) {
					findPalaceSpaces(miniMap[i][j].getX(), miniMap[i][j].getY(), mapCopy);
				}
			}
		}
		
		for (int i = 0; i < connectedPalaces.size(); i++) {
			for (int j = i + 1; j < connectedPalaces.size(); j++) {
				if (connectedPalaces.get(i) != null && connectedPalaces.get(j) != null) {
					if (connectedPalaces.get(i) != connectedPalaces.get(j)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}

	private void findPalaceSpaces(int x, int y, JavaCell[][] map) {
		boolean canUp = (x - 1 >= 0);
		boolean canDown = (x + 1 < map.length);
		boolean canRight = (y + 1 < map[0].length);
		boolean canLeft = (y - 1 >= 0);

		if (map[x][y] != null && map[x][y].getCellType() != null &&
			map[x][y].getCellType() == "palace") {
				connectedPalaces.add(map[x][y]);
		}
		
		map[x][y] = null;
		if (canUp && 
		   (map[x - 1][y] != null && 
		   (map[x - 1][y].getCellType() == "village" || map[x - 1][y].getCellType() == "palace"))) {
				findPalaceSpaces(x - 1, y, map);
		}
		
		if (canDown && 
		   (map[x + 1][y] != null && 
		   (map[x + 1][y].getCellType() == "village" || map[x - 1][y].getCellType() == "palace"))) {
				findPalaceSpaces(x + 1, y, map);
		}
		
		if (canLeft && 
		   (map[x][y - 1] != null && 
		   (map[x][y - 1].getCellType() == "village" || map[x][y - 1].getCellType() == "palace"))) {
				findPalaceSpaces(x, y - 1, map);
		}
		
		if (canRight && 
		   (map[x][y + 1] != null && 
		   (map[x][y + 1].getCellType() == "village" || map[x - 1][y].getCellType() == "palace"))) {
				findPalaceSpaces(x, y + 1, map);
		}
		
		return;
	}
	
	private boolean checkEdgePlacement(JavaCell[][] miniMap, Tile tile) {
		return true;
	}

	public boolean placeDeveloper(Point location, JavaPlayer player) {
		// Set the point equal to the cell on the board
		JavaCell locationCell = map[location.getX()][location.getY()];

		// Check with validity method first
		if (!canPlaceDeveloper(locationCell, player))
			return false;

		// Set developer on board
		return true; // TODO

	}

	public boolean canPlaceDeveloper(JavaCell locationCell, JavaPlayer player) {
		
		// Can only place on village or rice space
		if (!(isTileOrLand(locationCell.getX(), locationCell.getY())))
			return false;

		// Can only place if no developer is already occupying space
		if (locationCell.hasDeveloper())
			return false;

		// Space must be on or inside border into central java
		if (locationCell.isBorder()) {
			// If it's on the border, must see that there is a land space tile
			// there
			// But we already checked that above, so by this point we are good
		}
		// If it's not on the border, have to check that at least on adjacent
		// space is open in outer java
		else if (!hasAdjacentEmptyTile(locationCell))
			return false;

		// Check that player has available AP for this
		// First determine type of move/cost
		if (!player.decrementNActionPoints(1)) // TODO: Check lowlands or
												// mountains
			return false;

		return true;
	}

	public boolean hasAdjacentEmptyTile(JavaCell cell) {
		int x = cell.getX();
		int y = cell.getY();

		// Check if the cell is adjacent to any empty border cells
		if (x + 1 == 13) {
			if (map[13][y].getCellType().equals("Blank"))
				return true;
		}

		if (x - 1 == 0) {
			if (map[0][y].getCellType().equals("Blank"))
				return true;
		}
		if (y + 1 == 13) {
			if (map[x][13].getCellType().equals("Blank"))
				return true;
		}
		if (y - 1 == 0) {
			if (map[x][0].getCellType().equals("Blank"))
				return true;
		}

		return false;

	}

	public boolean isTileOrLand(int x, int y) {
		if (!(map[x][y].getCellType().equals("Village") || map[x][y]
				.getCellType().equals("Rice")))
			return false;
		return true;
	}

	public void removeDeveloper(Point point, JavaPlayer player) {
		JavaCell pointCell = map[point.getX()][point.getY()];

		// Turn off hasDeveloper
		pointCell.removeDeveloper();
		// Remove currently selected developer from dev array
		player.removeDeveloperFromArray(); // TODO: correct indices
		// Decrement actions points
		player.decrementNActionPoints(1);
	}

	public JavaCell[][] getMap() {
		return map;
	}

	// Given a root cell, finds and adds to a list all adjacent cells with the
	// same type
	// This can be used to retrieve the cells that make up a city.
	public ArrayList<JavaCell> getCityFromRootCell(JavaCell root) {
		ArrayList<JavaCell> connected = new ArrayList<JavaCell>();
		int x = root.getX();
		int y = root.getY();

		connected.add(root);

		int i = 0;
		while (i < connected.size()) {
			Cell temp = connected.get(i);
			HashSet<JavaCell> adjacent = new HashSet<JavaCell>();
			if (y < 14 && map[y + 1][x].getCellType().equals("village")
					|| map[y + 1][x].getCellType().equals("palace"))
				adjacent.add(map[y + 1][x]);
			if (y > 0 && map[y - 1][x].getCellType().equals("village")
					|| map[y - 1][x].getCellType().equals("palace"))
				adjacent.add(map[y - 1][x]);
			if (x < 14 && map[y][x + 1].getCellType().equals("village")
					|| map[y][x + 1].getCellType().equals("palace"))
				adjacent.add(map[y][x + 1]);
			if (x > 0 && map[y][x - 1].getCellType().equals("village")
					|| map[y][x - 1].getCellType().equals("palace"))
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
   
   public ArrayList<JavaCell> getBodyOfWater(JavaCell root) {
		ArrayList<JavaCell> connected = new ArrayList<JavaCell>();
		int x = root.getX();
		int y = root.getY();

		connected.add(root);

		int i = 0;
		while (i < connected.size()) {
			Cell temp = connected.get(i);
			HashSet<JavaCell> adjacent = new HashSet<JavaCell>();
			if (y < 14 && map[y + 1][x].getCellType().equals("irrigation"))
				adjacent.add(map[y + 1][x]);
			if (y > 0 && map[y - 1][x].getCellType().equals("irrigation"))
				adjacent.add(map[y - 1][x]);
			if (x < 14 && map[y][x + 1].getCellType().equals("irrigation"))
				adjacent.add(map[y][x + 1]);
			if (x > 0 && map[y][x - 1].getCellType().equals("irrigation"))
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
