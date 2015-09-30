package partybattle.gui;

import partybattle.Board;
import partybattle.Guest;
import partybattle.PartyLog;
import partybattle.Position;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.*;

public class BattleWindow extends JFrame {
	private static final long serialVersionUID = 3427642868750313104L;

	public Board board;
	
	private StatusRows statusRows = new StatusRows();
	private BattleGrid grid;
	private Random rng;
	
	public BattleWindow(Board board) {
		
		super("PartyBattle");
		
		this.board = board;
		
		grid = new BattleGrid(this);
		rng = new Random(1);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		add(grid, BorderLayout.CENTER);
		add(statusRows, BorderLayout.NORTH);

		setPreferredSize(new Dimension(800, 600));
		
        pack();
	}
	
	
	public void shootAt(int col, int row) {
		Guest guest = board.guestAt(col, row);
		if (guest == null) {
			status("Miss!");
			grid.setImageAt(col, row, Assets.missImage);
			return;
		}

		if (guest.isSpecial()) {
			status("A guest of honor was hit! Bouncing...");
			shootAt(rng.nextInt(board.COLS), rng.nextInt(board.ROWS));
		} else {
			grid.setImageAt(col, row, Assets.sunkenBoats[guest.boatPos()]);
			shootGuest(guest);
		}
	}
	
	
	
	private void shootGuest(Guest guest) {
		guest.alive = false;
		
		status("Waa! " + guest.name + " was hit!");
		
		Position crewmatePos = guest.crewmatePosition();
		Guest crewmate = board.guestAt(crewmatePos);
		
		if (crewmate.alive) {
			status(crewmate.name + " is now allowed to shoot!");
			grid.setImageAt(crewmatePos, Assets.boats[crewmate.boatPos()]);
		} else {
			status("The ship of " + guest.name + " and " + crewmate.name + "is no more! The guest of honor may take a shot!");
		}
	}
	
	
	private void status(String msg) {
		PartyLog.log(msg);
		statusRows.update(msg);
	}
}