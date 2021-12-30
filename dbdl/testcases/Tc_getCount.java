import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
class DesignationGetCountTestCase{
	public static void main(String[] gg){
		try{
			int count;
			DesignationDAOInterface ddaoi = new DesignationDAO();
			count=ddaoi.getCount();
			System.out.println("Number of Designations : "+count);
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}