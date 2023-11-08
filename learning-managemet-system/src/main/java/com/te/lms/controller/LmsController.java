package com.te.lms.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.te.lms.dto.BatchDetailsDto;
import com.te.lms.dto.BatchEmployeesDto;
import com.te.lms.dto.EmployeeDetailModifyableDto;
import com.te.lms.dto.EmployeePrimaryInfoDto;
import com.te.lms.dto.EmployeeRequestedDto;
import com.te.lms.dto.MailRejectionDto;
import com.te.lms.dto.MentorDetailsDto;
import com.te.lms.dto.MockDetailsDto;
import com.te.lms.dto.MockRatingDto;
import com.te.lms.exception.ApprovalFailedException;
import com.te.lms.exception.BatchCreationException;
import com.te.lms.exception.BatchNotFoundException;
import com.te.lms.exception.EmployeeNotFoundException;
import com.te.lms.exception.MentorNotFoundException;
import com.te.lms.exception.RequiredDataNotFoundException;
import com.te.lms.response.LmsResponse;
import com.te.lms.service.LmsService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LmsController {

	@Autowired
	private LmsService lmsService;

	@Autowired
	private LmsResponse response;

	// ** Employee_Controller Details **

	Logger logger = LogManager.getLogger(LmsController.class);
	
	@PostMapping("/employeeApply")
	public ResponseEntity<LmsResponse> addEmployee(@RequestBody EmployeePrimaryInfoDto primaryInfoDto) {
		logger.info("Applying Job Application..!!");
		EmployeePrimaryInfoDto primaryInfo = lmsService.addEmployeeInfo(primaryInfoDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data added Succesfully..!!");
		response.setLmsList(Arrays.asList(primaryInfo));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Get Employee Profile By ID
	@GetMapping("/getProfile/{id}")
	public ResponseEntity<LmsResponse> getEmployeeProfileDetails(@PathVariable("id") int id)
			throws EmployeeNotFoundException {
		logger.info("Retriving Employee Profile!!");
		EmployeePrimaryInfoDto primaryInfoDto = lmsService.getEmployeeByUserId(id);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Succesfully fetched..!");
		response.setLmsList(Arrays.asList(primaryInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/employee/modifyEmployee/{id}")
	public ResponseEntity<LmsResponse> modifyEmployeeDetails(@PathVariable("id") int id,@RequestBody EmployeeDetailModifyableDto employeeModifyDto)
			throws EmployeeNotFoundException {
		logger.info("Modifying Employee Profile!!");
		EmployeeDetailModifyableDto modifyInfoDto = lmsService.modifyEmployeeByUserId(id,employeeModifyDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Succesfully modified..!");
		response.setLmsList(Arrays.asList(modifyInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Get Employee By Authentication.....
	@GetMapping("/employee/getProfile")
	public ResponseEntity<LmsResponse> getEmployeeProfile(HttpServletRequest request) throws EmployeeNotFoundException {
		logger.info("Retriving Employee Profile!!");
		EmployeePrimaryInfoDto primaryInfoDto = lmsService.getEmployeeByUserName(request);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Retrived Succesfully..!");
		response.setLmsList(Arrays.asList(primaryInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// *** Admin_Cntroller Details ***

	@GetMapping("/admin/getRequestedEmployee")
//	@GetMapping("/getRequestedEmployee")
	public ResponseEntity<LmsResponse> getAllRequestedEmployee() throws EmployeeNotFoundException {
		logger.info("Retriving All Requested Employees..!!");
		List<EmployeeRequestedDto> employeeList = lmsService.getAllRequestedEmployeeData();
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data fetched Succesfully !");
		response.setLmsList(Arrays.asList(employeeList));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/admin/getAllApprovedEmployees")
//	@GetMapping("/getAllApprovedEmployees")
	public ResponseEntity<LmsResponse> getAllApprovedEmployees() throws EmployeeNotFoundException {
		logger.info("Retriving All Approved Employees..!!");
		List<EmployeeRequestedDto> employeeList = lmsService.getAllApprovedEmployeeData();
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data fetched Succesfully !");
		response.setLmsList(Arrays.asList(employeeList));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/admin/deleteEmployee/{id}")
//	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<LmsResponse> deleteEmployee(@PathVariable("id") int employeeId)
			throws EmployeeNotFoundException {
		logger.info("Deleting Perticular Employee..!!");
		EmployeePrimaryInfoDto primaryInfoDto = lmsService.deleteByID(employeeId);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Removed Succesfully!");
		response.setLmsList(Arrays.asList(primaryInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Mentor Operations

	@PostMapping("/admin/addMentors")
//	@PostMapping("/addMentors")
	public ResponseEntity<LmsResponse> addMentors(@RequestBody MentorDetailsDto mentorDetailsDto)
			throws MessagingException {
		logger.info("Adding New Mentor..!!");
		MentorDetailsDto mentorInfoDto = lmsService.addMentors(mentorDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data added Succesfully..! ");
		response.setLmsList(Arrays.asList(mentorInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/admin/getMentors")
//	@GetMapping("/getMentors")
	public ResponseEntity<LmsResponse> getAllMentor() throws MentorNotFoundException {
		logger.info("Retriving All Mentors..!!");
		List<MentorDetailsDto> mentor = lmsService.getAllMentorDetail();
		if (mentor.isEmpty())
			throw new MentorNotFoundException("Mentor not added yet..!!");
		else {
			response.setError(false);
			response.setStatus(200);
			response.setMessage("Data fetched Succesfully....!");
			response.setLmsList(Arrays.asList(mentor));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/admin/modifyMentor/{id}")
//	@PutMapping("/modifyMentor/{id}")
	public ResponseEntity<LmsResponse> modifyMentorDetails(@PathVariable("id") int mentorNo,
			@RequestBody MentorDetailsDto mentorDetailsDto) throws MentorNotFoundException {
		logger.info("Modifying Mentor Details..!!");
		MentorDetailsDto mentorInfoDto = lmsService.updateMentorDetails(mentorNo, mentorDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Modified Succesfully..!");
		response.setLmsList(Arrays.asList(mentorInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/admin/deleteMentor/{id}")
//	@DeleteMapping("/deleteMentor/{id}")
	public ResponseEntity<LmsResponse> deleteMentor(@PathVariable("id") int mentorId) throws MentorNotFoundException {
		logger.info("Deleting Particular Mentor..!!");
		MentorDetailsDto mentorDetailsDto = lmsService.deleteMentorByID(mentorId);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Deleted Succesfully..!");
		response.setLmsList(Arrays.asList(mentorDetailsDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Batch Operations

	@PostMapping("/admin/addbatch")
//	@PostMapping("/addbatch")
	public ResponseEntity<LmsResponse> addBatches(@RequestBody BatchDetailsDto batchDetailsDto)
			throws BatchNotFoundException, RequiredDataNotFoundException, MentorNotFoundException, MessagingException,
			BatchCreationException {
		BatchDetailsDto batchInfoDto = lmsService.addBatches(batchDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data added Succesfully..! ");
		response.setLmsList(Arrays.asList(batchInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/admin/getBatches")
//	@GetMapping("/getBatches")
	public ResponseEntity<LmsResponse> getAllBatches() throws BatchNotFoundException {
		List<BatchDetailsDto> batchList = lmsService.getAllBatchDetail();
		if (batchList.isEmpty())
			throw new BatchNotFoundException("Batch Not Creted Yet..!!");
		else {
			response.setError(false);
			response.setStatus(200);
			response.setMessage("Data fetched Succesfully.. !");
			response.setLmsList(Arrays.asList(batchList));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/admin/modifyBatch/{id}")
//	@PutMapping("/modifyBatch/{id}")
	public ResponseEntity<LmsResponse> modifyBatchDetails(@PathVariable("id") int batchNo,
			@RequestBody BatchDetailsDto batchDetailsDto) throws BatchNotFoundException {
		BatchDetailsDto batchDetailDto = lmsService.updateBatchDetail(batchNo, batchDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Modified Succesfully..!");
		response.setLmsList(Arrays.asList(batchDetailDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/admin/deleteBatch/{id}")
//	@DeleteMapping("/deleteBatch/{id}")
	public ResponseEntity<LmsResponse> deleteBatch(@PathVariable("id") int batchId) throws BatchNotFoundException {
		BatchDetailsDto batchInfoDto = lmsService.deleteBatchByID(batchId);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data Deleted Succesfully..!");
		response.setLmsList(Arrays.asList(batchInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Approval and Rejection of Requested Employees

	@PutMapping("/admin/approve/{id}")
//	@PutMapping("/approve/{id}")
	public ResponseEntity<LmsResponse> approveEmployee(@PathVariable("id") int employeeNo,
			@RequestBody EmployeePrimaryInfoDto primaryInfoDto)
			throws MessagingException, EmployeeNotFoundException, ApprovalFailedException {
		EmployeePrimaryInfoDto primaryInfo = lmsService.approveById(employeeNo, primaryInfoDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data updated Succesfully..!");
		response.setLmsList(Arrays.asList(primaryInfo));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/admin/rejectEmployee/{id}")
//	@PutMapping("/rejectEmployee/{id}")
	public ResponseEntity<LmsResponse> rejectEmployee(@PathVariable("id") int id,
			@RequestBody MailRejectionDto rejectionDto) throws MessagingException, EmployeeNotFoundException {
		MailRejectionDto mailRejectionDto = lmsService.rejectEmployee(id, rejectionDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Rjected Succesfully..!");
		response.setLmsList(Arrays.asList(mailRejectionDto));
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	// *** Mentor_Cntroller Details ***

	@PostMapping("/mentor/createMock")
//	@PostMapping("/createMock")
	public ResponseEntity<LmsResponse> addMockInfo(@RequestBody MockDetailsDto mockDetailsDto)
			throws RequiredDataNotFoundException, BatchNotFoundException, MentorNotFoundException {
		MockDetailsDto mockInfoDto = lmsService.addMockInformation(mockDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data added Succesfully..!");
		response.setLmsList(Arrays.asList(mockInfoDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/mentor/giveRating")
//	@PostMapping("/giveRating")
	public ResponseEntity<LmsResponse> giveMockRatings(@RequestBody MockRatingDto mockRatingDto)
			throws RequiredDataNotFoundException, EmployeeNotFoundException {
		MockRatingDto mockRatingInfo = lmsService.giveRatings(mockRatingDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data added Succesfully..!");
		response.setLmsList(Arrays.asList(mockRatingInfo));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/mentor/getBatchEmployees/{id}")
//	@GetMapping("/getBatchEmployees/{id}")
	public ResponseEntity<LmsResponse> getBatchEmployees(@PathVariable("id") int batchNo)
			throws BatchNotFoundException {
		List<BatchEmployeesDto> batchEmployeesListDto = lmsService.getAllBatchEmployees(batchNo);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data fetched Succesfully..!");
		response.setLmsList(Arrays.asList(batchEmployeesListDto));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping("/mentor/giveAttendance/{id}")
//	@PutMapping("/giveAttendance/{id}")
	public ResponseEntity<LmsResponse> giveAttendance(@PathVariable("id") int employeeNo)
			throws EmployeeNotFoundException {
		lmsService.giveAttendance(employeeNo);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Attendance added Succesfully..!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Password-Reset Operation(Mentor And Employee)

	@PutMapping("/employee/resetPasswordEmployee")
	public ResponseEntity<LmsResponse> resetPassword(@RequestBody EmployeePrimaryInfoDto primaryInfoDto)
			throws EmployeeNotFoundException {
		EmployeePrimaryInfoDto primaryInfo = lmsService.resetPassword(primaryInfoDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Password Changed");
		response.setLmsList(Arrays.asList(primaryInfo));
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping("/mentor/resetPasswordMentor")
	public ResponseEntity<LmsResponse> resetMentorPassword(@RequestBody MentorDetailsDto mentorDetailsDto)
			throws MentorNotFoundException {
		MentorDetailsDto mentorDetails = lmsService.resetMentorPassword(mentorDetailsDto);
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Password Changed");
		response.setLmsList(Arrays.asList(mentorDetails));
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

// When All Employee Record needed(Approved/Rejected)

	@GetMapping("/admin/getAllEmployee")
//	@GetMapping("/getAllEmployee")
	public ResponseEntity<LmsResponse> getAllEmployee() throws EmployeeNotFoundException {
		List<EmployeePrimaryInfoDto> employeeList = lmsService.getAllEmployeeData();
		response.setError(false);
		response.setStatus(200);
		response.setMessage("Data fetched Succesfully..!");
		response.setLmsList(Arrays.asList(employeeList));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
