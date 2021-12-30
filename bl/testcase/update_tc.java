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
class DesignationUpdateTestCase{
	public static void main(String gg[]){
		int code = Integer.valueOf(gg[0]);
		String title = gg[1];
		try{
			DesignationInterface di = new Designation();
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			di.setCode(code);
			di.setTitle(title);
			dmi.updateDesignation(di);
			System.out.println("Designation updated");
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