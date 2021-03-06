package njurestaurant.njutakeout.bl.user;

import njurestaurant.njutakeout.blservice.article.feed.FeedBlService;
import njurestaurant.njutakeout.blservice.user.UserBlService;
import njurestaurant.njutakeout.entity.article.Feed;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.article.feed.FeedItem;
import njurestaurant.njutakeout.response.article.feed.FeedListResponse;
import njurestaurant.njutakeout.response.user.CardItem;
import njurestaurant.njutakeout.response.user.CardListResponse;
import njurestaurant.njutakeout.response.user.PersonItem;
import njurestaurant.njutakeout.response.user.PersonListResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserBlServiceImplTest {
	@Autowired
	private UserBlService userBlService;
	@Autowired
	private FeedBlService feedBlService;

	@Test
	public void addUser() throws NotExistException {
		List<String> medals = Arrays.asList("common", "298", "998");
		userBlService.addUser(
				"111222", "张帅", "zs_face.jpg",
				medals, "13011112222", "hello@example.com",
				"NJU", "CS", "student",
				"我要在代码的世界里飞翔", "NJ", 1000,
				"地产交易", "common", true);
		userBlService.addUser(
				"222111", "陈智麒", "czq_face.jpg",
				medals, "13022221111", "helloczq@example.com",
				"PKU", "CS", "student",
				"我要在代码的世界里爬行", "NJ", -3,
				"融资租赁", "common", false);
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
				"地产交易", "common", true);
	}

	@Test
	public void deleteUser() throws NotExistException {
		userBlService.deleteUser("222222");
	}

	@Test
	public void getPersonList() throws NotExistException {
		PersonListResponse personListResponse = userBlService.getPersonList("capitalClass");
		for(PersonItem personItem:personListResponse.getPersons()) {
			System.out.println(personItem.getUsername());
		}
	}

	@Test
	public void getPersonListByCondition() {
	}

//	@Test
//	public void sendMyCard() {
//		userBlService.sendMyCard("111111", "222222");
//	}

	@Test
	public void getMyCardList() throws NotExistException {
		CardListResponse cardListResponse = userBlService.getMyCardList("222222", "current");
		for(CardItem cardItem:cardListResponse.getCards()) {
			System.out.println(cardItem.getUsername());
		}
	}

	@Test
	public void checkMyReceivedCard() throws NotExistException {
		userBlService.checkMyReceivedCard("111111", "222222");
	}


//	@Test
//	public void addLevel() {
//		userBlService.addLevel("common", 5, 0);
//		userBlService.addLevel("298", 10, 20);
//		userBlService.addLevel("998", 100, 50);
//	}
}