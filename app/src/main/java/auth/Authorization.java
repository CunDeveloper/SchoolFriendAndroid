package auth;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import model.UserInfo;

public class Authorization {

	private OkHttpClient client = null;
	private static final String BASE_URL = "http://my.chsi.com.cn";
	private static final String LOGIN_URL = "https://account.chsi.com.cn/passport/login";
	private static final String SERVICE = "?service=http://my.chsi.com.cn/archive/j_spring_cas_security_check";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String IT = "lt";
	private static final String EVENT_ID = "_eventId";
	private static final String SUBMIT = "submit";

	public Authorization()
	{
		client = new OkHttpClient();
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		client.setCookieHandler(cookieManager);
	}

	/**
	 * 获取隐藏字段It
	 * @return
	 * @throws IOException
	 */
	public  String getIt() throws IOException {
		Request request = new Request.Builder()
				.url(LOGIN_URL)
				.build();
		Response response = client.newCall(request).execute();
		Document doc = Jsoup.parse(response.body().string());
		Elements elements = doc.getElementsByAttributeValue("type", "hidden");
		Element element = elements.first();
		return element.val();
	}

	/**
	 * 登录验证
	 * @param It
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	public  String  postForm(String It,String username,String password) throws IOException {
		RequestBody formBody = new FormEncodingBuilder()
				.add(USERNAME,username)
				.add(PASSWORD,password)
				.add(IT,It)
				.add(EVENT_ID,"submit")
				.add(SUBMIT,"登  录")
				.build();
		Request request = new Request.Builder()
				.url(LOGIN_URL+SERVICE)
				.post(formBody)
				.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 获取验证码
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Response getCaptcha(String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response response = client.newCall(request).execute();
		return  response;
	}

	/**
	 *
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public Document getXueXinDoc(String html) throws IOException{
		Document document =Jsoup.parse(html);
		Elements elements = document.getElementsByTag("a");
		String url ="";
		for(Element element:elements){
			if(element.attr("title").equals("学信档案")){
				url = element.attr("href");
				break;
			}
		}
		if (!url.equals("")){
			Request request = new Request.Builder()
					.url(url).build();
			Response response =client.newCall(request).execute();
			Document conDoc = Jsoup.parse(response.body().string());
			return conDoc;
		}else{
			return null;
		}
	}

	/**
	 *
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public ArrayList<UserInfo> getUserInfo(Document document) throws IOException {
		Element ulElement = document.getElementById("leftList");
		Elements ulElementChild = ulElement.children();
		Element secondLiElement = ulElementChild.get(1);
		Elements secondLiElementChild = secondLiElement.children();
		Request request = new Request.Builder()
				.url(BASE_URL + secondLiElementChild.get(0).attr("href")).build();
		Response response = client.newCall(request).execute();
		String content = response.body().string();
		Document doc = Jsoup.parse(content);
		Element div = doc.getElementById("tabs");
		Elements divChildren = div.children();
		Element ul = divChildren.first();
		Elements ulChidren = ul.children();
		ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();
		UserInfo userInfo = null;
		System.out.println("size++"+ulChidren.size());
		for (Element element : ulChidren) {
			userInfo =new UserInfo();
			userInfo.setLabel(element.text());
			userInfoList.add(userInfo);
		}

		Elements tabsDiv = doc.getElementsByClass("tabsDiv");

		for(int j =0 ;j < tabsDiv.size();j++){
			Element element = tabsDiv.get(j);
			UserInfo info = userInfoList.get(j);
			Elements tables=element.getElementsByTag("table");
			for(Element table:tables){
				Elements trs = table.getElementsByTag("tr");
				for (Element tr : trs){
					Elements ths = tr.children();
					int length=ths.size();
					for(int i=0;i<length;){
						String label = ths.get(i).text().split("：")[0];
						//System.out.println(label);
						if(label.equals("姓名")){
							i=i+1;
							info.setName(ths.get(i).text());
						}else if(label.equals("性别")){
							i=i+1;
							info.setSex(ths.get(i).text());

						}else if(label.equals("院校名称")){
							i=i+1;
							info.setYuanXiaoName(ths.get(i).text());
						}else if(label.equals("分院")){
							i=i+1;
							info.setFenYuan(ths.get(i).text());
						}else if(label.equals("专业名称")){
							i=i+1;
							info.setMajor(ths.get(i).text());
						}else if(label.equals("入学日期")){
							i=i+1;
							info.setDate(ths.get(i).text());
						}
						i++;
					}
				}
			}
		}
		return userInfoList;
	}
	public int validate(String html){
		Document doc = Jsoup.parse(html);
		Element element = doc.getElementById("status");
		return element !=null ?1:0;
	}
}