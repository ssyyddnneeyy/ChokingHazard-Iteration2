package Models.Actions.MActions;

import Models.GameModel;
import Models.Actions.Action;
import Models.Actions.IrrigationTileAction;

public class SelectIrrigationTileAction extends SelectOneSpaceTileAction {

	public SelectIrrigationTileAction(String imageKey) {
		super(imageKey);
		// TODO Auto-generated constructor stub
	}
	
	public Action pressEnter(GameModel game) {
		return new IrrigationTileAction(-1, x, y);
	}
}
