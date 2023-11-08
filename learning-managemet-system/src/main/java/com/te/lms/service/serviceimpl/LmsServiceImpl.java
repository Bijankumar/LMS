package com.te.lms.service.serviceimpl;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.te.lms.controller.LmsController;
import com.te.lms.dto.BatchDetailsDto;
import com.te.lms.dto.BatchEmployeesDto;
import com.te.lms.dto.EmployeeDetailModifyableDto;
import com.te.lms.dto.EmployeePrimaryInfoDto;
import com.te.lms.dto.EmployeeProfileDto;
import com.te.lms.dto.EmployeeRequestedDto;
import com.te.lms.dto.MailRejectionDto;
import com.te.lms.dto.MentorDetailsDto;
import com.te.lms.dto.MockDetailsDto;
import com.te.lms.dto.MockRatingDto;
import com.te.lms.entity.BatchDetails;
import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MentorDetails;
import com.te.lms.entity.MockDetails;
import com.te.lms.entity.MockRating;
import com.te.lms.exception.ApprovalFailedException;
import com.te.lms.exception.BatchCreationException;
import com.te.lms.exception.BatchNotFoundException;
import com.te.lms.exception.EmployeeNotFoundException;
import com.te.lms.exception.MentorNotFoundException;
import com.te.lms.exception.RequiredDataNotFoundException;
import com.te.lms.exception.TraversalExceptionHandler;
import com.te.lms.repository.LmsBatchRepository;
import com.te.lms.repository.LmsEmployeeRepository;
import com.te.lms.repository.LmsMentorRepository;
import com.te.lms.repository.LmsMockRatingRepository;
import com.te.lms.repository.LmsMockRepository;
import com.te.lms.securityconfig.AutoGeneratePassword;
import com.te.lms.service.LmsService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class LmsServiceImpl implements LmsService {

	@Autowired
	private LmsEmployeeRepository lmsEmployeeRepository;

	@Autowired
	private LmsMentorRepository lmsMentorRepository;

	@Autowired
	private LmsBatchRepository lmsBatchRepository;

	@Autowired
	private LmsMockRepository lmsmockRepository;

	@Autowired
	private LmsMockRatingRepository lmsratingRepository;

	@Autowired
	private AutoGeneratePassword autoGenerate;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TraversalExceptionHandler traversalExceptionHandler;

	private String email, password, reason, person, userName;
	
	
	Logger logger = LogManager.getLogger(LmsServiceImpl.class);


	// Mail Sending Services..

	private void sendEmail(String person, String email, String userName, String password) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(email);
		helper.setSubject("Reset Password");
		helper.setText("Dear " + person + ",\nGreetings of the Day! \n  Your Current Login Credentials \n   username : "
				+ userName + "\n   password : " + password + "\n\nThank You.\nTESTYANTRA");
		mailSender.send(message);
	}

	private void sendEmail(String person, String email, Date dateOfJoin) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(email);
		helper.setSubject("Job Confirmation & Onboarding: Associate Software Engineer at TechnoElevate");
		helper.setText("Dear " + person + ",\nGreetings of the Day! \n  Your Date Of Joing is on " + dateOfJoin
				+ ", please do report at Jayanagar Office with your Original documents and Identity proof. \n\n\nThank You.\nTESTYANTRA");
		mailSender.send(message);
	}

	private void sendRejectionEmail(String email, String reason) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message);
		messageHelper.setTo(email);
		messageHelper.setSubject("Application Rejected");
		messageHelper.setText("Dear Applicant, \n      " + reason + "\n\nTry next time.\n\nThank You.\nTESTYANTRA");
		mailSender.send(message);
	}

	// New Employee adding...
	@Override
	public EmployeePrimaryInfoDto addEmployeeInfo(EmployeePrimaryInfoDto primaryInfoDto) {
		EmployeePrimaryInfo employeePrimaryInfo = new EmployeePrimaryInfo();
		BeanUtils.copyProperties(primaryInfoDto, employeePrimaryInfo);
		employeePrimaryInfo.setPswResetStatus("Default");
		lmsEmployeeRepository.save(employeePrimaryInfo);
		return primaryInfoDto;
	}

	// Approving Requested Employee...
	@Override
	public EmployeePrimaryInfoDto approveById(int employeeNo, EmployeePrimaryInfoDto primaryInfoDto)
			throws MessagingException, EmployeeNotFoundException, ApprovalFailedException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findById(employeeNo)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exists..!!"));
		if (employee.getEmployeeStatus() != null) {
			throw new ApprovalFailedException("Rejected Application Found");
		} else {
			email = employee.getEmployeeEmail();
			password = autoGenerate.generatePassword(8);
			userName = employee.getEmployeeEmail();
			person = employee.getEmployeeName();
			employee.setEmployeeStatus("Active");
			employee.setEmployeeId("TY" + autoGenerate.generateId(6));
			employee.setEmployeeCurrentDesignation(primaryInfoDto.getEmployeeCurrentDesignation());
			employee.setEmployeeRole("ROLE_" + primaryInfoDto.getEmployeeRole().toUpperCase());
			employee.setPassword(passwordEncoder.encode(password));
			sendEmail(person, email, userName, password);
			lmsEmployeeRepository.save(employee);
			return primaryInfoDto;
		}
	}

	// Reject Requested Employee...
	@Override
	public MailRejectionDto rejectEmployee(int id, MailRejectionDto rejectionDto)
			throws MessagingException, EmployeeNotFoundException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found..!!"));
		email = employee.getEmployeeEmail();
		reason = rejectionDto.getReasonOfReject();
		employee.setEmployeeStatus("Rejected");
		employee.setPassword(null);
		sendRejectionEmail(email, reason);
		lmsEmployeeRepository.save(employee);
		return rejectionDto;
	}

	// Delete Employee By ID..
	@Override
	public EmployeePrimaryInfoDto deleteByID(int employeeId) throws EmployeeNotFoundException {
		EmployeePrimaryInfo primaryInfo = lmsEmployeeRepository.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exists ..!"));
		EmployeePrimaryInfoDto primaryInfoDto = new EmployeePrimaryInfoDto();
		BeanUtils.copyProperties(primaryInfo, primaryInfoDto);
		lmsEmployeeRepository.delete(primaryInfo);
		return primaryInfoDto;
	}

	// Fetch All Approved Employee List..
	@Override
	public List<EmployeeRequestedDto> getAllApprovedEmployeeData() throws EmployeeNotFoundException {
		List<EmployeePrimaryInfo> listOfEmployee = lmsEmployeeRepository.findAll();
		if (listOfEmployee.isEmpty())
			throw new EmployeeNotFoundException("No Record Found..!");
		else {
			List<EmployeeRequestedDto> requestedEmployeeList = new ArrayList<>();
			listOfEmployee.forEach(employee -> {
				if (employee.getEmployeeStatus() != null && employee.getEmployeeStatus().contains("Active")) {
					EmployeeRequestedDto requestedEmployeeDto = new EmployeeRequestedDto();
					requestedEmployeeDto.setEmployeeNo(employee.getEmployeeNo());
					requestedEmployeeDto.setEmployeeId(employee.getEmployeeId());
					requestedEmployeeDto.setEmployeeName(employee.getEmployeeName());
					requestedEmployeeDto
							.setEmployeePercentage(employee.getEducationInfo().get(0).getEmployeePercentage());
					requestedEmployeeDto
							.setEmployeeYearOfPassOut(employee.getEducationInfo().get(0).getEmployeeYearOfPassOut());
					requestedEmployeeDto.setEmployeeYearOfExperience(
							employee.getExperienceInfo().get(0).getEmployeeYearOfExperience());
					requestedEmployeeDto
							.setEmployeeConatctNumber(employee.getContactInfo().get(0).getEmployeeConatctNumber());
					requestedEmployeeList.add(requestedEmployeeDto);
				}
			});
			return requestedEmployeeList;
		}
	}

	// Get All(Approved/Reject) Employee Data...
	@Override
	public List<EmployeePrimaryInfoDto> getAllEmployeeData() throws EmployeeNotFoundException {
		List<EmployeePrimaryInfo> employeeList = lmsEmployeeRepository.findAll();
		if (employeeList.isEmpty())
			throw new EmployeeNotFoundException("No Record Found.. !");
		else {
			List<EmployeePrimaryInfoDto> allEmployeeDto = new ArrayList<>();
			employeeList.forEach(employee -> {
				EmployeePrimaryInfoDto primaryInfoDto = new EmployeePrimaryInfoDto();
				primaryInfoDto.setEmployeeId(employee.getEmployeeId());
				primaryInfoDto.setEmployeeNo(employee.getEmployeeNo());
				primaryInfoDto.setEmployeeName(employee.getEmployeeName());
				primaryInfoDto.setEmployeeGender(employee.getEmployeeGender());
				primaryInfoDto.setEmployeeDateOfBirth(employee.getEmployeeDateOfBirth());
				primaryInfoDto.setEmployeeBloodGroup(employee.getEmployeeBloodGroup());
				primaryInfoDto.setEmployeeEmail(employee.getEmployeeEmail());
				primaryInfoDto.setEmployeeCurrentDateOfJoining(employee.getEmployeeCurrentDateOfJoining());
				primaryInfoDto.setEmployeeCurrentDesignation(employee.getEmployeeCurrentDesignation());
				primaryInfoDto.setEmployeeNationality(employee.getEmployeeNationality());
				primaryInfoDto.setEmployeeRole(employee.getEmployeeRole());
				primaryInfoDto.setEmployeeStatus(employee.getEmployeeStatus());
				allEmployeeDto.add(primaryInfoDto);
			});
			return allEmployeeDto;
		}
	}

	// Get All Mentor List

	@Override
	public List<MentorDetailsDto> getAllMentorDetail() throws MentorNotFoundException {
		List<MentorDetails> mentorList = lmsMentorRepository.findAll();
		if (mentorList != null) {
			List<MentorDetailsDto> mentorDtos = new ArrayList<>();
			mentorList.forEach(mentor -> {
				MentorDetailsDto detailsDto = new MentorDetailsDto();
				detailsDto.setMentorNo(mentor.getMentorNo());
				detailsDto.setMentorId(mentor.getMentorId());
				detailsDto.setMentorName(mentor.getMentorName());
				detailsDto.setMentorGender(mentor.getMentorGender());
				detailsDto.setMentorEmail(mentor.getMentorEmail());
				detailsDto.setMentorSkils(mentor.getMentorSkils());
				mentorDtos.add(detailsDto);
			});
			return mentorDtos;
		} else {
			throw new MentorNotFoundException("No Record Found...!!");
		}
	}

	// Get All Batch List..

	@Override
	public List<BatchDetailsDto> getAllBatchDetail() throws BatchNotFoundException {
		List<BatchDetails> batchList = lmsBatchRepository.findAll();
		if (batchList != null) {
			List<BatchDetailsDto> detailsDtos = new ArrayList<>();
			batchList.forEach(batch -> {
				BatchDetailsDto batchDetailsDto = new BatchDetailsDto();
				batchDetailsDto.setBatchNo(batch.getBatchNo());
				batchDetailsDto.setBatchId(batch.getBatchId());
				batchDetailsDto.setBatchName(batch.getBatchName());
				batchDetailsDto.setBatchStatus(batch.getBatchStatus());
				batchDetailsDto.setBatchTechnology(batch.getBatchTechnology());
				batchDetailsDto.setBatchStartDate(batch.getBatchStartDate());
				batchDetailsDto.setBatchEndDate(batch.getBatchEndDate());
				batchDetailsDto.setBatchStrength(batch.getBatchStrength());
				detailsDtos.add(batchDetailsDto);
			});
			return detailsDtos;
		} else
			throw new BatchNotFoundException("Record Not Found..!");
	}

	@Override
	public List<BatchDetailsDto> getBatchByMentor(Principal principal) throws BatchNotFoundException {
		MentorDetails mentorDetails = lmsMentorRepository.findBymentorEmail(principal.getName());

		List<BatchDetails> batchList = lmsBatchRepository.findAllBymentor(mentorDetails);
		if (batchList != null) {
			List<BatchDetailsDto> detailsDtos = new ArrayList<>();
			batchList.forEach(batch -> {
				BatchDetailsDto batchDetailsDto = new BatchDetailsDto();
				batchDetailsDto.setBatchNo(batch.getBatchNo());
				batchDetailsDto.setBatchId(batch.getBatchId());
				batchDetailsDto.setBatchName(batch.getBatchName());
				batchDetailsDto.setBatchStatus(batch.getBatchStatus());
				batchDetailsDto.setBatchTechnology(batch.getBatchTechnology());
				batchDetailsDto.setBatchStartDate(batch.getBatchStartDate());
				batchDetailsDto.setBatchEndDate(batch.getBatchEndDate());
				batchDetailsDto.setBatchStrength(batch.getBatchStrength());
				detailsDtos.add(batchDetailsDto);
			});
			return detailsDtos;
		} else
			throw new BatchNotFoundException("No Record Found!");
	}

	@Override
	public MentorDetailsDto addMentors(MentorDetailsDto mentorDetailsDto) throws MessagingException {
		MentorDetails mentors = new MentorDetails();
		BeanUtils.copyProperties(mentorDetailsDto, mentors);
		email = mentors.getMentorEmail();
		password = autoGenerate.generatePassword(8);
		userName = mentors.getMentorEmail();
		person = mentorDetailsDto.getMentorName();
		mentors.setMentorId("TY" + autoGenerate.generateId(6));
		mentors.setMentorRole("ROLE_MENTOR");
		mentors.setPassword(passwordEncoder.encode(password));
		mentors.setPswResetStatus("Default");
		lmsMentorRepository.save(mentors);
		sendEmail(person, email, userName, password);
		BeanUtils.copyProperties(mentors, mentorDetailsDto);
		return mentorDetailsDto;
	}

	// ** Reset Password By Postman (Mentor & Employee) **

	@Override
	public MentorDetailsDto resetMentorPassword(MentorDetailsDto mentorDetailsDto) throws MentorNotFoundException {
		MentorDetails mentorDetails = lmsMentorRepository.findBymentorEmail(mentorDetailsDto.getMentorEmail());
		if (mentorDetails != null) {
			mentorDetails.setPassword(passwordEncoder.encode(mentorDetailsDto.getPassword()));
			mentorDetails.setPswResetStatus("Updated");
			lmsMentorRepository.save(mentorDetails);
			BeanUtils.copyProperties(mentorDetails, mentorDetailsDto);
			return mentorDetailsDto;
		} else
			throw new MentorNotFoundException("User Not Exists!");
	}

	@Override
	public EmployeePrimaryInfoDto resetPassword(EmployeePrimaryInfoDto primaryInfoDto)
			throws EmployeeNotFoundException {
		EmployeePrimaryInfo employeePrimaryInfo = lmsEmployeeRepository
				.findByemployeeEmail(primaryInfoDto.getEmployeeEmail());
		if (employeePrimaryInfo != null) {
			employeePrimaryInfo.setPassword(passwordEncoder.encode(primaryInfoDto.getPassword()));
			employeePrimaryInfo.setPswResetStatus("Updated");
			lmsEmployeeRepository.save(employeePrimaryInfo);
			BeanUtils.copyProperties(employeePrimaryInfo, primaryInfoDto);
			return primaryInfoDto;
		} else
			throw new EmployeeNotFoundException("User couldn't validated..!");
	}

	@Override
	public void giveAttendance(int employeeNo) throws EmployeeNotFoundException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findById(employeeNo)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found!"));
		if (employee.getEmployeeStatus() != null && employee.getEmployeeStatus().equals("Active")) {
			int attendance = employee.getEmployeeAttendance();
			employee.setEmployeeAttendance(++attendance);
			lmsEmployeeRepository.save(employee);
		} else
			throw new EmployeeNotFoundException("Sorry!Not a valid Employee..!");
	}

	@Override
	public BatchDetailsDto addBatches(BatchDetailsDto batchDetailsDto)
			throws RequiredDataNotFoundException, MentorNotFoundException, MessagingException, BatchCreationException {
		if (batchDetailsDto != null) {
			BatchDetails batchDetails = new BatchDetails();

			// Fetch the existing mentor by mentorId
			MentorDetails mentor = lmsMentorRepository.findById(batchDetailsDto.getMentorNo())
					.orElseThrow(() -> new MentorNotFoundException("Mentor Not Exists !"));

			// Fetch the existing employee by employeeIds
			List<EmployeePrimaryInfo> employees = lmsEmployeeRepository.findAllById(batchDetailsDto.getEmployeeIds());

			// Creating an Empty List to store valid employees
			List<EmployeePrimaryInfo> validEmployees = new ArrayList<>();

			// Filtering the valid Employees
			employees.forEach(employee -> {
				if (employee.getEmployeeStatus() != null && employee.getEmployeeStatus().equals("Active")) {
					validEmployees.add(employee);
				}
			});

			if (validEmployees.isEmpty())
				throw new BatchCreationException("Valid Employee not found for Batch Creation");
			else {
				BeanUtils.copyProperties(batchDetailsDto, batchDetails);
				batchDetails.setBatchId("#0" + autoGenerate.generateId(6));
				batchDetails.setBatchStrength(validEmployees.size());
				batchDetails.setMentor(mentor);
				batchDetails.setEmployeeInfo(validEmployees);
				lmsBatchRepository.save(batchDetails);

				validEmployees.forEach(employee -> {
					Date dateOfJoin = batchDetailsDto.getBatchStartDate();
					employee.setEmployeeCurrentDateOfJoining(dateOfJoin);
					try {
						sendEmail(employee.getEmployeeName(), employee.getEmployeeEmail(), dateOfJoin);
					} catch (Exception e) {
						traversalExceptionHandler.handelException(e);
					}
					lmsEmployeeRepository.save(employee);
				});
				return batchDetailsDto;
			}
		} else
			throw new RequiredDataNotFoundException("Data required for Store!");
	}

	@Override
	public BatchDetailsDto updateBatchDetail(int batchNo, BatchDetailsDto batchDetailsDto)
			throws BatchNotFoundException {
		BatchDetails batch = lmsBatchRepository.findById(batchNo)
				.orElseThrow(() -> new BatchNotFoundException("Batch Not Exists !"));
		batch.setBatchName(batchDetailsDto.getBatchName());
		batch.setBatchStartDate(batchDetailsDto.getBatchStartDate());
		batch.setBatchEndDate(batchDetailsDto.getBatchEndDate());
		batch.setBatchTechnology(batchDetailsDto.getBatchTechnology());
		batch.setBatchStatus(batchDetailsDto.getBatchStatus());
		batch.setBatchStrength(batchDetailsDto.getBatchStrength());
		lmsBatchRepository.save(batch);
		return batchDetailsDto;
	}

	@Override
	public BatchDetailsDto deleteBatchByID(int batchId) throws BatchNotFoundException {
		BatchDetails batch = lmsBatchRepository.findById(batchId)
				.orElseThrow(() -> new BatchNotFoundException("Batch Not Exists..!"));
		BatchDetailsDto batchDto = new BatchDetailsDto();
		BeanUtils.copyProperties(batch, batchDto);
		batch.setMentor(null);
		batch.getEmployeeInfo().clear();
		lmsBatchRepository.delete(batch);
		return batchDto;
	}

	@Override
	public MockDetailsDto addMockInformation(MockDetailsDto mockDetailsDto)
			throws RequiredDataNotFoundException, BatchNotFoundException, MentorNotFoundException {
		if (mockDetailsDto != null) {
			// Fetch Data From Existing Batch..
			BatchDetails batchInfo = lmsBatchRepository.findById(mockDetailsDto.getBatchNo())
					.orElseThrow(() -> new BatchNotFoundException("Batch Not Exists!"));

			// Fetch Existing Mentor Details (Panel Member)
			List<MentorDetails> mentors = lmsMentorRepository.findAllById(mockDetailsDto.getMentorsNo());
			if (mentors.isEmpty()) {
				throw new MentorNotFoundException("Given Mentor Not Exists.");
			} else {
				MockDetails mockInfo = new MockDetails();
				BeanUtils.copyProperties(mockDetailsDto, mockInfo);
				mockInfo.setBatchDetail(batchInfo);
				mockInfo.setPanel(mentors);
				lmsmockRepository.save(mockInfo);
				return mockDetailsDto;
			}
		} else
			throw new RequiredDataNotFoundException("Data Not Found for Store!");
	}

	@Override
	public MockRatingDto giveRatings(MockRatingDto mockRatingDto)
			throws RequiredDataNotFoundException, EmployeeNotFoundException {
		if (mockRatingDto != null) {

			// Fetch Existing Employee..
			EmployeePrimaryInfo employee = lmsEmployeeRepository.findById(mockRatingDto.getEmployeeNo())
					.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found!"));

			MockRating rating = new MockRating();
			BeanUtils.copyProperties(mockRatingDto, rating);
			employee.setMockRatings(Arrays.asList(rating));
			lmsratingRepository.save(rating);
			return mockRatingDto;
		} else
			throw new RequiredDataNotFoundException("Data Not Found for Store!");
	}

	@Override
	public EmployeePrimaryInfoDto getEmployeeByUserId(int id) throws EmployeeNotFoundException {
		logger.info("Applynig buisness logic..!!");
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exist !"));

		EmployeePrimaryInfoDto dto = new EmployeePrimaryInfoDto();
		BeanUtils.copyProperties(employee, dto);
		return dto;
	}

	@Override
	public EmployeeDetailModifyableDto modifyEmployeeByUserId(int id, EmployeeDetailModifyableDto employeeModifyDto)
			throws EmployeeNotFoundException {
		EmployeePrimaryInfo employeeInfoToModify = lmsEmployeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exist !"));
		
		employeeInfoToModify.setEmployeeName(employeeModifyDto.getEmployeeName());
		employeeInfoToModify.setEmployeeGender(employeeModifyDto.getEmployeeGender());
		employeeInfoToModify.setEmployeeDateOfBirth(employeeModifyDto.getEmployeeDateOfBirth());
		employeeInfoToModify.setEmployeeEmail(employeeModifyDto.getEmployeeEmail());
		employeeInfoToModify.setEmployeeBloodGroup(employeeModifyDto.getEmployeeBloodGroup());
		employeeInfoToModify.setEmployeeNationality(employeeModifyDto.getEmployeeNationality());
		employeeInfoToModify.setPassword(employeeModifyDto.getPassword());
		
		employeeInfoToModify.getSecondaryInfo().setEmployeePanNumber(employeeModifyDto.getEmployeePanNumber());
		employeeInfoToModify.getSecondaryInfo().setEmployeeAdharNumber(employeeModifyDto.getEmployeeAdharNumber());
		employeeInfoToModify.getSecondaryInfo().setEmployeeFatherName(employeeModifyDto.getEmployeeFatherName());
		employeeInfoToModify.getSecondaryInfo().setEmployeeMotherName(employeeModifyDto.getEmployeeMotherName());
		employeeInfoToModify.getSecondaryInfo().setEmployeeSpouseName(employeeModifyDto.getEmployeeSpouseName());
		employeeInfoToModify.getSecondaryInfo().setEmployeePassportNumber(employeeModifyDto.getEmployeePassportNumber());
		employeeInfoToModify.getSecondaryInfo().setEmployeeMaritalStatus(employeeModifyDto.getEmployeeMaritalStatus());

		employeeInfoToModify.getEducationInfo().get(0).setEmployeeEducationType(employeeModifyDto.getEmployeeEducationType());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeeYearOfPassOut(employeeModifyDto.getEmployeeYearOfPassOut());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeePercentage(employeeModifyDto.getEmployeePercentage());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeeUniversityName(employeeModifyDto.getEmployeeUniversityName());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeeInstituteName(employeeModifyDto.getEmployeeInstituteName());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeeSpecialization(employeeModifyDto.getEmployeeSpecialization());
		employeeInfoToModify.getEducationInfo().get(0).setEmployeeEducationState(employeeModifyDto.getEmployeeEducationState());

		employeeInfoToModify.getAddressInfo().get(0).setEmployeeAddressType(employeeModifyDto.getEmployeeAddressType());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeeDoorNo(employeeModifyDto.getEmployeeDoorNo());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeeStreet(employeeModifyDto.getEmployeeStreet());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeeCity(employeeModifyDto.getEmployeeCity());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeeState(employeeModifyDto.getEmployeeState());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeePinCode(employeeModifyDto.getEmployeePinCode());
		employeeInfoToModify.getAddressInfo().get(0).setEmployeeLandMark(employeeModifyDto.getEmployeeLandMark());

		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankAccountNo(employeeModifyDto.getEmployeeBankAccountNo());
		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankName(employeeModifyDto.getEmployeeBankName());
		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankAccountType(employeeModifyDto.getEmployeeBankAccountType());
		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankAccountIfscCode(employeeModifyDto.getEmployeeBankAccountIfscCode());
		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankAccountBarnchName(employeeModifyDto.getEmployeeBankAccountBarnchName());
		employeeInfoToModify.getBankDetails().get(0).setEmployeeBankAccountBranchState(employeeModifyDto.getEmployeeBankAccountBranchState());

		employeeInfoToModify.getContactInfo().get(0).setEmployeeConatctType(employeeModifyDto.getEmployeeConatctType());
		employeeInfoToModify.getContactInfo().get(0).setEmployeeConatctNumber(employeeModifyDto.getEmployeeConatctNumber());

		return employeeModifyDto;
	}

	@Override
	public List<EmployeeRequestedDto> getAllRequestedEmployeeData() throws EmployeeNotFoundException {
//		List<EmployeePrimaryInfo> listOfEmployee = lmsEmployeeRepository.findAll(Sort.by(Sort.Direction.ASC,employeeNo));
		List<EmployeePrimaryInfo> listOfEmployee = lmsEmployeeRepository.findAll();
		if (listOfEmployee.isEmpty())
			throw new EmployeeNotFoundException("No Record Found!");
		else {
			List<EmployeeRequestedDto> dtoList = new ArrayList<>();
			listOfEmployee.forEach(employee -> {
				if (employee.getEmployeeStatus() == null) {
					EmployeeRequestedDto requestedDto = new EmployeeRequestedDto();
					requestedDto.setEmployeeNo(employee.getEmployeeNo());
					requestedDto.setEmployeeId(employee.getEmployeeId());
					requestedDto.setEmployeeName(employee.getEmployeeName());
					requestedDto.setEmployeePercentage(employee.getEducationInfo().get(0).getEmployeePercentage());
					requestedDto.setEmployeeYearOfPassOut(employee.getEducationInfo().get(0).getEmployeeYearOfPassOut());
					requestedDto.setEmployeeYearOfExperience(employee.getExperienceInfo().get(0).getEmployeeYearOfExperience());
					requestedDto.setEmployeeConatctNumber(employee.getContactInfo().get(0).getEmployeeConatctNumber());

					dtoList.add(requestedDto);
				}
			});
			return dtoList;
		}
	}

	@Override
	public MentorDetailsDto deleteMentorByID(int mentorId) throws MentorNotFoundException {
		MentorDetails mentor = lmsMentorRepository.findById(mentorId)
				.orElseThrow(() -> new MentorNotFoundException("Mentor Not Found!"));
		MentorDetailsDto mentorDto = new MentorDetailsDto();
		BeanUtils.copyProperties(mentor, mentorDto);
		mentor.getBatchDetails().clear();
		mentor.getMockDetails().clear();
		lmsMentorRepository.delete(mentor);
		return mentorDto;
	}

	@Override
	public MentorDetailsDto updateMentorDetails(int mentorNo, MentorDetailsDto mentorDetailsDto)
			throws MentorNotFoundException {
		MentorDetails mentor = lmsMentorRepository.findById(mentorNo)
				.orElseThrow(() -> new MentorNotFoundException("Mentor Not Found!"));
		mentor.setMentorName(mentorDetailsDto.getMentorName());
		mentor.setMentorEmail(mentorDetailsDto.getMentorEmail());
		mentor.setMentorGender(mentorDetailsDto.getMentorGender());
		mentor.setMentorSkils(mentorDetailsDto.getMentorSkils());
		lmsMentorRepository.save(mentor);
		return mentorDetailsDto;
	}

	// ** WEB-Page Services.... **

	@Override
	public EmployeePrimaryInfoDto deleteEmployeeByID(HttpServletRequest request)
			throws NumberFormatException, EmployeeNotFoundException {
		EmployeePrimaryInfo primaryInfo = lmsEmployeeRepository
				.findById(Integer.parseInt(request.getParameter("employeeNo")))
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exists..! "));
		EmployeePrimaryInfoDto primaryInfoDto = new EmployeePrimaryInfoDto();
		BeanUtils.copyProperties(primaryInfo, primaryInfoDto);
		lmsEmployeeRepository.delete(primaryInfo);
		return primaryInfoDto;
	}

	// ** Reset Password (Mentor & Employee) **

	@Override
	public MentorDetails resetMentorsPassword(HttpServletRequest request, Principal principal)
			throws MentorNotFoundException {
		MentorDetails mentor = lmsMentorRepository.findBymentorEmail(principal.getName());
		if (mentor != null) {
			mentor.setPassword(passwordEncoder.encode(request.getParameter("password")));
			mentor.setPswResetStatus("Updated");
			lmsMentorRepository.save(mentor);
			return mentor;
		} else
			throw new MentorNotFoundException("User Not Found..! ");
	}

	@Override
	public EmployeePrimaryInfo resetEmployeePassword(HttpServletRequest request, Principal principal)
			throws EmployeeNotFoundException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findByemployeeEmail(principal.getName());
		if (employee != null) {
			employee.setPassword(passwordEncoder.encode(request.getParameter("password")));
			employee.setPswResetStatus("Updated");
			lmsEmployeeRepository.save(employee);
			return employee;
		} else
			throw new EmployeeNotFoundException("User Not Found ! ");
	}

	@Override
	public EmployeeProfileDto getEmployeeByUserName(Principal principal) throws EmployeeNotFoundException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findByemployeeEmail(principal.getName());
		if (employee != null) {
			EmployeeProfileDto profileDto = new EmployeeProfileDto();
			BeanUtils.copyProperties(employee, profileDto);

			profileDto.setEmployeePanNumber(employee.getSecondaryInfo().getEmployeePanNumber());
			profileDto.setEmployeeAdharNumber(employee.getSecondaryInfo().getEmployeeAdharNumber());
			profileDto.setEmployeeFatherName(employee.getSecondaryInfo().getEmployeeFatherName());
			profileDto.setEmployeeMotherName(employee.getSecondaryInfo().getEmployeeMotherName());
			profileDto.setEmployeeSpouseName(employee.getSecondaryInfo().getEmployeeSpouseName());
			profileDto.setEmployeePassportNumber(employee.getSecondaryInfo().getEmployeePassportNumber());
			profileDto.setEmployeeMaritalStatus(employee.getSecondaryInfo().getEmployeeMaritalStatus());

			profileDto.setEmployeeEducationType(employee.getEducationInfo().get(0).getEmployeeEducationType());
			profileDto.setEmployeeYearOfPassOut(employee.getEducationInfo().get(0).getEmployeeYearOfPassOut());
			profileDto.setEmployeePercentage(employee.getEducationInfo().get(0).getEmployeePercentage());
			profileDto.setEmployeeUniversityName(employee.getEducationInfo().get(0).getEmployeeUniversityName());
			profileDto.setEmployeeInstituteName(employee.getEducationInfo().get(0).getEmployeeInstituteName());
			profileDto.setEmployeeSpecialization(employee.getEducationInfo().get(0).getEmployeeSpecialization());
			profileDto.setEmployeeEducationState(employee.getEducationInfo().get(0).getEmployeeEducationState());

			profileDto.setEmployeeAddressType(employee.getAddressInfo().get(0).getEmployeeAddressType());
			profileDto.setEmployeeDoorNo(employee.getAddressInfo().get(0).getEmployeeDoorNo());
			profileDto.setEmployeeStreet(employee.getAddressInfo().get(0).getEmployeeStreet());
			profileDto.setEmployeeCity(employee.getAddressInfo().get(0).getEmployeeCity());
			profileDto.setEmployeeState(employee.getAddressInfo().get(0).getEmployeeState());
			profileDto.setEmployeePinCode(employee.getAddressInfo().get(0).getEmployeePinCode());
			profileDto.setEmployeeLandMark(employee.getAddressInfo().get(0).getEmployeeLandMark());

			profileDto.setEmployeeBankAccountNo(employee.getBankDetails().get(0).getEmployeeBankAccountNo());
			profileDto.setEmployeeBankName(employee.getBankDetails().get(0).getEmployeeBankName());
			profileDto.setEmployeeBankAccountType(employee.getBankDetails().get(0).getEmployeeBankAccountType());
			profileDto
					.setEmployeeBankAccountIfscCode(employee.getBankDetails().get(0).getEmployeeBankAccountIfscCode());
			profileDto.setEmployeeBankAccountBarnchName(
					employee.getBankDetails().get(0).getEmployeeBankAccountBarnchName());
			profileDto.setEmployeeBankAccountBranchState(
					employee.getBankDetails().get(0).getEmployeeBankAccountBranchState());

			profileDto.setEmployeeSkillType(employee.getSkillsInfo().get(0).getEmployeeSkillType());
			profileDto.setEmployeeSkillRatings(employee.getSkillsInfo().get(0).getEmployeeSkillRatings());
			profileDto.setEmployeeYearOfExperinceOverSkill(
					employee.getSkillsInfo().get(0).getEmployeeYearOfExperienceOverSkill());

			profileDto.setEmployeeDateOfJoining(employee.getExperienceInfo().get(0).getEmployeeDateOfJoining());
			profileDto.setEmployeeYearOfExperience(employee.getExperienceInfo().get(0).getEmployeeYearOfExperience());
			profileDto.setEmployeeCompnyName(employee.getExperienceInfo().get(0).getEmployeeCompanyName());
			profileDto.setEmployeeDesignation(employee.getExperienceInfo().get(0).getEmployeeDesignation());

			profileDto.setEmployeeConatctType(employee.getContactInfo().get(0).getEmployeeConatctType());
			profileDto.setEmployeeConatctNumber(employee.getContactInfo().get(0).getEmployeeConatctNumber());

			return profileDto;
		} else
			throw new EmployeeNotFoundException("User Not Found!");
	}

	@Override
	public EmployeePrimaryInfoDto getEmployeeByUserName(HttpServletRequest request) throws EmployeeNotFoundException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository.findByemployeeEmail(request.getParameter("username"));
		if (employee != null) {
			EmployeePrimaryInfoDto dto = new EmployeePrimaryInfoDto();
			BeanUtils.copyProperties(employee, dto);
			return dto;
		} else
			throw new EmployeeNotFoundException("User Not Found!");
	}

	@Override
	public List<BatchEmployeesDto> getAllBatchEmployees(int batchNo) throws BatchNotFoundException {
		BatchDetails batchDetails = lmsBatchRepository.findById(batchNo)
				.orElseThrow(() -> new BatchNotFoundException("Batch Not Exists!"));
		List<EmployeePrimaryInfo> employees = batchDetails.getEmployeeInfo();
		List<BatchEmployeesDto> batchEmployee = new ArrayList<>();
		employees.forEach(employee -> {
			BatchEmployeesDto batchEmployeesDto = new BatchEmployeesDto();
			batchEmployeesDto.setEmployeeNo(employee.getEmployeeNo());
			batchEmployeesDto.setEmployeeId(employee.getEmployeeId());
			batchEmployeesDto.setEmployeeName(employee.getEmployeeName());
			batchEmployeesDto.setEmployeeAttendance(employee.getEmployeeAttendance());
			batchEmployeesDto.setEmployeeStatus(employee.getEmployeeStatus());
			batchEmployee.add(batchEmployeesDto);
		});
		return batchEmployee;
	}

	@Override
	public EmployeePrimaryInfoDto approveEmployeeById(HttpServletRequest request, EmployeePrimaryInfoDto primaryInfoDto)
			throws NumberFormatException, EmployeeNotFoundException, MessagingException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository
				.findById(Integer.parseInt(request.getParameter("employeeNo")))
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exists!"));
		email = employee.getEmployeeEmail();
		password = autoGenerate.generatePassword(8);
		userName = employee.getEmployeeEmail();
		person = employee.getEmployeeName();
		employee.setEmployeeStatus("Active");
		employee.setEmployeeId("TY" + autoGenerate.generateId(6));
		employee.setEmployeeCurrentDesignation(primaryInfoDto.getEmployeeCurrentDesignation());
		employee.setEmployeeRole("ROLE_" + primaryInfoDto.getEmployeeRole().toUpperCase());
		employee.setPassword(passwordEncoder.encode(password));
		sendEmail(person, email, userName, password);
		lmsEmployeeRepository.save(employee);
		return primaryInfoDto;
	}

	@Override
	public MailRejectionDto rejectionOfEmployee(HttpServletRequest request, MailRejectionDto rejectionDto)
			throws NumberFormatException, EmployeeNotFoundException, MessagingException {
		EmployeePrimaryInfo employee = lmsEmployeeRepository
				.findById(Integer.parseInt(request.getParameter("employeeNo")))
				.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Exists!"));
		email = employee.getEmployeeEmail();
		reason = rejectionDto.getReasonOfReject();
		employee.setEmployeeStatus("Rejected");
		sendRejectionEmail(email, reason);
		lmsEmployeeRepository.save(employee);
		return rejectionDto;
	}

}
