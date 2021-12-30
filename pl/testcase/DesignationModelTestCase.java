import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
class DesignationModelTestCase extends JFrame{
	private JTable table;
	private DesignationModel dm;
	private JScrollPane jsp;
	private Container container;
	public DesignationModelTestCase(){
		dm = new DesignationModel();
		table = new JTable(dm);
		jsp = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container = getContentPane();
		container.add(jsp);
		setLocation(200,200);
		setSize(400,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String gg[]){
		System.out.println("...");
		DesignationModelTestCase dmtc = new DesignationModelTestCase();
	}
}