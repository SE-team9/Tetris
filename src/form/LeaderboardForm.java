package form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import tetris.Tetris;

public class LeaderboardForm extends JFrame {
	private JTable leaderboard;
	private DefaultTableModel tm;
	private String leaderboardFile = "learderboard";
	private TableRowSorter<TableModel> sorter;
	
	//private List<Integer> highRank = new ArrayList<Integer>();
	private final static int MAX = 3;

	public LeaderboardForm() {
		initThisFrame();
		initControls();
		
		initTableData();
		initTable();
		initTableSorter();
	}
	
	// ���Ͽ��� ������ �����ͷ� ���̺� �ʱ�ȭ
	private void initTableData() {
		String header[] = {"Player", "Score"};
		Object contents[][] = {};
		
		Vector ci = new Vector();
		ci.add("Palyer");
		ci.add("Score");
		
		tm = new DefaultTableModel(contents, header){
			@Override
		     public boolean isCellEditable(int row, int column) {
		        return false; // ���̺��� ��� �� ���� �Ұ����ϵ��� 
		     }
		};
		
		try {
			FileInputStream fs = new FileInputStream(leaderboardFile);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			// Score�� ���ڿ��� �ƴ϶� ���� Ÿ������ �о�;� 
			// ���ڸ� �̻��� ���ڵ� ���� �����ϴٰ� �Ѵ�.
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);
			
			os.close();
			fs.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// ȭ�鿡 ���̺� ǥ��
	private void initTable() {
		leaderboard = new JTable(tm);
		JScrollPane scrollPane = new JScrollPane(leaderboard);
		scrollPane.setBounds(20, 70, 542, 314);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	// ������ �������� �������� �����ϴ� Sorter �ʱ�ȭ
	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
	}
	
	// ���Ͽ� ���̺� ������ �����ϱ�
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
	
	// ���� ����Ǹ� �÷��̾� ��� �߰��ϸ鼭, ���ھ�� �����ֱ� 
	// 3������� �ϴ� �� ����ϰ�, �� ���Ŀ� ���� ������ ������ �� 3���� ���� ��쿡�� ����Ѵ�!
	public void addPlayer(String playerName, int score) {
		if(tm.getRowCount() < MAX) {
			
			tm.addRow(new Object[] { playerName, score });
			sorter.sort();
			saveLeaderboard(); // �ִ� 3����� �߰�
			
			// 1. ��� ���̺� ����� ���� �����ؼ� ǥ��
			this.setVisible(true);
			
			
		}else { // ���� ������ ������ �����ǿ� ����� üũ
			
			
		}
	}
	
//	@Override
//	public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){  
//	    Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);  
//
//	    if(rowIndex % 2 == 0) {  
//	       componenet.setBackground(Color.RED);  
//	    } else {
//	       componenet.setBackground(Color.GREEN);
//	    }
//	    return componenet;
//	} 
	
	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}
	
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();
		
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Tetris.showStartup();
			}
		});
	}
	
	// LeaderboardForm ������ ����
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LeaderboardForm().setVisible(true);
			}
		});
	}
}
