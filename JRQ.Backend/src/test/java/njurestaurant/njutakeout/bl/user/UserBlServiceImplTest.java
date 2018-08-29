package njurestaurant.njutakeout.bl.user;

import njurestaurant.njutakeout.blservice.user.UserBlService;
import njurestaurant.njutakeout.exception.NotExistException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBlServiceImplTest {
	@Autowired
	private UserBlService userBlService;

	@Test
	public void addUser() {
		List<String> medals = Arrays.asList("common", "298", "998");
		userBlService.addUser(
				"111111", "zs", "zs_face.jpg",
				medals, "13011112222", "hello@example.com",
				"NJU", "CS", "student",
				"我要在代码的世界里飞翔", "NJ", 1000,
				"地产交易", true);
		userBlService.addUser(
				"222222", "czq", "czq_face.jpg",
				medals, "13022221111", "helloczq@example.com",
				"PKU", "CS", "student",
				"我要在代码的世界里爬行", "NJ", -3,
				"融资租赁", false);
	}

	@Test
	public void getUser() throws NotExistException {
		System.out.println(userBlService.getUser("111111").getUser().getIntro());
	}

	@Test
	public void getUserList() {
		System.out.println(userBlService.getUserList().getUsers().get(1).getIntro());
	}

	@Test
	public void updateUser() throws NotExistException {
		List<String> medals = Arrays.asList("common", "298");
		userBlService.updateUser("111111", "zs", "zs_face.jpg",
				medals, "13011112222", "hellozs@example.com",
				"NJU", "CS", "student",
				"我要在代码的世界里飞翔", "NJ", 1000,
				"地产交易", true);
	}

	@Test
	public void deleteUser() {
		userBlService.deleteUser("222222");
	}

	@Test
	public void getPersonList() {
		System.out.println(userBlService.getPersonList("地产交易").getPersons().get(0).getUsername());
	}
}