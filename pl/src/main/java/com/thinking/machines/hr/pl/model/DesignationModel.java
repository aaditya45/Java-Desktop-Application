/*
NOTE : - Model class is always used to extract data from Business Layer.

*/
package com.thinking.machines.hr.pl.model;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;

//iTextPDF jar file imports
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.font.*;
import com.itextpdf.io.font.*;
import com.itextpdf.io.image.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.borders.*;


public class DesignationModel extends AbstractTableModel{
	private String title[];
	private java.util.List<DesignationInterface> data;
	private DesignationManagerInterface dmi;
	public DesignationModel(){
		populateDataStructure();
	}
	private void populateDataStructure(){
		this.title = new String[2];
		this.title[0] = "S.No.";
		this.title[1] = "Designations";
		DesignationInterface di = new Designation();
		dmi = null;
		Set<DesignationInterface> designations = null;
		try{
			dmi = DesignationManager.getDesignationManager();
			designations = dmi.getDesignations();
		}catch(BLException ble){
			System.out.println("BL exception got raised");
		}
		this.data = new LinkedList<>();
		for(DesignationInterface designation : designations){
			this.data.add(designation);
		}
		Collections.sort(this.data,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right){
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
	}
	public String getColumnName(int columnIndex){
		return this.title[columnIndex];
	}
	public int getColumnCount(){
		return 2;
	}
	public int getRowCount(){
		return this.data.size();
	}
	public Object getValueAt(int rowIndex, int columnIndex){
		if(columnIndex == 0) return rowIndex+1;
		else return this.data.get(rowIndex).getTitle();
	}
	public boolean isCellEditable(int rowIndex, int columnIndex){
		return false;
	}
	public java.lang.Class<?> getColumnClass(int columnIndex){
		Class c = null;
		try{
			if(columnIndex == 0) c = Class.forName("java.lang.Integer");
			if(columnIndex == 1) c = Class.forName("java.lang.String");
		}catch(Exception e){
			System.out.println(e);
		}
		return c;
	}
	//Application Specific methods******************************************************************************
	public void add(DesignationInterface designation) throws BLException{
		dmi = DesignationManager.getDesignationManager();
		dmi.addDesignation(designation);
		data.add(designation);
		//worse possible thing to do
		Collections.sort(this.data,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right){
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
		fireTableDataChanged();
	}
	public int indexLocation(DesignationInterface designation) throws BLException{
		Iterator<DesignationInterface> iterator = this.data.iterator();
		DesignationInterface d;
		int counter = -1;
		while(iterator.hasNext()){
			d = iterator.next();
			counter++;
			if(d.equals(designation)){
				return counter;
			}
		}
		BLException ble = new BLException();
		ble.setGenericException("Invalid Designation : Designation Does Not Exists");
		throw ble;
	}
	//Index Of Title, as mentioned in its name, it returns the index of Designation which starts with given title. Partial Left Search gets true if the title given is not complete i.e. "ujja" (Ujjain)
	public int indexOfTitle(String title, boolean partialLeftSearch) throws BLException{
		int counter = -1;
		if(partialLeftSearch){
			for(DesignationInterface designation : this.data){
				counter++;
				if(designation.getTitle().toUpperCase().startsWith(title.toUpperCase())) return counter;	
			}
		}else{
			for(DesignationInterface designation : this.data){
				counter++;
				if(designation.getTitle().toUpperCase().equals(title.toUpperCase())) return counter;	
			}
		}
		BLException ble = new BLException();
		ble.setGenericException("Invalid title : "+title);
		throw ble;
	}
	public void update(DesignationInterface designation) throws BLException{
		dmi = DesignationManager.getDesignationManager();
		dmi.updateDesignation(designation);
		data.remove(designation);
		data.add(designation);
		//worse possible thing to do
		Collections.sort(this.data,new Comparator<DesignationInterface>(){
			public int compare(DesignationInterface left,DesignationInterface right){
				return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
			}
		});
		fireTableDataChanged();
	}
	public void delete(int code) throws BLException{
		dmi = DesignationManager.getDesignationManager();
		dmi.removeDesignation(code);
		int index = 0;
		Iterator<DesignationInterface> iterator = this.data.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getCode() == code) break;
			index++;
		}
		if(index == this.data.size()){
			BLException ble = new BLException();
			ble.setGenericException("Invalid Code : "+code);
			throw ble;
		}
		data.remove(index);
		fireTableDataChanged();
	}
	public DesignationInterface getByIndex(int index) throws BLException{
		BLException ble = new BLException();
		if(index<0 || index >= this.data.size()){
			ble.setGenericException("Wrong index");
			throw ble;
		}else return this.data.get(index);
	}
	public void exportToPdf(File file) throws BLException{
		if(file.exists()) file.delete();
		try{
			PdfWriter pdfWriter = new PdfWriter(file);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document doc = new Document(pdfDocument);
			com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(ImageDataFactory.create(this.getClass().getResource("/icon/logo.png")));
			Paragraph logoPara = new Paragraph();
			logoPara.add(logo);
			Paragraph companyName = new Paragraph();
			companyName.add("\tDesignation List");
			PdfFont companyNameFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			companyName.setFont(companyNameFont);
			companyName.setFontSize(18);
			Paragraph reportTitle = new Paragraph("List of designations.");
			PdfFont reportTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			reportTitle.setFont(reportTitleFont);
			reportTitle.setFontSize(15);
			PdfFont columnTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			Paragraph columnTitle1 = new Paragraph("S.No.");
			columnTitle1.setFont(columnTitleFont);
			columnTitle1.setFontSize(15);
			Paragraph columnTitle2 = new Paragraph("Designations");
			columnTitle2.setFont(columnTitleFont);
			columnTitle2.setFontSize(15);
			Paragraph pageNumberParagraph;
			PdfFont pageNumberFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			Paragraph dataParagraph;
			PdfFont dataFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			float topTableColumnWidth[] = {1,5};
			float dataTableColumnWidth[] = {1,5};
			int sno,x,pageSize;
			pageSize = 24;
			DesignationInterface designation;
			sno = 0;
			x = 0;
			boolean newPage = true;
			Table pageNumberTable;
			Table topTable;
			Table dataTable = null;
			Cell cell;
			int pageNumber = 0 ;
			int numberOfPages = this.data.size()/pageSize;
			if((this.data.size()%pageSize)!=0) numberOfPages ++;
			while(x<this.data.size()){
				if(newPage == true){
					//create new page header
					pageNumber++;
					topTable = new Table(UnitValue.createPercentArray(topTableColumnWidth));
					cell = new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(logoPara);
					topTable.addCell(cell);
					cell = new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(companyName);
					cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
					topTable.addCell(cell);
					doc.add(topTable);
					pageNumberParagraph = new Paragraph("Page : "+pageNumber+"/"+numberOfPages);
					pageNumberParagraph.setFont(pageNumberFont);
					pageNumberParagraph.setFontSize(13);
					pageNumberTable = new Table(1);
					pageNumberTable.setWidth(UnitValue.createPercentValue(100));
					cell = new Cell();
					cell.setBorder(Border.NO_BORDER);
					cell.add(pageNumberParagraph);
					cell.setTextAlignment(TextAlignment.RIGHT);
					pageNumberTable.addCell(cell);
					doc.add(pageNumberTable);
					dataTable = new Table(UnitValue.createPercentArray(dataTableColumnWidth));
					dataTable.setWidth(UnitValue.createPercentValue(100));
					cell = new Cell(1,2);
					cell.add(reportTitle);
					cell.setTextAlignment(TextAlignment.CENTER);
					dataTable.addHeaderCell(cell);
					dataTable.addHeaderCell(columnTitle1);
					dataTable.addHeaderCell(columnTitle2);
					newPage = false;
				}
				designation = this.data.get(x);
				//add Row table
				sno++;
				cell = new Cell();
				dataParagraph = new Paragraph(String.valueOf(sno));
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				cell.setTextAlignment(TextAlignment.RIGHT);
				dataTable.addCell(cell);
				
				cell = new Cell();
				dataParagraph = new Paragraph(designation.getTitle());
				dataParagraph.setFont(dataFont);
				dataParagraph.setFontSize(14);
				cell.add(dataParagraph);
				cell.setTextAlignment(TextAlignment.LEFT);
				dataTable.addCell(cell);
				
				x++;
				if(sno%pageSize == 0 || x  == this.data.size()){
					//create footer
					doc.add(dataTable);
					doc.add(new Paragraph("Software by Aaditya Sharma\nMentored By Mr.Prafull Kelkar"));
					if(x < this.data.size()){
						//add new page to document
						doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
						newPage = true;
					}
				}
			}
			doc.close();
		}catch(Exception exception){
			BLException ble = new BLException();
			ble.setGenericException(exception.getMessage());
		}
	}
}
