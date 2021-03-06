package version1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainPanel extends JPanel {
	static int setYear;
	static int setMonth;
	static int setDay;

	CalendarPanel calendarPanel;
	JPanel MenuPanel;
	JPanel datePanel;
	JPanel plusPanel;

	
	ImageIcon backGroundImageIcons[] = new ImageIcon[12];
	Image backGroundImages[] = new Image[12];
	BorderLayout layout;

	JTextField textField;
	Font font;

	Calendar cal;
	
	Connection connection = null;
	Statement st = null;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		cal = Calendar.getInstance();
		setYear = cal.get(cal.YEAR);
		setMonth = cal.get(cal.MONTH) + 1;
		setDay = cal.get(cal.DATE);
		// 메뉴패널 할당
		MenuPanel = new MenuPanel();
		// 배경사진 로딩
		for (int i = 0; i < 12; i++) {
			backGroundImageIcons[i] = new ImageIcon("images/" + (i + 1) + ".png");
			backGroundImages[i] = backGroundImageIcons[i].getImage();
		}
		// 폰트 설정
		font = MainFrame.basicFont;
		// 레이아웃 설정
		setLayout(new BorderLayout());
		layout = (BorderLayout) getLayout();
		// North에 컴포넌트 추가할 Date패널 생성
		datePanel = new DatePanel();
		// calendarPanel을 CENTER에 추가
		calendarPanel = new CalendarPanel(setYear, setMonth - 1);
		// datePanel을 BorderLayout NORTH에 추가
		add(datePanel, BorderLayout.NORTH);
		// add(textField, BorderLayout.SOUTH);
		add(MenuPanel, BorderLayout.SOUTH);
		add(calendarPanel, BorderLayout.CENTER);


	}

	public void removeCenter() {
		remove(layout.getLayoutComponent(BorderLayout.CENTER));
	}

	public void rePaint() {
		remove(datePanel);
		remove(MenuPanel);
		remove(calendarPanel);
		
		MenuPanel = new MenuPanel();
		datePanel = new DatePanel();
		calendarPanel = new CalendarPanel(setYear, setMonth - 1);
		
		add(datePanel, BorderLayout.NORTH);
		add(MenuPanel, BorderLayout.SOUTH);
		add(calendarPanel, BorderLayout.CENTER);
		calendarPanel.updateUI();
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(MainFrame.isBackground)
		g.drawImage(backGroundImages[setMonth - 1], 0, 0, getWidth(), getHeight(), this);
	}

	/*
	 * 현재 월 표기와 월 전환
	 */
	class DatePanel extends JPanel {

		// 왼쪽 버튼 기본 이미지
		ImageIcon leftButtonBasicImage = new ImageIcon("images/leftButtonBasicImage.png");
		// 왼쪽 버튼 Enter 이미지
		ImageIcon leftButtonEnterImage = new ImageIcon("images/leftButtonEnterImage.png");
		// 오른쪽 버튼 기본 이미지
		ImageIcon rightButtonBasicImage = new ImageIcon("images/rightButtonBasicImage.png");
		// 오른쪽 버튼 Enter 이미지
		ImageIcon rightButtonEnterImage = new ImageIcon("images/rightButtonEnterImage.png");
		Font font;
		JLabel date;
		Calendar cal;

		/**
		 * Create the panel.
		 */
		public DatePanel() {
			font = MainFrame.basicFont;
			JButton left = new JButton(leftButtonBasicImage);
			JButton right = new JButton(rightButtonBasicImage);
			// Button UI 설정
			setButtonUI(left);
			setButtonUI(right);
			// 데이터베이스 연결
			connection = MainFrame.connection;
			// Mouse 이벤트 처리
			left.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					// Entered이미지로 변경 시켜준다.
					left.setIcon(leftButtonEnterImage);
					// 커서의 모양을 바꿔준다
					left.setCursor(new Cursor(Cursor.HAND_CURSOR));
					// 효과음 재생
				}

				// 마우스가 버튼에 나갔을때 이벤트 처리
				@Override
				public void mouseExited(MouseEvent e) {
					left.setIcon(leftButtonBasicImage);
					// 커서의 모양을 바꿔준다
					left.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

				}

				// 버튼을 클릭했을때 이벤트 처리
				@Override
				public void mousePressed(MouseEvent e) {
					setMonth--;
					if (setMonth <= 0) {
						setYear--;
						setMonth = 12;
					}
					date.setText(setYear + "년" + setMonth + "월");
					rePaint();
				}
			});
			
			right.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					// Entered이미지로 변경 시켜준다.
					right.setIcon(rightButtonEnterImage);
					// 커서의 모양을 바꿔준다
					right.setCursor(new Cursor(Cursor.HAND_CURSOR));
					// 효과음 재생
				}

				// 마우스가 버튼에 나갔을때 이벤트 처리
				@Override
				public void mouseExited(MouseEvent e) {
					right.setIcon(rightButtonBasicImage);
					// 커서의 모양을 바꿔준다
					right.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

				}

				// 버튼을 클릭했을때 이벤트 처리
				@Override
				public void mousePressed(MouseEvent e) {
					setMonth++;
					if (setMonth > 12) {
						setYear++;
						setMonth = 1;
					}
					date.setText(setYear + "년" + setMonth + "월");
					rePaint();
				}
			});
			// 레이아웃 설정
			setLayout(new BorderLayout());

			// 달력 생성
			cal = Calendar.getInstance();


			// 날짜를 표현할 JLabel 선언
			date = new JLabel(setYear + "년 " + setMonth + "월");
			// 글자색 지정
			date.setForeground(Color.RED);
			// 폰트 지정
			date.setFont(font);
			// 글자 정렬
			date.setHorizontalAlignment(JLabel.CENTER);
			// 배경화면 설정
			setOpaque(false);
			// 컴포넌트 추가
			add(date, BorderLayout.CENTER);
			add(left, BorderLayout.WEST);
			add(right, BorderLayout.EAST);

		}

		public void setButtonUI(JButton button) {
			// 외곽선 제거
			button.setBorderPainted(false);
			// 내용 체우기 제거
			button.setContentAreaFilled(false);
			// 포커스 되었을시 테두리 제거
			button.setFocusPainted(false);
		}
	}

	class MenuPanel extends JPanel {
		ImageIcon plusButtonImage = new ImageIcon("images/plusButton.png");
		ImageIcon plusButtonEnterImage = new ImageIcon("images/plusEnterButton.png");
		ImageIcon taskButtonImage = new ImageIcon("images/taskButton.png");
		ImageIcon taskButtonEnterImage = new ImageIcon("images/taskEnterButton.png");
		ImageIcon settingButtonImage = new ImageIcon("images/settingButton.png");
		ImageIcon settingButtonEnterImage = new ImageIcon("images/settingEnterButton.png");
		JButton plusButton;
		JButton taskButton;
		JButton settingButton;

		public MenuPanel() {
			plusButton = new JButton(plusButtonImage);
			taskButton = new JButton(taskButtonImage);
			settingButton = new JButton(settingButtonImage);
			plusButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					plusButton.setIcon(plusButtonEnterImage);
					plusButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				// 마우스가 버튼에 나갔을때 이벤트 처리
				@Override
				public void mouseExited(MouseEvent e) {
					plusButton.setIcon(plusButtonImage);
					plusButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				// 버튼을 클릭했을때 이벤트 처리
				@Override
				public void mousePressed(MouseEvent e) {
					UIManager.put("OptionPane.messageFont", MainFrame.basicFont);
					String solaDate = Integer.toString(setYear) + "-" + setMonth + "-" + setDay;
					String memo = JOptionPane.showInputDialog(null, (setYear +"년" + setMonth+  "월" + setDay + "일" + "\n일정을 입력하세요"), "일정을 입력하세요", JOptionPane.OK_CANCEL_OPTION);
					String sql;
					if(memo != null) {
					sql = "INSERT INTO `schedule`(`solar`,`memo`) VALUES('" + solaDate + "','" + memo + "');";
					try {
						st = connection.createStatement();
						st.executeUpdate(sql);
					} catch (Exception err) {
						err.printStackTrace();
					}
					calendarPanel.updatePanel();
					}
				}
			});
			
			taskButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					taskButton.setIcon(taskButtonEnterImage);
					taskButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				// 마우스가 버튼에 나갔을때 이벤트 처리
				@Override
				public void mouseExited(MouseEvent e) {
					taskButton.setIcon(taskButtonImage);
					taskButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				// 버튼을 클릭했을때 이벤트 처리
				@Override
				public void mousePressed(MouseEvent e) {
					MainFrame.taskPanel.updatePanel();
					MainFrame.taskPanel.visible(true);
				}
			});
			
			settingButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					settingButton.setIcon(settingButtonEnterImage);
					settingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				// 마우스가 버튼에 나갔을때 이벤트 처리
				@Override
				public void mouseExited(MouseEvent e) {
					settingButton.setIcon(settingButtonImage);
					settingButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

				// 버튼을 클릭했을때 이벤트 처리
				@Override
				public void mousePressed(MouseEvent e) {
					MainFrame.settingPanel.updatePanel();
					MainFrame.settingPanel.visible(true);
					rePaint();
				}
			});

			setButtonUI(plusButton);
			setButtonUI(taskButton);
			setButtonUI(settingButton);

			setLayout(new BorderLayout());
			setOpaque(false);
			add(plusButton, BorderLayout.CENTER);
			add(taskButton, BorderLayout.WEST);
			add(settingButton, BorderLayout.EAST);
		}

		public void setButtonUI(JButton button) {
			// 외곽선 제거
			button.setBorderPainted(false);
			// 내용 체우기 제거
			button.setContentAreaFilled(false);
			// 포커스 되었을시 테두리 제거
			button.setFocusPainted(false);
		}
	}


}
