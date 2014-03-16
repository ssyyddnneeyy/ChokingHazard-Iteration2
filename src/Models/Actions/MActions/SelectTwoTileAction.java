package Models.Actions.MActions;

import Helpers.Json;
import Helpers.JsonObject;


public class SelectTwoTileAction extends SelectRotatableTileAction {

	public SelectTwoTileAction(String imageKey) {
		super(imageKey);
	}

	protected boolean isRotatableComponentOnBoard(int x, int y, int rotationState){
		if(rotationState == 0){
			if((x < 0 || x > 13) && (y < 0 || y > 12)){ //check x changes
				return false;
			}
		}
		else if(rotationState == 1){
			if((x < 0 || x > 12) && (y < 0 || y > 13)){ //check x changes
				return false;
			}
			
		}
		else if(rotationState == 2){
			if((x < 0 || x > 12) && (y < 0 || y > 13)){ //check x changes
				return false;
			}
		}
		else if(rotationState == 3){
			if((x < 1 || x > 13) && (y < 0 || y > 13)){ //check x changes
				return false;
			}
		}
		else{
			return false;
		}
		return true;
	}

	public SelectTwoTileAction loadObject(JsonObject json) {
		// TODO Auto-generated method stub
		return null;
	}
}
