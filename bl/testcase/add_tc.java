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
class DesignationAddTestCase{
	public static void main(String gg[]){
		String title = gg[0];
		int code = 0;
		try{
			DesignationInterface di = new Designation();
			DesignationManagerInterface dmi = DesignationManager.getDesignationManager();
			di.setCode(code);
			di.setTitle(title);
			dmi.addDesignation(di);
			System.out.println("Designation Added at position : "+ di.getCode());
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