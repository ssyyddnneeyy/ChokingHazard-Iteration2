package Models.Actions;

import Helpers.JsonObject;
import Models.GameModel;

public class TakeDeveloperOffBoardAction extends OneSpaceComponentAction {

	public TakeDeveloperOffBoardAction(int actionID, int famePointsEarned) {
		super(actionID, famePointsEarned);
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
