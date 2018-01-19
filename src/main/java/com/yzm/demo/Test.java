package com.yzm.demo;

import com.alibaba.fastjson.JSONObject;
import com.fly.httptest.utils.HttpClientUtils;
import com.yzm.demo.api.MyControl;
import com.yzm.demo.api.UserInfo;
import com.yzm.demo.api.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
	private static UserInfo userInfo = new UserInfo();

	/**
	 * 主入口，在这里调用登陆，获取手机号，验证码等方法
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

//		mixin();

//        String proxyUrl = "125.40.8.71";
//        int proxyPort = 8118;
//        String code = "6356";
//        String verification_id = "85c5d8b0-f253-4f2c-aa7c-a249798a200c";
//        String jsonStr2 = "{\"code\":\"" + code + "\",\"phone\":\"+86" + "18745488624" + "\",\"verification_id\":\"" + verification_id + "\",\"invitation_code\":\"198353\",\"purpose\":\"SESSION\",\"platform\":\"Web\"}";
//        HttpClientUtils.postWithProxy("https://api.mixin.one/verifications/" + verification_id, jsonStr2, proxyUrl, proxyPort);

//		String valid = "15604457404|Mixin code 5300 [PIN]";
//		String code = valid.split("\\|")[1].split(" ")[2];
//		System.out.println(code);
//		candy();

		String inviteId = "";
		String phone = "";
		String ip = "";
		int port = 0;

	}


	private static void candy() throws Exception {
		Boolean loginBoolean = login("mephistodemon", "fishcatdog1");
		if (loginBoolean) {
			getUserInfos();
//			getRecvingInfo("0");

			boolean isUse = true;
			String phone = "";
			List<JSONObject> proxyInfoList = getProxyInfo();
			int count = 1;
			for (JSONObject proxyInfo : proxyInfoList) {
				System.out.println("-------------------------" + count++);
				String ip = proxyInfo.getString("ip");
				int port = proxyInfo.getIntValue("port");

				String result = HttpClientUtils.getWithProxy("https://www.baidu.com/", ip, port);
				if (null != result && result.contains("<!--STATUS OK-->")) { // 检测代理有效

					if (isUse) {
						System.out.println("start get phone...");
						phone = getMobileNum("38100").split("\\|")[0]; // 获取手机号
						System.out.println("start get phone success...");
						isUse = false;
					}

					// 发送验证码
					System.out.println("start send verifications...");
					int id = 189225;
					String validCodeUrl = "https://candy.one/i/" + id;
					Header[] validCodeHeader = new Header[] {
							new BasicHeader("Host", "candy.one"),
							new BasicHeader("Connection", "keep-alive"),
							new BasicHeader("Origin", "https://candy.one"),
							new BasicHeader("Upgrade-Insecure-Requests", "1"),
							new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
							new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"),
							new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
							new BasicHeader("Referer", validCodeUrl),
							new BasicHeader("Accept-Encoding", "gzip, deflate, br"),
							new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9")
					};
					List<NameValuePair> paramList = new ArrayList<NameValuePair>();
					paramList.add(new BasicNameValuePair("phone", phone));
					paramList.add(new BasicNameValuePair("dialcode", "86"));
					paramList.add(new BasicNameValuePair("countrycode", "cn"));
					paramList.add(new BasicNameValuePair("status", "login"));
					paramList.add(new BasicNameValuePair("enroll_id", id + ""));

					String resultStr = HttpClientUtils.postWithProxyParamList(validCodeUrl, paramList, validCodeHeader, ip, port);
					System.out.println("start send verifications success...");
					if (null == resultStr) {
						System.out.println("empty verifications....");
						continue;
					}
					if (!resultStr.contains("//服务器访问正常")) {
						continue;
					}
					Thread.sleep(5000);
					System.out.println("start get verifications...");
					// 获取验证码
					String valid = getVcodeAndReleaseMobile(phone, "mephistodemon", 0);
					System.out.println("start get verifications success...");
//					System.out.println(valid);
					if (StringUtils.isNotBlank(valid)) {
						FileUtils.write(new File("candy_phone.txt"), valid + "\n", "utf-8", true);
						// 解析验证码
						String code = valid.split(" ")[2].substring(0, 6);
						System.out.println(code);
						if (StringUtils.isNotBlank(code)) {

							String checkValidCode = "https://candy.one/check_msg?phone=86" + phone + "&code=" + code;
							Header[] checkHeader = new Header[] {
									new BasicHeader("Host", "candy.one"),
									new BasicHeader("Connection", "keep-alive"),
									new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"),
									new BasicHeader("Accept", "*/*"),
									new BasicHeader("Referer", validCodeUrl),
									new BasicHeader("Accept-Encoding", "gzip, deflate, br"),
									new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9")
							};

							String re = HttpClientUtils.getWithProxyHeader(checkValidCode, checkHeader, ip, port);
							if (null != re && re.equals("ok")) {
								String submitUrl = "https://candy.one/user";
								Header[] submitHeader = new Header[] {
										new BasicHeader("Host", "candy.one"),
										new BasicHeader("Connection", "keep-alive"),
										new BasicHeader("Origin", "https://candy.one"),
										new BasicHeader("Upgrade-Insecure-Requests", "1"),
										new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
										new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36"),
										new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
										new BasicHeader("Referer", validCodeUrl),
										new BasicHeader("Accept-Encoding", "gzip, deflate, br"),
										new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9"),
								};
								List<NameValuePair> paramSubmitList = new ArrayList<NameValuePair>();
								paramSubmitList.add(new BasicNameValuePair("phone", "86" + phone));
								paramSubmitList.add(new BasicNameValuePair("code", code));
								paramSubmitList.add(new BasicNameValuePair("countrycode", "CN"));
								paramSubmitList.add(new BasicNameValuePair("status", "send_msg"));

								String submitResult = HttpClientUtils.postWithProxyParamList(submitUrl, paramSubmitList, submitHeader, ip, port);
								System.out.println(submitResult);
								isUse = true;
							}
						}
					} else {
						addIgnore(phone, "38100");
						System.out.println(phone + "---------- destroy");
						isUse = true;
					}
					break;
				}
			}

		} else {
			System.out.println("登陆失败");
		}
	}

	private static void mixin() throws Exception {
		Boolean loginBoolean = login("mephistodemon", "fishcatdog1");
		if (loginBoolean) {
			getUserInfos();
//			getRecvingInfo("0");

			boolean isUse = true;
			String phone = "";
			List<JSONObject> proxyInfoList = getProxyInfo();
			int count = 1;
			for (JSONObject proxyInfo : proxyInfoList) {
				System.out.println("-------------------------" + count++);
				String ip = proxyInfo.getString("ip");
				int port = proxyInfo.getIntValue("port");

				String result = HttpClientUtils.getWithProxy("https://www.baidu.com/", ip, port);
				if (null != result && result.contains("<!--STATUS OK-->")) { // 检测代理有效

					if (isUse) {
						System.out.println("start get phone...");
						phone = getMobileNum("33544").split("\\|")[0]; // 获取手机号
						System.out.println("start get phone success...");
						isUse = false;
					}

					String jsonStr = "{\"phone\":\"+86%s\",\"purpose\":\"SESSION\"}";
					// 发送验证码
					System.out.println("start send verifications...");
					JSONObject jsonObject = HttpClientUtils.postWithProxy("https://api.mixin.one/verifications", String.format(jsonStr, phone), ip, port);
					System.out.println("start send verifications success...");
					if (null == jsonObject) {
						System.out.println("empty verifications....");
						continue;
					}
					Thread.sleep(10000);
					System.out.println("start get verifications...");
					// 获取验证码
					String valid = getVcodeAndReleaseMobile(phone, "mephistodemon", 0);
					System.out.println("start get verifications success...");
//					System.out.println(valid);
					if (StringUtils.isNotBlank(valid)) {
						FileUtils.write(new File("phone.txt"), valid + "\n", "utf-8", true);
						// 解析验证码
						String code = valid.split("\\|")[1].split(" ")[2];
						System.out.println(code);
						if (StringUtils.isNotBlank(code)) {
							// 注册
							String verification_id = jsonObject.getJSONObject("data").getString("id");
							String jsonStr2 = "{\"code\":\"" + code + "\",\"phone\":\"+86" + phone + "\",\"verification_id\":\"" + verification_id + "\",\"invitation_code\":\"274565\",\"purpose\":\"SESSION\",\"platform\":\"Web\"}";
							System.out.println("start register...");
							HttpClientUtils.postWithProxy("https://api.mixin.one/verifications/" + verification_id, jsonStr2, ip, port);
							System.out.println("start register success...");
							isUse = true;
						}
					} else {
						addIgnore(phone, "33544");
						System.out.println(phone + "---------- destroy");
						isUse = true;
					}
				}
			}

		} else {
			System.out.println("登陆失败");
		}
	}


	private static List<JSONObject> getProxyInfo() throws Exception {
		Header[] headers = new Header[]{
				new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
				new BasicHeader("Accept-Encoding", "gzip, deflate"),
				new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9"),
				new BasicHeader("Cache-Control", "max-age=0"),
				new BasicHeader("Connection", "keep-alive"),
				new BasicHeader("Host", "www.xicidaili.com"),
				new BasicHeader("Referer", "http://www.xicidaili.com/nn/2"),
				new BasicHeader("Upgrade-Insecure-Requests", "1"),
				new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
		};
		String agent = "http://www.xicidaili.com/nn/";
		String content = HttpClientUtils.getResponseString(agent, headers);
		Document document = Jsoup.parse(content);
		Elements elements = document.select("#ip_list tr");
		List<JSONObject> proxyInfoList = new ArrayList<JSONObject>();
		for (int i = 1; i < elements.size(); i++) {
			Element e = elements.get(i);
			String ip = e.child(1).text();
			String port = e.child(2).text();
			JSONObject proxyInfo = new JSONObject();
			proxyInfo.put("ip", ip);
			proxyInfo.put("port", port);
			proxyInfoList.add(proxyInfo);
		}
		return proxyInfoList;
	}


	/**
	 * 登 陆
	 *
	 * @param userName 用户名
	 * @param password 密码
	 * @return true or false
	 */
	public static Boolean login(String userName, String password) {
		userInfo.setUid(userName);
		userInfo.setPwd(password);
		String result = UserService.login(userInfo);
		if (MyControl.isNull(result)) {
			System.out.println("login fail  result = " +result);
			return false;
		}
		if (result.toLowerCase().startsWith(userInfo.getUid().toLowerCase())) {
			String[] strings = result.split("\\|");
			userInfo.setToken(strings[1]);
			return true;
		}
		else{
			System.out.println("login fail  result = " +result);
			return false;
		}
	}

	/**
	 * 获取用户个人信息
	 *
	 * @return 成功返回：用户名;积分;余额;可同时获取号码数 失败请参考文档
	 */
	public static String getUserInfos() {
		if (userInfo.isLogin()) {
			String result = UserService.getUserInfo(userInfo.getUid(), userInfo.getToken());
			System.out.println(result);
			return result;
		}
		return "";
	}

	/**
	 * 获取手机号码
	 *
	 * @param pid 项目ID
	 * @return 成功返回：手机号码|token 失败请参考文档
	 */
	public static String getMobileNum(String pid) {
		if (userInfo.isLogin()) {
			String result = UserService.getMobileNum(pid, userInfo.getUid(), userInfo.getToken());
			System.out.println(result);
			return result;
		}
		return "";
	}

	/**
	 *
	 * @param mobileNum 用getMobilenum方法获取到的手机号
	 * @param pid 项目ID
	 * @return 成功返回：加黑成功的号码数量 失败请参考文档
	 */
	public static String addIgnore(String mobileNum, String pid) {
		try {
			if (userInfo.isLogin()) {
				String result = UserService.addIgnore(mobileNum, userInfo.getUid(), userInfo.getToken(), pid);
				System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			addIgnore(mobileNum, pid);
		}
		return "";
	}

	/**
	 * 获取验证码并不再使用本号
	 *
	 * @param mobileNum 用getMobilenum方法获取到的手机号
	 * @param author_uid 软件开发者用户名(可选, 可得10%的消费分成)
	 * @return 成功返回：手机号码|验证码短信 失败请参考文档
	 */
	public static String getVcodeAndReleaseMobile(String mobileNum, String author_uid, int times) {
		try {
			if (userInfo.isLogin()) {
				String result = UserService.getVcodeAndReleaseMobile(userInfo.getUid(), userInfo.getToken(), mobileNum, author_uid);
				System.out.println(result);
				if ("not_receive".equals(result) || "message|please try again later".equals(result)) {
					try {
						if (times < 10) {
							Thread.sleep(5000);
							return getVcodeAndReleaseMobile(mobileNum, author_uid, ++times);
						} else {
							return "";
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return result;
			}
		} catch (Exception e) {
			return getVcodeAndReleaseMobile(mobileNum, author_uid, ++times);
		}
		return "";
	}

	/**
	 *
	 * @param mobileNum 用getMobilenum方法获取到的手机号
	 * @param author_uid 软件开发者用户名(可选, 可得10%的消费分成)
	 * @param nextId 下个要接收的项目ID
	 * @return 成功返回：发送号码|验证码|token 失败请参考文档
	 */
	public static String getVcodeAndHoldMobilenum(String mobileNum, String author_uid, String nextId) {
		if (userInfo.isLogin()) {
			String result = UserService.getVcodeAndHoldMobilenum(userInfo.getUid(), userInfo.getToken(), mobileNum, nextId, author_uid);
			System.out.println(result);
			return result;
		}
		return "";
	}

	public static String getRecvingInfo(String pid) {
		if (userInfo.isLogin()) {
			String result = UserService.getRecvingInfo(userInfo.getUid(), userInfo.getToken(), pid);
			System.out.println(result);
			return result;
		}
		return "";
	}
}
