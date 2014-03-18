package Models.Actions;

import Helpers.JsonObject;
import Models.GameModel;
import Models.JavaCell;

public class OneSpaceTileAction extends NonRotatableComponentAction {

	public JavaCell cell;

	public OneSpaceTileAction(int actionID, int famePointsEarned, int actionPointsEarned, int x, int y, JavaCell cell) {
		super(actionID, famePointsEarned, actionPointsEarned, x, y);
		this.cell = cell;
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
		
	}
	
}
   