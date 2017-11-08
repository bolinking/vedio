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
 * ���Ͷ��� �� �ʼ�������
 * ע:���ڵ�¼������֤���ʼ���֤
 *
 */
public class SendMessageUtils {
	
	private static final Logger logger = Logger.getLogger(SendMessageUtils.class);

	//���ע����50������http://www.ihuyi.com/
	private static String URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
	//InitSystemVisionConfig�лḳֵ
	public static String ACCOUNT = "C13319686";
	public static String PASSWORD = "8be9ce7a056deba3cfc5e4d99acab0cb";
	
	private static String name = "lbl";
	private static String pwd = "Bolin_112";
	private static String host = "gwall";
	private static Properties properties = new Properties();
	
	private SendMessageUtils() {
		properties.put("mail.smtp.host", "smtp."+host+".com");	// �洢�����ʼ�����������Ϣ
		properties.put("mail.smtp.auth", "true");				// ͬʱͨ����֤
	}
	
	/**
	 * ���÷����ʼ���Ϣ
	 * @param host
	 * @param name
	 * @param pwd
	 */
	public static void setEmailUser(String host,String name,String pwd){
		SendMessageUtils.host = host;
		SendMessageUtils.name = name;
		SendMessageUtils.pwd = pwd;
		properties.put("mail.smtp.host", "smtp."+host+".com");	// �洢�����ʼ�����������Ϣ
		properties.put("mail.smtp.auth", "true");				// ͬʱͨ����֤
	}
	/**
	 * ���÷��Ͷ����˺�
	 * ���ע����50������http://www.ihuyi.com/
	 * @param host
	 * @param name
	 * @param pwd
	 */
	public static void setSMSUser(String name,String pwd){
		SendMessageUtils.ACCOUNT = name;
		SendMessageUtils.PASSWORD = pwd;
	}
	
	public static void main(String[] args) {
		//SendMessageUtils.sendEmail("1263036958@qq.com;1696239709@qq.com;1401644339@qq.com;522398213@qq.com;1160844346@qq.com;920661552@qq.com;3275913756@qq.com","ͨ����","�����������������ˣ�");
		SendMessageUtils.sendEmail("920661552@qq.com","ͨ����","�����������������ˣ�");

	}
	
	/**
	 * ���Ͷ���
	 * 
	 * @param mobileCode
	 * @param content
	 * @return OK�ɹ� ���������Ϣ
	 */
	public static String sendSMS(String mobileCode, String content) {
		HttpClient client = new HttpClient();
		String param = "account=" + ACCOUNT + "&password=" + PASSWORD + "&mobile=" + mobileCode + "&content=" + content;
		String result = client.pub(URL, param);
		try {
		    DocumentBuilderFactory  dbf = DocumentBuilderFactory.newInstance();  
		    DocumentBuilder db = dbf.newDocumentBuilder();// ����db������documentBuilderFatory�����÷���documentBuildr����
		    InputStream is = new ByteArrayInputStream(result.getBytes());
		    Document dt = db.parse(is); // �õ�һ��DOM�����ظ�document����  
		    Element element = dt.getDocumentElement();// �õ�һ��elment��Ԫ��  
		    
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
	 * ���ʼ�,����÷ֺŸ���
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
			session = Session.getInstance(properties);// ���������½�һ���ʼ��Ự
			message = new MimeMessage(session);// ���ʼ��Ự�½�һ����Ϣ����
			message.setFrom(new InternetAddress(name + "@" + host + ".cn"));// ���÷�����
			String tos[] = toEmail.split(";");
			InternetAddress[] iAddress = new InternetAddress[tos.length]; // ;�ָ���Ⱥ��
			for (int i = 0; i < tos.length; i++) {
				iAddress[i] = new InternetAddress(tos[i]); // �ռ���
			}
			message.setRecipients(Message.RecipientType.TO, iAddress);// �������������ΪTO
			message.setSubject(subject);
			//����
			BodyPart mbp = new MimeBodyPart();
			mbp.setContent(content, "text/html; charset=utf-8");
			//����
			BodyPart mbp_attachment = new MimeBodyPart();			
			DataHandler dh=new DataHandler(new FileDataSource("D:\\b.xls"));
			mbp_attachment.setDataHandler(dh);
			mbp_attachment.setFileName("b.xls");
			//���ĺ͸�����������
			MimeMultipart mmp=new MimeMultipart();
			mmp.addBodyPart(mbp);
			//mmp.addBodyPart(mbp_attachment);
			message.setContent(mmp);
			//message.setContent(content, "text/html; charset=utf-8");
			message.setSentDate(new Date());// ���÷���ʱ��
			message.saveChanges();// �洢�ʼ���Ϣ
			transport = session.getTransport("smtp");
			//transport.connect("smtp.exmail." + host + ".com", name, pwd);// ��smtp��ʽ��¼����
			transport.connect("smtp." + host + ".con", name, pwd);// ��smtp��ʽ��¼����
			transport.sendMessage(message, message.getAllRecipients());// �����ʼ�,���еڶ�������������
			return "OK";
		} catch (Exception e) {
			logger.error("�ʼ�����ʧ��:", e);
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
	
//	String subject = "�����̷�����ϸ��������·��������ʼ�����";
//	//	sendEmail(email,subject,map.get("fileName").toString(),path);
//	MailMessage mailMessage=new MailMessage();
//	//������
//	//mailMessage.setFrom("it@arvato-apac.cn");
//	mailMessage.setFrom("hjl@gwall.cn");
//	//�ʼ�����
//	mailMessage.setSubject(subject);
//	String filePath =path;
//	String OSName = System.getProperty("os.name");
//	if(OSName.contains("Windows")) {
//		filePath = filePath + "exports\\"+map.get("fileName").toString();
//	} else if(OSName.contains("unix")) {
//		filePath = filePath + "exports/"+map.get("fileName").toString();
//	}
//	//�ʼ�����
//	mailMessage.setFileNames(new String[]{filePath});
//	StringBuffer sBuffer=new StringBuffer();
//	sBuffer.append("Dear all,");
//	sBuffer.append("  �����ǽ���������ʼ�����ʱ������������̷�����ϸ�����ͳ����Ϣ��");
//	sBuffer.append("�����·����������̷�����ϸ�������ʼ�,���ݱ����ʼ�������,��ע�����,лл!");
//	//�ʼ�����
//	mailMessage.setContent(sBuffer.toString());
//	//�ʼ��ռ���The "To" (primary) recipients.
//	mailMessage.setTos(tos);
//	//�ʼ����ܳ�����The "Cc" (carbon copy) recipients.
//	mailMessage.setCcs(new String[]{});
//	//The "Bcc" (blind carbon copy) recipients.
//	mailMessage.setBccs(new String[]{});
//	//�����ʼ�
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
