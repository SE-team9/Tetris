package form;

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
import javax.swing.JLabel;
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

import tetris.Tetris;

// TODO: 스코어 보드 기록 초기화

public class LeaderboardForm extends JFrame {
	private int w, h;
	private JTable leaderboard;
	private DefaultTableModel tm;
	private String[] leaderboardFile = { "leaderboardFile_Normal", "leaderboardFile_Item" };
	private TableRowSorter<TableModel> sorter;
	private JScrollPane scrollLeaderboard;

	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private JLabel[] lblGameMode;
	private String gameMode[] = { "Normal Mode", "Item Mode" };
	private int curCol;

	public LeaderboardForm() {
		this.w = 600;
		this.h = 450;
		initComponents(w, h);
		
		initTableData(0);
		initLeaderboard(tm);
		initTableSorter();
		initScrollLeaderboard();

		initControls();
	}

	public void initComponents(int w, int h) {
		this.w = w;
		this.h = h;
		
		initThisFrame();
		initLabel();
	}

	// ---------------------------------------------------------------------초기화
	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

    // 모드, 난이도 표시 레이블 초기화 
	private void initLabel() {
		lblGameMode = new JLabel[2];
		for (int i = 0; i < lblGameMode.length; i++) {
			lblGameMode[i] = new JLabel(gameMode[i]);
			lblGameMode[i].setHorizontalAlignment(JLabel.CENTER);
			lblGameMode[i].setBounds(w / 3, h / 30, 200, 30);
			lblGameMode[i].setVisible(false);
			this.add(lblGameMode[i]);
		}
		lblGameMode[0].setVisible(true);
		
		lblArrow[0].setBounds(w/3 + 10, h/30, 30, 30);
		lblArrow[1].setBounds(w - (w/3 + 20), h/30, 30, 30);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}
  
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveNextMode();
			}
		});
		
		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePreviousMode();
			}
		});
		
		am.put("back", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Tetris.showStartup();
			}
		});
	}

	private void moveNextMode() {
		lblGameMode[curCol].setVisible(false);
		scrollLeaderboard.setVisible(false);

		curCol++;
		if (curCol > lblGameMode.length - 1) {
			curCol = 0;
		}

		lblGameMode[curCol].setVisible(true);

		remakeScrollLeaderboard(curCol);
	}

	private void movePreviousMode() {
		lblGameMode[curCol].setVisible(false);
		scrollLeaderboard.setVisible(false);

		curCol--;
		if (curCol < 0) {
			curCol = lblGameMode.length - 1;
		}

		lblGameMode[curCol].setVisible(true);

		remakeScrollLeaderboard(curCol);
	}

	// 현재 모드에 맞는 파일에 따라 보드를 다시 생성한다. 
	private void remakeScrollLeaderboard(int mode) {
		this.remove(scrollLeaderboard);
		initTableData(mode);
		initLeaderboard(tm);
		initTableSorter();
		sorter.sort();
		initScrollLeaderboard();
	}

	// 파일로부터 데이터 가져오기 (de-serialization)
	private void initTableData(int mode) {
		String header[] = { "Player", "Score", "Level" };
		String contents[][] = {};

		tm = new DefaultTableModel(contents, header) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}

			@Override // score --> Int 형으로
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1)
					return Integer.class;

				return super.getColumnClass(columnIndex).getClass();
			}
		};
		
		Vector ci = new Vector();
		ci.add("Player");
		ci.add("Score");
		ci.add("Level");

		try {
			FileInputStream fs = new FileInputStream(leaderboardFile[mode]);
			ObjectInputStream os = new ObjectInputStream(fs);

			// 점수를 문자열이 아닌 int 타입으로 읽어야 두자리 이상의 숫자도 정렬 가능!
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);

			os.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLeaderboard(DefaultTableModel tm) {
		leaderboard = new JTable(tm);
		leaderboard.setFocusable(false);

		// 내용 중앙 정렬
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = leaderboard.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {

			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
	}

	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);

		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
	}

	// 생성된 테이블 데이터를 사용하여 리더보드 생성
	private void initScrollLeaderboard() {
		scrollLeaderboard = new JScrollPane(leaderboard);
		scrollLeaderboard.setBounds(w / 30, h / 10, w - 50, h - 100);
		this.add(scrollLeaderboard);
	}

	// 파일에 데이터 저장하기 (serialization)
	private void saveLeaderboard() {
		try {
			FileOutputStream fs = new FileOutputStream(leaderboardFile[Tetris.getGameMode()]);
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

		this.remove(scrollLeaderboard); 				// 현재 리더보드 제거
		initTableData(Tetris.getGameMode()); 			// 현재 모드에 맞는 파일 데이터 읽어오기
		tm.addRow(new Object[] { playerName, score, strGameLevel }); 	// 읽어온 데이터에 기록 추가
		initLeaderboard(tm); 							// 읽어온 데이터로 리더보드 생성
		initTableSorter();								// 리더보드 정렬
		sorter.sort(); 									// 리더보드 정렬
		saveLeaderboard(); 								// 파일에 저장

		initScrollLeaderboard(); // leaderboard를 기반으로 보드 생성

		curCol = Tetris.getGameMode();

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