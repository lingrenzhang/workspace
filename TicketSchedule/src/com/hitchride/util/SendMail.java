package com.hitchride.util;

import java.util.Date;  
import java.util.Properties;  

import javax.mail.Address;  
import javax.mail.Authenticator;  
import javax.mail.Message;  
import javax.mail.PasswordAuthentication;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;  

import com.hitchride.emailTemplate.Template;
  
public class SendMail
{  
    private String _host = "smtp.mandrillapp.com";  // smtp server
    
    private String _username = "zhouyu.hott@gmail.com";
    
    private String _password = "XN3eDXIXJEgabQpbRM8_wg";

    private Template _template;

    private String _sendTo;
    public SendMail(Template t, String sendTo){
    	_template = t;
    	_sendTo = sendTo;
    }

    public void send() throws Exception  
    {  
        try  
        {  
            Properties props = new Properties(); // 获取系统环境  
            Authenticator auth = new SimpleAutherticator(_username, _password); // 进行邮件服务器用户认证  
            props.put("mail.smtp.host", _host);  
            props.put("mail.smtp.auth", "true");  
            Session session = Session.getDefaultInstance(props, auth);  
            // 设置session,和邮件服务器进行通讯。  
            MimeMessage message = new MimeMessage(session);  
            // message.setContent("foobar, "application/x-foobar"); // 设置邮件格式  
            message.setSubject(this._template.getSubject()); // 设置邮件主题  
            message.setText(this._template.getSubject()); // 设置邮件正文  
            //message.setHeader(mail_head_name, mail_head_value); // 设置邮件标题  
            message.setSentDate(new Date()); // 设置邮件发送日期  
            Address address = new InternetAddress(this._template.getSendFrom(), this._template.getSendFromAlias());  
            message.setFrom(address); // 设置邮件发送者的地址  
            Address toAddress = new InternetAddress(this._sendTo); // 设置邮件接收方的地址  
            message.addRecipient(Message.RecipientType.TO, toAddress);  
            Transport.send(message); // 发送邮件  
            System.out.println("send ok!");  
        } catch (Exception ex)  
        {  
            ex.printStackTrace();  
            throw new Exception(ex.getMessage());  
        }  
    }  
  
    /** *//** 
     * 用来进行服务器对用户的认证 
     */  
    public class SimpleAutherticator extends Authenticator  
    {
    	private String _user;
    	private String _pwd;
        public SimpleAutherticator(String user, String pwd)  
        {
        	this._user = user;
        	this._pwd = pwd;
        }

        public PasswordAuthentication getPasswordAuthentication()  
        {  
            return new PasswordAuthentication(this._user, this._pwd);  
        }  
    }    
  
}