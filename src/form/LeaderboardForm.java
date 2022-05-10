package form;
import tetris.*;

import java.awt.BorderLayout;
import java.awt.Font;
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

public class LeaderboardForm extends JFrame {
	private int w, h;
	private JTable leaderboard;
	private DefaultTableModel tm;
	private String[] leaderboardFile = { "leaderboardFile_Normal", "leaderboardFile_Item" };
	private TableRowSorter<TableModel> sorter;
	private JScrollPane scrollLeaderboard; // 화면 범위를 넘어갈 때 스크롤 가능하도록

	private JLabel lblGameMode;
	private String gameMode[] = { "Normal Mode", "Item Mode" };
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private int curMode;
	private Vector ci;
	
	// Tetris에서 객체가 처음 생성될 때 초기화 작업 
	public LeaderboardForm() {
		this.w = 600;
		this.h = 450;
		this.curMode = 0;
		initComponents(w, h, 0);
		
		// 외부에서 호출할 때 모드를 인자로 넘겨줄 수 있도록 
		updateTableWithMode(0); 
		
		initControls();
	}

	public void initComponents(int w, int h, int col) {
		this.w = w;
		this.h = h;
		
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		
		// 현재 모드에 따라 레이블 초기화 (시작 메뉴에서는 일반 모드)
		lblGameMode = new JLabel(gameMode[col]);
		
		lblGameMode.setHorizontalAlignment(JLabel.CENTER);
		lblGameMode.setBounds(w / 3, h / 30, 200, 30);
		this.add(lblGameMode);
		
		lblArrow[0].setBounds(w/3 + 10, h/30, 30, 30);
		lblArrow[1].setBounds(w - (w/3 + 20), h/30, 30, 30);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}

	
	public void updateTableWithMode(int mode) {
		this.curMode = mode;
		
		initTableData();
		initTableSorter();
		initScrollLeaderboard();
	}

	// 현재 모드에 따라 파일 데이터 가져오기
	private void initTableData() {
		String header[] = { "Player", "Score", "Level" };
		String contents[][] = {};
		
		// 테이블의 데이터를 관리하는 테이블 모델 (어떤 파일에서든지 사용 가능)
		tm = new DefaultTableModel(contents, header) {
			@Override // 모든 셀 편집 불가능하도록 
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override // 점수 읽어올 때 정수 타입으로 
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1) // Score 
					return Integer.class;
				return super.getColumnClass(columnIndex).getClass();
			}
		};
		
		// 테이블의 칼럼 식별자 초기화 
		ci = new Vector();
		ci.add("Player");
		ci.add("Score");
		ci.add("Level");

		try {
			// 현재 멤버변수 curMode에 따라 다른 파일을 열어서 데이터 읽어오기!!!
			FileInputStream fs = new FileInputStream(leaderboardFile[curMode]);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);

			os.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 테이블 모델을 이용하여 테이블 초기화 
		leaderboard = new JTable(tm);
		leaderboard.setFocusable(false);
		
		// 셀의 내용 가운데 정렬 
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = leaderboard.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
	}

	// 현재 테이블에 대한 Sort 설정 
	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
		
		sorter.sort();
	}

	// 최종적으로, 스크롤 가능한 테이블 생성!
	private void initScrollLeaderboard() {
		scrollLeaderboard = new JScrollPane(leaderboard);
		scrollLeaderboard.setBounds(w / 30, h / 10, w - 50, h - 100);
		this.add(scrollLeaderboard);
	}

	// curMode에 따라 서로 다른 파일에 데이터 저장하기 
	private void saveLeaderboard() {
		try {
			FileOutputStream fs = new FileOutputStream(leaderboardFile[curMode]);
			ObjectOutputStream os = new ObjectOutputStream(fs);

			os.writeObject(tm.getDataVector());

			os.close();
			fs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------ 키 입력에 따른 이벤트 처리
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveRight();
			}
		});
		
		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveLeft();
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

	private void moveRight() {
		scrollLeaderboard.setVisible(false);
		
		curMode++;
		if (curMode > gameMode.length - 1) {
			curMode = 0;
		}
		
		// 현재 칼럼 위치에 따라 스코어보드 보여주기 
		this.remove(scrollLeaderboard);
		updateTableWithMode(curMode);
		
		// 레이블 텍스트 변경
		lblGameMode.setText(gameMode[curMode]);
	}

	private void moveLeft() {
		scrollLeaderboard.setVisible(false);

		curMode--;
		if (curMode < 0) {
			curMode = gameMode.length - 1;
		}
		
		this.remove(scrollLeaderboard);
		updateTableWithMode(curMode);
		
		lblGameMode.setText(gameMode[curMode]);
	}
	
	// 게임 종료 후 유저 이름 입력 받아서 스코어보드 띄우는 경우: 현재 선택된 모드에 따라 보여주기 
	public void addPlayer(int gameMode, String name, int score, String level) {
		// 현재 모드에 따라 파일 읽어서 테이블 데이터 초기화
		this.remove(scrollLeaderboard);
		updateTableWithMode(gameMode);
		
		// 유저 정보 추가
		tm.addRow(new Object[] { name, score, level });
		sorter.sort(); // 재정렬
		saveLeaderboard(); // 파일에 저장
		
		this.setVisible(true);
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