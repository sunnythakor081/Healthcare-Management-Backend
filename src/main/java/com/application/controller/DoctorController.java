package com.application.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.application.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.application.service.AppointmentBookingService;
import com.application.service.DoctorRegistrationService;
import com.application.service.PrescriptionService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {
	@Autowired
	private DoctorRegistrationService doctorRegisterService;

	@Autowired
	private AppointmentBookingService appointmentBookingService;

	@Autowired
	private PrescriptionService prescriptionService;


	@GetMapping("/doctor/dashboard")
//    @CrossOrigin(origins = "http://localhost:4202")
	public ResponseEntity<Map<String, Integer>> getDashboardStats() throws Exception {
		Map<String, Integer> stats = new HashMap<>();


		// Get total doctors
		List<Doctor> doctors = doctorRegisterService.getAllDoctors();
		stats.put("totalDoctors", doctors.size());

		// Get total slots
		List<Slots> slots = appointmentBookingService.getSlotList();
		stats.put("totalSlots", slots.size());

		// Get total appointments
		List<Appointments> appointments = appointmentBookingService.getAllAppointments();

		// Get total prescriptions
		List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
		stats.put("totalPrescriptions", prescriptions.size());

		int totalPatients = (int) appointments.stream().map(Appointments::getPatientname).distinct().count(); // Unique kar
		stats.put("totalPatients", totalPatients);
		return new ResponseEntity<Map<String, Integer>>(stats, HttpStatus.OK);
	}

	@GetMapping("/doctorlist")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Doctor>> getDoctors() throws Exception {
		List<Doctor> doctors = doctorRegisterService.getAllDoctors();
		return new ResponseEntity<List<Doctor>>(doctors, HttpStatus.OK);
	}

	@GetMapping("/gettotaldoctors")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Integer>> getTotalDoctors() throws Exception {
		List<Doctor> doctors = doctorRegisterService.getAllDoctors();
		List<Integer> al = new ArrayList<>();
		al.add(doctors.size());
		return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
	}

	@GetMapping("/gettotalslots")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Integer>> getTotalSlots() throws Exception {
		List<Slots> slots = appointmentBookingService.getSlotList();
		List<Integer> al = new ArrayList<>();
		al.add(slots.size());
		return new ResponseEntity<List<Integer>>(al, HttpStatus.OK);
	}

	@GetMapping("/acceptstatus/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<String>> updateStatus(@PathVariable String email) throws Exception {
		doctorRegisterService.updateStatus(email);
		List<String> al = new ArrayList<>();
		al.add("accepted");
		return new ResponseEntity<List<String>>(al, HttpStatus.OK);
	}

	@GetMapping("/rejectstatus/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<String>> rejectStatus(@PathVariable String email) throws Exception {
		doctorRegisterService.rejectStatus(email);
		List<String> al = new ArrayList<>();
		al.add("rejected");
		return new ResponseEntity<List<String>>(al, HttpStatus.OK);
	}

	@GetMapping("/acceptpatient/{slot}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<String>> updatePatientStatus(@PathVariable String slot) throws Exception {
		List<Appointments> patient = appointmentBookingService.findPatientBySlot(slot);
		String doctorName = "";
		for (Appointments obj : patient) {
			if (obj.getSlot().equals(slot))
				doctorName = obj.getDoctorname();
		}
		doctorRegisterService.updatePatientStatus(slot, doctorName);
		List<String> al = new ArrayList<>();
		al.add("accepted");
		return new ResponseEntity<List<String>>(al, HttpStatus.OK);
	}

	@GetMapping("/rejectpatient/{slot}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<String>> rejectPatientStatus(@PathVariable String slot) throws Exception {
		List<Appointments> patient = appointmentBookingService.findPatientBySlot(slot);
		String doctorName = "";
		for (Appointments obj : patient) {
			if (obj.getSlot().equals(slot))
				doctorName = obj.getDoctorname();
		}
		doctorRegisterService.rejectPatientStatus(slot, doctorName);
		List<String> al = new ArrayList<>();
		al.add("rejected");
		return new ResponseEntity<List<String>>(al, HttpStatus.OK);
	}

	@PostMapping("/addBookingSlots")
//	@CrossOrigin(origins = "http://localhost:4200")
	public String addNewSlot(@RequestBody Slots slots) throws Exception {
		appointmentBookingService.saveSlots(slots);
		return "modified successfully !!!";
	}

	@GetMapping("/doctorlistbyemail/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Doctor>> getRequestHistoryByEmail(@PathVariable String email) throws Exception {
		System.out.print("requesting");
		List<Doctor> history = doctorRegisterService.getDoctorListByEmail(email);
		return new ResponseEntity<List<Doctor>>(history, HttpStatus.OK);
	}

	@GetMapping("/slotDetails/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Slots>> getSlotDetails(@PathVariable String email) throws Exception {
		List<Slots> slots = appointmentBookingService.getSlotDetails(email);
		return new ResponseEntity<List<Slots>>(slots, HttpStatus.OK);
	}

	@GetMapping("/slotDetails")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Slots>> getSlotList() throws Exception {
		List<Slots> slots = appointmentBookingService.getSlotList();
		return new ResponseEntity<List<Slots>>(slots, HttpStatus.OK);
	}

	@GetMapping("/slotDetailsWithUniqueDoctors")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<Set<String>> getSlotDetailsWithUniqueDoctors() throws Exception {
		List<Slots> slots = appointmentBookingService.getSlotDetailsWithUniqueDoctors();
		Set<String> set = new LinkedHashSet<>();
		for (Slots obj : slots) {
			set.add(obj.getDoctorname());
		}
		return new ResponseEntity<Set<String>>(set, HttpStatus.OK);
	}

	@GetMapping("/slotDetailsWithUniqueSpecializations")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<Set<String>> getSlotDetailsWithUniqueSpecializations() throws Exception {
		List<Slots> slots = appointmentBookingService.getSlotDetailsWithUniqueSpecializations();
		Set<String> set = new LinkedHashSet<>();
		for (Slots obj : slots) {
			set.add(obj.getSpecialization());
		}
		return new ResponseEntity<Set<String>>(set, HttpStatus.OK);
	}

	@GetMapping("/patientlistbydoctoremail/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Appointments>> getPatientDetails(@PathVariable String email) throws Exception {
		List<Doctor> history = doctorRegisterService.getDoctorListByEmail(email);
		String doctorname = "";
		for (Doctor obj : history) {
			if (obj.getEmail().equals(email)) {
				doctorname = obj.getDoctorname();
				break;
			}
		}
		List<Appointments> patients = appointmentBookingService.findPatientByDoctorName(doctorname);
		return new ResponseEntity<List<Appointments>>(patients, HttpStatus.OK);
	}

	@PostMapping("/addPrescription")
	public ResponseEntity<?> addNewPrescription(@RequestBody Prescription prescription) {
		try {
			List<Appointments> patients = appointmentBookingService.getAllPatients();
			String patientID = patients.stream()
					.filter(obj -> obj.getPatientname().equals(prescription.getPatientname()))
					.findFirst()
					.map(Appointments::getPatientid)
					.orElse(null);
			if (patientID == null) {
				return new ResponseEntity<>("Patient not found", HttpStatus.BAD_REQUEST);  // This returns
			}
			prescription.setPatientid(patientID);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			prescription.setDate(formatter.format(date));

			Prescription savedPrescription = prescriptionService.savePrescriptions(prescription);
			return new ResponseEntity<>(savedPrescription, HttpStatus.OK);  // This returns
		} catch (Exception e) {
			// Ensure return here if missing
			return new ResponseEntity<>("Error saving prescription: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);  // Add/confirm this
		}
		// No code after catch—compiler sees all paths covered
	}
	@GetMapping("/doctorProfileDetails/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Doctor> getDoctorProfileDetails(@PathVariable String email) throws Exception {
		Doctor doctor = doctorRegisterService.fetchProfileByEmail(email);
		if (doctor == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 if no doctor found
		}
		return new ResponseEntity<>(doctor, HttpStatus.OK);
	}
	
	@PutMapping("/updatedoctor")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<Doctor> updateDoctorProfile(@RequestBody Doctor doctor) throws Exception
	{
		Doctor doctorobj = doctorRegisterService.updateDoctorProfile(doctor);
		return new ResponseEntity<Doctor>(doctorobj, HttpStatus.OK);
	}
	@GetMapping("/doctors/approved")
	public ResponseEntity<List<Doctor>> getApprovedDoctors() {
		System.out.println("Controller: /doctors/approved endpoint called"); // Log call
		List<Doctor> approved = doctorRegisterService.getApprovedDoctors(); // Service call
		System.out.println("Controller: Returning " + approved.size() + " approved doctors"); // Log response
		return ResponseEntity.ok(approved);
	}
	
	@GetMapping("/patientlistbydoctoremailanddate/{email}")
//	@CrossOrigin(origins = "http://localhost:4205")
	public ResponseEntity<List<Appointments>> getPatientDetailsAndDate(@PathVariable String email) throws Exception
	{
		List<Appointments> patients = appointmentBookingService.getAllPatients();
		List<Doctor> history = doctorRegisterService.getDoctorListByEmail(email);
		String doctorname = "";
		OUTER:for(Doctor obj : history)
		{
			if(obj.getEmail().equals(email))
			{
				doctorname = obj.getDoctorname();
				break OUTER;
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();  
        String todayDate = formatter.format(date);
        List<Appointments> appointmentsList = new ArrayList<>();
		OUTER:for(Appointments obj : patients)
		{
			if(obj.getDoctorname().equals(doctorname) && obj.getDate().equals(todayDate))
			{
				doctorname = obj.getDoctorname();
				appointmentsList.add(obj);
				break OUTER;
			}
		}
		return new ResponseEntity<List<Appointments>>(appointmentsList, HttpStatus.OK);
	}


	
}