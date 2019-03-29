/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.activeMQ;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Administrator
 */
public class MQ implements ExceptionListener
{
    public static final int MQ_MODE_READ  = 1;
    public static final int MQ_MODE_WRITE = 2;
    
    private int               gQMode;
    public String             gQName;
    public String             gQURL;
    
    ActiveMQConnectionFactory gConnectionFactory;
    Connection                gConnection;
    Session                   gSession;
    Destination               gDestination;
    
    MessageProducer           gProducer;
    MessageConsumer           gConsumer;
    
    int                       gTimeout_seconds = 1000;
    
    public static String TESTMSG = "mqtest";
    
    public MQ()
    {
        
    }
    
    public boolean isValid()
    {
        try
        {
            switch(gQMode)
            {
                case MQ_MODE_READ:

                    //return testPop(); //NOT APPLICABLE - NEVER USE
                    return false;

                    
                case MQ_MODE_WRITE:

                    return testPush();

                default:

                    return false;
            }

        }
        catch(Exception e)
        {            
            return false;
        }
    }
    
    /*
    private boolean testPop()
    {
        try
        {
            if (gConnectionFactory!=null)
            {
                try
                {
                    pop(20);
                }
                catch(Exception e)
                {
                    close();//close if it is open
                    return false;
                }

                return true;
            }
            else
            {
                return false;
            }
            
        }
        catch(Exception e)
        {
            return false;
        }        
    }
    */
    
    private boolean testPush()
    {
        try
        {
            
            if (gConnectionFactory!=null)
            {
                try
                {
                    push(TESTMSG);
                }
                catch(Exception e)
                {
                    close();//close if it is open
                    return false;
                }

                return true;
            }
            else
            {
                return false;
            }
            
        }
        catch(Exception e)
        {
            return false;
        }
        
    }
    
    public boolean reset()
    {
        try
        {
            close();
                
            open(gQMode, gQName, gQURL);
            
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean open(int pMQMode, String pQueuePath, String pQueueName) throws Exception
    {
        gQMode = pMQMode;
        gQName = pQueueName;
        gQURL  = pQueuePath;

        switch(gQMode)
        {
            case MQ_MODE_READ:
                
                createConsumer(pQueuePath, pQueueName);
                
                break;
            case MQ_MODE_WRITE:
                
                createProducer(pQueuePath, pQueueName);
                
                break;
            default:
                
                return false;
        }
        
        return true;

    }
    
    public void push(String pMsg) throws Exception
    {
        try
        {
            // Create a messages
            //String text = "Hello world! From: " + Thread.currentThread().getName() + " : ";// + this.hashCode();
            TextMessage message = gSession.createTextMessage(pMsg);

            // Tell the producer to send the message
            //System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            gProducer.send(message);
            
        }
        catch(Exception e)
        {
            throw e;
        }
    }

    public void close() throws Exception
    {
        try
        {
            gConnectionFactory = null;
            
            switch(gQMode)
            {
                case MQ_MODE_READ:

                    closeConsumer();
                    
                    break;
                case MQ_MODE_WRITE:

                    closeProducer();

                default:

                    return ;
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        
    }
    
    public void closeProducer() throws Exception
    {
        try
        {
            // Clean up
            gSession.close();
            gConnection.close();
            
        }
        catch(Exception e)
        {
            throw e;
        }

    }
    
    public void closeConsumer() throws Exception
    {
        try
        {
            gConsumer.close();
            gSession.close();
            gConnection.close();
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    
    //Sample pBrokerURL : "tcp://localhost:61616" pQueueName = "TEST.FOO"
    public void createProducer(String pBrokerURL, String pQueueName) throws Exception
    {
        try
        {
            // Create a ConnectionFactory
            gConnectionFactory = new ActiveMQConnectionFactory(pBrokerURL);

            // Create a Connection
            gConnection = gConnectionFactory.createConnection();
            gConnection.start();

            // Create a Session
            gSession = gConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            gDestination = gSession.createQueue(pQueueName);
            
            // Create a MessageProducer from the Session to the Topic or Queue
            gProducer = gSession.createProducer(gDestination);
            gProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        }
        catch (Exception e) 
        {
            System.out.println("Caught: " + e);
            e.printStackTrace();
            throw e;
        }
        
    }

    public void createConsumer(String pBrokerURL, String pQueueName) throws Exception
    {
        try
        {
            // Create a ConnectionFactory
            gConnectionFactory = new ActiveMQConnectionFactory(pBrokerURL);

            // Create a Connection
            gConnection = gConnectionFactory.createConnection();
            gConnection.start();

            // Create a Session
            gSession = gConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            gDestination = gSession.createQueue(pQueueName);
            
            // Create a MessageConsumer from the Session to the Topic or Queue
            gConsumer = gSession.createConsumer(gDestination);            
            
        }
        catch(Exception e)
        {
            throw e;
        }

    }
    
    public String pop(int pTimeoutseconds) throws Exception
    {
        Message message; 
        try
        {
            String text = "";
            //Wait for a message
            message = gConsumer.receive(pTimeoutseconds);

            if (message==null)
                return null;
            
            if (message instanceof TextMessage) 
            {
                TextMessage textMessage = (TextMessage) message;
                text = textMessage.getText();
                
                //if (text.toString().trim().equals(TESTMSG)==true)
                //    return "";//This is not a real data
                //System.out.println("Received: " + text);
            } 
            else 
            {
                text = message.toString();
                
                //if (text.toString().trim().equals(TESTMSG)==true)
                //    return "";//This is not a real data
                //System.out.println("Received: " + message);
            }
            
            return text;
        }

        //catch(NullPointerException e)
        //{
        //    return null;
        //}
        catch(Exception e)
        {
            throw e;
        }
    }
    
    public synchronized void onException(JMSException ex)
    {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }        

}


