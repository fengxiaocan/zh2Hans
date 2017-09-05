package com.evil.hans;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class zh2Hans {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					zh2Hans window = new zh2Hans();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public zh2Hans() {
		initialize();
	}

	private String selectPath;
	private ArrayList<String> sCharsetList = CharsetList.getCharsetList();
	private ZHConverter traditional = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
	private ZHConverter simplified = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	private JButton button_1;
	private JButton button_2;
	private JComboBox comboBox;

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(500, 400, 700, 120);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton button = new JButton("选择转换文本");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileSystemView fsv = FileSystemView.getFileSystemView(); // 注意了，这里重要的一句
				FileNameExtensionFilter filter1 = new FileNameExtensionFilter("text", "txt");
				fileChooser.addChoosableFileFilter(filter1);
				FileNameExtensionFilter filter2 = new FileNameExtensionFilter("java", "java");
				fileChooser.addChoosableFileFilter(filter2);
				FileNameExtensionFilter filter3 = new FileNameExtensionFilter("xml", "xml");
				fileChooser.addChoosableFileFilter(filter3);
				fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
				fileChooser.setDialogTitle("请选择要转码的文件或文件夹");
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (JFileChooser.APPROVE_OPTION == result) {
					selectPath = fileChooser.getSelectedFile().getPath();
				}
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 20));

		button_1 = new JButton("繁体转简体");
		button_1.setFont(new Font("宋体", Font.PLAIN, 20));

		button_2 = new JButton("简体转繁体");
		button_2.setFont(new Font("宋体", Font.PLAIN, 20));

		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				translation(simplified);
			}
		});
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				translation(traditional);
			}
		});

		comboBox = new JComboBox();
		addCharsetItem(comboBox);
		comboBox.setSelectedIndex(1);

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addGap(19)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE).addGap(18)
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
						.addGap(21)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(23)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(25, Short.MAX_VALUE)));
		frame.getContentPane().setLayout(groupLayout);
	}

	/**
	 * 给下拉选择框设置选择文本
	 * 
	 * @param jcb
	 *            选择器
	 */
	private void addCharsetItem(JComboBox jcb) {
		for (String string : sCharsetList) {
			jcb.addItem(string);
		}
	}

	/**
	 * 选择转换
	 * 
	 * @param zh
	 *            简繁转换
	 */
	private void translation(ZHConverter zh) {
		if (selectPath == null) {
			// 提示
			JOptionPane.showMessageDialog(button_2, "请选择待转换文件!");
		} else {
			new Thread(new Runnable() {
				public void run() {
					FileInputStream fis = null;
					PrintStream ps = null;
					BufferedReader br = null;
					try {
						File file = new File(selectPath);
						fis = new FileInputStream(file);
						// 获取原来的文件名
						String name = file.getName();
						int pos = name.lastIndexOf('.');
						// 组合成新的文件名
						String newName = name.substring(0, pos) + "_translation" + name.substring(pos, name.length());
						ps = new PrintStream(new File(file.getParent(), newName));
						br = new BufferedReader(new InputStreamReader(fis, (String) comboBox.getSelectedItem()));
						String str = br.readLine();
						while (str != null) {
							String text = zh.convert(str);
							ps.println(text);
							str = br.readLine();
						}
						JOptionPane.showMessageDialog(button_2, "转换完成!");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(button_2, "转换失败!");
					} finally {
						close(fis);
						close(ps);
						close(br);
					}
				}
			}).start();
		}
	}

	/**
	 * 关流
	 * 
	 * @param clo
	 */
	private void close(Closeable clo) {
		if (clo != null) {
			try {
				clo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
