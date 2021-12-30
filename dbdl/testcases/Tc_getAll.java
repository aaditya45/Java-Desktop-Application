import java.util.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationGetAllTestCase{
	public static void main(String[] gg){
		try{
			Set<DesignationDTOInterface> designations;
			designations = new TreeSet<>();
			DesignationDAOInterface ddaoi = new DesignationDAO();
			designations = ddaoi.getAll();
			designations.forEach(x->System.out.println(x.getTitle()));
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}
}