package gwall.test.sendAnonymousemail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**  
 *对象来描述发送者的授权问题。即，发送者在发送邮件直线需要获取SMTP服务器的授权，
 *只有经过授权的账户才能发送邮件
 * @author  
 * @date 2017年11月1日  
 * @version 3.0.0  
 */
public class MailAuthenticator extends Authenticator{
	/**
	 * Represents the username of sending SMTP server.
	 * <p>For example: If you use smtp.163.com as your smtp server, then the related
	 * username should be: <br>'<b>testname@163.com</b>', or just '<b>testname</b>' is OK.
	 */
	private String username = null;
	/**
	 * Represents the password of sending SMTP sever.
	 * More explicitly, the password is the password of username.
	 */
	private String password = null;

	public MailAuthenticator(String user, String pass) {
		username = user;
		password = pass;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

}
