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

// TODO: ���ھ� ���� ��� �ʱ�ȭ

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

	// ---------------------------------------------------------------------�ʱ�ȭ
	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

    // ���, ���̵� ǥ�� ���̺� �ʱ�ȭ 
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

	// ���� ��忡 �´� ���Ͽ� ���� ���带 �ٽ� �����Ѵ�. 
	private void remakeScrollLeaderboard(int mode) {
		this.remove(scrollLeaderboard);
		initTableData(mode);
		initLeaderboard(tm);
		initTableSorter();
		sorter.sort();
		initScrollLeaderboard();
	}

	// ���Ϸκ��� ������ �������� (de-serialization)
	private void initTableData(int mode) {
		String header[] = { "Player", "Score", "Level" };
		String contents[][] = {};

		tm = new DefaultTableModel(contents, header) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}

			@Override // score --> Int ������
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

			// ������ ���ڿ��� �ƴ� int Ÿ������ �о�� ���ڸ� �̻��� ���ڵ� ���� ����!
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

		// ���� �߾� ����
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

	// ������ ���̺� �����͸� ����Ͽ� �������� ����
	private void initScrollLeaderboard() {
		scrollLeaderboard = new JScrollPane(leaderboard);
		scrollLeaderboard.setBounds(w / 30, h / 10, w - 50, h - 100);
		this.add(scrollLeaderboard);
	}

	// ���Ͽ� ������ �����ϱ� (serialization)
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

	// ���� ����Ǹ� �÷��̾� ��� �߰��ϸ鼭, ���ÿ� ���ھ�� �����ֱ�
	public void addPlayer(String playerName, int score, int gameLevel) {
		String strGameLevel;
		if (gameLevel == 0)
			strGameLevel = "Easy";
		else if (gameLevel == 1)
			strGameLevel = "Normal";
		else
			strGameLevel = "Hard";

		this.remove(scrollLeaderboard); 				// ���� �������� ����
		initTableData(Tetris.getGameMode()); 			// ���� ��忡 �´� ���� ������ �о����
		tm.addRow(new Object[] { playerName, score, strGameLevel }); 	// �о�� �����Ϳ� ��� �߰�
		initLeaderboard(tm); 							// �о�� �����ͷ� �������� ����
		initTableSorter();								// �������� ����
		sorter.sort(); 									// �������� ����
		saveLeaderboard(); 								// ���Ͽ� ����

		initScrollLeaderboard(); // leaderboard�� ������� ���� ����

		curCol = Tetris.getGameMode();

		this.setVisible(true); // ���ھ�� ǥ��
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