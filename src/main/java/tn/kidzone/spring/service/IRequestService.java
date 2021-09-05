package tn.kidzone.spring.service;

import java.util.List;

import tn.kidzone.spring.entity.Request;

public interface IRequestService {
	public List<Request> getAllRequests();
	public Request addRequest(Request r);
	public void deleteRequest(Request r);
	public Request updateRequest(Request r);
	public Request getRequest(String id);
	public Request getRequestByEmail(String email);
	public void sendMail(String to, String subject, String text);
}
