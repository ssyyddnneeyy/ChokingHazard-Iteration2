package FestivalMiniGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class HoldFestivalPanel extends JPanel{
	private HashMap<String, String> imageSourceHashMap;
	private ArrayList<HoldFestivalPlayerPanel> players;
	private JPanel[] playedCardsPanels;
	private JPanel centerPanel;
	private HoldFestivalController controller;
	private JButton dropFromFestival;
	
	public HoldFestivalPanel(ArrayList<JavaFestivalPlayer> festivalPlayers, int currentPlayerIndex, String festivalHashKey, int palaceValue){
		super(new BorderLayout());
		
		initHashMap();
		setPreferredSize(new Dimension(780, 780));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setBackground(Color.WHITE);
		
		dropFromFestival = new JButton("Drop Out of Festival");
		dropFromFestival.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.dropPlayerFromFestival();
			}
		});
		
		initPanels(festivalPlayers, currentPlayerIndex, festivalHashKey);
	}
	
	private void initPanels(ArrayList<JavaFestivalPlayer> festivalPlayers, int currentPlayerIndex, String festivalHashKey){
		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(550, 550));
		centerPanel.setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.add(palaceLabel(imageSourceHashMap.get("label_"+festivalHashKey)), BorderLayout.CENTER);

		//init player panels and add them to the panel
		players = new ArrayList<HoldFestivalPlayerPanel>(festivalPlayers.size());
		playedCardsPanels = new JPanel[festivalPlayers.size()];
		for(int i = 0; i < festivalPlayers.size(); ++i){
			players.add(new HoldFestivalPlayerPanel(i, festivalPlayers.get(i).getName(), festivalPlayers.get(i).getColor(), festivalPlayers.get(i).getNumPalaceCards(), imageSourceHashMap));
			playedCardsPanels[i] = new JPanel();
			if(i == 0){
				add(players.get(i), BorderLayout.NORTH);
				playedCardsPanels[i].setPreferredSize(new Dimension(500, 100));
				centerPanel.add(playedCardsPanels[i], BorderLayout.NORTH);
			}
			else if (i == 1){
				add(players.get(i), BorderLayout.EAST);
				playedCardsPanels[i].setPreferredSize(new Dimension(100, 500));
				centerPanel.add(playedCardsPanels[i], BorderLayout.EAST);
			}
			else if (i == 2){
				add(players.get(i), BorderLayout.SOUTH);
				playedCardsPanels[i].setPreferredSize(new Dimension(500, 100));
				centerPanel.add(playedCardsPanels[i], BorderLayout.SOUTH);
			}
			else{
				add(players.get(i), BorderLayout.WEST);
				playedCardsPanels[i].setPreferredSize(new Dimension(100, 500));
				centerPanel.add(playedCardsPanels[i], BorderLayout.WEST);
			}
		}
		
		//set currentIndex as the one who is selected
		players.get(currentPlayerIndex).setCurrentPlayer(true);
	}
	
	public void setFestivalController(HoldFestivalController c){
		this.controller = c;
	}
	
	private JLabel palaceLabel(String src){
		JLabel label= new JLabel();
		label.setIcon(new ImageIcon(src));
		label.setPreferredSize(new Dimension(60, 100));
		return label;
	}
	
	public void endPlayerTurn(int index){
		players.get(index).setCurrentPlayer(false);
	}
	
	public void startPlayerTurn(int index){
		players.get(index).setCurrentPlayer(true);
		dropFromFestival.setVisible(true);
	}
	
	public void tabThroughPlayerPalaceCards(int indexOfCard, int numCards, int indexOfPlayer, String hashKey){
		players.get(indexOfPlayer).selectCardAtIndex(indexOfCard, numCards, hashKey);
	}
	
	public void playPalaceCardAtIndex(int indexOfPlayer, String hashKey, int newNumCards){
		players.get(indexOfPlayer).clearSelectedCard(newNumCards);
		addCardToCenterPanelWithImage(hashKey, indexOfPlayer);
	}
	
	public void cancelTabbing(int index, int numCards){
		players.get(index).clearSelectedCard(numCards);
	}
	
	public void dropCurrentPlayer(int index){
		players.get(index).setVisible(false);
		players.remove(index);
	}
	
	public void displayThatUserShouldDropOutOfFestival(int index){
		//TODO test to see if this works
		players.get(index).add(dropFromFestival);
	}
	
	public boolean askIfWouldLikeToSpiltWinnings(ArrayList<JavaFestivalPlayer> playersTied){
		String players = playersTied.get(0).getName();
		for(int i = 1; i < playersTied.size(); i++){
			if(i == (playersTied.size()-1)){
				players = players+" and "+playersTied.get(i).getName();
			}
			else{
				players = players+", "+playersTied.get(i).getName();
			}
		}
		int split = JOptionPane.showConfirmDialog(null, players+" are currently tied - would you like to split the winnings?\nIf not, there will be another Festival round", "Replay Mode", JOptionPane.YES_NO_OPTION);
		if(split == 0)
			return true;
		return false;
	}
	
	public void tellUserThatHeHasToPlayAPalaceCardOrDropOut(){
		JOptionPane.showMessageDialog(null, "You need to either play a Palace card or drop out of the festival", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void addCardToCenterPanelWithImage(String imageHashKey, int indexOfPlayer){
		playedCardsPanels[indexOfPlayer].add(palaceLabel(imageSourceHashMap.get("label_"+imageHashKey)));
		updateUI();
	}
	
	private void initHashMap(){
		File imageSourceFile = null;
		this.imageSourceHashMap = new HashMap<String, String>();
		try{
			imageSourceFile = new File("bin/files/FestivalImageStrings.txt");
			BufferedReader fileReader = new BufferedReader(new FileReader(imageSourceFile));
			String line = "";
			while((line = fileReader.readLine()) != null){
				String[] hash = line.split(" ");
				imageSourceHashMap.put(hash[0], hash[1]);
			}
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
