import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
class DesignationDeleteDesignationTestCase{
	public static void main(String[] gg){
		int code = Integer.parseInt(gg[0]);
		try{
			DesignationDAOInterface ddaoi = new DesignationDAO();
			ddaoi.delete(code);
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}