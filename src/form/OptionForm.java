package form;
import tetris.*;

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

/* optionRerRow�� �ึ�� ���õ� ���� ��������.
 * 
	ȭ�� ũ�� ���� - 3�� -> �� ȭ�鿡�� �ٷ� ���� 
	���� Ű ���� - 2�� -> GameForm���� ����
	���̵� ���� - 3�� -> GameThread���� ���� (���� �ӵ� ����, �� ���� Ȯ�� ����)
	
	���� ��� on/off -> �� ���� �ʱ�ȭ �� �� ��� 
	���ھ�� ��� �ʱ�ȭ on/off -> LeaderboardForm���� ��� 
	�⺻ �������� �ǵ����� on/off -> �� ȭ�鿡�� �ٷ� ���� 
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
	
	private static final int ROW = 6;
	private JLabel[] lblOption = new JLabel[ROW]; 
	private String[] options = { "ȭ�� ũ��", "���� Ű", "���̵�", "���� ���", "���ھ�� ��� �ʱ�ȭ", "�⺻ �������� �ǵ�����"};
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private JButton[] btnOption = new JButton[ROW];
	
	private int w, h;
	private JLabel lblKey = new JLabel("left, right, down, rotate, drop, quit, exit");
	
	private String[][] optionArray = {
		 { "Small (default)", "Medium", "Large" }, // ȭ�� ũ��
		 { "��, ��, ��, ��, SPACE, q, e", "a, d, s, w, ENTER, q, e"}, // ���� Ű 
		 { "Easy", "Normal", "Hard" }, // ���̵�
		 { "NO", "YES" }, // ���� ���
		 { "NO", "YES" }, // ���� ��� �ʱ�ȭ
		 { "NO", "YES" } // �⺻ �������� �ǵ�����
	};
	
	
	// �ึ�� ��Ŀ���� ���� Į�� ��ġ�� �ٸ��� ������ �迭�� ���� ���̴�!!!
	// ���� ���� Į���� � �ɼǿ� ��Ŀ���� ���� �ִ��� �˷��ִ� �迭
	private int row = 0;
	private int[] focusPerRow = new int[ROW];
	
	// ���� ������ Ȯ���� Į�� ���� ���� (�ٸ� ������ ���� ����)
	private int[] confirmPerRow = new int[ROW]; 

	public OptionForm() {
		this.w = 600;
		this.h = 450;
		
		// ��� ���� �ɼ��� ù��° Į������ �ʱ�ȭ
		initDefaultSettings();
		
		// �⺻ ũ��� ����
		initComponents(w, h);
		
		initControls();
	}
	
	// �⺻ �������� �ʱ�ȭ
	private void initDefaultSettings() {
		for(int i = 0; i < ROW; i++) {
			focusPerRow[i] = 0;
			confirmPerRow[i] = 0;
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
	
	// TODO: �ܺο��� �� �Լ��� ȣ���Ͽ� ũ�� ������ �� �ֵ��� �Ϸ��� ���ڿ� w, h �־���� �� ��.
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
			btnOption[i] = new JButton(optionArray[i][focusPerRow[i]]);
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
				confirmPerRow[row] = focusPerRow[row];
				
				System.out.println(row + " " + confirmPerRow[row]);
				
				if(row == 0) {
					switch(confirmPerRow[row]) {
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
				}
				else if(row == 5 && confirmPerRow[row] == 1) {
					initDefaultSettings(); // ù��° Į������ �ɼ� �ʱ�ȭ
					updateFrameSize(600, 450); // ��� ������Ʈ ũ�� ����
			
					// �⺻ �������� �ǵ����� NO�� ��쿡�� ���� ���� �״�� ����
				}
			}
		});
	}
	
	private void moveUp() {
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
		focusPerRow[row]++;
		
		// ���� ���� �ִ� �ɼ� ���̸� ������ 0���� �ʱ�ȭ
		if(focusPerRow[row] >= optionArray[row].length) {
			focusPerRow[row] = 0;
		}
		
		btnOption[row].setText(optionArray[row][focusPerRow[row]]);
	}

	private void moveLeft() {
		focusPerRow[row]--;
		
		if(focusPerRow[row] < 0) {
			focusPerRow[row] = optionArray[row].length - 1;
		}
		
		btnOption[row].setText(optionArray[row][focusPerRow[row]]);
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
	
	// leaderboard ���� �����ϱ� (JTable ������ �����)
	private void initScoreboard() {
		
	}
	
	public int getCurrentKeyMode() {
		return 0;
	}
	
	public int getCurrentColorMode() {
		return 0;
	}
	
	public int getCurrentGameLevel() {
		return 0;
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