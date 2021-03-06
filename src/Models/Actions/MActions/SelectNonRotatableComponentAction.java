package Models.Actions.MActions;

public abstract class SelectNonRotatableComponentAction extends MAction {

	public SelectNonRotatableComponentAction(String imageKey) {
		super(imageKey);
	}

	public boolean pressSpace() { //should do nothing.
		return false;
	}
	
	public boolean pressArrow(int xChange, int yChange) {
		int newX = x + xChange;
		int newY = y + yChange;
		if(isNonRotatableComponentOnBoard(newX, newY)){
			x = newX;
			y = newY;
			return true;
		}
		else{
			return false;
		}
	}
	
	public abstract boolean isNonRotatableComponentOnBoard(int x, int y);

}
