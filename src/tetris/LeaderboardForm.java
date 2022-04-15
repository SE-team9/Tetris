package tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class LeaderboardForm extends JFrame {
	private JTable leaderboard;
	private DefaultTableModel tm;
	private String leaderboardFile = "leaderboard";
	private TableRowSorter<TableModel> sorter;

	public LeaderboardForm() {
		initThisFrame();
		initTableData();
		initTableSorter();
		
		initControls();
	}
	
	// ESC�� ������ ���� ȭ������ �̵�
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToMainMenu();
			}
		});
	}
	
	private void goToMainMenu() {
		this.setVisible(false);
		Tetris.showStartup();
	}

	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}
	
	// ���Ϸκ��� ������ �������� (de-serialization)
	private void initTableData() {
		String header[] = {"Player", "Score"};
		Object contents[][] = {};
		
		Vector columnIdentifier = new Vector();
		columnIdentifier.add("Palyer");
		columnIdentifier.add("Score");
		
		tm = new DefaultTableModel(contents, header){
			@Override
		     public boolean isCellEditable(int row, int column) {
		        // all cells false
		        return false;
		     }
		};
		
		try {
			FileInputStream fs = new FileInputStream(leaderboardFile);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			// ������ ���ڿ��� �ƴ� int Ÿ������ �о�� ���ڸ� �̻��� ���ڵ� ���� ����!
			tm.setDataVector((Vector<Vector>) os.readObject(), columnIdentifier);
			
			os.close();
			fs.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
		
		initTable(tm);
	}
	
	private void initTable(DefaultTableModel tm) {
		leaderboard = new JTable(tm);
		
		JScrollPane scrollPane = new JScrollPane(leaderboard);
		scrollPane.setBounds(20, 70, 542, 314);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
	}
	
	// ���Ͽ� ������ �����ϱ� (serialization)
	private void saveLeaderboard() {
		try {
			FileOutputStream fs = new FileOutputStream(leaderboardFile);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeObject(tm.getDataVector());
			
			os.close();
			fs.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayer(String playerName, int score) {
		tm.addRow(new Object[] { playerName, score });
		sorter.sort();
		saveLeaderboard();
		
		this.setVisible(true);
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LeaderboardForm().setVisible(true);
			}
		});
	}
}
