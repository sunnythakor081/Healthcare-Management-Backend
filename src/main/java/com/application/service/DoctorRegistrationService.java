package com.application.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.model.Doctor;
import com.application.repository.DoctorRegistrationRepository;

@Service
public class DoctorRegistrationService 
{
	@Autowired
	private DoctorRegistrationRepository doctorRegistrationRepo;
	
	public Doctor saveDoctor(Doctor  doctor)
	{
		return doctorRegistrationRepo.save(doctor);
	}

	public Doctor updateDoctorProfile(Doctor doctor)
	{
		return doctorRegistrationRepo.save(doctor);
	}
	public List<Doctor> getAllDoctors()
	{
		return (List<Doctor>)doctorRegistrationRepo.findAll();
	}

	public void updateStatus(String email)
	{
		doctorRegistrationRepo.updateStatus(email);
	}

	public void rejectStatus(String email)
	{
		doctorRegistrationRepo.rejectStatus(email);
		System.out.print("rejected");
	}

	
	public void updatePatientStatus(String slot, String doctorname)
	{
		doctorRegistrationRepo.updatePatientStatus(slot, doctorname);
	}
	
	public void rejectPatientStatus(String slot, String doctorname)
	{
		doctorRegistrationRepo.rejectPatientStatus(slot, doctorname);
		System.out.print("rejected");
	}
	
	public List<Doctor> getDoctorListByEmail(String email) 
	{
		return (List<Doctor>)doctorRegistrationRepo.findDoctorListByEmail(email);
	}
	
	public Doctor fetchDoctorByEmail(String email)
	{
		return doctorRegistrationRepo.findByEmail(email);
	}
	
	public Doctor fetchDoctorByDoctorname(String doctorname)
	{
		return doctorRegistrationRepo.findByDoctorname(doctorname);
	}
	
	public Doctor fetchDoctorByEmailAndPassword(String email, String password)
	{
		return doctorRegistrationRepo.findByEmailAndPassword(email, password);
	}
	public List<Doctor> getApprovedDoctors() {
		System.out.println("Service: getApprovedDoctors called - Fetching all doctors..."); // Log start
		List<Doctor> allDoctors = (List<Doctor>) doctorRegistrationRepo.findAll();
		System.out.println("Service: Total doctors found: " + allDoctors.size()); // Total count

		List<Doctor> approvedDoctors = new ArrayList<>();
		for (Doctor doctor : allDoctors) {
			System.out.println("Service: Checking doctor " + doctor.getEmail() + " - Status: " + doctor.getStatus()); // Per doctor log
			if ("accept".equals(doctor.getStatus())) { // Exact match "accept"
				approvedDoctors.add(doctor);
			}
		}
		System.out.println("Service: Approved doctors count: " + approvedDoctors.size()); // Final count
		return approvedDoctors;
	}

	public Doctor fetchProfileByEmail(String email) {
		return doctorRegistrationRepo.findByEmail(email);
	}

}