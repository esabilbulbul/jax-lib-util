/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.email;

import com.sun.mail.imap.IMAPFolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author Hüseyin Şahin AKBAL
 */
public class MailIMAP {

    private Store store;
    private IMAPFolder folder;
    private long UIDNext;
    private final String host;
    private final String username;
    private final String password;
    private final String folderName;

    public MailIMAP(String host, String username, String password, String folderName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.folderName = folderName;
    }

    public long getMailFolderNextMessageID() {
        return UIDNext;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFolderName() {
        return folderName;
    }

    public void connect() throws Exception {
        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        Session emailSession = Session.getDefaultInstance(properties);
        // emailSession.setDebug(true);
        store = emailSession.getStore("imaps"); // NoSuchProviderException
        store.connect(host, username, password); // MessagingException
        folder = (IMAPFolder) store.getFolder(folderName); // MessagingException
        folder.open(Folder.READ_ONLY); // MessagingException
        UIDNext = folder.getUIDNext(); // MessagingException
    }

    public void disconnect() throws Exception {
        folder.close(false); // MessagingException
        store.close(); // MessagingException
    }

    public Message[] getMessagesByIDSubject(long start, long end, String subject) throws Exception {
        List<Message> messages = new ArrayList<>();

        Message[] tempMessages = folder.getMessagesByUID(start, end); // MessagingException

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        folder.fetch(tempMessages, fp); // MessagingException

        for (Message message : tempMessages) {
            String message_subject = message.getSubject();
            // If the mail does not have a subject, subject value becomes null.
            if (message_subject != null && message_subject.contains(subject)) { // MessagingException
                messages.add(message);
            }
        }

        return messages.toArray(new Message[messages.size()]);
    }

    public long getLastID(Message[] messages) throws Exception {
        if (messages.length > 0) {
            long lastUID = folder.getUID(messages[0]); // MessagingException
            for (Message message : messages) {
                long messageUID = folder.getUID(message); // MessagingException
                if (messageUID > lastUID) {
                    lastUID = messageUID;
                }
            }
            return lastUID;
        } else {
            return 0;
        }
    }

    public String[] getHTMLContentOfMessages(Message[] messages) throws Exception {
        List<String> htmlContents = new ArrayList<>();

        for (Message message : messages) {
            if (message.isMimeType("multipart/*")) { // MessagingException
                Multipart mp = (Multipart) message.getContent(); // IOException, MessagingException
                int count = mp.getCount(); // MessagingException
                for (int i = 0; i < count; i++) {
                    if (mp.getBodyPart(i).isMimeType("text/html")) { // MessagingException
                        htmlContents.add((String) mp.getBodyPart(i).getContent()); // MessagingException, IOException
                    }
                }
            }
        }

        return htmlContents.toArray(new String[htmlContents.size()]);
    }

    // TODO Alınan mesaj dizisindeki mesajların eklerinin (attachments) kaydedilmesi ile ilgili bir metot eklenecek.
}
