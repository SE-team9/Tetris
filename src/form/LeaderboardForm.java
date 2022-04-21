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
	private JScrollPane scrollLeaderboard; // ȭ�� ������ �Ѿ �� ��ũ�� �����ϵ���

	private JLabel lblGameMode;
	private String gameMode[] = { "Normal Mode", "Item Mode" };
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private int curMode;
	private Vector ci;
	
	// Tetris���� ��ü�� ó�� ������ �� �ʱ�ȭ �۾� 
	public LeaderboardForm() {
		this.w = 600;
		this.h = 450;
		this.curMode = 0;
		initComponents(w, h, 0);
		
		// �ܺο��� ȣ���� �� ��带 ���ڷ� �Ѱ��� �� �ֵ��� 
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
		
		// ���� ��忡 ���� ���̺� �ʱ�ȭ (���� �޴������� �Ϲ� ���)
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

	// ���� ��忡 ���� ���� ������ ��������
	private void initTableData() {
		String header[] = { "Player", "Score", "Level" };
		String contents[][] = {};
		
		// ���̺��� �����͸� �����ϴ� ���̺� �� (� ���Ͽ������� ��� ����)
		tm = new DefaultTableModel(contents, header) {
			@Override // ��� �� ���� �Ұ����ϵ��� 
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override // ���� �о�� �� ���� Ÿ������ 
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 1) // Score 
					return Integer.class;
				return super.getColumnClass(columnIndex).getClass();
			}
		};
		
		// ���̺��� Į�� �ĺ��� �ʱ�ȭ 
		ci = new Vector();
		ci.add("Player");
		ci.add("Score");
		ci.add("Level");

		try {
			// ���� ������� curMode�� ���� �ٸ� ������ ��� ������ �о����!!!
			FileInputStream fs = new FileInputStream(leaderboardFile[curMode]);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);

			os.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ���̺� ���� �̿��Ͽ� ���̺� �ʱ�ȭ 
		leaderboard = new JTable(tm);
		leaderboard.setFocusable(false);
		
		// ���� ���� ��� ���� 
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = leaderboard.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
	}

	// ���� ���̺� ���� Sort ���� 
	private void initTableSorter() {
		sorter = new TableRowSorter<>(tm);
		leaderboard.setRowSorter(sorter);
		
		ArrayList<SortKey> keys = new ArrayList<>();
		keys.add(new SortKey(1, SortOrder.DESCENDING)); // column index, sort order
		sorter.setSortKeys(keys);
		
		sorter.sort();
	}

	// ����������, ��ũ�� ������ ���̺� ����!
	private void initScrollLeaderboard() {
		scrollLeaderboard = new JScrollPane(leaderboard);
		scrollLeaderboard.setBounds(w / 30, h / 10, w - 50, h - 100);
		this.add(scrollLeaderboard);
	}

	// curMode�� ���� ���� �ٸ� ���Ͽ� ������ �����ϱ� 
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

	// ------------------------------------------------------ Ű �Է¿� ���� �̺�Ʈ ó��
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
		
		// ���� Į�� ��ġ�� ���� ���ھ�� �����ֱ� 
		this.remove(scrollLeaderboard);
		updateTableWithMode(curMode);
		
		// ���̺� �ؽ�Ʈ ����
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
	
	// ���� ���� �� ���� �̸� �Է� �޾Ƽ� ���ھ�� ���� ���: ���� ���õ� ��忡 ���� �����ֱ� 
	public void addPlayer(int gameMode, String name, int score, String level) {
		// ���� ��忡 ���� ���� �о ���̺� ������ �ʱ�ȭ
		this.remove(scrollLeaderboard);
		updateTableWithMode(gameMode);
		
		// ���� ���� �߰�
		tm.addRow(new Object[] { name, score, level });
		sorter.sort(); // ������
		saveLeaderboard(); // ���Ͽ� ����
		
		this.setVisible(true);
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