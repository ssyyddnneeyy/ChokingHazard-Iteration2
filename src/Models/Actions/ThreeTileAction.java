package Models.Actions;

import Helpers.JsonObject;
import Models.GameModel;
import Models.JavaCell;

public class ThreeTileAction extends RotatableComponentAction {

	public boolean isFinalRound;

	public ThreeTileAction(int actionID, int famePointsEarned, int x, int y, int rotationState, boolean isFinalRound, int elevation, JavaCell[] cell) {
		super(actionID, famePointsEarned, x, y, rotationState, elevation,  cell);
		this.isFinalRound = isFinalRound;
		this.imageKey = "threeTile";
	}
	
	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action loadObject(JsonObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redo(GameModel game) {
		// TODO Auto-generated method stub
		
	}
}
