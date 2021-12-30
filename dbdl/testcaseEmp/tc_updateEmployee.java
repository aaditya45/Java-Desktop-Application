import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.enums.*;
class EmployeeUpdateEmployeeTestCase{
	public static void main(String gg[]){
		String EmpID=gg[0];
		String name = gg[1];
		int DesignationCode = Integer.parseInt(gg[2]);;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dateOfBirth = null;
		try{
			dateOfBirth = sdf.parse(gg[3]);
		}catch(ParseException pe){
			System.out.println(pe.getMessage());
			return ;
		}
		char Gender = gg[4].charAt(0);
		boolean IsIndian = Boolean.parseBoolean(gg[5]);
		BigDecimal BasicSalary = new BigDecimal(gg[6]);
		String PANNumber = gg[7];
		String AadharCardNumber = gg[8];
		try{
			EmployeeDTOInterface edtoi = new EmployeeDTO();
			EmployeeDAOInterface edaoi = new EmployeeDAO();
			edtoi.setEmployeeId(EmpID);
			edtoi.setName(name);
			edtoi.setDesignationCode(DesignationCode);
			edtoi.setDateOfBirth(dateOfBirth);
				if(Gender == 'M'){
					edtoi.setGender(GENDER.MALE);
				}else if(Gender == 'F'){
					edtoi.setGender(GENDER.FEMALE);
				}
			edtoi.setIsIndian(IsIndian);
			edtoi.setBasicSalary(BasicSalary);
			edtoi.setPANNumber(PANNumber);
			edtoi.setAadharCardNumber(AadharCardNumber);
			edaoi.update(edtoi);
			System.out.println("Emloyee with Code "+ edtoi.getEmployeeId()+" got UPDATED.");
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
	}	
}