import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationGetByCodeTestCase{
	public static void main(String[] gg){
		int code = Integer.parseInt(gg[0]);
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddtoi = ddaoi.getByCode(code);
			System.out.println(ddtoi.getTitle() + " is Designation at "+ code);
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}