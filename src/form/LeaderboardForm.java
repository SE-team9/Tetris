package form;

import tetris.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class LeaderboardForm extends JFrame {
	private int w, h;
	private int position;
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
	//TableCellRenderer renderer;

	// Tetris���� ��ü�� ó�� ������ �� �ʱ�ȭ �۾�
	public LeaderboardForm() {
		this.w = 600;
		this.h = 450;
		this.curMode = 0;
		
		initComponents(w, h);
		updateTableWithMode(curMode); 
		
		initControls();
	}

	public void initComponents(int w, int h) {
		this.w = w;
		this.h = h;

		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		
		// �Ϲݸ�� �ؽ�Ʈ�� �ʱ�ȭ 
		lblGameMode = new JLabel(gameMode[0]);

		lblGameMode.setHorizontalAlignment(JLabel.CENTER);
		lblGameMode.setBounds(w / 3, h / 30, 200, 30);
		this.add(lblGameMode);

		lblArrow[0].setBounds(w / 3 + 10, h / 30, 30, 30);
		lblArrow[1].setBounds(w - (w / 3 + 20), h / 30, 30, 30);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}
	public void updateTableWithMode(int mode) {		
		// ��� ���� 
		this.curMode = mode;
		
		// ���̺� ������ ���ε� 
		initTableData();
		makeLeaderboard(-1); // highlight ���� initTableData���� �и�
		initTableSorter();
		initScrollLeaderboard();
	}
	
	// ���� ���� ��忡 ���� ���� �ٸ� ���� �о���� (FileInputStream)
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
			// �ش� �̸��� ������ �������� �ʴ� ��� ���� ���� 
			File file = new File(leaderboardFile[curMode]);
			if(!file.exists()) { 
				file.createNewFile(); 
				System.out.println("Create new file.");
			};
			
			FileInputStream fs = new FileInputStream(file);
			ObjectInputStream os = new ObjectInputStream(fs);
			
			// de-serialization (����ȭ �� ����Ʈ ������ -> ��ü Ÿ������ �о����)
			// ���⼭ ������ �о�� �� ������ ���� �̸��� eof ���� �߻���. (�ϴ� ��ŵ)
			tm.setDataVector((Vector<Vector>) os.readObject(), ci);
			
			os.close();
			fs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void makeLeaderboard(int position) {
		// ���̺� ���� �̿��Ͽ� ���̺� �ʱ�ȭ
		leaderboard = new JTable(tm) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				JComponent component = (JComponent) super.prepareRenderer(renderer, row, column);
				if (row == position) {
					component.setBackground(Color.YELLOW);
				} else {
					component.setBackground(Color.WHITE);
				}
				return component;
			}
		};
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

	// ���� ���� ��忡 ���� ���� �ٸ� ���Ͽ� ������ �����ϱ� (FileOutputStream)
	private void saveLeaderboard() {
		try {
			// �ش� �̸��� ������ �������� �ʴ� ���, ���� �����ϱ�
			File file = new File(leaderboardFile[curMode]);			
			if(!file.exists()) { 
				System.out.println("Create new file.");
				file.createNewFile(); 
			};

			FileOutputStream fs = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			// serialization (��ü -> ����Ʈ �����ͷ� ����ȭ�Ͽ� ���Ͽ� ����)
			os.writeObject(tm.getDataVector());
		    
			os.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		this.remove(scrollLeaderboard); // ȭ�鿡�� ������Ʈ ���� 
		
		curMode++;
		if (curMode > gameMode.length - 1) {
			curMode = 0;
		}
		updateTableWithMode(curMode);
		
		lblGameMode.setText(gameMode[curMode]);
	}

	private void moveLeft() {
		this.remove(scrollLeaderboard);

		curMode--;
		if (curMode < 0) {
			curMode = gameMode.length - 1;
		}
		updateTableWithMode(curMode);

		lblGameMode.setText(gameMode[curMode]);
	}
	
	// ���� ���� �� ���� �̸� �Է� �޾Ƽ� ���ھ�� ����
	public void addPlayer(int mode, String name, int score, String level) {
		this.remove(scrollLeaderboard);
		this.curMode = mode;
		initTableData();
		//sorter.sort(); // ������, ���� ��� �۵��ǳ�...?
		
		// ���� ���� �߰�
		tm.addRow(new Object[] { name, score, level });

		// �Ʒ��� �ߺ��Ǵ� �����̱�� �ѵ� sort()�Ŀ� highlight�ϱ� ���ؼ� �ʿ��� -> position �� �����µ� ���
		leaderboard = new JTable(tm);
		initTableSorter();
		
		position = leaderboard.convertRowIndexToView(tm.getRowCount() - 1);
		//System.out.println(tm.getRowCount() + " " + position);
		
		makeLeaderboard(position); // highlight
		initTableSorter();
		initScrollLeaderboard();
		
		saveLeaderboard(); // ���Ͽ� ����
		

		// ���ھ�� �����ֱ�
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