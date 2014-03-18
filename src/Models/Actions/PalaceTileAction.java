package Models.Actions;

import Models.JavaCell;

public class PalaceTileAction extends OneSpaceTileAction {
	
	int value;

	public PalaceTileAction(int actionID, int famePointsEarned, int x, int y, int value, JavaCell cell) {
		super(actionID, famePointsEarned, x, y, cell);
		this.value = value;
		this.imageKey = "palace" + value + "Tile";
	}

	public int getValueOfPalace() {
		return value;
	}
}
