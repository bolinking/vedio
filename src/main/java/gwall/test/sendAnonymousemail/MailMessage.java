package gwall.test.sendAnonymousemail;

/**  
 * 邮件对象，内封装了收信人、发信人、邮件内容、抄送人、密件抄送等等诸多代表邮件的属性 
 * @author
 * @date 2017年11月1日  
 * @version 3.0.0  
 */
public class MailMessage {
	private String subject;
	private String from;
	private String[] tos;
	private String[] ccs;
	private String[] bccs;
	private String content;
	private String[] fileNames;

	/**
	 * No parameter constructor.
	 */
	public MailMessage() {
	}

	/**
	 * Construct a MailMessage object.
	 */
	public MailMessage(String subject, String from, String[] tos, String[] ccs, String[] bccs, String content,
			String[] fileNames) {
		this.subject = subject;
		this.from = from;
		this.tos = tos;
		this.ccs = ccs;
		this.bccs = bccs;
		this.content = content;
		this.fileNames = fileNames;
	}

	/**
	 * Construct a simple MailMessage object.
	 */
	public MailMessage(String subject, String from, String to, String content) {
		this.subject = subject;
		this.from = from;
		this.tos = new String[] { to };
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTos() {
		return tos;
	}

	public void setTos(String[] tos) {
		this.tos = tos;
	}

	public String[] getCcs() {
		return ccs;
	}

	public void setCcs(String[] ccs) {
		this.ccs = ccs;
	}

	public String[] getBccs() {
		return bccs;
	}

	public void setBccs(String[] bccs) {
		this.bccs = bccs;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
}
