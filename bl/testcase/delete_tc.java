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
class DesignationDeleteTestCase{
	public static void main(String gg[]){
		int code = Integer.valueOf(gg[0]);
		try{
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			dmi.removeDesignation(code);
			System.out.println("Designation deleted");
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