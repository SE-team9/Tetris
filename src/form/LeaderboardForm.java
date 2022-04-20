package form;
import tetris.*;

import java.awt.BorderLayout;
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
	private JTable leaderboard;

	private DefaultTableModel tm;
	private String[] leaderboardFile = { "leaderboardFile_Normal", "leaderboardFile_Item" };
	private TableRowSorter<TableModel> sorter;
	JScrollPane scrollLeaderboard;

	private JLabel[] gameModeLabel;
	private String gameMode[] = { "�Ϲݸ��", "�����۸��" };
	private int curCol;

	public LeaderboardForm() {
		initThisFrame();
		initLabel();

		initTableData(0);
		initLeaderboard(tm);
		initTableSorter();
		initScrollLeaderboard();

		initControls();
	}

	// ---------------------------------------------------------------------�ʱ�ȭ
	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

  // ���, ���̵� ǥ�� ���̺� �ʱ�ȭ 
	private void initLabel() {
		gameModeLabel = new JLabel[2];
		for (int i = 0; i < gameModeLabel.length; i++) {
			gameModeLabel[i] = new JLabel(gameMode[i]);
			gameModeLabel[i].setHorizontalAlignment(JLabel.CENTER);
			gameModeLabel[i].setBounds(200, 10, 200, 30);
			gameModeLabel[i].setVisible(false);
			this.add(gameModeLabel[i]);
		}
		gameModeLabel[0].setVisible(true);
	}
  
	// ESC�� ������ ���� ȭ������ �̵�
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
		gameModeLabel[curCol].setVisible(false);
		scrollLeaderboard.setVisible(false);

		curCol++;
		if (curCol > gameModeLabel.length - 1) {
			curCol = 0;
		}

		gameModeLabel[curCol].setVisible(true);

		remakeScrollLeaderboard(curCol);
	}

	private void movePreviousMode() {
		gameModeLabel[curCol].setVisible(false);
		scrollLeaderboard.setVisible(false);

		curCol--;
		if (curCol < 0) {
			curCol = gameModeLabel.length - 1;
		}

		gameModeLabel[curCol].setVisible(true);

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

			@Override // score--> Int ������
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1)
					return Integer.class;

				return super.getColumnClass(columnIndex).getClass();
			}
		};
		
		Vector columnIdentifier = new Vector();
		columnIdentifier.add("Player");
		columnIdentifier.add("Score");
		columnIdentifier.add("Level");

		try {
			FileInputStream fs = new FileInputStream(leaderboardFile[mode]);
			ObjectInputStream os = new ObjectInputStream(fs);

			// ������ ���ڿ��� �ƴ� int Ÿ������ �о�� ���ڸ� �̻��� ���ڵ� ���� ����!
			tm.setDataVector((Vector<Vector>) os.readObject(), columnIdentifier);

			os.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLeaderboard(DefaultTableModel tm) {
		leaderboard = new JTable(tm);
		leaderboard.setFocusable(false);

		// ���빰 �߾� ����
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
		scrollLeaderboard.setBounds(40, 50, 520, 350);
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