package Models;

import Helpers.Json;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.HashMap;
import Helpers.JsonObject;
import Models.Tile.TileType;

public class BoardModel implements Serializable<BoardModel>  {
	private JavaCell[][] map;
	private JavaCell[][] irrigationMap;
	private LinkedList<JavaCell> path;
	private ArrayList<JavaCell> connectedPalaces;
	private ArrayList<JavaCell> visitedVillages;
	private ArrayList<ArrayList<JavaCell>> bodiesOfWater;
	//private ArrayList<JavaCell> coast;
	private JavaCell[] outerCells;
	public int cellId;
	

	public BoardModel() {
		this.map = new JavaCell[14][14];
		this.outerCells = new JavaCell[52];
		this.irrigationMap = new JavaCell[14][14];
		this.path = new LinkedList<JavaCell>();
		connectedPalaces = new ArrayList<JavaCell>();
		visitedVillages = new ArrayList<JavaCell>();
		bodiesOfWater = new ArrayList<ArrayList<JavaCell>>();
		//coast = new ArrayList<JavaCell>();
		cellId = 0;

		int i = 0;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y] = new JavaCell(x, y, 0);
				irrigationMap[x][y] = new JavaCell(x, y, 0);

				if ((x == 0 || x == 13)) {
					outerCells[i] = map[x][y];
					i++;
				}

				else if ((y == 0 || y == 13) && (x != 0 && x != 13)) {
					outerCells[i] = map[x][y];
					i++;
				}
			}
		}
	}

	public boolean placeTile(int xC, int yC, Tile tile, JavaPlayer player) {
		JavaCell[][] miniMap = createTestMap(xC, yC);
		TileType[][] tileCells = tile.getTileCells();

		if (checkValidTilePlacement(xC, yC, tile, player, miniMap)) {
			/*
			 * are we going to be creating the tile here? if we are then we need
			 * to increase cellId here.
			 */int number;
			if (tile.getType() == "two") {
				number = 2;
			} else if (tile.getType() == "three") {
				number = 3;

			} else if (tile.getType() == "one") {
				number = 1;
			} else {
				number = 0;
			}
			cellId++;
			for (int i = 0; i < tileCells.length; i++)
				for (int j = 0; j < tileCells[i].length; j++)
					if (tileCells[i][j] != null) {
						map[miniMap[i][j].getX()][miniMap[i][j].getY()]
								.setNumOriginalSpaces(tile.numOfSpaces());
						map[miniMap[i][j].getX()][miniMap[i][j].getY()]
								.setCellType(tileCells[i][j]);
						map[miniMap[i][j].getX()][miniMap[i][j].getY()]
								.setCellId(cellId);
						map[miniMap[i][j].getX()][miniMap[i][j].getY()]
								.setElevation(map[miniMap[i][j].getX()][miniMap[i][j]
										.getY()].getElevation() + 1);
						map[miniMap[i][j].getX()][miniMap[i][j].getY()]
								.setNumOriginalSpaces(number);

					}

			if (placedLandTile(xC, yC))
				player.placedLandTile();

//			System.out.println(toString());
			return true;
		}

		return false;
	}

	public boolean placedLandTile(int xC, int yC) {

		if (map[xC][yC].getCellType() == "village"
				|| map[xC][yC].getCellType() == "rice")
			return true;

		return false;
	}

	public boolean checkValidTilePlacement(int xC, int yC, Tile tile,
			JavaPlayer player, JavaCell[][] miniMap) {
		// creating a small map with the cells we need to compare
		// JavaCell[][] miniMap = createTestMap(xC, yC);

		int neededActionPoints = checkNeededActionPoints(miniMap, tile);

		// boolean needed to check the amount of available AP points
		boolean isLandTile = "villagerice".contains(tile.getTileCells()[1][1]
				.toString());
		boolean palaceOK = true;
		if (tile.getTileCells()[1][1].isPalace()) {
			palaceOK = placePalace(xC, yC, miniMap[1][1], map, player);
		}

//		System.out.println("palace placement: "
//				+ checkPalacePlacement(miniMap, tile));
//		System.out.println("palace tilesBelow: "
//				+ checkTilesBelow(miniMap, tile));
//		System.out.println("palace elevation: "
//				+ checkElevation(miniMap, tile, xC, yC));
//		System.out.println("palace Irrigation: "
//				+ checkIrrigationPlacement(miniMap, tile));
//		System.out.println("palace DevOnCell: "
//				+ checkDeveloperOnCell(miniMap, tile));
//		System.out.println("palace CityConn: "
//				+ checkCityConnection(miniMap, tile));
//		System.out.println("palace edge: " + checkEdgePlacement(miniMap, tile));
//		System.out
//				.println("palace action: "
//						+ player.decrementNActionPoints(neededActionPoints,
//								isLandTile));

		if (checkPalacePlacement(miniMap, tile)
				&& checkTilesBelow(miniMap, tile)
				&& checkElevation(miniMap, tile, xC, yC)
				&& checkIrrigationPlacement(miniMap, tile)
				&& checkDeveloperOnCell(miniMap, tile)
				&& checkCityConnection(miniMap, tile)
				&& checkEdgePlacement(miniMap, tile)
				&& player
						.decrementNActionPoints(neededActionPoints, isLandTile)
				&& palaceOK
				&& checkIrrigationOnBoard(miniMap, tile)) {

			return true;
		}

		return false;
	}

	private JavaCell[][] createTestMap(int xC, int yC) {

		JavaCell[][] testingMap = new JavaCell[3][3];

		for (int i = 0, x = xC - 1; i < 3; i++, x++) {
			for (int j = 0, y = yC - 1; j < 3; j++, y++) {
				if ((x >= 0 && x < map.length) && (y >= 0)
						&& (y < map[0].length)) {
					testingMap[i][j] = map[x][y];
				}
			}
		}

		return testingMap;
	}

	private int checkNeededActionPoints(JavaCell[][] miniMap, Tile tile) {
		int outsideCount = 1;
		int mapRowLength = map.length;
		int mapColumnSize = map[0].length;
		TileType[][] tileCells = tile.getTileCells();

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

	private boolean checkIrrigationOnBoard(JavaCell[][] miniMap, Tile tile) {
		if (tile.getTileCells()[1][1].equals(Tile.TileType.irrigation)) {
			if (miniMap[1][1].getElevation() != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkPalacePlacement(JavaCell[][] miniMap, Tile tile) {
		TileType[][] tileCells = tile.getTileCells();

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (miniMap[i][j] != null
							&& miniMap[i][j].getCellType() != null
							&& miniMap[i][j].getCellType() == "palace") {

						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean checkTilesBelow(JavaCell[][] miniMap, Tile tile) {
		TileType[][] tileCells = tile.getTileCells();
		int numberOfTilesBelow = 0;
		int testId = miniMap[1][1].getCellId();
		int testing = 0;

		int number;
		if (tile.getType() == "two") {
			number = 2;
		} else if (tile.getType() == "three") {
			number = 3;

		} else if (tile.getType() == "one") {
			number = 1;
		} else {
			number = 0;
		}

		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				// System.out.println("original: "+miniMap[i][j].getNumOriginalSpaces()
				// + "  " + number);
				// System.out.println(i + " " + j +
				// miniMap[i][j].getCellType());

				if (miniMap[1][1].getNumOriginalSpaces() == 1 && number == 1)
					return false;
				if (miniMap[1][1].getNumOriginalSpaces() > 1 && number == 1)
					return true;

				if (tileCells[i][j] != null
						&& miniMap[i][j].getElevation() >= 0) {
//					System.out.println("in the 1st if statement: " + testId);

					if (testId == 0) {
						testId = miniMap[i][j].getCellId();
//						System.out.println("The id is: " + testId);
					} else {
						if (testId != miniMap[i][j].getCellId())
							return true;
						else
							numberOfTilesBelow++;
					}
				}
				// System.out.println("elevation: " +
				// miniMap[i][j].getElevation() + "getCellIdmini: " +
				// miniMap[1][1].getCellId() + "getCellId: " +
				// miniMap[i][j].getCellId() );
				// System.out.println("The # of tiles below: " +
				// numberOfTilesBelow + "testing: " + testing);

				if (miniMap[i][j] != null
						&& miniMap[i][j].getElevation() >= 0
						&& miniMap[1][1].getCellId() == miniMap[i][j]
								.getCellId()) {
					testing++;
				}

			}
		}

		if (number != numberOfTilesBelow || testing > numberOfTilesBelow)
			return true;
		else
			return false;
	}

	private boolean checkElevation(JavaCell[][] miniMap, Tile tile, int xC,
			int yC) {
		TileType[][] tileCells = tile.getTileCells();

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
		TileType[][] tileCells = tile.getTileCells();

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
		TileType[][] tileCells = tile.getTileCells();

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

		TileType[][] tileCells = tile.getTileCells();
		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[0].length; j++) {
				if (tileCells[i][j] != null
						&& tileCells[i][j] == TileType.village
						&& miniMap[i][j] != null) {
					findPalaceSpaces(miniMap[i][j].getX(),
							miniMap[i][j].getY(), mapCopy);
				}
			}
		}

		for (int i = 0; i < connectedPalaces.size(); i++) {
			for (int j = i + 1; j < connectedPalaces.size(); j++) {
				if (connectedPalaces.get(i) != null
						&& connectedPalaces.get(j) != null) {
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

		if (map[x][y] != null && map[x][y].getCellType() != null
				&& map[x][y].getCellType() == "palace") {
			connectedPalaces.add(map[x][y]);
		}

		map[x][y] = null;

		// fixed the bug here
		if (canUp
				&& (map[x - 1][y] != null && (map[x - 1][y].getCellType() == "village" || map[x - 1][y]
						.getCellType() == "palace"))) {
			findPalaceSpaces(x - 1, y, map);
		}

		if (canDown
				&& (map[x + 1][y] != null && (map[x + 1][y].getCellType() == "village" || map[x + 1][y]
						.getCellType() == "palace"))) {
			findPalaceSpaces(x + 1, y, map);
		}

		if (canLeft
				&& (map[x][y - 1] != null && (map[x][y - 1].getCellType() == "village" || map[x][y - 1]
						.getCellType() == "palace"))) {
			findPalaceSpaces(x, y - 1, map);
		}

		if (canRight
				&& (map[x][y + 1] != null && (map[x][y + 1].getCellType() == "village" || map[x][y + 1]
						.getCellType() == "palace"))) {
			findPalaceSpaces(x, y + 1, map);
		}

		return;
	}

	public boolean placePalace(int x, int y, JavaCell palace, JavaCell[][] map,
			JavaPlayer player) {
		if (map[x][y].getCellType() == "village"
				&& mutualPalacePlacementRequirementsOK(x, y, palace, map,
						player)) {

			return true;
		}

		else if (map[x][y].getCellType().contains("palace")
				&& mutualPalacePlacementRequirementsOK(x, y, palace, map,
						player) && canUpgradePalace(x, y, palace, map)) {

			return true;
		}

		else {
			return false;
		}
	}

	public boolean canUpgradePalace(int x, int y, JavaCell palace,
			JavaCell[][] map) {
		if (getPalaceSize(map[x][y]) < getPalaceSize(palace)) {
			return true;
		}

		return false;
	}

	public boolean mutualPalacePlacementRequirementsOK(int x, int y,
			JavaCell palace, JavaCell[][] map, JavaPlayer player) {
		if (findNumberConnected(x, y, map, visitedVillages) >= getPalaceSize(palace)
				&& !hasAlreadyBeenModified(palace, player)) {
			return true;
		}

		return false;
	}

	public int getPalaceSize(JavaCell palace) {
		int palaceSize = 200;

		switch (palace.getCellType()) {
		case "2palace":
			palaceSize = 2;
			break;
		case "4palace":
			palaceSize = 4;
			break;
		case "6palace":
			palaceSize = 6;
			break;
		case "8palace":
			palaceSize = 8;
			break;
		case "10palace":
			palaceSize = 10;
			break;
		default:
			break;
		}

		return palaceSize;
	}

	private boolean hasAlreadyBeenModified(JavaCell palace, JavaPlayer player) {

		return player.cellInPalacesInteractedWith(palace);
	}

	public static int findNumberConnected(int x, int y, JavaCell[][] map, ArrayList<JavaCell> visitedVillages) {
		visitedVillages.add(map[x][y]);
		
		boolean canUp = (x - 1 >= 0);
		boolean canDown = (x + 1 < map.length);
		boolean canRight = (y + 1 < map[0].length);
		boolean canLeft = (y - 1 >= 0);

		int up = 0;
		int down = 0;
		int right = 0;
		int left = 0;
		
		if (canUp
			&& map[x - 1][y] != null 
			&& !visitedVillages.contains(map[x - 1][y])
			&& map[x - 1][y].getCellType() == "village") {
			up = findNumberConnected(x - 1, y, map, visitedVillages);
		}

		if (canDown
			&& map[x + 1][y] != null 
			&& !visitedVillages.contains(map[x + 1][y])
			&& map[x + 1][y].getCellType() == "village") {
			up = findNumberConnected(x + 1, y, map, visitedVillages);
		}

		if (canLeft
			&& map[x][y - 1] != null 
			&& !visitedVillages.contains(map[x][y - 1])
			&& map[x][y - 1].getCellType() == "village") {
			up = findNumberConnected(x, y - 1, map, visitedVillages);
		}

		if (canRight 
			&& map[x][y + 1] != null
			&& !visitedVillages.contains(map[x][y + 1])
			&& (map[x][y + 1].getCellType() == "village")) {
			up = findNumberConnected(x, y + 1, map, visitedVillages);
		}

		return up + left + right + down + 1;
	}

	private boolean checkEdgePlacement(JavaCell[][] miniMap, Tile tile) {

		TileType[][] tileCells = tile.getTileCells();
		JavaCell[] cells = new JavaCell[4];
		int count = 0;

		int x = 0;

		for (int i = 0; i < miniMap.length; i++)
			for (int j = 0; j < miniMap[i].length; j++)
				if (tileCells[i][j] != null) {
					cells[x] = miniMap[i][j];
					x++;
				}

		int number;
		if (tile.getType() == "two") {
			number = 2;
		}

		else if (tile.getType() == "three") {
			number = 3;
		}

		else if (tile.getType() == "one") {
			number = 1;
		}

		else {
			number = 0;
		}

		System.out.println("in checkedge the outer cell length is: "
				+ outerCells.length);

		for (int i = 0; i < outerCells.length; i++) {
			for (int j = 0; j < cells.length; j++) {
				if (cells[j] != null && outerCells[i] != null
						&& cells[j].getX() == outerCells[i].getX()
						&& cells[j].getY() == outerCells[i].getY()) {
					count++;
				}
			}
		}

//		System.out.println("COUNT IS: " + count);

//		System.out.println("count: " + count + "number" + number);

		if (count == number) {
			return false;
		}

		return true;
	}

	public JavaCell getCellAtXY(int x, int y) {
		return map[x][y];
	}

	public boolean placeDeveloper(JavaCell jc, JavaPlayer player) {
		// Check with validity method first
		if (!canPlaceDeveloper(jc, player))
			return false;

		// Set developer on board
		player.placeDevOnBoard(jc);
		return true; // TODO Specific index?? cc: Cameron

	}

	public boolean canPlaceDeveloper(JavaCell locationCell, JavaPlayer player) {
		// Can only place on village or rice space
		if (!isTileOrLand(locationCell.getX(), locationCell.getY()))
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
		if (!player.decrementNActionPoints(
				locationCell.getActionPointsFromDeveloperMove(), false)
				|| !player.canPlaceDeveloperOnBoard()) // TODO: Check lowlands
														// or
			// mountains
			return false;

		return true;
	}

	public boolean removeDatDeveloperOffDaBoard(JavaCell jailCell,
			JavaPlayer theHomie) {
		// Check if its an border cell
		if (!jailCell.isBorder()) {
			// If its not on the border, needs to be next to it
			if (!jailCell.isNextToBorder())
				return false; // Can't do it, homie goes back to Jail

			// If it's next to the border, needs an adjacent empty tile in
			// border
			if (!hasAdjacentEmptyTile(jailCell))
				return false; // Can't do it, homie goes back to Jail
		}

		// Else, he is on the border, we can proceed with bail procedures
		if (!theHomie.decrementNActionPoints(
				jailCell.getActionPointsFromDeveloperMove(), false))
			return false; // Can't do it, homie goes back to Jail

		// By this point, we've made it through all the bail procedures and
		// homie has paid his dues (action points)
		// He is now free to go

		theHomie.removeDeveloperFromArray();

		return true; // The homie is free ~ ~ ~

	}

	public boolean hasAdjacentEmptyTile(JavaCell cell) {
		int x = cell.getX();
		int y = cell.getY();

		// Check if the cell is adjacent to any empty border cells
		if (x + 1 == 13) {
			if (map[13][y].getCellType().equals("blank"))
				return true;
		}

		if (x - 1 == 0) {
			if (map[0][y].getCellType().equals("blank"))
				return true;
		}
		if (y + 1 == 13) {
			if (map[x][13].getCellType().equals("blank"))
				return true;
		}
		if (y - 1 == 0) {
			if (map[x][0].getCellType().equals("blank"))
				return true;
		}

		return false;

	}

	public boolean isTileOrLand(int x, int y) {
		if (!(map[x][y].getCellType().equals("village") || map[x][y]
				.getCellType().equals("rice")))
			return false;
		return true;
	}

	// Method to determine cost of moving dev onto board: 2 from lowlands, 1
	// from mountains
	public int getCost(JavaCell cell) {
		int x = cell.getX();

		if (x <= 6)
			return 2;
		else
			return 1;
	}

	public boolean moveDeveloper(JavaPlayer player, LinkedList<JavaCell> path) {
		int pathSize = path.size();
		int actionPoints = 0;
		JavaCell currentCell = path.removeLast();
		JavaCell nextCell = path.removeLast();
		for (int i = 0; i < pathSize - 2; i++) {
			if (fromVillageToRice(currentCell, nextCell)) {
				actionPoints++;
			}

			currentCell = nextCell;
			nextCell = path.removeLast();
		}

		if (fromVillageToRice(currentCell, nextCell)) {
			actionPoints++;
		}

		if (player.decrementNActionPoints(actionPoints, false)) {
			player.associateDeveloperWithCell(nextCell);
			return true;
		}

		return false;
	}

//	public boolean addJavaCellToPath(JavaCell javaCell) {
//		int pathSize = path.size();
//		LinkedList<JavaCell> temp = new LinkedList<JavaCell>();
//
//		for (int i = 0; i < pathSize; i++) {
//			temp.push(path.pop());
//		}
//
//		JavaCell currentCell = temp.pop();
//		int count = 0;
//
//		while ((currentCell != javaCell) && count < pathSize - 1) {
//			path.push(currentCell);
//			currentCell = temp.pop();
//			count++;
//		}
//
//		path.push(currentCell);
//		return true;
//	}

	public boolean hasAdjacentLandSpaceTile(JavaCell cell) {
		int x = cell.getX();
		int y = cell.getY();

		// Check if the cell is adjacent to any border cells
		if (x + 1 == 13) {
			if (isTileOrLand(x + 1, y)) {
				return true;
			}
		}

		if (x - 1 == 0) {
			if (isTileOrLand(x - 1, y))
				return true;
		}

		if (y + 1 == 13) {
			if (isTileOrLand(x, y + 1)) {
				return true;
			}
		}

		if (y - 1 == 0) {
			if (isTileOrLand(x, y - 1)) {
				return true;
			}
		}

		return false;
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
			// Cell temp = connected.get(i);
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

	public boolean nextToIrrigation(int xC, int yC, Tile tile, JavaPlayer player, JavaCell[][] irrigationMap) {
		TileType[][] tileCells = tile.getTileCells();
		
		boolean right = false;
		boolean left = false;
		boolean up = false;
		boolean down = false;
		
		for (int i = 0; i < tileCells.length; i++) {
			for (int j = 0; j < tileCells[i].length; j++) {
				if (tileCells[i][j] != null) {
					if (yC < 13 && irrigationMap[xC][yC + 1] != null && irrigationMap[xC][yC + 1].getCellType().equals("irrigation")) {
						right = createNewBodyOfWater(xC, yC + 1);
					}

					if (yC > 0 && irrigationMap[xC][yC - 1] != null && irrigationMap[xC][yC - 1].getCellType().equals("irrigation")) {
						left = createNewBodyOfWater(xC, yC - 1);
					}

					if (xC < 13 && irrigationMap[xC + 1][yC] != null && irrigationMap[xC + 1][yC].getCellType().equals("irrigation")) {
						down = createNewBodyOfWater(xC + 1, yC);
					}

					if (xC > 0 && irrigationMap[xC - 1][yC] != null && irrigationMap[xC - 1][yC].getCellType().equals("irrigation")) {
						up = createNewBodyOfWater(xC - 1, yC);
					}


				}
			}
		}
		
		return right || left || down || up;
	}

	public boolean createNewBodyOfWater(int xC, int yC) {
		if (!isAlreadyInBodyOfWater(xC, yC)) {
			addToBodiesOfWater(xC, yC);
			return true;
		}
		
		return false;
	}
	
	private boolean isAlreadyInBodyOfWater(int xC, int yC) {
		for (int i = 0; i < bodiesOfWater.size(); i++) {
			for (int j = 0; j < bodiesOfWater.get(i).size(); j++) {
				if (bodiesOfWater.get(i).contains(irrigationMap[i][j])) {
					return true;
				}
			}
		}
				
		return false;
	}
	
	private void addToBodiesOfWater(int xC, int yC) {
		int bowSize = bodiesOfWater.size();
		bodiesOfWater.get(bowSize - 1).add(map[xC][yC]);
		
		boolean canUp = (xC - 1 >= 0);
		boolean canDown = (xC + 1 < map.length);
		boolean canRight = (yC + 1 < map[0].length);
		boolean canLeft = (yC - 1 >= 0);
		
		if (canUp
			&& map[xC - 1][yC] != null 
			&& !bodiesOfWater.get(bowSize - 1).contains(map[xC - 1][yC])
			&& map[xC - 1][yC].getCellType() == "irrigation") {
			addToBodiesOfWater(xC - 1, yC);
		}

		if (canDown
			&& map[xC + 1][yC] != null 
			&& !bodiesOfWater.get(bowSize - 1).contains(map[xC + 1][yC])
			&& map[xC + 1][yC].getCellType() == "irrigation") {
			addToBodiesOfWater(xC + 1, yC);
		}

		if (canLeft
			&& map[xC][yC - 1] != null 
			&& !bodiesOfWater.get(bowSize - 1).contains(map[xC][yC - 1])
			&& map[xC][yC - 1].getCellType() == "irrigation") {
			addToBodiesOfWater(xC, yC - 1);
		}

		if (canRight 
			&& map[xC][yC + 1] != null
			&& !bodiesOfWater.get(bowSize - 1).contains(map[xC][yC + 1])
			&& (map[xC][yC + 1].getCellType() == "irrigation")) {
			addToBodiesOfWater(xC, yC + 1);
		}

		return;
	}
	
	public ArrayList<JavaCell> collectCoastFromBodyOfWater(ArrayList<JavaCell> bodyOfWater) {
		ArrayList<JavaCell> coast = new ArrayList<JavaCell>();
		
		for(int i = 0; i < bodyOfWater.size(); i++) {
			JavaCell currentElement = bodyOfWater.get(i);
			int xC = currentElement.getX();
			int yC = currentElement.getY();
			
			boolean canUp = currentElement.getX() - 1 >= 0;
			boolean canDown = currentElement.getX() + 1 <= 0;
			boolean canLeft = currentElement.getY() - 1 >= 0;
			boolean canRight = currentElement.getY() + 1 >= 0;
			
			if (canUp
				&& map[xC - 1][yC] != null 
				&& !coast.contains(map[xC - 1][yC])
				&& map[xC - 1][yC].isLandOrPalaceOrIrrigation()) {
				coast.add(map[xC - 1][yC]);
			}

			if (canDown
				&& map[xC + 1][yC] != null 
				&& !coast.contains(map[xC + 1][yC])
				&& map[xC + 1][yC].isLandOrPalaceOrIrrigation()) {
				coast.add(map[xC + 1][yC]);
			}

			if (canLeft
				&& map[xC][yC - 1] != null 
				&& !coast.contains(map[xC][yC - 1])
				&& map[xC][yC - 1].isLandOrPalaceOrIrrigation()) {
				coast.add(map[xC][yC - 1]);
			}

			if (canRight 
				&& map[xC][yC + 1] != null 
				&& !coast.contains(map[xC][yC + 1])
				&& map[xC][yC + 1].isLandOrPalaceOrIrrigation()) {
				coast.add(map[xC][yC + 1]);
			}
		}
		
		return coast;
	}
	
	public JavaPlayer playerWithHighestRankedDeveloperSurroundingBodyOfWater(ArrayList<JavaCell> coast, LinkedList<Developer> gameDevelopers) {
		int dontSearchAtOrAboveThisElevation = 200;
		int maxElevation = 0;
		HashMap<JavaPlayer, Integer> playersWithElevation = new HashMap<JavaPlayer, Integer>();
		while (dontSearchAtOrAboveThisElevation > 0) {
			for (int i = 0; i < coast.size(); i++) {
				if (maxElevation < coast.get(i).getElevation() && coast.get(i).getElevation() < dontSearchAtOrAboveThisElevation) {
					maxElevation = coast.get(i).getElevation();
				}
			}
			
			for (int i = 0; i < coast.size(); i++) {
				if (maxElevation == coast.get(i).getElevation()) {
					for (int j = 0; j < gameDevelopers.size(); i++) {
						if (gameDevelopers.get(j).getX() == coast.get(i).getX()
							&& gameDevelopers.get(j).getY() == coast.get(i).getY()) {
						    	playersWithElevation.put(gameDevelopers.get(j).getOwner(), playersWithElevation.get(gameDevelopers.get(j).getOwner()) + 1);
						    	break;
						}
					}
				}
			}
			
			
			
		}
		
		return new JavaPlayer("name", "color");
	}
	
	public boolean awardFamePointsForSurroundingBodyOfWater(int numSurroundedIrrigationTiles, JavaPlayer player) {
		player.changeFamePoints(numSurroundedIrrigationTiles * 3);
		return false;
	}

	public boolean fromVillageToRice(JavaCell jc1, JavaCell jc2) {
		if (jc1.getCellType().equals("village")
				&& jc2.getCellType().equals("rice")
				|| jc1.getCellType().equals("rice")
				&& jc2.getCellType().equals("village")) {
			return true;
		}

		return false;
	}

	public String toString() {
		String s = "";

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				s += map[i][j].getElevation() + " ";
			}
			s += "\n";
		}
		return s;
	}

	@Override
	public String serialize() {
		return Json.jsonObject(Json.jsonMembers(
				Json.jsonPair("map", Json.serializeArray(map)),
				//Json.jsonPair("path", Json.serializeArray(path)),
				Json.jsonPair("connectedPalaces",
						Json.serializeArray(connectedPalaces))));
	}

	@Override
	public BoardModel loadObject(JsonObject json) {
		return this;
	}

	public int getNextCellId() {
		return ++cellId;
	}

	public int getElevationAtCellXY(int x, int y) {
		return map[x][y].getElevation();
	}
	
	public ArrayList<JavaCell> getPalacesOnBoard()	
	{
		ArrayList<JavaCell> list = new ArrayList<JavaCell>();
		
		for (int i = 0; i < map.length; i++ )
		{
			for (int j = 0; j < map[0].length; j++)
			{
				if(map[i][j].getCellType().toString().startsWith("palace"))
				{
					list.add(map[i][j]);
				}
			}
		}
		
		return list;
	}

	public void reset() {
		this.map = new JavaCell[14][14];
		this.outerCells = new JavaCell[52];
		this.irrigationMap = new JavaCell[14][14];
		this.path = new LinkedList<JavaCell>();
		connectedPalaces = new ArrayList<JavaCell>();
		visitedVillages = new ArrayList<JavaCell>();
		bodiesOfWater = new ArrayList<ArrayList<JavaCell>>();
		//coast = new ArrayList<JavaCell>();
		cellId = 0;

		int i = 0;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				map[x][y] = new JavaCell(x, y, 0);
				irrigationMap[x][y] = new JavaCell(x, y, 0);

				if ((x == 0 || x == 13)) {
					outerCells[i] = map[x][y];
					i++;
				}

				else if ((y == 0 || y == 13) && (x != 0 && x != 13)) {
					outerCells[i] = map[x][y];
					i++;
				}
			}
		}
	}
}
