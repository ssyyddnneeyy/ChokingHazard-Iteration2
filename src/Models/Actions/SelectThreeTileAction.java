package Models.Actions;

public class SelectThreeTileAction extends MAction {

	int rotationState = 0;
	int x = 1;
	int y = 1;
	
	public SelectThreeTileAction(int playerIndex, int x, int y, int rotationState) {
		super(playerIndex);
		// TODO Auto-generated constructor stub
		
	}

	public void pressSpace(){
		rotationState += 1 % 4;
	}
	
	public void pressCursor(){
		
	}
	
	
	
	
}