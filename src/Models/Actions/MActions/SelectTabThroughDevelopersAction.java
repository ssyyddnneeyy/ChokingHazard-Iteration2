package Models.Actions.MActions;

import Models.Developer;
import Models.GameModel;
import Models.JavaCell;
import Models.JavaPlayer;
import Models.Actions.Action;
import Models.Actions.TakeDeveloperOffBoardAction;

public class SelectTabThroughDevelopersAction extends MAction {

	int tabCount = 0;
	int numOfDevelopers;
	Developer[] developersArray;

	public SelectTabThroughDevelopersAction(String imageKey, Developer[] developersArray) {
		super(imageKey);
		this.numOfDevelopers = developersArray.length;
		this.developersArray = developersArray;
		this.x = this.getXOfDeveloperAtIndexTabCount();
		this.y = this.getYOfDeveloperAtIndexTabCount();
	}

	
	public boolean pressSpace(){
		return false;
	}
	
	public boolean pressArrow(int xChange, int yChange){
		return false;
	}
	
	public boolean pressTab(){
		if(numOfDevelopers > 0){
			System.out.println("SelTab numDev > 0");
			//tabCount += 1;
			tabCount = ++tabCount % numOfDevelopers;
			System.out.println("tab count it " + tabCount +  " number of developers on board is " + numOfDevelopers );
			this.x = getXOfDeveloperAtIndexTabCount();
			this.y = getYOfDeveloperAtIndexTabCount();
			return true;
		}
		System.out.println("SelTab not numDev > 0");
		return false;
	}
	
	public Action pressDelete(GameModel game){
		//Brett is writing the method below right now
		if(true){//game.removeDeveloperOffBoard(this.getXOfDeveloperAtIndexTabCount(), this.getYOfDeveloperAtIndexTabCount())){
			return new TakeDeveloperOffBoardAction(-1,this.getXOfDeveloperAtIndexTabCount(), this.getYOfDeveloperAtIndexTabCount() );//JavaCell javaCell, JavaPlayer player
		}
		else
			return null;
	}
	
	public Action pressEnter(){
		return null;
	}
	
	public MAction pressM(){
		return null; //new SelectMoveDeveloperAroundBoardAction(this.imageKey, this.getXOfDeveloperAtIndexTabCount(), this.getYOfDeveloperAtIndexTabCount());
	}
	
	private int getXOfDeveloperAtIndexTabCount(){
		return developersArray[tabCount].getX();
	}
	
	private int getYOfDeveloperAtIndexTabCount(){
		return developersArray[tabCount].getY();
	}


	@Override
	public Action pressDelete() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Action pressEnter(GameModel game) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
}
