package tetris;

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
	private JLabel[][] currentOptionArrow;

	private static JButton[][] optionButtons = new JButton[2][];
	private String[] colorMode = { "변경안함", "색맹모드" };
	private String[] gameLevel = { "이지모드", "노멀모드","하드모드" };

	private int optionRow;
	private int optionCol;

	// 한 설정당 하나씩 선택된 값 저장 (한 행당 하나의 열 값을 저장) 
	private static int[] optionSet = new int[optionButtons.length];  

	public OptionForm() {
		initThisFrame();
		initLabel();
		initColorButtons();
		initGameButtons();

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
				back();
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
	}

	private void back() {
		this.setVisible(false);
		Tetris.showStartup();
	}

	public int getCurrentGameLevel() {
		return optionSet[0];
	}

	public int getCurrentColorMode() {
		return optionSet[1];
	}

	private void initThisFrame() {
		this.setSize(600, 450);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
	}

	private void initLabel() {
		gameLevelChange = new JLabel("게임 난이도 변경");
		gameLevelChange.setBounds(200, 100, 200, 30);
		gameLevelChange.setHorizontalAlignment(JLabel.CENTER);
		this.add(gameLevelChange);

		blockColorChange = new JLabel("블록 색 변경");
		blockColorChange.setBounds(200, 200, 200, 30);
		blockColorChange.setHorizontalAlignment(JLabel.CENTER);
		this.add(blockColorChange);

		// 현재 위치한 버튼 양 옆 < > 표시
		currentOptionArrow = new JLabel[2][2];
		for (int i = 0; i < currentOptionArrow.length; i++) {
			currentOptionArrow[i][0] = new JLabel("<");
			currentOptionArrow[i][1] = new JLabel(">");
			currentOptionArrow[i][0].setBounds(180, 130 + (100 * i), 20, 30);
			currentOptionArrow[i][1].setBounds(400, 130 + (100 * i), 20, 30);
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

	private void initGameButtons() {
		optionButtons[0] = new JButton[3];
		for (int i = 0; i < optionButtons[0].length; i++) {
			optionButtons[0][i] = new JButton(gameLevel[i]);
			optionButtons[0][i].setBounds(200, 130, 200, 30);
			optionButtons[0][i].setBackground(Color.white);
			optionButtons[0][i].setVisible(false);
			this.add(optionButtons[0][i]);
		}
		optionButtons[0][optionSet[0]].setVisible(true);
	}

	private void initColorButtons() {
		optionButtons[1] = new JButton[2];
		for (int i = 0; i < optionButtons[1].length; i++) {
			optionButtons[1][i] = new JButton(colorMode[i]);
			optionButtons[1][i].setBounds(200, 230, 200, 30);
			optionButtons[1][i].setBackground(Color.white);
			optionButtons[1][i].setVisible(false);
			this.add(optionButtons[1][i]);
		}
		optionButtons[1][optionSet[1]].setVisible(true);
	}
	
}