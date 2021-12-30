import java.util.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.dto.*;
class DesignationGetAllTestCase{
	public static void main(String gg[]){
		try{
			Set<DesignationInterface> designations = new TreeSet<>();
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			designations = dmi.getDesignations();
			DesignationInterface di;
			System.out.println("Total Registrations : "+designations.size());
			for(DesignationInterface designation : designations){
				di = designation;
				System.out.println("Name : "+di.getTitle());	
			}
			System.out.println("---*---*---");
		}catch(BLException ble){
			if(ble.hasGenericException()){
				System.out.println(ble.getGenericException());
			}
			List<String> exceptions = new ArrayList<>();
			exceptions = ble.getProperties();
			for(String property : exceptions){
				System.out.println(ble.getException(property));
			}
		}
	}
}