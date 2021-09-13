package tn.kidzone.spring.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import tn.kidzone.spring.entity.Request;
import tn.kidzone.spring.entity.Request.Subject;
import tn.kidzone.spring.repository.RequestRepository;

@Service
public class RequestServiceImpl implements IRequestService {
  @Autowired
  RequestRepository requestRepository;
  private static final Logger l = LogManager.getLogger(IRequestService.class);

  @Autowired
  private JavaMailSender emailSender;

  @Override
  public List<Request> getAllRequests() {
    List<Request> requests = (List<Request>) requestRepository.findAll();
    for (Request request : requests) {
      l.info("request list : " + request);
    }
    return requests;
  }

  @Override
  public Request addRequest(Request r) {
    return requestRepository.save(r);
  }

  @Override
  public void deleteRequest(Request r) {
    requestRepository.delete(r);

  }

  @Override
  public Request updateRequest(Request r) {
    return requestRepository.save(r);
  }

  @Override
  public Request getRequest(String id) {
    return requestRepository.findById(Long.parseLong(id)).get();
  }

  @Override
  public Request getRequestByEmail(String email) {
    return requestRepository.retrieveRequestByEmail(email);
  }

  @Override
  public void sendMail(String to, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("service@KidZone.com");
    message.setTo(to);
    message.setSubject(subject);
    message.setText(text);
    emailSender.send(message);
  }

  @Override
  public int NumberOfRequests() {
    return requestRepository.NumberOfRequests();
  }

  @Override
  public int NumberOfRecentRequests() {
    return requestRepository.NumberOfRecentRequests();
  }

  @Override
  public int Numberof(Subject subject) {
		return requestRepository.NumberOf(subject);
  }

}
