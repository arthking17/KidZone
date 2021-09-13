package tn.kidzone.spring.service;

import java.util.List;

import tn.kidzone.spring.entity.Request;
import tn.kidzone.spring.entity.Request.Subject;

public interface IRequestService {
	public List<Request> getAllRequests();
	public Request addRequest(Request r);
	public void deleteRequest(Request r);
	public Request updateRequest(Request r);
	public Request getRequest(String id);
	public Request getRequestByEmail(String email);
	public void sendMail(String to, String subject, String text);
	public int NumberOfRequests();
	public int NumberOfRecentRequests();
	public int Numberof(Subject subject);
}
