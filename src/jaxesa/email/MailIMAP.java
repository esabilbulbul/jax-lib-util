/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.email;

import com.sun.mail.imap.IMAPFolder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

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
        // TEST If you want to show detailed session information:
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

    public Message[] getMessages(long start, long end) throws Exception {
        Message[] messages = folder.getMessagesByUID(start, end); // MessagingException

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        folder.fetch(messages, fp); // MessagingException

        return messages;
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

    public String getHTMLContent(Message message) throws Exception {
        String htmlContent = null;

        if (message.isMimeType("multipart/*")) { // MessagingException
            Multipart mp = (Multipart) message.getContent(); // IOException, MessagingException
            int count = mp.getCount(); // MessagingException
            for (int i = 0; i < count; i++) {
                if (mp.getBodyPart(i).isMimeType("text/html")) { // MessagingException
                    htmlContent = (String) mp.getBodyPart(i).getContent();
                }
            }
        }

        return htmlContent;
    }

    // TODO Alınan mesaj dizisindeki mesajların eklerinin (attachments) kaydedilmesi ile ilgili bir metot eklenecek.
    public void saveAttachments(Message message, String folder) throws Exception {
        Path path = Paths.get(folder);
        if (!Files.isWritable(path)) {
            throw new Exception("Attachment folder is not writable.");
        }

        if (message.isMimeType("multipart/*")) { // MessagingException
            Multipart mp = (Multipart) message.getContent(); // IOException, MessagingException
            int count = mp.getCount(); // MessagingException
            for (int i = 0; i < count; i++) {
                MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    part.saveFile(folder + File.separator + MimeUtility.decodeText(fileName));
                }
            }
        }
    }
}
