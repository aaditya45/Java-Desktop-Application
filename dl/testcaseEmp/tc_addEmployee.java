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
class EmployeeAddEmployeeTestCase{
	public static void main(String gg[]){
		String name = gg[0];
		int DesignationCode = Integer.parseInt(gg[1]);;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dateOfBirth = null;
		try{
			dateOfBirth = sdf.parse(gg[2]);
		}catch(ParseException pe){
			System.out.println(pe.getMessage());
			return ;
		}
		char Gender = gg[3].charAt(0);
		boolean IsIndian = Boolean.parseBoolean(gg[4]);
		BigDecimal BasicSalary = new BigDecimal(gg[5]);
		String PANNumber = gg[6];
		String AadharCardNumber = gg[7];
		try{
			EmployeeDTOInterface edtoi = new EmployeeDTO();
			EmployeeDAOInterface edaoi = new EmployeeDAO();
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
			edaoi.add(edtoi);
			System.out.println("Emloyee Code is : "+ edtoi.getEmployeeId());
		}catch(DAOException daoe){
			System.out.println(daoe.getMessage());
		}
		
		
		
	}	
}