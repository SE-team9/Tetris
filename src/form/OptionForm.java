package form;
import tetris.*;
import form.LeaderboardForm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

/*  
	ȭ�� ũ�� ���� - 3�� -> �� ȭ�鿡�� �ٷ� ���� 
	�⺻ �������� �ǵ����� on/off -> �� ȭ�鿡�� �ٷ� ���� 
	���ھ�� ��� �ʱ�ȭ on/off -> LeaderboardForm���� ��� 
	
	���� Ű ���� - 2�� -> GameForm���� ����
	���̵� ���� - 3�� -> GameThread���� ���� (���� �ӵ� ����, �� ���� Ȯ�� ����)
	���� ��� on/off -> �� ���� �ʱ�ȭ �� �� ��� 
 */

public class OptionForm extends JFrame {
	public class Pair<W, H> {
		public W width;
		public H height;
		public Pair(W w, H h){
			this.width = w;
			this.height = h;
		}
	}
	private int w, h;
	
	private static final int ROW = 6;
	private JLabel[] lblOption = new JLabel[ROW]; 
	private String[] options = { "ȭ�� ũ��", "���� Ű", "���̵�", "���� ���", "���ھ�� ��� �ʱ�ȭ", "�⺻ �������� �ǵ�����"};
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private JButton[] btnOption = new JButton[ROW];
	
	private String[][] optionArray = {
		 { "Small (default)", "Medium", "Large" }, // ȭ�� ũ��
		 { "��, ��, ��, ��, SPACE, q, e", "a, d, s, w, ENTER, q, e"}, // ���� Ű 
		 { "Easy", "Normal", "Hard" }, // ���̵�
		 { "NO", "YES" }, // ���� ���
		 { "NO", "YES" }, // ���� ��� �ʱ�ȭ
		 { "NO", "YES" } // �⺻ �������� �ǵ�����
	};
	
	LeaderboardForm lf = new LeaderboardForm();
	
	// �ึ�� ��Ŀ���� ���� Į�� ��ġ�� �ٸ��� ������ �迭 �����
	private int row = 0;
	private int[] focusColumn = new int[ROW];
	
	// ���� ������ Ȯ���� Į�� ���� ���� (�ٸ� ������ ���� ����)
	private int[] confirmedColumn = new int[ROW]; 

	public OptionForm() {
		this.w = 600;
		this.h = 450;
		
		// ��� ���� �ɼ��� ù��° Į������ �ʱ�ȭ
		initDefaultSettings();
		
		// �⺻ ũ��� ����
		initComponents(w, h);
		
		initControls();
	}
	
	// �� ȭ�� ������ ũ�⸦ �ٲ� ���� ��Ŀ�� ���� �ؽ�Ʈ�� ������� ������,
	// ȭ���� �����ٰ� �ٽ� ���� ���� Ȯ���� ���� ���� ����� �Ѵ�. 
	public void showConfirmedOption() {
		// Ȯ���� ���� ������ �ؽ�Ʈ �����ֱ�
		for(int i = 0; i < ROW; i++) {
			btnOption[i] = new JButton(optionArray[i][confirmedColumn[i]]); 
			btnOption[i].setBounds(w/4, h/30 + i * 60 + 25, w/2, 25);
			btnOption[i].setBackground(Color.white);
			btnOption[i].setFocusable(false);
			this.add(btnOption[i]);
		}
		
		// ȭ��ǥ ��ġ �ʱ�ȭ
		row = 0;
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}
	
	// �⺻ �������� �ʱ�ȭ
	private void initDefaultSettings() {
		for(int i = 0; i < ROW; i++) {
			focusColumn[i] = 0;
			confirmedColumn[i] = 0;
		}
	}
	
	private void initThisFrame() {
		this.setSize(w, h);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}
	
	// �ٸ� ȭ�鿡�� ���� ȭ�� ������ ������ w, h�� �޾ƿͼ� ������� �ʱ�ȭ!!
	public void initComponents(int w, int h) {
		this.w = w;
		this.h = h;
		initThisFrame();
		
		// ���̺�� ȭ��ǥ�� ���� �����ϰ�!
		for(int i = 0; i < ROW; i++) {
			lblOption[i] = new JLabel(options[i]);
			lblOption[i].setBounds(w/4, h/30 + i * 60, w/2, 25);
			lblOption[i].setHorizontalAlignment(JLabel.CENTER);
			this.add(lblOption[i]);
			
			// ��ư 6�� �ʱ�ȭ
			// repaint�� ȭ�� �ٽ� �׷���, ���� ��Ŀ���� ���� Į������ �ؽ�Ʈ �ʱ�ȭ!!!
			btnOption[i] = new JButton(optionArray[i][focusColumn[i]]);
			btnOption[i].setBounds(w/4, h/30 + i * 60 + 25, w/2, 25);
			btnOption[i].setBackground(Color.white);
			btnOption[i].setFocusable(false);
			this.add(btnOption[i]);
		}
		
		// ��Ŀ���� ���� �࿡ ȭ��ǥ ǥ��
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}
	
	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUp();
			}
		});
		am.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
			}
		});
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
		
		// ���͸� ������ ���� Į���� �ɼ��� Ȯ����.
		am.put("enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmedColumn[row] = focusColumn[row];
				//System.out.println(row + " " + confirmedColumn[row]); // ����� �뵵
				
				switch(row) {
				case 0: // ȭ�� ũ�� ���� 
					switch(confirmedColumn[row]) {
					case 0: 
						updateFrameSize(600, 450);
						break;
					case 1:
						updateFrameSize(700, 550);
						break;
					case 2:
						updateFrameSize(800, 650);
						break;
					}
					break;
				case 4: // ���ھ�� �ʱ�ȭ on/off
					if(confirmedColumn[row] == 1) {
						initScoreboard();						
					}
				case 5: // �⺻ ���� on/off
					if(confirmedColumn[row] == 1) {
						initDefaultSettings(); // ù��° Į������ �ɼ� �ʱ�ȭ
						updateFrameSize(600, 450); // ��� ������Ʈ ũ�� ����
					}
				}
			}
		});
	}
	
	private void moveUp() {		
		// ���� ���� �ؽ�Ʈ�� ��Ŀ���� �ɷ�!
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
		
		lblArrow[0].setVisible(false);
		lblArrow[1].setVisible(false);
		
		row--;
		
		if(row < 0) {
			row = ROW - 1;
		}
		
		// ���� �࿡ ���� ȭ��ǥ�� ��ġ�� visibility ����
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		lblArrow[0].setVisible(true);
		lblArrow[1].setVisible(true);
	}

	private void moveDown() {
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
		
		lblArrow[0].setVisible(false);
		lblArrow[1].setVisible(false);
		
		row++;
		if(row >= ROW) {
			row = 0;
		}
		
		// ���� �࿡ ���� ȭ��ǥ�� ��ġ�� visibility ����
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		lblArrow[0].setVisible(true);
		lblArrow[1].setVisible(true);
	}

	private void moveRight() {
		// ó������ Į�� ��ġ�� 0���� �ʱ�ȭ �Ǿ� ����.
		focusColumn[row]++;
		
		// ���� ���� �ִ� �ɼ� ���̸� ������ 0���� �ʱ�ȭ
		if(focusColumn[row] >= optionArray[row].length) {
			focusColumn[row] = 0;
		}
		
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
	}

	private void moveLeft() {
		focusColumn[row]--;
		
		if(focusColumn[row] < 0) {
			focusColumn[row] = optionArray[row].length - 1;
		}
		
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
	}
	
	// ������ ũ�� ���� (���� ������ �ɼǰ��� ȭ�鿡�� �״�� �����Ǿ�� ��)
	private void updateFrameSize(int w, int h) {
		getContentPane().removeAll();
		
		// ��� ���� w, h �����ؼ� ȭ�� ũ�� ����!
		// ȭ�� ũ�� �ٲ㵵 ���� ��Ŀ���� ���� ��ư �ؽ�Ʈ�� ������.
		initComponents(w, h);
		this.setVisible(true);
		
		getContentPane().repaint();
	}

	// �������� ������ �ʺ�, ���̿� ���� ��� �������� ũ��� ������Ʈ���� ��ġ �����ϱ�!!!
	public Pair<Integer, Integer> getFrameSize() {
		return new Pair<>(w, h);
	}
	
	// ���� Ű
	public int getCurrentKeyMode() {
		return confirmedColumn[1];
	}
	
	// ���̵� 
	public int getCurrentGameLevel() {
		return confirmedColumn[2];
	}
	
	// ���� ��� 
	public int getCurrentColorMode() {
		return confirmedColumn[3];
	}
	
	// �� ȭ�� ��ü���� ���� ������ �� ���� �� (�ٸ� JTable�� ������������ ����)
	private void initScoreboard() {
		lf.deleteLeaderboard();
	}
	
	// OptionForm ������ ����
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new OptionForm().setVisible(true);
			}
		});
	}
}