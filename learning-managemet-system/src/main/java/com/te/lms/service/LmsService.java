package com.te.lms.service;

import java.security.Principal;
import java.util.List;

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
import com.te.lms.entity.EmployeePrimaryInfo;
import com.te.lms.entity.MentorDetails;
import com.te.lms.exception.ApprovalFailedException;
import com.te.lms.exception.BatchCreationException;
import com.te.lms.exception.BatchNotFoundException;
import com.te.lms.exception.EmployeeNotFoundException;
import com.te.lms.exception.MentorNotFoundException;
import com.te.lms.exception.RequiredDataNotFoundException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface LmsService {

	EmployeePrimaryInfoDto addEmployeeInfo(EmployeePrimaryInfoDto primaryInfoDto);

	EmployeePrimaryInfoDto resetPassword(EmployeePrimaryInfoDto primaryInfoDto) throws EmployeeNotFoundException;

	List<EmployeePrimaryInfoDto> getAllEmployeeData() throws EmployeeNotFoundException;

	List<MentorDetailsDto> getAllMentorDetail() throws MentorNotFoundException;

	List<BatchDetailsDto> getAllBatchDetail() throws BatchNotFoundException;

	EmployeePrimaryInfoDto deleteByID(int employeeId) throws EmployeeNotFoundException;

	EmployeePrimaryInfoDto deleteEmployeeByID(HttpServletRequest request) throws NumberFormatException, EmployeeNotFoundException;

	MentorDetailsDto addMentors(MentorDetailsDto mentorDetailsDto) throws MessagingException;

	BatchDetailsDto addBatches(BatchDetailsDto batchDetailsDto) throws BatchNotFoundException, RequiredDataNotFoundException, MentorNotFoundException, MessagingException, BatchCreationException;

	MockDetailsDto addMockInformation(MockDetailsDto mockDetailsDto) throws RequiredDataNotFoundException, BatchNotFoundException, MentorNotFoundException;

	MockRatingDto giveRatings(MockRatingDto mockRatingDto) throws RequiredDataNotFoundException, EmployeeNotFoundException;

	EmployeePrimaryInfoDto approveById(int employeeNo, EmployeePrimaryInfoDto primaryInfoDto) throws MessagingException, EmployeeNotFoundException, ApprovalFailedException;

	EmployeePrimaryInfoDto getEmployeeByUserId(int id) throws EmployeeNotFoundException;

	EmployeePrimaryInfoDto getEmployeeByUserName(HttpServletRequest request) throws EmployeeNotFoundException;

	List<EmployeeRequestedDto> getAllRequestedEmployeeData() throws EmployeeNotFoundException;

	List<EmployeeRequestedDto> getAllApprovedEmployeeData() throws EmployeeNotFoundException;

	MentorDetailsDto deleteMentorByID(int mentorId) throws MentorNotFoundException;

	BatchDetailsDto deleteBatchByID(int batchId) throws BatchNotFoundException;

	MentorDetailsDto updateMentorDetails(int mentorNo, MentorDetailsDto mentorDetailsDto) throws MentorNotFoundException;

	BatchDetailsDto updateBatchDetail(int batchNo, BatchDetailsDto batchDetailsDto) throws BatchNotFoundException;

	MailRejectionDto rejectEmployee(int id, MailRejectionDto rejectionDto) throws MessagingException, EmployeeNotFoundException;

	MentorDetailsDto resetMentorPassword(MentorDetailsDto mentorDetailsDto) throws MentorNotFoundException;

	MentorDetails resetMentorsPassword(HttpServletRequest request, Principal principal) throws MentorNotFoundException;

	EmployeePrimaryInfo resetEmployeePassword(HttpServletRequest request, Principal principal) throws EmployeeNotFoundException;

	EmployeeProfileDto getEmployeeByUserName(Principal principal) throws EmployeeNotFoundException;

	List<BatchEmployeesDto> getAllBatchEmployees(int batchNo) throws BatchNotFoundException;

	EmployeePrimaryInfoDto approveEmployeeById(HttpServletRequest request,EmployeePrimaryInfoDto primaryInfoDto) throws NumberFormatException, EmployeeNotFoundException, MessagingException;

	MailRejectionDto rejectionOfEmployee(HttpServletRequest request, MailRejectionDto rejectionDto) throws NumberFormatException, EmployeeNotFoundException, MessagingException;

	void giveAttendance(int employeeNo) throws EmployeeNotFoundException;

	List<BatchDetailsDto> getBatchByMentor(Principal principal) throws BatchNotFoundException;

	EmployeeDetailModifyableDto modifyEmployeeByUserId(int id, EmployeeDetailModifyableDto employeeModifyDto) throws EmployeeNotFoundException;

}
