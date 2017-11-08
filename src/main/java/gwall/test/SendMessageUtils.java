package gwall.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.mail.internet.MimeBodyPart;



import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 发送短信 和 邮件工具类
 * 注:用于登录短信验证和邮件验证
 *
 */
public class SendMessageUtils {
	
	private static final Logger logger = Logger.getLogger(SendMessageUtils.class);

	//免费注册送50条短信http://www.ihuyi.com/
	private static String URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	//InitSystemVisionConfig中会赋值
	public static String ACCOUNT = "C13319686";
	public static String PASSWORD = "8be9ce7a056deba3cfc5e4d99acab0cb";
	
	private static String name = "lbl";
	private static String pwd = "Bolin_112";
	private static String host = "gwall";
	private static Properties properties = new Properties();
	
	private SendMessageUtils() {
		properties.put("mail.smtp.host", "smtp."+host+".com");	// 存储发送邮件服务器的信息
		properties.put("mail.smtp.auth", "true");				// 同时通过验证
	}
	
	/**
	 * 设置发送邮件信息
	 * @param host
	 * @param name
	 * @param pwd
	 */
	public static void setEmailUser(String host,String name,String pwd){
		SendMessageUtils.host = host;
		SendMessageUtils.name = name;
		SendMessageUtils.pwd = pwd;
		properties.put("mail.smtp.host", "smtp."+host+".com");	// 存储发送邮件服务器的信息
		properties.put("mail.smtp.auth", "true");				// 同时通过验证
	}
	/**
	 * 设置发送短信账号
	 * 免费注册送50条短信http://www.ihuyi.com/
	 * @param host
	 * @param name
	 * @param pwd
	 */
	public static void setSMSUser(String name,String pwd){
		SendMessageUtils.ACCOUNT = name;
		SendMessageUtils.PASSWORD = pwd;
	}
	
	public static void main(String[] args) {
		//SendMessageUtils.sendEmail("1263036958@qq.com;1696239709@qq.com;1401644339@qq.com;522398213@qq.com;1160844346@qq.com;920661552@qq.com;3275913756@qq.com","通了吗","阿拉啦啦啦啦阿里了！");
		SendMessageUtils.sendEmail("920661552@qq.com","通了吗","阿拉啦啦啦啦阿里了！");

	}
	
	/**
	 * 发送短信
	 * 
	 * @param mobileCode
	 * @param content
	 * @return OK成功 否则错误信息
	 */
	public static String sendSMS(String mobileCode, String content) {
		HttpClient client = new HttpClient();
		String param = "account=" + ACCOUNT + "&password=" + PASSWORD + "&mobile=" + mobileCode + "&content=" + content;
		String result = client.pub(URL, param);
		try {
		    DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();  
		    DocumentBuilder db = dbf.newDocumentBuilder();// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		    InputStream is = new ByteArrayInputStream(result.getBytes());
		    Document dt = db.parse(is); // 得到一个DOM并返回给document对象  
		    Element element = dt.getDocumentElement();// 得到一个elment根元素  
		    
		    String code = element.getAttribute("code");
		    String msg = element.getAttribute("msg");
		    
		    is.close();
		    
			if ("2".equals(code)) {
				return "OK";
			}
			return msg;
		} catch (Exception e) {
			logger.error(e);
			return "OK";
		}
	}
	
	/**
	 * 发邮件,多个用分号隔开
	 * 
	 * @param toEmail
	 * @param subject
	 * @param content
	 * @return
	 */
	public static String sendEmail(String toEmail, String subject, String content) {
		Session session = null;
		Transport transport = null;
		MimeMessage message;
		try {
			session = Session.getInstance(properties);// 根据属性新建一个邮件会话
			message = new MimeMessage(session);// 由邮件会话新建一个消息对象
			message.setFrom(new InternetAddress(name + "@" + host + ".cn"));// 设置发件人
			String tos[] = toEmail.split(";");
			InternetAddress[] iAddress = new InternetAddress[tos.length]; // ;分隔是群发
			for (int i = 0; i < tos.length; i++) {
				iAddress[i] = new InternetAddress(tos[i]); // 收件人
			}
			message.setRecipients(Message.RecipientType.TO, iAddress);// 设置其接收类型为TO
			message.setSubject(subject);
			//正文
			BodyPart mbp = new MimeBodyPart();
			mbp.setContent(content, "text/html; charset=utf-8");
			//附件
			BodyPart mbp_attachment = new MimeBodyPart();			
			DataHandler dh=new DataHandler(new FileDataSource("D:\\b.xls"));
			mbp_attachment.setDataHandler(dh);
			mbp_attachment.setFileName("b.xls");
			//正文和附件存入邮箱
			MimeMultipart mmp=new MimeMultipart();
			mmp.addBodyPart(mbp);
			//mmp.addBodyPart(mbp_attachment);
			message.setContent(mmp);
			//message.setContent(content, "text/html; charset=utf-8");
			message.setSentDate(new Date());// 设置发信时间
			message.saveChanges();// 存储邮件信息
			transport = session.getTransport("smtp");
			//transport.connect("smtp.exmail." + host + ".com", name, pwd);// 以smtp方式登录邮箱
			transport.connect("smtp." + host + ".con", name, pwd);// 以smtp方式登录邮箱
			transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
			return "OK";
		} catch (Exception e) {
			logger.error("邮件发送失败:", e);
			return e.getMessage();
		}finally{
			if(transport != null){
				try {
					transport.close();
				} catch (MessagingException e) {
				}
			}
		}
	}
	
//	String subject = "承运商费用明细报表根据月份物流按邮件发送";
//	//	sendEmail(email,subject,map.get("fileName").toString(),path);
//	MailMessage mailMessage=new MailMessage();
//	//发件人
//	//mailMessage.setFrom("it@arvato-apac.cn");
//	mailMessage.setFrom("hjl@gwall.cn");
//	//邮件主题
//	mailMessage.setSubject(subject);
//	String filePath =path;
//	String OSName = System.getProperty("os.name");
//	if(OSName.contains("Windows")) {
//		filePath = filePath + "exports\\"+map.get("fileName").toString();
//	} else if(OSName.contains("unix")) {
//		filePath = filePath + "exports/"+map.get("fileName").toString();
//	}
//	//邮件附件
//	mailMessage.setFileNames(new String[]{filePath});
//	StringBuffer sBuffer=new StringBuffer();
//	sBuffer.append("Dear all,");
//	sBuffer.append("  以下是今天截至到邮件发送时间点物流承运商费用明细报表的统计信息，");
//	sBuffer.append("根据月份物流承运商费用明细报表发送邮件,数据报表邮件附件中,请注意查收,谢谢!");
//	//邮件正文
//	mailMessage.setContent(sBuffer.toString());
//	//邮件收件人The "To" (primary) recipients.
//	mailMessage.setTos(tos);
//	//邮件秘密抄送人The "Cc" (carbon copy) recipients.
//	mailMessage.setCcs(new String[]{});
//	//The "Bcc" (blind carbon copy) recipients.
//	mailMessage.setBccs(new String[]{});
//	//发送邮件
//	SendMailMessageUtil.sendAnonymousEmail(mailMessage);
	
	
//	List<Object[]> paramVals = new ArrayList<Object[]>();
//	if(pcdeList == null || pcdeList.size() == 0){
//		return null;
//	}
//	String tableName = pcdeList.get(0).getClass().getSimpleName(); 
//	StringBuffer insertFields = new StringBuffer();
//	StringBuffer placeholder = new StringBuffer();
//
//	for (int i = 0;i < pcdeList.size();i++) {
//		Pcde entity = pcdeList.get(i);
//		Field[] fields = entity.getClass().getDeclaredFields();
//		List<Object> insertVals = new ArrayList<Object>();
//
//		for (int j = 0; j < fields.length;j++) {
//			String name = fields[j].getName();
//			Transient tra =fields[j].getAnnotation(Transient.class);
//			if(tra != null || "serialVersionUID".equals(name)){
//				if(i == 0 && j == fields.length - 1){
//					insertFields.deleteCharAt(insertFields.length() - 1);
//					placeholder.deleteCharAt(placeholder.length() - 1);
//				}
//				continue;
//			}
//			if(i == 0){
//				if(j < fields.length - 1){
//					insertFields.append(name + ",");
//					placeholder.append("?,");
//				}else{
//					insertFields.append(name);
//					placeholder.append("?");
//				}
//			}
//			PropertyDescriptor pd = new PropertyDescriptor(name, entity.getClass());
//			Method method = pd.getReadMethod();
//			insertVals.add(method.invoke(entity));
//		}
//		paramVals.add(insertVals.toArray());
//	}
//
//	return getDatabase().batchUpdate("insert into " + tableName + " (" + insertFields.toString() + ") values (" + placeholder.toString() + ")", paramVals);
}
