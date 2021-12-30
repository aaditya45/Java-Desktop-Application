import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationUpdateDesignationTestCase{
	public static void main(String[] gg){
		int code = Integer.parseInt(gg[0]);
		String updated_title = gg[1];
		try{
			DesignationDTOInterface ddtoi = new DesignationDTO();
			ddtoi.setTitle(updated_title);
			ddtoi.setCode(code);
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddaoi.update(ddtoi);
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}