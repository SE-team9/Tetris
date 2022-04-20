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
	
	// 파일에서 가져온 데이터로 테이블 초기화
	private void initTableData() {
		String header[] = {"Player", "Score"};
		Object contents[][] = {};
		
		Vector ci = new Vector();
		ci.add("Palyer");
		ci.add("Score");
		
		tm = new DefaultTableModel(contents, header){
			@Override
		     public boolean isCellEditable(int row, int column) {
		        return false; // 테이블의 모든 셀 편집 불가능하도록 
		     }
		};
		
		try {
			FileInputStream fs = new FileInputStream(leaderboardFile);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			// Score를 문자열이 아니라 정수 타입으로 읽어와야 
			// 두자리 이상의 숫자도 정렬 가능하다고 한다.
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);
			
			os.close();
			fs.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 화면에 테이블 표시
	private void initTable() {
		leaderboard = new JTable(tm);
		JScrollPane scrollPane = new JScrollPane(leaderboard);
		scrollPane.setBounds(20, 70, 542, 314);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	// 점수를 기준으로 내림차순 정렬하는 Sorter 초기화
	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
	}
	
	// 파일에 테이블 데이터 저장하기
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
	
	// 게임 종료되면 플레이어 기록 추가하면서, 스코어보드 보여주기 
	// 3명까지는 일단 다 기록하고, 그 이후에 새로 들어오는 점수가 그 3명보다 높은 경우에만 기록한다!
	public void addPlayer(String playerName, int score) {
		if(tm.getRowCount() < MAX) {
			
			tm.addRow(new Object[] { playerName, score });
			sorter.sort();
			saveLeaderboard(); // 최대 3명까지 추가
			
			// 1. 방금 테이블에 저장된 라인 강조해서 표시
			this.setVisible(true);
			
			
		}else { // 현재 유저의 점수가 순위권에 드는지 체크
			
			
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
	
	// LeaderboardForm 프레임 실행
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LeaderboardForm().setVisible(true);
			}
		});
	}
}
