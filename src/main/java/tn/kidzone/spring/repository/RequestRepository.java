package tn.kidzone.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.kidzone.spring.entity.Request;

@Repository("requestRepository")
public interface RequestRepository extends CrudRepository<Request, Long>{

	@Query("SELECT r FROM Request r WHERE r.email= :email")
	Request retrieveRequestByEmail(@Param("email") String email);
}
