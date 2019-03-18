/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Administrator
 */
public class JEmail
{
    private Properties getProperties(String pEmail)//YAHOO, EXCHANGE
    {
        Properties props = new Properties();

        if (pEmail.toUpperCase().trim().indexOf("YAHOO")>=0)
        {
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.socketFactory.port","465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.host","smtp.mail.yahoo.com");
            props.put("mail.smtp.port","465");
        }
        else
        {
            boolean debug = true;

            //Exchange - default                
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.host", "m.outlook.com");
            props.put("mail.smtp.auth", "true");
        }

        return props;
    }

    //pContentTypes = text/html
    public boolean send(String pFrom, String pTo, String pSubject, String pPWD, String pContent, String pContentType)
    {
        try
        {
            Session mailSession = Session.getInstance(getProperties(pFrom), new PasswordAuthenticator(pFrom, pPWD));

            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(pFrom));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(pTo));

            msg.setSubject(pSubject, "UTF-8");
            msg.setContent(pContent, pContentType);//"text/html");

            Transport.send(msg);

            return true;
        }

        catch(Exception e)
        {
            return false;
        }
    }

    private class PasswordAuthenticator extends Authenticator
    {
        String email;
        String pwd;

        PasswordAuthenticator(String pEmail, String pPWD)
        {
            email = pEmail;
            pwd   = pPWD;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(email, pwd);
        }

    }
    
}
