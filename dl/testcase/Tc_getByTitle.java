import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationGetByTitleTestCase{
	public static void main(String[] gg){
		String name = gg[0];
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddtoi = ddaoi.getByTitle(name);
			System.out.println(ddtoi.getCode() + " is code of Title "+ ddtoi.getTitle());
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}