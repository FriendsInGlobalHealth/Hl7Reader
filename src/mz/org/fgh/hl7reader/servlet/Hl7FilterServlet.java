package mz.org.fgh.hl7reader.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ADT_A24;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;
import mz.org.fgh.hl7reader.model.DemographicData;

/**
 * Servlet implementation class Hl7FilterServlet
 */
@WebServlet("/pesquisarHl7")
public class Hl7FilterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String partialNid = request.getParameter("partialNid");  
		PrintWriter out = response.getWriter();
		out.println("Parial nid " +partialNid+ " sent for search");
		
		// Open an InputStream to read from the file
        File file = new File("Patient_Demographic_Data_1040114.hl7"); 
        InputStream is = new FileInputStream(file);

        // It's generally a good idea to buffer file IO
        is = new BufferedInputStream(is);

        // The following class is a HAPI utility that will iterate over
        // the messages which appear over an InputStream
        Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(is);
        
        List<DemographicData> demographicData = new ArrayList<DemographicData>();
        
        while (iter.hasNext()) {

            Message hapiMsg = iter.next();
            
            ADT_A24 adtMsg = (ADT_A24) hapiMsg;
            ca.uhn.hl7v2.model.v251.segment.PID pid = adtMsg.getPID();
            
            DemographicData data = new DemographicData();
            
    		data.setNid(pid.getPatientID().getIDNumber().getValue());
    		data.setFirstName(pid.getPatientName(0).getGivenName().getValue());
    		data.setMiddleName(pid.getPatientName(0).getSecondAndFurtherGivenNamesOrInitialsThereof().getValue());
            data.setLastName(pid.getPatientName(0).getFamilyName().getSurname().getValue());
            data.setDateOfBirth(pid.getDateTimeOfBirth().getTime().getValue());
            data.setGender(pid.getAdministrativeSex().getValue());
            data.setAddress(pid.getPatientAddress(0).getStreetAddress().getStreetName().getValue()+" "+pid.getPatientAddress(0).getCity().getValue()+" "+
            		pid.getPatientAddress(0).getStateOrProvince().getValue()+" "+pid.getPatientAddress(0).getCountry().getValue());
            
            demographicData.add(data);
        }
        
        List<DemographicData> filteredDemo = new ArrayList<DemographicData>();
        
        for (DemographicData data : demographicData) {
			if (data.getNid().contains(partialNid)) {
				filteredDemo.add(data);
			}
		}
        
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
        request.setAttribute("filteredPatients", filteredDemo);
        requestDispatcher.forward(request, response); 
	}
}
