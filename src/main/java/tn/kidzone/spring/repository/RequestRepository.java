package tn.kidzone.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.kidzone.spring.entity.Request;
import tn.kidzone.spring.entity.Request.Subject;

@Repository("requestRepository")
public interface RequestRepository extends CrudRepository<Request, Long>{

	@Query("SELECT r FROM Request r WHERE r.email= :email")
	Request retrieveRequestByEmail(@Param("email") String email);

	@Query("select count(*) FROM Request r where r.createdDate >= ADDDATE(DATE( NOW() ), -31)")
	int NumberOfRecentRequests();

	@Query("select count(*) FROM Request r")
	int NumberOfRequests();

	@Query("select count(*) FROM Request u where subject = :subject")
	int NumberOf(@Param("subject") Subject subject);
}
