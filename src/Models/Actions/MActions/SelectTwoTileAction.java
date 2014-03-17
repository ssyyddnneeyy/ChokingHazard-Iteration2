package Models.Actions.MActions;

public class SelectTwoTileAction extends SelectRotatableTileAction {

	public SelectTwoTileAction(String imageKey) {
		super(imageKey);
		//System.out.println("(in SelectTwoTileAction)New 2 tile is created with rotation state " + rotationState + " and center " + x +"," + y);
	}

	protected boolean isRotatableComponentOnBoard(int x, int y, int rotationState){
		if(rotationState == 0){
			if((x < 0 || x > 12) || (y < 0 || y > 13)){ //check x changes
				//System.out.println("(in SelectTwoTileAction)New 2 tile center is not on board. State " + rotationState + " center " + x + "," + y);
				return false;
			}
		}
		else if(rotationState == 1){
			//System.out.println("(in SelectTwoTileAction)New 2 tile center is not on board. State " + rotationState + " center " + x + "," + y);
			if((x < 0 || x > 13) || (y < 0 || y > 12)){ //check x changes
				return false;
			}
			
		}
		else if(rotationState == 2){
			//System.out.println("(in SelectTwoTileAction)New 2 tile center is not on board. State " + rotationState + " center " + x + "," + y);
			if((x < 1 || x > 13) || (y < 0 || y > 13)){ //check x changes
				return false;
			}
		}
		else if(rotationState == 3){
			//System.out.println("(in SelectTwoTileAction)New 2 tile center is not on board. State " + rotationState + " center " + x + "," + y);
			if((x < 0 || x > 13) || (y < 1 || y > 13)){ //check x changes
				return false;
			}
		}
		else{
			return false;
		}
		return true;
	}
}
