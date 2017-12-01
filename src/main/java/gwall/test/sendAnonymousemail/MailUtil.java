package gwall.test.sendAnonymousemail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

public class MailUtil { 
	private static final Logger LOGGER = Logger.getLogger(MailUtil.class);

	private static String SMTPServer;
	private static String SMTPUsername;
	private static String SMTPPassword;
	private static String POP3Server;
	private static String POP3Username;
	private static String POP3Password;
	private static String SOCKETPORT;
	private static String MAILPORT;
	
	static {
	    loadConfigProperties();
	}
	
	public static void main(String[] args) {
	    //�����ʼ�
//	    MailMessage mail = new MailMessage(
//	            "test-subject",
//	            "lbl@gwall.cn",
//	            "lbl@gwall.cn",
//	            "This is mail content");
	    //set attachments
	    String[] attachments = new String[]{
	            "D:\\b.xls",
	            "E:\\bbb.txt",};
//	    mail.setFileNames(attachments);
//	    sendEmail(mail);
//	
//	    //�����ʼ�
//	    receiveEmail(POP3Server, POP3Username, POP3Password);
	
	    //���������ʼ�
	    MailMessage anonymousMail = new MailMessage("10��ף����",
	        "522398213@qq.com", "920661552@qq.com", "��������10�£���ر�ؽ�ƣ�");
	    anonymousMail.setFileNames(attachments);
	    
	    sendAnonymousEmail(anonymousMail);
	}
	
	/**
	 * Load configuration properties to initialize attributes.
	 */
	private static void loadConfigProperties() {
	    File f = new File("");
	    //this path would point to AbcCommon
	    String absolutePath = f.getAbsolutePath();
	    String propertiesPath = "";
	    String OSName = System.getProperty("os.name");
	    if(OSName.contains("Windows")) {
	        propertiesPath = "D:\\workspace\\vedio\\src\\main\\resources\\project.properties";
	    } else if(OSName.contains("unix")) {
	        propertiesPath = absolutePath + "/../src/main/resources/project.properties";
	    }
	    f = new File(propertiesPath);
	    if(!f.exists()) {
	        throw new RuntimeException("Porperties file not found at: " + f.getAbsolutePath());
	    }
	    Properties props = new Properties();
	    try {
	        props.load(new FileInputStream(f));
	        SMTPServer = props.getProperty("MailCommon.mail.SMTPServer");
			SMTPUsername = props.getProperty("MailCommon.mail.SMTPUsername");
			SMTPPassword = props.getProperty("MailCommon.mail.SMTPPassword");
			POP3Server = props.getProperty("MailCommon.mail.POP3Server");
			POP3Username = props.getProperty("MailCommon.mail.POP3Username");
			POP3Password = props.getProperty("MailCommon.mail.POP3Password");
			SOCKETPORT=props.getProperty("mail.smtp.socketFactory.port");
			MAILPORT=props.getProperty("mail.smtp.port");
	    } catch (FileNotFoundException e) {
	        LOGGER.error("File not found at " + f.getAbsolutePath(), e);
	    } catch (IOException e) {
	        LOGGER.error("Error reading config file " + f.getName(), e);
	    }
	}
	
	/**
	 * Send email. Note that the fileNames of MailMessage are the absolute path of file.
	 * @param mail The MailMessage object which contains at least all the required
	 *        attributes to be sent.
	 */
//	public static void sendEmail(MailMessage mail) {
//	    sendEmail(null, mail, false);
//	}
	
	/**
	 * Send anonymous email. Note that although we could give any address as from address,
	 * (for example: <b>'a@a.a' is valid</b>), the from of MailMessage should always be the
	 * correct format of email address(for example the <b>'aaaa' is invalid</b>). Otherwise
	 * an exception would be thrown say that username is invalid.
	 * @param mail The MailMessage object which contains at least all the required
	 *        attributes to be sent.
	 */
	public static void sendAnonymousEmail(MailMessage mail) {
	    String dns = "dns://";
	    Hashtable<String, String> env = new Hashtable<String, String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
	    env.put(Context.PROVIDER_URL, dns);
	    String[] tos = mail.getTos();
	    try {
	        DirContext ctx = new InitialDirContext(env);
	        for(String to:tos) {
	            String domain = to.substring(to.indexOf('@') + 1);
	            //Get MX(Mail eXchange) records from DNS
	            Attributes attrs = ctx.getAttributes(domain, new String[] { "MX" });
	            if (attrs == null || attrs.size() <= 0) {
	                throw new java.lang.IllegalStateException(
	                    "Error: Your DNS server has no Mail eXchange records!");
	            }
	            @SuppressWarnings("rawtypes")
	            NamingEnumeration servers = attrs.getAll();
	            String smtpHost = null;
	            boolean isSend = false;
	            StringBuffer buf = new StringBuffer();
	            //try all the mail exchange server to send the email.
	            while (servers.hasMore()) {
	                Attribute hosts = (Attribute) servers.next();
	                for (int i = 0; i < hosts.size(); ++i) {
	                    //sample: 20 mx2.qq.com
	                    smtpHost = (String) hosts.get(i);
	                    //parse the string to get smtpHost. sample: mx2.qq.com
	                    smtpHost = smtpHost.substring(smtpHost.lastIndexOf(' ') + 1);
	                    try {
	                        sendEmail(smtpHost, mail, true);
	                        isSend = true;
	                        return;
	                    } catch (Exception e) {
	                        LOGGER.error("", e);
	                        buf.append(e.toString()).append("\r\n");
	                        continue;
	                    }
	                }
	            }
	            if (!isSend) {
	                throw new java.lang.IllegalStateException("Error: Send email error."
	                        + buf.toString());
	            }
	        }
	    } catch (NamingException e) {
	        LOGGER.error("", e);
	    }
	} 
	
	/**
	 * Send Email. Use string array to represents attachments file names.
	 * @see #sendEmail(String, String, String[], String[], String[], String, File[])
	 */
	private static void sendEmail(String smtpHost,
	    MailMessage mail, boolean isAnonymousEmail) {
	    if(mail == null) {
	        throw new IllegalArgumentException("Param mail can not be null.");
	    }
	    String[] fileNames = mail.getFileNames();
	    //only needs to check the param: fileNames, other params would be checked through
	    //the override method.
	    File[] files = null;
	    if(fileNames != null && fileNames.length > 0) {
	        files = new File[fileNames.length];
	        for(int i = 0; i < files.length; i++) {
	            File file = new File(fileNames[i]);
	            files[i] = file;
	        }
	    }
	    sendEmail(smtpHost, mail.getSubject(), mail.getFrom(), mail.getTos(),
	            mail.getCcs(), mail.getBccs(), mail.getContent(), files, isAnonymousEmail);
	}
	
	/**
	 * Send Email. Note that content and attachments cannot be empty at the same time.
	 * @param smtpHost The SMTPHost. This param is needed when sending an anonymous email.
	 *        When sending normal email, the param is ignored and the default SMTPServer
	 *        configured is used.
	 * @param subject The email subject.
	 * @param from The sender address. This address must be available in SMTPServer.
	 * @param tos The receiver addresses. At least 1 address is valid.
	 * @param ccs The 'copy' receiver. Can be empty.
	 * @param bccs The 'encrypt copy' receiver. Can be empty.
	 * @param content The email content.
	 * @param attachments The file array represent attachments to be send.
	 * @param isAnonymousEmail If this mail is send in anonymous mode. When set to true, the
	 *        param smtpHost is needed and sender's email address from should be in correct
	 *        pattern.
	 */
	private static void sendEmail(String smtpHost, String subject,
	        String from, String[] tos, String[] ccs, String[] bccs,
	        String content, File[] attachments, boolean isAnonymousEmail) {
	    //parameter check
		System.out.println(from+"��������"+tos[0]+"...."+isAnonymousEmail);
	    if(isAnonymousEmail && smtpHost == null) {
	        throw new IllegalStateException(
	            "When sending anonymous email, param smtpHost cannot be null");
	    }
	    if(subject == null || subject.length() == 0) {
	        subject = "Auto-generated subject";
	    }
	    if(from == null) {
	        throw new IllegalArgumentException("Sender's address is required.");
	    }
	    if(tos == null || tos.length == 0) {
	        throw new IllegalArgumentException(
	            "At lease 1 receive address is required.");
	    }
	    if(content == null && (attachments == null || attachments.length == 0)) {
	        throw new IllegalArgumentException(
	            "Content and attachments cannot be empty at the same time");
	    }
	    if(attachments != null && attachments.length > 0) {
	        List<File> invalidAttachments = new ArrayList<File>();
	        for(File attachment:attachments) {
	            if(!attachment.exists() || attachment.isDirectory()
	                || !attachment.canRead()) {
	                invalidAttachments.add(attachment);
	            }
	        }
	        if(invalidAttachments.size() > 0) {
	            String msg = "";
	            for(File attachment:invalidAttachments) {
	                msg += "\n\t" + attachment.getAbsolutePath();
	            }
	            throw new IllegalArgumentException(
	                "The following attachments are invalid:" + msg);
	        }
	    }
	    Session session;
	    Properties props = new Properties();
	    props.put("mail.transport.protocol", "smtp");
	
	    if(isAnonymousEmail) {
			//only anonymous email needs param smtpHost
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "false");
			props.put("mail.smtp.port", MAILPORT);
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.port", SOCKETPORT);
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.auth", "false");
			session = Session.getInstance(props, null);
		} else {
			//normal email does not need param smtpHost and uses the default host SMTPServer
			props.put("mail.smtp.host", SMTPServer);
			props.put("mail.smtp.port", MAILPORT);
			props.put("mail.smtp.socketFactory.port", SOCKETPORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			session = Session.getInstance(props,
					new MailAuthenticator(SMTPUsername, SMTPPassword));
		}
	    //create message
	    MimeMessage msg = new MimeMessage(session);
	    try {
	        //Multipart is used to store many BodyPart objects.
	        Multipart multipart=new MimeMultipart();
	
	        BodyPart part = new MimeBodyPart();
	        part.setContent(content,"text/html;charset=gb2312");
	        //add email content part.
	        multipart.addBodyPart(part);
	
	        //add attachment parts.
	        if(attachments != null && attachments.length > 0) {
	            for(File attachment: attachments) {
	                String fileName = attachment.getName();
	                DataSource dataSource = new FileDataSource(attachment);
	                DataHandler dataHandler = new DataHandler(dataSource);
	                part = new MimeBodyPart();
	                part.setDataHandler(dataHandler);
	                //solve encoding problem of attachments file name.
	                try {
	                    fileName = MimeUtility.encodeText(fileName);
	                } catch (UnsupportedEncodingException e) {
	                    LOGGER.error(
	                        "Cannot convert the encoding of attachments file name.", e);
	                }
	                //set attachments the original file name. if not set,
	                //an auto-generated name would be used.
	                part.setFileName(fileName);
	                multipart.addBodyPart(part);
	            }
	        }
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	        //set sender
	        msg.setFrom(new InternetAddress(from));
	        //set receiver,
	        for(String to: tos) {
	            msg.addRecipient(RecipientType.TO, new InternetAddress(to));
	        }
	        if(ccs != null && ccs.length > 0) {
	            for(String cc: ccs) {
	                msg.addRecipient(RecipientType.CC, new InternetAddress(cc));
	            }
	        }
	        if(bccs != null && bccs.length > 0) {
	            for(String bcc: bccs) {
	                msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
	            }
	        }
	        msg.setContent(multipart);
	        //save the changes of email first.
	        msg.saveChanges();
	        //to see what commands are used when sending a email, use session.setDebug(true)
	        //session.setDebug(true);
	        //send email
	        Transport.send(msg);
	        LOGGER.info("Send email success.");
	        System.out.println("Send html email success.");
	    } catch (NoSuchProviderException e) {
	        LOGGER.error("Email provider config error.", e);
	    } catch (MessagingException e) {
	        LOGGER.error("Send email error.", e);
	    }
	}
	
	/**
	 * Receive Email from POPServer. Use POP3 protocal by default. Thus,
	 * call this method, you need to provide a pop3 mail server address.
	 * @param emailAddress The email account in the POPServer.
	 * @param password The password of email address.
	 */
	public static void receiveEmail(String host, String username, String password) {
	    //param check. If param is null, use the default configured value.
	    if(host == null) {
	        host = POP3Server;
	    }
	    if(username == null) {
	        username = POP3Username;
	    }
	    if(password == null) {
	        password = POP3Password;
	    }
	    Properties props = System.getProperties();
	    //MailAuthenticator authenticator = new MailAuthenticator(username, password);
	    try {
	        Session session = Session.getDefaultInstance(props, null);
	        // Store store = session.getStore("imap");
	        Store store = session.getStore("pop3");
	        // Connect POPServer
	        store.connect(host, username, password);
	        Folder inbox = store.getFolder("INBOX");
	        if (inbox == null) {
	            throw new RuntimeException("No inbox existed.");
	        }
	        // Open the INBOX with READ_ONLY mode and start to read all emails.
	        inbox.open(Folder.READ_ONLY);
	        System.out.println("TOTAL EMAIL:" + inbox.getMessageCount());
	        Message[] messages = inbox.getMessages();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        for (int i = 0; i < messages.length; i++) {
	            Message msg = messages[i];
	            String from = InternetAddress.toString(msg.getFrom());
	            String replyTo = InternetAddress.toString(msg.getReplyTo());
	            String to = InternetAddress.toString(
	                msg.getRecipients(Message.RecipientType.TO));
	            String subject = msg.getSubject();
	            Date sent = msg.getSentDate();
	            Date ress = msg.getReceivedDate();
	            String type = msg.getContentType();
	            System.out.println((i + 1) + ".---------------------------------------------");
	            System.out.println("From:" + mimeDecodeString(from));
	            System.out.println("Reply To:" + mimeDecodeString(replyTo));
	            System.out.println("To:" + mimeDecodeString(to));
	            System.out.println("Subject:" + mimeDecodeString(subject));
	            System.out.println("Content-type:" + type);
	            if (sent != null) {
	                System.out.println("Sent Date:" + sdf.format(sent));
	            }
	            if (ress != null) {
	                System.out.println("Receive Date:" + sdf.format(ress));
	            }
	//            //Get message headers.
	//            @SuppressWarnings("rawtypes")
	//            Enumeration headers = msg.getAllHeaders();
	//            while (headers.hasMoreElements()) {
	//                Header h = (Header) headers.nextElement();
	//                String name = h.getName();
	//                String val = h.getValue();
	//                System.out.println(name + ": " + val);
	//            }
	
	//            //get the email content.
	//            Object content = msg.getContent();
	//            System.out.println(content);
	//            //print content
	//            Reader reader = new InputStreamReader(
	//                    messages[i].getInputStream());
	//            int a = 0;
	//            while ((a = reader.read()) != -1) {
	//                System.out.print((char) a);
	//            }
	        }
	        // close connection. param false represents do not delete messaegs on server.
	        inbox.close(false);
	        store.close();
	//    } catch(IOException e) {
	//        LOGGER.error("IOException caught while printing the email content", e);
	    } catch (MessagingException e) {
	        LOGGER.error("MessagingException caught when use message object", e);
	    }
	}
	
	/**
	 * For receiving an email, the sender, receiver, reply-to and subject may
	 * be messy code. The default encoding of HTTP is ISO8859-1, In this situation,
	 * use MimeUtility.decodeTex() to convert these information to GBK encoding.
	 * @param res The String to be decoded.
	 * @return A decoded String.
	 */
	private static String mimeDecodeString(String res) {
	    if(res != null) {
	        String from = res.trim();
	        try {
	            if (from.startsWith("=?GB") || from.startsWith("=?gb")
	                    || from.startsWith("=?UTF") || from.startsWith("=?utf")) {
	                from = MimeUtility.decodeText(from);
	            }
	        } catch (Exception e) {
	            LOGGER.error("Decode string error. Origin string is: " + res, e);
	        }
	        return from;
	    }
	    return null;
     }
	}
