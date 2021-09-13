package tn.kidzone.spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.util.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import tn.kidzone.spring.entity.Request;
import tn.kidzone.spring.entity.Request.Subject;
import tn.kidzone.spring.service.IRequestService;

@Scope(value = "session")
@Controller(value = "requestController")
@ELBeanName(value = "requestController")
@Join(path = "/back/pages/requests", to = "/back/pages/requests/index.jsf")
public class RequestController {

    @Autowired
    IRequestService rs;

    private String id;
    private Date creationDate;
    private String requestEmail;
    private String name;
    private String question;
    private Subject subject;
    private Subject[] subjects;
    private String email;

    private List<Request> requests;

    private Request selectedRequest;
    private List<Request> selectedRequests;

    private List<Request> filteredRequests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestEmail() {
        return requestEmail;
    }

    public void setRequestEmail(String requestEmail) {
        this.requestEmail = requestEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Subject[] getSubjects() {
        return Request.Subject.values();
    }

    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void sendRequest() {
        Request request = new Request(email, name, question, subject, new Date(), 0);
        rs.addRequest(request);
        FacesMessage facesMessage = new FacesMessage("Request Sent with success.");
        FacesContext.getCurrentInstance().addMessage("form:btn", facesMessage);
        // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Form
        // validation failed"));
        PrimeFaces.current().ajax().update("form:messages");
        email = null;
        name = null;
        question = null;
        subject = null;
    }

    public List<Request> getRequests() {
        requests = rs.getAllRequests();
        return requests;
    }

    public Request getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(Request selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public List<Request> getSelectedRequests() {
        return selectedRequests;
    }

    public void setSelectedRequests(List<Request> selectedRequests) {
        this.selectedRequests = selectedRequests;
    }

    public void deleteSelectedRequest() {
        System.out.println("ddddddddddddddddddd" + this.selectedRequest);
        rs.deleteRequest(this.selectedRequest);
        this.requests.remove(this.selectedRequest);
        this.selectedRequest = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Request Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:requests");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedRequests()) {
            int size = this.selectedRequests.size();
            return size > 1 ? size + " requests selected" : "1 request selected";
        }

        return "Delete";
    }

    public boolean hasSelectedRequests() {
        return this.selectedRequests != null && !this.selectedRequests.isEmpty();
    }

    public void deleteSelectedRequests() {
        this.selectedRequests.forEach(u -> rs.deleteRequest(u));
        this.requests.removeAll(this.selectedRequests);
        this.selectedRequests = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Requests Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:requests");
        PrimeFaces.current().executeScript("PF('requestsTable').clearFilters()");
    }

    public List<Request> getFilteredRequests() {
        return filteredRequests;
    }

    public void setFilteredRequests(List<Request> filteredRequests) {
        this.filteredRequests = filteredRequests;
    }

    private List<FilterMeta> filterBy;

    @PostConstruct
    public void init() {
        requests = rs.getAllRequests();

        filterBy = new ArrayList<>();

        /*
         * filterBy.add(FilterMeta.builder() .field("role")
         * .filterValue(RequestStatus.NEW) .matchMode(MatchMode.EQUALS) .build());
         */

        filterBy.add(FilterMeta.builder().field("createdDate")
                .filterValue(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28)))
                .matchMode(MatchMode.RANGE).build());

        filterBy.add(FilterMeta.builder().field("validateDate")
                .filterValue(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28)))
                .matchMode(MatchMode.RANGE).build());
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        Request request = (Request) value;
        return request.getName().toLowerCase().contains(filterText)
                || request.getQuestion().toLowerCase().contains(filterText)
                || request.getEmail().toLowerCase().contains(filterText)
                || request.getId().toLowerCase().contains(filterText)
                || request.getCreatedDate().toString().toLowerCase().contains(filterText)
                || request.getSubject().name().toLowerCase().contains(filterText);
    }

    public void validateRequest(Request request, int state) {
        String navigateTo = "/back/pages/requests/index.xhtml?faces-redirect=true";
        System.out.println(" ddddddddddddddddddd " + state + " state " + request);
        request.setState(state);
        request.setValidateDate(new Date());
        rs.updateRequest(request);
        this.selectedRequest = null;
        if (state == 1) {
            String to = request.getEmail();
            String subject = "Request Confirm | KidZone";
            String text = "Your recent request has been confirmed. We'll see what we can do. Have a nice day!";
            rs.sendMail(to, subject, text);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Request Confirm"));
        } else if (state == 2) {
            String to = request.getEmail();
            String subject = "Request Confirm | KidZone";
            String text = "Your recent request has been rejected. we can't do anything for you. Have a nice day!";
            rs.sendMail(to, subject, text);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Success", "Request Reject"));
        }

        this.requests.forEach(req -> {
            if (req.getId().equals(request.getId())) {
                req.setState(state);
            }
        });

        PrimeFaces.current().ajax().update("form:messages", "form:requestsTable", "form:requests");
        System.out.println("\nrequest validate ");
        // return "/back/pages/requests/index.xhtml";
    }

    private String mailTo;
    private String mailContent;
    private String mailSubject;

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String showSendMailForm(Request req) 
    {
        String navigateTo = "/back/pages/requests/mail.xhtml?faces-redirect=true";
        mailTo = req.getEmail();
        return navigateTo;
    }

    public String respondRequest() {
        String navigateTo = "/back/pages/requests/index.xhtml?faces-redirect=true";
        String to = mailTo;
        String subject = mailSubject;
        String text = mailContent;
        rs.sendMail(to, subject, text);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Mail Sent"));
        return navigateTo;
    }

    public void resetFormMail() {
        mailTo = null;
        mailContent = null;
        mailSubject = null;
        PrimeFaces.current().ajax().update("form:form-mail");
    }
}
