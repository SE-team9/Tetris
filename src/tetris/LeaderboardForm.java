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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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

	// ESC를 누르면 시작 화면으로 이동
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

	// 파일로부터 데이터 가져오기 (de-serialization)
	private void initTableData() {
		String header[] = { "Player", "Score", "Level" };
		// Object contents[][] = {};
		String contents[][] = {};

		Vector columnIdentifier = new Vector();
		columnIdentifier.add("Player");
		columnIdentifier.add("Score");
		columnIdentifier.add("Level");

		tm = new DefaultTableModel(contents, header) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}

			@Override // score--> Int 형으로
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(1, columnIndex).getClass();
			}
		};

		try {
			FileInputStream fs = new FileInputStream(leaderboardFile);
			ObjectInputStream os = new ObjectInputStream(fs);

			// 점수를 문자열이 아닌 int 타입으로 읽어야 두자리 이상의 숫자도 정렬 가능!
			tm.setDataVector((Vector<Vector>) os.readObject(), columnIdentifier);

			os.close();
			fs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		initTable(tm);
	}

	private void initTable(DefaultTableModel tm) {
		leaderboard = new JTable(tm);
		
		// 내용물 중앙 정렬
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = leaderboard.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {

			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);

		}

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

	// 파일에 데이터 저장하기 (serialization)
	private void saveLeaderboard() {
		try {
			FileOutputStream fs = new FileOutputStream(leaderboardFile);
			ObjectOutputStream os = new ObjectOutputStream(fs);

			os.writeObject(tm.getDataVector());

			os.close();
			fs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 게임 종료되면 플레이어 기록 추가하면서, 동시에 스코어보드 보여주기
	public void addPlayer(String playerName, int score, int gameLevel) {
		String strGameLevel;
		if (gameLevel == 0)
			strGameLevel = "Easy";
		else if (gameLevel == 1)
			strGameLevel = "Normal";
		else
			strGameLevel = "Hard";

		tm.addRow(new Object[] { playerName, score, strGameLevel });
		sorter.sort();
		saveLeaderboard();

		this.setVisible(true); // 스코어보드 표시
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
