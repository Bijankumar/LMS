package com.te.lms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.te.lms.dto.BatchDetailsDto;
import com.te.lms.dto.BatchEmployeesDto;
import com.te.lms.dto.EmployeePrimaryInfoDto;
import com.te.lms.dto.EmployeeProfileDto;
import com.te.lms.dto.EmployeeRequestedDto;
import com.te.lms.dto.MailRejectionDto;
import com.te.lms.dto.MentorDetailsDto;
import com.te.lms.dto.MockDetailsDto;
import com.te.lms.dto.MockRatingDto;
import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MentorDetails;
import com.te.lms.exception.BatchCreationException;
import com.te.lms.exception.BatchNotFoundException;
import com.te.lms.exception.EmployeeNotFoundException;
import com.te.lms.exception.MentorNotFoundException;
import com.te.lms.exception.RequiredDataNotFoundException;
import com.te.lms.repository.LmsEmployeeRepository;
import com.te.lms.repository.LmsMentorRepository;
import com.te.lms.service.LmsService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LmsControollerForBrowser {

	@Autowired
	private LmsEmployeeRepository employeeRepository;

	@Autowired
	private LmsMentorRepository mentorRepository;

	@Autowired
	private LmsService lmsService;

	// Building APIs for Web Page

	@GetMapping("/")
	public String getHome() {
		return "home";
	}

	@GetMapping("/signin")
	public String getSignIn() {
		return "login";
	}

	@GetMapping("/apply")
	public String getRegistered() {
		return "applyform";
	}

	@PostMapping("/application")
	public String applyForJob(@ModelAttribute EmployeePrimaryInfoDto primaryInfoDto) {
		lmsService.addEmployeeInfo(primaryInfoDto);
		return "redirect:/apply";
	}
	
	@GetMapping("/mentor/mentorPage")
	public String mentorPage(Principal principal) throws MentorNotFoundException {
		MentorDetails mentor = mentorRepository.findBymentorEmail(principal.getName());
		if (mentor != null) {
			if (mentor.getPswResetStatus().equals("Updated"))
				return "redirect:/mentor/getBatchDetail";
			else
				return "redirect:/resetMentor";
		} else
			throw new MentorNotFoundException("User Not Found");
	}

	@GetMapping("/employee/employeePage")
	public String employeePage(Principal principal) throws EmployeeNotFoundException {
		EmployeePrimaryInfo employee = employeeRepository.findByemployeeEmail(principal.getName());
		if (employee != null) {
			if (employee.getPswResetStatus().equals("Updated"))
				return "redirect:/employee/employeesPage";
			else
				return "redirect:/resetEmployee";
		} else
			throw new EmployeeNotFoundException("User Not Found");
	}

	@GetMapping("/employee/employeesPage")
	public String getEMployeeProfileDetails(Model model, Principal principal) throws EmployeeNotFoundException {
		EmployeeProfileDto employee = lmsService.getEmployeeByUserName(principal);
		model.addAttribute("employee", employee);
		return "employeePage";
	}

	@GetMapping("/resetEmployee")
	public String resetEmployeePassword() {
		return "resetEmployeePassword";
	}

	@GetMapping("/resetMentor")
	public String resetMentorPassword() {
		return "resetMentorPassword";
	}

	@GetMapping("/admin/adminPage")
	public String requestedEmployee() {
		return "redirect:/admin/requestedEmployees";
	}

	@GetMapping("/admin/requestedEmployees")
	public String requestedEmployees(Model model) throws EmployeeNotFoundException {
		List<EmployeeRequestedDto> employee = lmsService.getAllRequestedEmployeeData();
		if (employee.isEmpty()) {
			return "noRequestFound";
		} else {
			model.addAttribute("employee", employee);
			return "requestedEmployees"; // Return the name of the HTML template
		}
	}

	@GetMapping("/admin/approving")
	public String approveEmployee(HttpServletRequest request, @ModelAttribute EmployeePrimaryInfoDto primaryInfoDto)
			throws NumberFormatException, EmployeeNotFoundException, MessagingException {
		lmsService.approveEmployeeById(request, primaryInfoDto);
		return "redirect:/admin/requestedEmployees";
	}

	@GetMapping("/admin/employeeRejection")
	public String rejectEmployee(HttpServletRequest request, @ModelAttribute MailRejectionDto rejectionDto)
			throws MessagingException, EmployeeNotFoundException {
		lmsService.rejectionOfEmployee(request, rejectionDto);
		return "redirect:/admin/requestedEmployees";
	}

	@GetMapping("/admin/approvedEmployees")
	public String approvedEmployees(Model model) throws EmployeeNotFoundException {
		List<EmployeeRequestedDto> employee = lmsService.getAllApprovedEmployeeData();
		if (employee.isEmpty()) {
			return "noActiveEmployeeFound";
		} else {
			model.addAttribute("employee", employee);
			return "approvedList"; // Return the name of the HTML template
		}
	}

	@PostMapping("/admin/addNewMentor")
	public String createNewMentor(@ModelAttribute MentorDetailsDto mentorDetailsDto) throws MessagingException {
		lmsService.addMentors(mentorDetailsDto);
		return "redirect:/admin/getAllMentors";
	}

	@GetMapping("/admin/getAllMentors")
	public String listMentors(Model model) throws MentorNotFoundException {
		List<MentorDetailsDto> mentor = lmsService.getAllMentorDetail();
		if (mentor.isEmpty()) {
			return "mentornotFound";
		} else {
			model.addAttribute("mentor", mentor);
			return "mentorDetails";
		}
	}

	@PostMapping("/admin/addNewBatch")
	public String createNewBatch(@ModelAttribute BatchDetailsDto batchDetailsDto) throws BatchNotFoundException,
			RequiredDataNotFoundException, MentorNotFoundException, MessagingException, BatchCreationException {
		lmsService.addBatches(batchDetailsDto);
		return "redirect:/admin/getBatchDetails";
	}

	@GetMapping("/admin/getBatchDetails")
	public String listBatches(Model model) throws BatchNotFoundException {
		List<BatchDetailsDto> batch = lmsService.getAllBatchDetail();
		if (batch.isEmpty()) {
			return "contentNotFound";
		} else {
			model.addAttribute("batch", batch);
			return "batchDetails";
		}
	}

	@GetMapping("/mentor/getBatchDetail")
	public String getBatches(Model model, Principal principal) throws BatchNotFoundException {
		List<BatchDetailsDto> batch = lmsService.getBatchByMentor(principal);
		if (batch.isEmpty()) {
			return "batchNotfound";
		} else {
			model.addAttribute("batch", batch);
			return "mentorpage";
		}
	}

	@PostMapping("/mentor/assignMock")
	public String createMock(@ModelAttribute MockDetailsDto mockDetailsDto)
			throws RequiredDataNotFoundException, BatchNotFoundException, MentorNotFoundException {
		lmsService.addMockInformation(mockDetailsDto);
		return "redirect:/mentor/getBatchDetail";
	}

	private int number;

	@GetMapping("/mentor/getBatchEmployees")
	public String getBatchEmployees(Model model, HttpServletRequest request)
			throws NumberFormatException, BatchNotFoundException {
		List<BatchEmployeesDto> employee = lmsService
				.getAllBatchEmployees(Integer.parseInt(request.getParameter("batchNo")));
		if (employee.isEmpty()) {
			return "contentNotFound";
		} else {
			model.addAttribute("employee", employee);
			number = Integer.parseInt(request.getParameter("batchNo"));
			return "batchEmployees";
		}
	}

	@PostMapping("/mentor/addAttendance")
	public String giveAttendance(Model model, HttpServletRequest request) throws EmployeeNotFoundException {
		lmsService.giveAttendance(Integer.parseInt(request.getParameter("employeeNo")));
		return "redirect:/mentor/getCurrentBatchEmployees";
	}

	// ** Supporting API for Attendance API **
	// Not Recommended for Direct hit...
	@GetMapping("/mentor/getCurrentBatchEmployees")
	public String getCurrentBatchEmployees(Model model) throws BatchNotFoundException {
		List<BatchEmployeesDto> employee = lmsService.getAllBatchEmployees(number);
		if (employee.isEmpty()) {
			return "contentNotFound";
		} else {
			model.addAttribute("employee", employee);
			return "batchEmployees";
		}
	}

	@PostMapping("/mentor/giveMockRating")
	public String giveMockRatings(@ModelAttribute MockRatingDto mockRatingDto)
			throws RequiredDataNotFoundException, EmployeeNotFoundException {
		lmsService.giveRatings(mockRatingDto);
		return "redirect:/mentor/getCurrentBatchEmployees";
	}

	@GetMapping("/admin/deleteEmployee")
	public void deleteEmployeeData(HttpServletRequest request) throws NumberFormatException, EmployeeNotFoundException {
		lmsService.deleteEmployeeByID(request);
	}

	@PostMapping("/resetMentorsPassword")
	public String resetMentorPassword(HttpServletRequest request, Principal principal) throws MentorNotFoundException {
		lmsService.resetMentorsPassword(request, principal);
		return "redirect:/logout";
	}

	@PostMapping("/resetEmployeePassword")
	public String resetEmployeePassword(HttpServletRequest request, Principal principal)
			throws EmployeeNotFoundException {
		lmsService.resetEmployeePassword(request, principal);
		return "redirect:/logout";
	}
}
