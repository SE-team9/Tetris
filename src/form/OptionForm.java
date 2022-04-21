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

/* optionRerRow에 행마다 선택된 값을 저장하자.
 * 
	화면 크기 조절 - 3개 -> 이 화면에서 바로 적용 
	조작 키 설정 - 2개 -> GameForm에서 참조
	난이도 선택 - 3개 -> GameThread에서 참조 (낙하 속도 조절, 블럭 생성 확률 조절)
	
	색맹 모드 on/off -> 블럭 색상 초기화 할 때 사용 
	스코어보드 기록 초기화 on/off -> LeaderboardForm에서 사용 
	기본 설정으로 되돌리기 on/off -> 이 화면에서 바로 적용 
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
	private String[] options = { "화면 크기", "조작 키", "난이도", "색맹 모드", "스코어보드 기록 초기화", "기본 설정으로 되돌리기"};
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private JButton[] btnOption = new JButton[ROW];
	
	private int w, h;
	private JLabel lblKey = new JLabel("left, right, down, rotate, drop, quit, exit");
	
	private String[][] optionArray = {
		 { "Small (default)", "Medium", "Large" }, // 화면 크기
		 { "←, →, ↓, ↑, SPACE, q, e", "a, d, s, w, ENTER, q, e"}, // 조작 키 
		 { "Easy", "Normal", "Hard" }, // 난이도
		 { "NO", "YES" }, // 색맹 모드
		 { "NO", "YES" }, // 점수 기록 초기화
		 { "NO", "YES" } // 기본 설정으로 되돌리기
	};
	
	
	// 행마다 포커스가 놓인 칼럼 위치가 다르기 때문에 배열로 만든 것이다!!!
	// 현재 행의 칼럼이 어떤 옵션에 포커스가 놓여 있는지 알려주는 배열
	private int row = 0;
	private int[] focusPerRow = new int[ROW];
	
	// 엔터 눌러서 확정된 칼럼 값을 저장 (다른 곳에서 참조 가능)
	private int[] confirmPerRow = new int[ROW]; 

	public OptionForm() {
		this.w = 600;
		this.h = 450;
		
		// 모든 행의 옵션을 첫번째 칼럼으로 초기화
		initDefaultSettings();
		
		// 기본 크기로 설정
		initComponents(w, h);
		
		initControls();
	}
	
	// 기본 설정으로 초기화
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
	
	// TODO: 외부에서 이 함수를 호출하여 크기 설정할 수 있도록 하려면 인자에 w, h 넣어줘야 할 듯.
	public void initComponents(int w, int h) {
		this.w = w;
		this.h = h;
		initThisFrame();
		
		// 레이블과 화살표는 높이 동일하게!
		for(int i = 0; i < ROW; i++) {
			lblOption[i] = new JLabel(options[i]);
			lblOption[i].setBounds(w/4, h/30 + i * 60, w/2, 25);
			lblOption[i].setHorizontalAlignment(JLabel.CENTER);
			this.add(lblOption[i]);
			
			// 버튼 6개 초기화
			// repaint로 화면 다시 그려도, 현재 포커스가 놓인 칼럼으로 텍스트 초기화!!!
			btnOption[i] = new JButton(optionArray[i][focusPerRow[i]]);
			btnOption[i].setBounds(w/4, h/30 + i * 60 + 25, w/2, 25);
			btnOption[i].setBackground(Color.white);
			btnOption[i].setFocusable(false);
			this.add(btnOption[i]);
		}
		
		// 포커스가 놓인 행에 화살표 표시
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
		
		// 엔터를 눌러야 현재 칼럼의 옵션이 확정됨.
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
					initDefaultSettings(); // 첫번째 칼럼으로 옵션 초기화
					updateFrameSize(600, 450); // 모든 컴포넌트 크기 조정
			
					// 기본 설정으로 되돌리기 NO인 경우에는 현재 설정 그대로 유지
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
		
		// 현재 행에 따라 화살표의 위치와 visibility 조절
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
		
		// 현재 행에 따라 화살표의 위치와 visibility 조절
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		lblArrow[0].setVisible(true);
		lblArrow[1].setVisible(true);
	}

	private void moveRight() {
		// 처음에는 칼럼 위치가 0으로 초기화 되어 있음.
		focusPerRow[row]++;
		
		// 현재 행의 최대 옵션 길이를 넘으면 0으로 초기화
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
	
	// 프레임 크기 변경 (현재 설정된 옵션값은 화면에서 그대로 유지되어야 함)
	private void updateFrameSize(int w, int h) {
		getContentPane().removeAll();
		
		// 멤버 변수 w, h 변경해서 화면 크기 조절!
		// 화면 크기 바꿔도 현재 포커스에 따라 버튼 텍스트는 유지됨.
		initComponents(w, h);
		this.setVisible(true);
		
		getContentPane().repaint();
	}

	// 설정에서 선택한 너비, 높이에 따라 모든 프레임의 크기와 컴포넌트들의 위치 조정하기!!!
	public Pair<Integer, Integer> getFrameSize() {
		return new Pair<>(w, h);
	}
	
	// leaderboard 파일 삭제하기 (JTable 데이터 지우기)
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
	
	// OptionForm 프레임 실행
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new OptionForm().setVisible(true);
			}
		});
	}
}