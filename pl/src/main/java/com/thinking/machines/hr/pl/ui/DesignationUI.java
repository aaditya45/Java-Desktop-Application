/*
MODE : Mode is very useful concept in UI. We can set application behaviour according to the mode in which application is. When an application is in 
a mode it will remain until another mode gets set. Application always stays in a mode during its lifecycle. Beginning mode is view mode.
*/
package com.thinking.machines.hr.pl.ui;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;

public class DesignationUI extends JFrame{
	private JLabel headingLabel, searchLabel, errorMessageLabel;
	private JTable table;
	private JTextField searchTextField;
	private DesignationModel dm;
	private JScrollPane jsp;
	private JButton clearSearchTextButton;
	private DesignationPanel designationPanel;
	private Container container;
	private int rowIndex;
	private enum MODE{
		VIEW,
		ADD,
		EDIT,
		DELETE,
		EXPORT_TO_PDF
	}
	private MODE mode;
	public DesignationUI(){
		try{
			for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
				if("Windows".equals(info.getClassName())){
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}catch(Exception e){}
		initComponents();
		setAppearance();
		addListeners();
		setViewMode();
		designationPanel.setViewMode();
	}
	public void initComponents(){
		headingLabel = new JLabel("<HTML><U>Designations</U></HTML>");
		searchLabel = new JLabel("Search");
		searchTextField = new JTextField();
		errorMessageLabel = new JLabel("");
		dm = new DesignationModel();
		table = new JTable(dm);
		clearSearchTextButton = new JButton("x");
		jsp = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		designationPanel = new DesignationPanel();
		container = getContentPane();
	}
	public void setAppearance(){
		//Setting all component appearence
		headingLabel.setFont(new Font("Roberto Condensed",Font.BOLD,24));
		searchLabel.setFont(new Font("Baloo Bhai 2",Font.PLAIN,18));
		searchTextField.setFont(new Font("Baloo Bhai 2",Font.PLAIN,18));
		errorMessageLabel.setFont(new Font("Baloo Bhai 2",Font.PLAIN,12));
		errorMessageLabel.setForeground(Color.RED);
		table.setRowHeight(40);
		table.setFont(new Font("Baloo Bhai 2",Font.PLAIN,16));
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setGridColor(new Color(255, 77, 77));
		table.setShowVerticalLines(false);
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Baloo Bhai 2",Font.BOLD,15));
		header.setOpaque(false);
		header.setBackground(new Color(255, 77, 77));
		header.setForeground(Color.WHITE);
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		clearSearchTextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		//setting locations
		int tm=0 ,lm =0;
		headingLabel.setBounds(lm+10,tm+10,150,30);
		searchLabel.setBounds(lm+10,tm+10+30+10,100,30);
		searchTextField.setBounds(lm+10+100,tm+10+30+10,300,30);
		clearSearchTextButton.setBounds(lm+10+100+300+10,tm+10+30+10,40,30);
		errorMessageLabel.setBounds(lm+10+100+145,tm+10+25,75,15);
		jsp.setBounds(lm+10,tm+10+30+10+30+20,470,275);
		designationPanel.setBounds(lm+10,tm+10+30+10+30+20+275+10,470,200);

		//putting them in JFrame
		container.setLayout(null);	
		container.add(headingLabel);
		container.add(searchLabel);
		container.add(searchTextField);
		container.add(clearSearchTextButton);
		container.add(errorMessageLabel);
		container.add(jsp);
		container.add(designationPanel);
		int width = 505;
		int height = 635;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(((d.width/2)-(width/2)),((d.height/2)-(height/2)));
		container.setBackground(new Color(255, 165, 0));
		setSize(width,height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}
	public void addListeners(){
		clearSearchTextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				searchTextField.setText("");
			}
		});
		searchTextField.getDocument().addDocumentListener(new DocumentListener(){//using anonymous class here. Also make DesignationUI Document listener.
			public void search(){
				errorMessageLabel.setText("");
				if(searchTextField.getText().trim().length() == 0){
					table.setRowSelectionAllowed(false);//to stop selecting every thing
					return;
				}
				try{
					rowIndex = dm.indexOfTitle(searchTextField.getText().trim(),true);
				}catch(BLException ble){
					errorMessageLabel.setText("Not Found");
					table.setRowSelectionAllowed(false);
					return;
				}
				table.setRowSelectionAllowed(true);//to start selecting
				table.setRowSelectionInterval(rowIndex, rowIndex);
				Rectangle rectangle = table.getCellRect(rowIndex, 0, true);
				table.scrollRectToVisible(rectangle);
			}

			public void insertUpdate(DocumentEvent documentEvent){
				//this method gets called when any letter gets added in textfield
				search();
			}
			public void removeUpdate(DocumentEvent documentEvent){
				//this method gets called when any letter gets removed in textfield
				search();
			}
			public void changedUpdate(DocumentEvent documentEvent){
				//this method gets called on a special event related to styling
				search();
			}
		});//Document Listener Ends here
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent lse){
				int rowIndex = table.getSelectedRow();
				try{
					designationPanel.setDesignation(dm.getByIndex(rowIndex));
				}catch(BLException ble){
					
				}
			}
		});
	}
	private void setViewMode(){
		this.mode = MODE.VIEW;
		if(dm.getRowCount() == 0){
			searchTextField.setEnabled(false);
			clearSearchTextButton.setEnabled(false);
			table.setEnabled(false);
		}else{
			searchTextField.setEnabled(true);
			clearSearchTextButton.setEnabled(true);
			table.setEnabled(true);
		}
	}
	private void setAddMode(){
		this.mode = MODE.ADD;
		searchTextField.setEnabled(false);
		clearSearchTextButton.setEnabled(false);
		table.setEnabled(false);
	}
	private void setEditMode(){
		this.mode = MODE.EDIT;
		searchTextField.setEnabled(false);
		clearSearchTextButton.setEnabled(false);
		table.setEnabled(false);
	}
	private void setDeleteMode(){
		this.mode = MODE.DELETE;
		searchTextField.setEnabled(false);
		clearSearchTextButton.setEnabled(false);
		table.setEnabled(false);
	}
	private void setExportToPDFMode(){
		this.mode = MODE.EXPORT_TO_PDF;
		searchTextField.setEnabled(false);
		clearSearchTextButton.setEnabled(false);
		table.setEnabled(false);	
	}

	//Bottom Panel containing Other stuff
	//Programming starts from here
	private class DesignationPanel extends JPanel{
		private JLabel designationLabel;
		private JLabel errorMessage;
		private JButton addButton, editButton, removeButton, cancelButton, exportToPDFButton;
		private JLabel titleLabel;
		private JTextField inputTextField;
		private ButtonPanel buttonPanel;
		private DesignationInterface designation;
		private ImageIcon addIcon, deleteIcon, cancelIcon, editIcon, exportToPDFIcon,saveIcon, updateIcon;
		DesignationPanel(){
			setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
			initComponents();
			setAppearance();
			addListeners();
		}
		private void initComponents(){
			addIcon = new ImageIcon(this.getClass().getResource("/icon/add_icon.png"));
			editIcon = new ImageIcon(this.getClass().getResource("/icon/edit_icon.png"));
			deleteIcon = new ImageIcon(this.getClass().getResource("/icon/delete_icon.png"));
			cancelIcon = new ImageIcon(this.getClass().getResource("/icon/cancel_icon.png"));
			exportToPDFIcon = new ImageIcon(this.getClass().getResource("/icon/pdf_icon.png"));
			saveIcon = new ImageIcon(this.getClass().getResource("/icon/save_icon.png"));
			updateIcon = new ImageIcon(this.getClass().getResource("/icon/update_icon.png"));
			designationLabel = new JLabel("Designation | ");
			titleLabel = new JLabel("");
			errorMessage = new JLabel("");
			inputTextField = new JTextField();
			inputTextField.setVisible(false);
			//addButton.setIcon(new Icon());
			addButton = new JButton(addIcon);
			addButton.setEnabled(false);
			editButton = new JButton(editIcon);
			editButton.setEnabled(false);
			removeButton = new JButton(deleteIcon);
			removeButton.setEnabled(false);
			cancelButton = new JButton(cancelIcon);
			cancelButton.setEnabled(false);
			exportToPDFButton = new JButton(exportToPDFIcon);
			exportToPDFButton.setEnabled(true);
			buttonPanel = new ButtonPanel();
			designation = null;
		}
		private void setAppearance(){
			designationLabel.setFont(new Font("Roberto Condensed",Font.BOLD,20));
			titleLabel.setFont(new Font("Baloo Bhai 2",Font.PLAIN,18));
			errorMessage.setFont(new Font("Baloo Bhai 2",Font.PLAIN,15));
			inputTextField.setFont(new Font("Baloo Bhai 2",Font.PLAIN,18));
			addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			exportToPDFButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			int tm=0 ,lm =0;
			designationLabel.setBounds(lm+10,tm+10,135,30);
			titleLabel.setBounds(lm+10+135,tm+10,170,30);
			inputTextField.setBounds(lm+10+135+10,tm+10,270,30);
			errorMessage.setBounds(lm+10+135+10+270,tm+10,75,30);
			buttonPanel.setBounds(lm+10,tm+10+30+75,450,75);
			addButton.setBounds(40,12, 50, 50);
			editButton.setBounds(40+30+50,12,50,50);
			removeButton.setBounds(40+30+30+50+50,12,50,50);
			cancelButton.setBounds(40+30+30+30+50+50+50,12,50,50);
			exportToPDFButton.setBounds(40+30+30+30+30+50+50+50+50,12,50,50);
			setLayout(null);
			setBackground(new Color(255, 203, 107));
			add(designationLabel);
			add(titleLabel);
			add(errorMessage);
			add(inputTextField);
			buttonPanel.setLayout(null);
			buttonPanel.add(addButton);
			buttonPanel.add(editButton);
			buttonPanel.add(removeButton);
			buttonPanel.add(cancelButton);
			buttonPanel.add(exportToPDFButton);
			add(buttonPanel);
		}//setAppearence method ends here
		
		private boolean addDesignation(){
			String title = inputTextField.getText().trim();
			DesignationInterface d = null;
			int rowIndex = 0;
			if(title.length() == 0){
				JOptionPane.showMessageDialog(DesignationUI.this,"Some text Required.");
				return false;
			}
			try{
				d = new Designation();
				d.setTitle(title);
				DesignationUI.this.dm.add(d);
				rowIndex = DesignationUI.this.dm.indexLocation(d);
			}catch(BLException ble){
				if(ble.hasGenericException()){
					System.out.println(ble.getGenericException());
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getGenericException());
					return false;
				}
				java.util.List<String> exceptions = new ArrayList<>();
				exceptions = ble.getProperties();
				for(String property : exceptions){
					System.out.println(ble.getException(property));
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getException(property));
					return false;
				}
			}
			table.setRowSelectionAllowed(true);//to start selecting
			table.setRowSelectionInterval(rowIndex, rowIndex);
			Rectangle rectangle = table.getCellRect(rowIndex, 0, true);
			table.scrollRectToVisible(rectangle);
			return true;
		}

		private boolean updateDesignation(){//update Designation starts
			String title = inputTextField.getText().trim();
			int code = 0;
			DesignationInterface d = null;
			int rowIndex = 0;
			if(title.length() == 0){
				JOptionPane.showMessageDialog(DesignationUI.this,"Some text Required.");
				return false;
			}
			try{
				d = new Designation();
				d.setTitle(title);
				d.setCode(this.designation.getCode());
				DesignationUI.this.dm.update(d);
				rowIndex = DesignationUI.this.dm.indexLocation(d);
			}catch(BLException ble){
				if(ble.hasGenericException()){
					System.out.println(ble.getGenericException());
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getGenericException());
					return false;
				}
				java.util.List<String> exceptions = new ArrayList<>();
				exceptions = ble.getProperties();
				for(String property : exceptions){
					System.out.println(ble.getException(property));
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getException(property));
					return false;
				}
			}
			table.setRowSelectionAllowed(true);//to start selecting
			table.setRowSelectionInterval(rowIndex, rowIndex);
			Rectangle rectangle = table.getCellRect(rowIndex, 0, true);
			table.scrollRectToVisible(rectangle);
			return true;
		}//update Designation Ends here

		public boolean deleteDesignation(){//delete Designation starts from here
			String title = inputTextField.getText().trim();
			int code = this.designation.getCode();
			int selection = JOptionPane.showConfirmDialog(DesignationUI.this,"Do you want to delete "+title+"?","Confirm",JOptionPane.YES_NO_OPTION);
			if(selection == JOptionPane.NO_OPTION) return false;
			try{
				DesignationUI.this.dm.delete(code);
			}catch(BLException ble){
				if(ble.hasGenericException()){
					System.out.println(ble.getGenericException());
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getGenericException());
					return false;
				}
				java.util.List<String> exceptions = new ArrayList<>();
				exceptions = ble.getProperties();
				for(String property : exceptions){
					System.out.println(ble.getException(property));
					JOptionPane.showMessageDialog(DesignationUI.this,ble.getException(property));
					return false;
				}
			}
			JOptionPane.showMessageDialog(DesignationUI.this,title + " got deleted.");
			this.titleLabel.setText("");
			return true;
		}//delete Designation Ends here
		private void addListeners(){
			//add button event handling
			this.addButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					if(DesignationUI.this.mode == MODE.VIEW){
						setAddMode();
					}else{
						if(addDesignation()){
							setViewMode();
						}
					}
				}
			});
			//edit button event handling
			this.editButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					if(DesignationUI.this.mode == MODE.VIEW){
						setEditMode();
					}else{
						if(updateDesignation()){
							setViewMode();
						}
					}
				}
			});
			//delete button event programming
			this.removeButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent actionEvent){
					setDeleteMode();
					setViewMode();
				}
			});
			//cancel button event programming
			this.cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					setViewMode();
				}
			});
			//export to pdf button event programming
			this.exportToPDFButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					JFileChooser jfc = new JFileChooser();
					jfc.setCurrentDirectory(new File("."));
					int selectedOption = jfc.showSaveDialog(DesignationUI.this);
					if(selectedOption == JFileChooser.APPROVE_OPTION){
						try{
							File file = jfc.getSelectedFile();
							String pdfFile = file.getAbsolutePath();
							if(pdfFile.endsWith(".")) pdfFile +="pdf";
							else if(pdfFile.endsWith(".pdf")==false) pdfFile +=".pdf";
							File newFile = new File(pdfFile);
							File parentFile = new File(newFile.getParent());
							if(parentFile.exists()==false || parentFile.isDirectory()==false){
								JOptionPane.showMessageDialog(DesignationUI.this,newFile.getAbsolutePath()+ " path is Incorrect.", "Something wrong happend", JOptionPane.OK_OPTION);
								return;
							}
							dm.exportToPdf(newFile);
							JOptionPane.showMessageDialog(DesignationUI.this,"Yay! your PDF File is successfuly\nsaved at "+newFile.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
						}catch(BLException ble){
							JOptionPane.showMessageDialog(DesignationUI.this, ble.getGenericException());
						}catch(Exception e){
							System.out.println(e.getMessage());
						}
					}
				}
			});
		}//actionListeners method ends here
		private class ButtonPanel extends JPanel{
			ButtonPanel(){
				setBorder(BorderFactory.createLineBorder(new Color(150,150,150)));
			}
		}
		public void setDesignation(DesignationInterface designation){
			this.designation = designation;
			if(designation == null){
				titleLabel.setText("");
			}else{
				titleLabel.setText(designation.getTitle());
			}
		}
		public DesignationInterface getDesignation(){
			return this.designation;
		}
		public void setViewMode(){
			DesignationUI.this.setViewMode();
			this.addButton.setIcon(addIcon);
			this.editButton.setIcon(editIcon);
			this.titleLabel.setEnabled(true);
			this.titleLabel.setVisible(true);
			this.errorMessage.setVisible(false);
			this.inputTextField.setEnabled(false);
			this.inputTextField.setVisible(false);
			this.addButton.setEnabled(true);
			this.cancelButton.setEnabled(false);
			if(dm.getRowCount() == 0){
				this.editButton.setEnabled(false);
				this.removeButton.setEnabled(false);
				this.exportToPDFButton.setEnabled(false);
			}else{
				this.editButton.setEnabled(true);
				this.removeButton.setEnabled(true);
				this.exportToPDFButton.setEnabled(true);
			}
		}
		public void setAddMode(){
			DesignationUI.this.setAddMode();
			this.titleLabel.setVisible(false);
			this.inputTextField.setText("");
			this.inputTextField.setVisible(true);
			this.inputTextField.setEnabled(true);
			this.errorMessage.setVisible(false);
			this.addButton.setIcon(saveIcon);
			this.editButton.setEnabled(false);
			this.removeButton.setEnabled(false);
			this.cancelButton.setEnabled(true);
			this.exportToPDFButton.setEnabled(false);
		}
		public void setEditMode(){
			if(table.getSelectedRow()<0 || table.getSelectedRow()>=dm.getRowCount()){
				JOptionPane.showMessageDialog(this, "Please Select a Designation First.");
				return;
			}
			DesignationUI.this.setEditMode();
			this.titleLabel.setVisible(false);
			this.inputTextField.setText(this.designation.getTitle());
			this.inputTextField.setEnabled(true);
			this.inputTextField.setVisible(true);
			this.errorMessage.setVisible(false);
			this.addButton.setEnabled(false);
			this.editButton.setIcon(updateIcon);
			this.removeButton.setEnabled(false);
			this.cancelButton.setEnabled(true);
			this.exportToPDFButton.setEnabled(false);
		}
		public void setDeleteMode(){
			if(table.getSelectedRow()<0 || table.getSelectedRow()>=dm.getRowCount()){
				JOptionPane.showMessageDialog(this, "Please Select a Designation First.");
				return;
			}
			DesignationUI.this.setDeleteMode();
			this.titleLabel.setEnabled(true);
			this.inputTextField.setEnabled(false);
			this.errorMessage.setVisible(false);
			this.editButton.setEnabled(false);
			this.addButton.setEnabled(false);
			this.removeButton.setEnabled(false);
			this.cancelButton.setEnabled(false);
			this.exportToPDFButton.setEnabled(false);
			deleteDesignation();
		}
		public void setExportToPDFMode(){
			DesignationUI.this.setExportToPDFMode();
			this.titleLabel.setEnabled(false);
			this.inputTextField.setEnabled(false);
			this.errorMessage.setVisible(false);
			this.addButton.setEnabled(false);
			this.removeButton.setEnabled(false);
			this.cancelButton.setEnabled(false);
			this.exportToPDFButton.setEnabled(false);
		}
	}
}