import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationAddDesignationTestCase{
	public static void main(String[] gg){
		String title = gg[0];
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			ddtoi.setTitle(title);
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddaoi.add(ddtoi);
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}