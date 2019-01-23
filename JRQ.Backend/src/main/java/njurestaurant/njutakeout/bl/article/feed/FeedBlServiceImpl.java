package njurestaurant.njutakeout.bl.article.feed;

import njurestaurant.njutakeout.blservice.article.feed.FeedBlService;
import njurestaurant.njutakeout.data.dao.user.SendCardDao;
import njurestaurant.njutakeout.dataservice.article.FeedDataService;
import njurestaurant.njutakeout.dataservice.article.LikeDataService;
import njurestaurant.njutakeout.dataservice.count.CountDataService;
import njurestaurant.njutakeout.dataservice.user.UserDataService;
import njurestaurant.njutakeout.entity.article.Course;
import njurestaurant.njutakeout.entity.article.Document;
import njurestaurant.njutakeout.entity.article.Feed;
import njurestaurant.njutakeout.entity.article.Project;
import njurestaurant.njutakeout.entity.count.Count;
import njurestaurant.njutakeout.entity.user.SendCard;
import njurestaurant.njutakeout.entity.user.User;
import njurestaurant.njutakeout.exception.NotExistException;
import njurestaurant.njutakeout.response.BoolResponse;
import njurestaurant.njutakeout.response.InfoResponse;
import njurestaurant.njutakeout.response.article.AbstractItem;
import njurestaurant.njutakeout.response.article.AbstractListResponse;
import njurestaurant.njutakeout.response.article.course.CourseItem;
import njurestaurant.njutakeout.response.article.course.CourseListResponse;
import njurestaurant.njutakeout.response.article.feed.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedBlServiceImpl implements FeedBlService {
	private final FeedDataService feedDataService;
	private final UserDataService userDataService;
	private final LikeDataService likeDataService;
	private final SendCardDao sendCardDao;
	private final CountDataService countDataService;

	@Autowired
	public FeedBlServiceImpl(FeedDataService feedDataService, UserDataService userDataService, LikeDataService likeDataService, SendCardDao sendCardDao, CountDataService countDataService) {
		this.feedDataService = feedDataService;
		this.userDataService = userDataService;
		this.likeDataService = likeDataService;
		this.sendCardDao = sendCardDao;
		this.countDataService = countDataService;
	}

	@Override
	public InfoResponse publishMyFeed(String content, List<String> images, String writerOpenid) {
		feedDataService.addFeed(new Feed(content, images, writerOpenid, System.currentTimeMillis(), 0));
		return new InfoResponse();
	}

	@Override
	public FeedResponse getFeed(String id) throws NotExistException {
		return new FeedResponse(new FeedItem(feedDataService.getFeedById(id)));
	}

	@Override
	public FeedListResponse getFeedList() {
		List<Feed> feeds = feedDataService.getAllFeeds();
		return getFeedItemList(feeds);
	}

	@Override
	public InfoResponse updateFeed(String id, String content, List<String> images) throws NotExistException {
		Feed feed = feedDataService.getFeedById(id);
		feed.setContent(content);
		feed.setImages(images);
		feed.setTimeStamp(System.currentTimeMillis());
		feedDataService.saveFeed(feed);
		return new InfoResponse();
	}

	@Override
	public InfoResponse deleteFeed(String id) throws NotExistException {
		feedDataService.deleteFeedById(id);
		return new InfoResponse();
	}

	@Override
	public FeedViewResponse getFeedView(String id) throws NotExistException {
		return new FeedViewResponse(new FeedViewItem(feedDataService.getFeedById(id), userDataService, false));
	}

	@Override
	public FeedViewListResponse getFeedViewList() throws NotExistException {
		List<Feed> feeds = feedDataService.getAllFeeds();
		List<FeedViewItem> feedViewItems = new ArrayList<>();
		for (Feed feed : feeds) {
			feedViewItems.add(new FeedViewItem(feed, userDataService, false));
		}
		return new FeedViewListResponse(feedViewItems);
	}

	@Override
	public FeedViewListResponse getFeedViewListBefore(String openid, String id) throws NotExistException {
		List<SendCard> sendCards = sendCardDao.findAllByReceiverOpenid(openid); //找到用户拥有的所有名片的openid
		List<String> friendOpenids = new ArrayList<>();
//		List<User> userList=userDataService.getAllUsers();
//		for(SendCard sendCard:sendCards) {
//			friendOpenids.add(sendCard.getSenderOpenid());
//		}
//		friendOpenids.add(openid); //把自己的openid加上
		List<User> userList=userDataService.getAllUsers();
		for(User user:userList) {
			friendOpenids.add(user.getOpenid());
		}

		List<Feed> feeds = null;
		if (id.equals("")) {
			feeds = feedDataService.getTop10ByWriterOpenidInOrderByTimeStampDesc(friendOpenids);
		} else {
			Feed feed = feedDataService.getFeedById(id);
			feeds = feedDataService.getTop10ByWriterOpenidInAndTimeStampBeforeOrderByTimeStampDesc(friendOpenids, feed.getTimeStamp());
		}

		if (!feeds.isEmpty()) {
			List<Feed> sameStampFeeds = feedDataService.getFeedsByWriterOpenidInAndTimeStamp(
					friendOpenids, feeds.get(feeds.size()-1).getTimeStamp()); //与feeds中最早的Feed时间戳相同的文章
			for (Feed ssf:sameStampFeeds) {
				boolean flag = false; //标记ssf是否在feeds中
				for (Feed f:feeds){
					if (ssf.getId().equals(f.getId())) {
						flag = true;
						break;
					}
				}
				if (!flag) { //ssf不在feeds里面，加入进去
					feeds.add(ssf);
				}
			}
		}

		List<FeedViewItem> feedViewItems = new ArrayList<>();
		for(Feed feed:feeds) {
			boolean hasLiked = likeDataService.isLikeExistent(openid, "feed", feed.getId());
			feedViewItems.add(new FeedViewItem(feed, userDataService, hasLiked));
		}

		//浏览圈子次数+1
		Count count = countDataService.getCountById(1);
		count.setViewFeed(count.getViewFeed()+1);
		countDataService.saveCount(count);

		return new FeedViewListResponse(feedViewItems);
	}

	@Override
	public AbstractListResponse getUserHistoryAbstractList(String myOpenid, String otherOpenid) throws NotExistException {
		List<Feed> feeds = feedDataService.getFeedsByWriterOpenid(otherOpenid);
		List<AbstractItem> abstractItems = new ArrayList<>();
		for (Feed feed:feeds) {
			boolean hasLiked = likeDataService.isLikeExistent(myOpenid, "feed", feed.getId());
			abstractItems.add(new AbstractItem(feed, userDataService, hasLiked));
		}
		return new AbstractListResponse(abstractItems);
	}

	@Override
	public InfoResponse updateMyFeed(String id, String content, List<String> images) throws NotExistException {
		Feed feed = feedDataService.getFeedById(id);
		feed.setContent(content);
		feed.setImages(images);
		feed.setTimeStamp(System.currentTimeMillis());
		return new InfoResponse();
	}

	@Override
	public InfoResponse deleteMyFeed(String id) throws NotExistException {
		feedDataService.deleteFeedById(id);
		return new InfoResponse();
	}

	@Override
	public FeedListResponse getFeedListBeforeByKind(String kind, String openid, String id) throws NotExistException {
		List<FeedItem> feedItems = new ArrayList<>();
		List<Feed> feeds = new ArrayList<>();
		switch (kind) {
			case "lasted":
				feeds = feedDataService.getFeedListByLikeNum(openid,id);
				for(Feed feed:feeds){
					feedItems.add(new FeedItem(feed));
				}
				break;
			case "weekly":
				feeds = feedDataService.getFeedListBeforeWeek(openid,id);
				for(Feed feed:feeds){
					feedItems.add(new FeedItem(feed));
				}
				break;
			case "monthly":
				feeds = feedDataService.getFeedListBeforeMonth(openid,id);
				for(Feed feed:feeds){
					feedItems.add(new FeedItem(feed));
				}
				break;
			default: ;
		}
		return new FeedListResponse(feedItems);
	}

	public FeedListResponse getFeedItemList(List<Feed> feeds){
		List<FeedItem> feedItems = new ArrayList<>();
		for(Feed feed:feeds){
			feedItems.add(new FeedItem(feed));
		}
		return new FeedListResponse(feedItems);
	}
}
