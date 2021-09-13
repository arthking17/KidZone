package tn.kidzone.spring.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.User.Role;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long>
{
	@Modifying
	@Query("update User u set u.role = :role where u.firstName = :fname")
	int updateUserStatusByFirstName(@Param("role") Role role, @Param("fname") String fname);

	@Query("SELECT u FROM User u WHERE u.email= :email")
	User retrieveUsersByEmail(@Param("email") String email);
	
	@Query("SELECT u FROM User u WHERE u.email= :email and u.password = :password")
	User getUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);

	@Query("select count(*) FROM User u")
	int NumberOfUsers();

	@Query("select count(*) FROM User u where role = 'VISITOR' and u.createdDate >= ADDDATE(DATE( NOW() ), -31)")
	int NumberOfRecentVisitors();

	@Query("select count(*) FROM User u where u.createdDate >= ADDDATE(DATE( NOW() ), -31)")
	int NumberOfRecentUsers();

	@Query("select count(*) FROM User u where role = 'VISITOR'")
	int NumberOfVisitors();

	@Query("select count(*) FROM User u where role = :role")
	int NumberOf(@Param("role") Role role);
}
