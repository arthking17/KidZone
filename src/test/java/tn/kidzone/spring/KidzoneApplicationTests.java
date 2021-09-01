package tn.kidzone.spring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.User.Role;
import tn.kidzone.spring.service.IUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KidzoneApplicationTests {

	@Autowired
	IUserService us;
	
	@Test
	public void contextLoads() throws ParseException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC+2"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = dateFormat.parse("1999-12-16"); 
		//LocalDateTime date2 = currentTime.withDayOfMonth(25).withYear(2023).withMonth(12);
		System.out.println("Date modifiée : " + d);
		User u = new User("arthur", "william", "nguesseuarthur17@gmail.com", "password", "img.png", "nour jaafer", d, Role.ADMIN, new Date());
		User u2 = new User("ngassa", "nguesseu", "nguesseuarthur@gmail.com", "password", "img.png", "ariana", d, Role.STUDENT, new Date());
		//us.addUser(u2);
		//us.addUser(u);
		us.getAllUsers(); 
	}

}
