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
	화면 크기 조절 - 3개 -> 이 화면에서 바로 적용 
	기본 설정으로 되돌리기 on/off -> 이 화면에서 바로 적용 
	스코어보드 기록 초기화 on/off -> LeaderboardForm에서 사용 
	
	조작 키 설정 - 2개 -> GameForm에서 참조
	난이도 선택 - 3개 -> GameThread에서 참조 (낙하 속도 조절, 블럭 생성 확률 조절)
	색맹 모드 on/off -> 블럭 색상 초기화 할 때 사용 
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
	private String[] options = { "화면 크기", "조작 키", "난이도", "색맹 모드", "스코어보드 기록 초기화", "기본 설정으로 되돌리기"};
	private JLabel[] lblArrow = { new JLabel("<"), new JLabel(">") };
	private JButton[] btnOption = new JButton[ROW];
	
	private String[][] optionArray = {
		 { "Small (default)", "Medium", "Large" }, // 화면 크기
		 { "←, →, ↓, ↑, SPACE, q, e", "a, d, s, w, ENTER, q, e"}, // 조작 키 
		 { "Easy", "Normal", "Hard" }, // 난이도
		 { "NO", "YES" }, // 색맹 모드
		 { "NO", "YES" }, // 점수 기록 초기화
		 { "NO", "YES" } // 기본 설정으로 되돌리기
	};
	
	LeaderboardForm lf = new LeaderboardForm();
	
	// 행마다 포커스가 놓인 칼럼 위치가 다르기 때문에 배열 만들기
	private int row = 0;
	private int[] focusColumn = new int[ROW];
	
	// 엔터 눌러서 확정된 칼럼 값을 저장 (다른 곳에서 참조 가능)
	private int[] confirmedColumn = new int[ROW]; 

	public OptionForm() {
		this.w = 600;
		this.h = 450;
		
		// 모든 행의 옵션을 첫번째 칼럼으로 초기화
		initDefaultSettings();
		
		// 기본 크기로 설정
		initComponents(w, h);
		
		initControls();
	}
	
	// 이 화면 내에서 크기를 바꿀 때는 포커스 놓인 텍스트를 보여줘야 하지만,
	// 화면을 나갔다가 다시 들어올 때는 확정된 설정 값을 띄워야 한다. 
	public void showConfirmedOption() {
		// 확정된 설정 값으로 텍스트 보여주기
		for(int i = 0; i < ROW; i++) {
			btnOption[i] = new JButton(optionArray[i][confirmedColumn[i]]); 
			btnOption[i].setBounds(w/4, h/30 + i * 60 + 25, w/2, 25);
			btnOption[i].setBackground(Color.white);
			btnOption[i].setFocusable(false);
			this.add(btnOption[i]);
		}
		
		// 화살표 위치 초기화
		row = 0;
		lblArrow[0].setBounds(w/3, h/30 + row * 60, 25, 25);
		lblArrow[1].setBounds(w - w/3, h/30 + row * 60, 25, 25);
		this.add(lblArrow[0]);
		this.add(lblArrow[1]);
	}
	
	// 기본 설정으로 초기화
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
	
	// 다른 화면에서 설정 화면 진입할 때마다 w, h값 받아와서 멤버변수 초기화!!
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
			btnOption[i] = new JButton(optionArray[i][focusColumn[i]]);
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
				confirmedColumn[row] = focusColumn[row];
				//System.out.println(row + " " + confirmedColumn[row]); // 디버깅 용도
				
				switch(row) {
				case 0: // 화면 크기 설정 
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
				case 4: // 스코어보드 초기화 on/off
					if(confirmedColumn[row] == 1) {
						initScoreboard();						
					}
				case 5: // 기본 설정 on/off
					if(confirmedColumn[row] == 1) {
						initDefaultSettings(); // 첫번째 칼럼으로 옵션 초기화
						updateFrameSize(600, 450); // 모든 컴포넌트 크기 조정
					}
				}
			}
		});
	}
	
	private void moveUp() {		
		// 원래 행의 텍스트는 포커스된 걸로!
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
		
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
		btnOption[row].setText(optionArray[row][focusColumn[row]]);
		
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
		focusColumn[row]++;
		
		// 현재 행의 최대 옵션 길이를 넘으면 0으로 초기화
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
	
	// 조작 키
	public int getCurrentKeyMode() {
		return confirmedColumn[1];
	}
	
	// 난이도 
	public int getCurrentGameLevel() {
		return confirmedColumn[2];
	}
	
	// 색맹 모드 
	public int getCurrentColorMode() {
		return confirmedColumn[3];
	}
	
	// 이 화면 자체에서 파일 삭제할 수 있을 듯 (다만 JTable은 리더보드폼에 있음)
	private void initScoreboard() {
		lf.deleteLeaderboard();
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