package tn.kidzone.spring.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@Column
	private String email;
	@Column
	private String name;
	@Column
	private String question;
	@Column
	@Enumerated(EnumType.STRING)
	private Subject subject;
	@Column
	@Temporal(TemporalType.DATE)
	private Date createdDate;

	public enum Subject {
		INFORMATION, COMPLAINT
	}

    public Request() {
		super();
    }

    public Request(String email, String name, String question, Subject subject, Date createdDate) {
		super();
        this.email = email;
        this.name = name;
        this.question = question;
        this.subject = subject;
        this.createdDate = createdDate;
    }

    public Request(String id, String email, String name, String question, Subject subject, Date createdDate) {
		super();
        this.id = id;
        this.email = email;
        this.name = name;
        this.question = question;
        this.subject = subject;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

    @Override
    public String toString() {
        return "Request [createdDate=" + createdDate + ", email=" + email + ", id=" + id + ", name=" + name
                + ", question=" + question + ", subject=" + subject + "]";
    }
    
}
