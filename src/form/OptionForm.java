package form;
import tetris.*;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

public class OptionForm extends JFrame {

	private JLabel gameLevelChange;
	private JLabel blockColorChange;
	private JLabel frameSizeChange;
	private JLabel[][] currentOptionArrow;
	private static StartupForm sf;

	private static JButton[][] optionButtons = new JButton[3][];
	private String[] colorMode = { "변경안함", "색맹모드" };
	private String[] gameLevel = { "이지모드", "노멀모드", "하드모드" };
	private String[] frameSize = { "작은 화면", "일반 화면", "큰 화면" };
	
	private int[][] size = {{600, 450}, {700, 500}, {900, 600}};

	private int optionRow;
	private int optionCol;

	// 한 설정당 하나씩 선택된 값 저장 (한 행당 하나의 열 값을 저장) 
	private static int[] optionSet = new int[optionButtons.length];  

	public OptionForm() {
		initThisFrame(600, 450);
		initLabel(600, 450);
		initColorButtons(600, 450);
		initGameButtons(600, 450);
		initSizeButtons(600, 450);

		initControls();
	}

	private void initControls() {
		InputMap im = this.getRootPane().getInputMap();
		ActionMap am = this.getRootPane().getActionMap();

		im.put(KeyStroke.getKeyStroke("UP"), "up");
		im.put(KeyStroke.getKeyStroke("DOWN"), "down");
		im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
		im.put(KeyStroke.getKeyStroke("LEFT"), "left");
		im.put(KeyStroke.getKeyStroke("ESCAPE"), "back");

		am.put("up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movePreviousOption();
			}
		});
		am.put("down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextOption();
			}
		});
		am.put("right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeChangeRight();
			}
		});
		am.put("left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modeChangeLeft();
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

	// 다음 옵션으로 행 이동
	private void nextOption() {
		currentOptionArrow[optionRow][0].setVisible(false);
		currentOptionArrow[optionRow][1].setVisible(false);
		
		optionRow++;
		if (optionRow > optionButtons.length - 1) {
			optionRow = 0;
		}
		optionCol = optionSet[optionRow];
		
		currentOptionArrow[optionRow][0].setVisible(true);
		currentOptionArrow[optionRow][1].setVisible(true);
	}

	// 이전 옵션으로 행 이동
	private void movePreviousOption() {
		currentOptionArrow[optionRow][0].setVisible(false);
		currentOptionArrow[optionRow][1].setVisible(false);

		optionRow--;
		if (optionRow < 0) {
			optionRow = optionButtons.length - 1;
		}
		optionCol = optionSet[optionRow];
		
		currentOptionArrow[optionRow][0].setVisible(true);
		currentOptionArrow[optionRow][1].setVisible(true);
	}

	// 옵션 선택 열 이동
	private void modeChangeRight() {

		optionButtons[optionRow][optionCol].setVisible(false);

		optionCol++;
		if (optionCol > optionButtons[optionRow].length - 1) {
			optionCol = 0;
		}

		optionSet[optionRow] = optionCol;
		optionButtons[optionRow][optionCol].setVisible(true);
		if(optionRow == 2) {
			changeSize(size[optionCol][0], size[optionCol][1]);
		}
	}

	// 옵션 선택 열 이동
	private void modeChangeLeft() {

		optionButtons[optionRow][optionCol].setVisible(false);

		optionCol--;
		if (optionCol < 0)
			optionCol = optionButtons[optionRow].length - 1;

		// 현재 옵션 선택 저장
		optionSet[optionRow] = optionCol;
		optionButtons[optionRow][optionCol].setVisible(true);
		if(optionRow == 2) {
			changeSize(size[optionCol][0], size[optionCol][1]);
		}
	}

	public int getCurrentGameLevel() {
		return optionSet[0];
	}

	public int getCurrentColorMode() {
		return optionSet[1];
	}
	
	public int getFrameWidth() {
		return size[optionSet[2]][0];
	}
	
	public int getFrameHeight() {
		return size[optionSet[2]][1];
	}
	
	//사이즈 변경
	// 버튼 등 화면 구성 요소들 invisible -> visible
	private void changeSize(int w, int h) {
		this.setSize(w, h);
		gameLevelChange.setVisible(false);
		blockColorChange.setVisible(false);
		frameSizeChange.setVisible(false);
		optionButtons[0][optionSet[0]].setVisible(false);
		optionButtons[1][optionSet[1]].setVisible(false);
		optionButtons[2][optionSet[2]].setVisible(false);
		currentOptionArrow[2][0].setVisible(false);
		currentOptionArrow[2][1].setVisible(false);
		initLabel(w, h);
		currentOptionArrow[0][0].setVisible(false);
		currentOptionArrow[0][1].setVisible(false);
		currentOptionArrow[2][0].setVisible(true);
		currentOptionArrow[2][1].setVisible(true);
		initColorButtons(w, h);
		initGameButtons(w, h);
		initSizeButtons(w, h);
	}

	private void initThisFrame(int w, int h) {
		this.setSize(w, h);
		this.setResizable(true);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initLabel(int w, int h) {
		gameLevelChange = new JLabel("게임 난이도 변경");
		gameLevelChange.setBounds(w / 3, h / 15, w / 3, h / 20);
		gameLevelChange.setHorizontalAlignment(JLabel.CENTER);
		this.add(gameLevelChange);

		blockColorChange = new JLabel("블록 색 변경");
		blockColorChange.setBounds(w / 3, (h / 15) * 3, w / 3, h / 20);
		blockColorChange.setHorizontalAlignment(JLabel.CENTER);
		this.add(blockColorChange);
		
		frameSizeChange = new JLabel("사이즈 변경");
		frameSizeChange.setBounds(w / 3, (h / 15) * 5, w / 3, h / 20);
		frameSizeChange.setHorizontalAlignment(JLabel.CENTER);
		this.add(frameSizeChange);

		// 현재 위치한 버튼 양 옆 < > 표시
		currentOptionArrow = new JLabel[3][2];
		for (int i = 0; i < currentOptionArrow.length; i++) {
			currentOptionArrow[i][0] = new JLabel("<");
			currentOptionArrow[i][1] = new JLabel(">");
			currentOptionArrow[i][0].setBounds(w / 4 - 20, h / 20 + (h / 15) * (2 * i + 1), 20, h / 15);
			currentOptionArrow[i][1].setBounds(w / 4 * 3, h / 20 + (h / 15) * (2 * i + 1), 20, h / 15);
			currentOptionArrow[i][0].setHorizontalAlignment(JLabel.CENTER);
			currentOptionArrow[i][1].setHorizontalAlignment(JLabel.CENTER);
			this.add(currentOptionArrow[i][0]);
			this.add(currentOptionArrow[i][1]);
	
			currentOptionArrow[i][0].setVisible(false);
			currentOptionArrow[i][1].setVisible(false);
		}

		currentOptionArrow[0][0].setVisible(true);
		currentOptionArrow[0][1].setVisible(true);
	}

	private void initGameButtons(int w, int h) {
		optionButtons[0] = new JButton[3];
		for (int i = 0; i < optionButtons[0].length; i++) {
			optionButtons[0][i] = new JButton(gameLevel[i]);
			optionButtons[0][i].setBounds(w / 4, h / 15 + h / 20, w / 2, h / 15);
			optionButtons[0][i].setBackground(Color.white);
			optionButtons[0][i].setVisible(false);
			this.add(optionButtons[0][i]);
		}
		optionButtons[0][optionSet[0]].setVisible(true);
	}

	private void initColorButtons(int w, int h) {
		optionButtons[1] = new JButton[2];
		for (int i = 0; i < optionButtons[1].length; i++) {
			optionButtons[1][i] = new JButton(colorMode[i]);
			optionButtons[1][i].setBounds(w / 4, (h / 15) * 3 + h / 20, w / 2, h / 15);
			optionButtons[1][i].setBackground(Color.white);
			optionButtons[1][i].setVisible(false);
			this.add(optionButtons[1][i]);
		}
		optionButtons[1][optionSet[1]].setVisible(true);
	}
	
	private void initSizeButtons(int w, int h) {
		optionButtons[2] = new JButton[3];
		for (int i = 0; i < optionButtons[2].length; i++) {
			optionButtons[2][i] = new JButton(frameSize[i]);
			optionButtons[2][i].setBounds(w / 4, (h / 15) * 5 + h / 20, w / 2, h / 15);
			optionButtons[2][i].setBackground(Color.white);
			optionButtons[2][i].setVisible(false);
			this.add(optionButtons[2][i]);
		}
		optionButtons[2][optionSet[2]].setVisible(true);
	}	
}