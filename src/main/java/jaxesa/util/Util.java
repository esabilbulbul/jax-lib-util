/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
//import com.mysql.jdbc.StringUtils;
//import com.mysql.cj.jdbc.StringUtils;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
//import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLType;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
/*import javax.xml.bind.DatatypeConverter;*/

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jaxesa.error.ErrorCode;
import jaxesa.persistence.annotations.Column;
import jaxesa.persistence.annotations.Id;
import jaxesa.persistence.misc.RowColumn;
import jaxesa.crypto.RSA;
import jaxesa.crypto.SHA256;
import jaxesa.crypto.ssoRSAKeyPair;
import jaxesa.session.SessionObject;
import jaxesa.string.patternmatch.Simil;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
//import jaxesa.error.ErrorCodes;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.zip.Checksum;
import java.util.zip.CRC32;
import jaxesa.annotations.AnnoAttributes;
import jaxesa.crypto.RSAMisc;
import jaxesa.crypto.ssoSessionKeys;
import jaxesa.framework.misc.HTTPReqParameter;
import jaxesa.framework.misc.frameworkMisc;
import jaxesa.redis.RedisAPI;
import jaxesa.api.callback.ssoCallbackParam;
import jaxesa.login.ssoLoginResults;
import jaxesa.persistence.cache.Caching;
import redis.clients.jedis.Jedis;
import org.joda.time.DateTime;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jaxesa.redis.ssoRedisLettuce;
import jaxesa.redis.ssoRedisUnionKey;
import jaxesa.redis.ssoUnionMatchScript;

/**
 *
 * @author Administrator
 */
public final class Util
{
    public static String gSS_API_COMMON_IP_N_LOC = "getIPNLocation"; // "gipl";
    
    public static class Address
    {
        
    }

    public static class Session
    {
        //psMethodName = REST API Method
        public static String calcSessionHash(String psMethodName)
        {
            String sHash = Long.toString(Util.crypto.crc32.calculate(psMethodName));
            
            return sHash;
        }
        
        public static SessionObject parseSessionInfo(String psUserName_at_SessionId)
        {
            SessionObject SObj   = new SessionObject();
            String[] SessionInfo = psUserName_at_SessionId.split("@");
            
            int index = 0 ;
            for (String Info: SessionInfo)
            {
                //if (index==0)
                switch(index)
                {
                    case 0:
                        SObj.UserId = Info;//UserName before
                        break;
                    case 1:
                        SObj.SessionId= Info;
                        break;
                        /*
                    case 2:
                        SObj.UserId = Info;
                        break;
                        */
                }
                index++;
            }
            
            return SObj;
        }
    }

    public static class images
    {
        //This create plain background images
        // pColor = #3F51B5 2000x2000 
        public static void createBackgroundImage(String pColor, int pWidth, int pHeight, String pOutputFileName, String pFileExtension, String pCWEBPath) throws Exception
        {
            try
            {
                int width  = pWidth;//1080;
                int height = pHeight;//1350;

                // Example hex color: #3F51B5
                //Color bgColor = Color.decode("#3F51B5");
                Color bgColor = Color.decode(pColor);

                BufferedImage image = new BufferedImage(
                                                        width,
                                                        height,
                                                        BufferedImage.TYPE_INT_RGB);

                Graphics2D g = image.createGraphics();
                g.setColor(bgColor);
                g.fillRect(0, 0, width, height);
                g.dispose();

                //ImageIO.write(image, "png", new File("background.png"));
                if(pFileExtension.equals("webp")==false)
                    ImageIO.write(image, pFileExtension, new File(pOutputFileName));
                else
                    write2File(pCWEBPath, pOutputFileName, image);
            }
            catch(Exception e)
            {
                throw e;
            }

        }

        public static String getFileExtension(String fileName) 
        {
            int lastDot = fileName.lastIndexOf('.');
            int lastSep = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
            if (lastDot <= lastSep || lastDot == fileName.length() - 1) {
                return ""; // no extension found
            }
            return fileName.substring(lastDot + 1).toLowerCase();
        }

        public static void write2File(String pCWEBPath, String outputFilePath, BufferedImage pbiFile) throws Exception
        {
            String sFileType = getFileExtension(outputFilePath);
                    
            if ("webp".equalsIgnoreCase(sFileType)) //extension png/webp
            {
                File tempPng = File.createTempFile("ss_webp_src_", ".png");
                try 
                {
                    ImageIO.write(pbiFile, "png", tempPng); // lossless intermediate, keeps alpha

                    java.io.File outFile = new java.io.File(outputFilePath.endsWith(".webp") ? outputFilePath : outputFilePath + ".webp");

                    ProcessBuilder pb = new ProcessBuilder(
                        //CWEBP_PATH, "-q", "90", "-alpha_q", "100", 
                        pCWEBPath, "-q", "90", "-alpha_q", "100", 
                        tempPng.getAbsolutePath(), "-o", outFile.getAbsolutePath()
                    );

                    pb.redirectErrorStream(true);
                    java.lang.Process p = pb.start();
                    try (var is = p.getInputStream()) { is.readAllBytes(); }
                    int exit = p.waitFor();
                    if (exit != 0) throw new IOException("cwebp failed, exit=" + exit);
                } finally {
                    tempPng.delete();
                }
            } 
            else 
            {
                // existing PNG/JPEG path unchanged
                //ImageIO.write(pImage, pExtension, new File(pOutputPath.endsWith("." + pExtension) ? pOutputPath : pOutputPath + "." + pExtension));
                File outputFile = new File(outputFilePath);
                File parentDir = outputFile.getParentFile();
                if (parentDir != null && !parentDir.exists())
                {
                    parentDir.mkdirs();
                }

                boolean success = ImageIO.write(pbiFile, sFileType, new File(outputFilePath));
            }
        }
        
        // String pCWEBPath = (OPTIONAL) only if the file is webp (EXTENSION)
        public static BufferedImage readImage(String filePath, String pCWEBPath) throws IOException, InterruptedException 
        {
            if (filePath.toLowerCase().endsWith(".webp")) 
            {
                return readWebp(filePath, pCWEBPath);
            }
            BufferedImage img = ImageIO.read(new java.io.File(filePath));
            if (img == null) {
                throw new IOException("Could not read image: " + filePath);
            }
            return img;
        }

        public static BufferedImage readWebp(String webpPath, String pCWEBPath) throws IOException, InterruptedException 
        {
            java.io.File tempPng = java.io.File.createTempFile("ss_webp_read_", ".png");
            try 
            {
                ProcessBuilder pb = new ProcessBuilder(
                    pCWEBPath, webpPath, "-o", tempPng.getAbsolutePath()
                );
                pb.redirectErrorStream(true);
                java.lang.Process p = pb.start();
                try (var is = p.getInputStream()) { is.readAllBytes(); }
                int exit = p.waitFor();
                if (exit != 0) throw new IOException("dwebp failed decoding " + webpPath + ", exit=" + exit);

                BufferedImage img = ImageIO.read(tempPng);
                if (img == null) throw new IOException("Could not read decoded PNG for: " + webpPath);
                return img;
            } finally 
            {
                tempPng.delete();
            }
        }
    }

    public static class Process
    {
        //poThreadManager class MUST implements Runnable
        public static void createThread(Runnable poThreadManager)
        {
            Thread thread = new Thread(poThreadManager);

            thread.start();
        }
    }

    public static class runtime
    {
        public static Method prepareMethod(String pClassName, String pMethodName, ArrayList<Class<?>> paParamTypes)
        {
            try
            {
                Method myMethod;
                Class<?> cls;

                Class<?> params[] = new Class[paParamTypes.size()];
                Integer  Index    = 0;

                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //              Prepare the parameters to method
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                for (Class<?> prmType:paParamTypes)
                {
                    params[Index] = prmType;

                    // you can do additional checks for other data types if you want.
                    Index++;
                }

                cls = Class.forName(pClassName);
                myMethod     = cls.getDeclaredMethod(pMethodName, params);//prototype

                return myMethod;
                
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        //method is prepared ready
        public static Object executeMethod(String pClassName, Method pMethod, ArrayList<Object> paParamVals)
        {
            try
            {
                Object _instance;

                _instance = Util.misc.getInstance(pClassName);

                Integer index = 0;
                Object[] OParVals = new Object[paParamVals.size()];
                for(Object ALPar:paParamVals)
                {
                    OParVals[index]=ALPar;
                    index++;
                }

                Object ReturnVal = pMethod.invoke(_instance, OParVals);

                return ReturnVal;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        public static Object executeMethod( String pClassName, 
                                            String pMethodName, 
                                            ArrayList<Class<?>> paParamTypes,
                                            ArrayList<Object> paParamVals)
        {
            try
            {
                Class<?> cls;
                Object _instance;
                Method myMethod;

                myMethod = prepareMethod(pClassName, pMethodName, paParamTypes);

                if (myMethod!=null)
                {
                    cls = Class.forName(pClassName);

                    Integer index = 0;
                    Object[] OParVals = new Object[paParamVals.size()];
                    for(Object ALPar:paParamVals)
                    {
                        OParVals[index]=ALPar;
                        index++;
                    }

                    _instance    = Util.misc.getInstance(pClassName);

                    Object ReturnVal = myMethod.invoke(_instance, OParVals);

                    return ReturnVal;
                }
                else
                {
                    return null;
                }
            }
            catch(Exception e)
            {
                return null;
            }
        }

    }
    
    public static class Mathx
    {
        public static BigDecimal calcDiff(String psNum1, String psNum2)
        {
            BigDecimal bdNum1 = new BigDecimal(psNum1);
            BigDecimal bdNum2 = new BigDecimal(psNum2);
            BigDecimal bdDiff = bdNum1.subtract(bdNum2);
            
            return bdDiff;
        }
        
        public static BigDecimal calcDiv(String psNum1, String psNum2, int pDigits)
        {
            BigDecimal bdNum1 = new BigDecimal(psNum1);
            BigDecimal bdNum2 = new BigDecimal(psNum2);
            BigDecimal bdDiff = bdNum1.subtract(bdNum2);
            
            BigDecimal bdPerc = bdDiff.divide(bdNum2, pDigits, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            
            return bdPerc;
        }

    }
    
    public static class Tax
    {
        // if tax included Tax = (Total * TaxRate) / (1 + TaxRate)
        public static BigDecimal calculate(String    psAmount, 
                                           String    psTaxRate, 
                                           boolean   pbTaxIncludedInTotal)
        {
            BigDecimal bdTotal    = new BigDecimal(psAmount);
            BigDecimal bdTaxRate  = new BigDecimal(psTaxRate);

            bdTaxRate = bdTaxRate.divide(new BigDecimal(100),  5, RoundingMode.HALF_DOWN);

            BigDecimal bd1plusN   = new BigDecimal(1).add(bdTaxRate);
            BigDecimal bdTax      = new BigDecimal(psTaxRate);

            if (pbTaxIncludedInTotal==true)
            {
                bdTax = bdTotal.multiply(bdTaxRate).divide(bd1plusN, 5, RoundingMode.HALF_DOWN);
            }
            else
            {
                bdTax = bdTotal.multiply(bdTaxRate);
            }

            return bdTax;
        }
    }

    public static class Tomcat
    {
        public static void print2TomcatLog(Logger plogger, String pMsg, String pAccId)//pAccId = optional
        {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            // [0] = Thread.getStackTrace
            // [1] = this method
            // [2] = caller method
            if (stack.length>2)
            {
                StackTraceElement caller = stack[2];
                String className  = caller.getClassName();
                String methodName = caller.getMethodName();

                plogger.logp(Level.INFO, className, methodName, "SHIPSHUK > " + pMsg);
            }
            else
                plogger.info("SHIPSHUK > " + pMsg);
        }

    }

    //Memory DB
    public static class Redis
    {
        // Used to create logs that are not easy to take in other media
        public static String addLog(String pLogName, String pLogData)
        {
            ssoRedisLettuce lettuce = new ssoRedisLettuce();
            try
            {
                //String sLogName = "addLog." + pLogName;
                lettuce = Util.Redis.getConnection(pLogName);
                
                Util.Redis.JString.set(lettuce, pLogName, pLogData);

                Util.Redis.releaseConnection(lettuce);
                
                return "";
            }
            catch(Exception e)
            {
                Util.Redis.releaseConnection(lettuce);
                return e.getMessage();
            }
        }
        
        public static String getConnectionPoolStats()
        {
            return RedisAPI.getConnectionPoolStats();
        }

        public static ssoRedisLettuce getConnection(String pConnectionName)// pConnectionName for follow up purpose
        {
            return RedisAPI.getConnection(pConnectionName);
        }

        public static ssoRedisLettuce getConnectionAsync(String pConnectionName)// pConnectionName for follow up purpose
        {
            return RedisAPI.getConnectionAsync(pConnectionName);
        }

        public static void releaseConnection(ssoRedisLettuce lettuce)
        {
            if (lettuce!=null)
                lettuce.close();
        }

        public static void publish(ssoRedisLettuce lettuce, String pChannel, Object poResponse)
        {
            try
            {
                String sResult = Util.JSON.toString(poResponse);

                //pjedis.publish(FTP_FILE_STATS_PUBLISH_CHANNEL, sResult);
                lettuce.publish(pChannel, sResult);

                Thread.sleep(25);//25 ms wait
            }
            catch(Exception e)
            {
                String s=  "";
            }

        }

        public static String connect(String psHost, int piPort, int pMaxConNumber, int pResourceTimeout) throws Exception
        {
            return RedisAPI.connect(psHost, piPort, pMaxConNumber, pResourceTimeout);
        }
        
        public static RedisClient getClient()
        {
            return RedisAPI.getClient();
        }

        // This will close the pool
        public static void close()
        {
            RedisAPI.close();
        }

        public static class JNumber
        {
            public static long increase(ssoRedisLettuce lettuce, String pKey,int pBy)
            {
                return RedisAPI.JNumber.increase(lettuce, pKey, pBy);
            }
            
            public static long decrease(ssoRedisLettuce lettuce, String pKey, int pBy)
            {
                return RedisAPI.JNumber.decrease(lettuce, pKey, pBy);
            }

        }
        
        // To execute scripts on Redis instead of commands
        public static class Lua
        {
            // Same groups will be uninized (combined) (SINTER) different groups will be cross matched
            public static ssoUnionMatchScript prepareUnionMatchScript(ArrayList<ssoRedisUnionKey> paKeys)
            {
                return RedisAPI.Lua.prepareUnionMatchScript(paKeys);
            }
            
            public static Set<String> execute(ssoRedisLettuce lettuce, ssoUnionMatchScript pckgScript)
            {
                String[] aKeys = pckgScript.keys;
                String[] aArgs = pckgScript.args;

                return RedisAPI.Lua.execute(lettuce, pckgScript.script, aKeys, aArgs);
            }

            public static Set<String> execute(ssoRedisLettuce lettuce, String pScript, String[] pKeys, String[] pArgs)
            {
                return RedisAPI.Lua.execute(lettuce, pScript, pKeys, pArgs);
            }
        }

        // Sinter is used to store the IDs in table sense and to find them matching with other sets
        public static class Sinter
        {
            public static RedisFuture<Long> set(ssoRedisLettuce lettuce, String pKey, String... paVal)
            {
                //String sVal = Util.JSON.Convert2JSON(poVal).toString();

                return RedisAPI.Sinter.sadd(lettuce, pKey, paVal);
            }

            public static RedisFuture<Long> set(ssoRedisLettuce lettuce, String pKey, int piExpirySeconds, String... paVal)
            {
                //String sVal = Util.JSON.Convert2JSON(poVal).toString();

                return RedisAPI.Sinter.saddex(lettuce, pKey, piExpirySeconds, paVal);
            }

            public static RedisFuture<Set<String>> get(ssoRedisLettuce lettuce, String pKey)
            {
                RedisFuture<Set<String>> saResult = RedisAPI.Sinter.get(lettuce, pKey);

                return saResult;
                
            }
            
            public static Set<String> findMatches(ssoRedisLettuce lettuce, String... pKey)
            {
                Set<String> matches = lettuce.connection.sync().sinter(pKey);
                
                return matches;
            }
        }

        //this uses JString in base
        public static class JObject
        {
            public static String set(ssoRedisLettuce lettuce, String pKey, Object poVal)
            {
                String sVal = Util.JSON.Convert2JSON(poVal).toString();

                return RedisAPI.JString.set(lettuce, pKey, sVal);
            }

            public static String set(ssoRedisLettuce lettuce, String pKey, Object poVal, int piExpirySeconds)
            {
                String sVal = Util.JSON.Convert2JSON(poVal).toString();

                return RedisAPI.JString.set(lettuce, pKey, sVal, piExpirySeconds);
            }

            public static <T extends Object> T get(ssoRedisLettuce lettuce, String pKey, Class<T> pClass)
            {
                String sObj = RedisAPI.JString.get(lettuce, pKey);
                
                if(sObj==null)
                    return null;
                
                Object retObj = Util.JSON.Convert2Obj(sObj, pClass);
                
                return pClass.cast(retObj);
                //return (T)Util.JSON.Convert2Obj(sObj, pClass);//deprecated
            }

        }

        public static class JString
        {
            public static String set(ssoRedisLettuce lettuce, String pKey, String pVal, int piExpirySeconds)
            {
                return RedisAPI.JString.set(lettuce, pKey, pVal, piExpirySeconds);
            }

            public static String set(ssoRedisLettuce lettuce, String pKey, String pVal)
            {
                return RedisAPI.JString.set(lettuce, pKey, pVal);
            }

            public static String setSync(ssoRedisLettuce lettuce, String pKey, String pVal)
            {
                return RedisAPI.JString.setSync(lettuce, pKey, pVal);
            }
            
            public static String get(ssoRedisLettuce lettuce, String pKey)
            {
                return RedisAPI.JString.get(lettuce, pKey);
            }

            public static String getSync(ssoRedisLettuce lettuce, String pKey)
            {
                return RedisAPI.JString.getSync(lettuce, pKey);
            }

            public static long remove(ssoRedisLettuce lettuce, String... pKeys)
            {
                return RedisAPI.JString.remove(lettuce, pKeys);
            }
        }
        
        // Key + Fields 
        // String might be faster in some cases 
        public static class JHashes
        {
            public static boolean set(ssoRedisLettuce lettuce, String psKey, String pFieldName, String pFieldVal)
            {
                return lettuce.hset(psKey, pFieldName, pFieldVal);
            }

            public static boolean set(ssoRedisLettuce lettuce, String psKey, String pFieldName, String pFieldVal, int piExpirySeconds)
            {
                return lettuce.hset(psKey, pFieldName, pFieldVal, piExpirySeconds);
            }

            public static String getField(ssoRedisLettuce lettuce, String psKey, String pFieldName)
            {
                return RedisAPI.JHashes.getField(lettuce, psKey, pFieldName);
            }

            public static long remove(ssoRedisLettuce lettuce, String... pKeys)
            {
                return RedisAPI.JHashes.remove(lettuce, pKeys);
            }
        }

        //Queues
        public static class JLists
        {
            //pListKey = Queue Name
            //pbFromTop = default TRUE
            public static long push(ssoRedisLettuce lettuce, String pListKey, String pListEl, boolean pbFromTop)
            {
                return RedisAPI.JLists.push(lettuce, pListKey, pListEl, pbFromTop);
            }

            //pListKey = Queue Name
            //pbFromTop = default TRUE
            public static long push(ssoRedisLettuce lettuce, String pListKey, Object pListEl, boolean pbFromTop)
            {
                String sListEl = Util.JSON.Convert2JSON(pListEl).toString();

                return RedisAPI.JLists.push(lettuce, pListKey, sListEl, pbFromTop);
            }

            //pListKey = Queue Name
            //pbFromTop = default FALSE
            public static String pop(ssoRedisLettuce lettuce, String pListKey, boolean pbFromTop)
            {
                return RedisAPI.JLists.pop(lettuce, pListKey, pbFromTop);
            } 
            
            public static <T extends Object> T pop(ssoRedisLettuce lettuce, String pListKey, Class<T> pClass, boolean pbFromTop)
            {
                String sElVal = RedisAPI.JLists.pop(lettuce, pListKey, pbFromTop);
                
                Object retObj = Util.JSON.Convert2Obj(sElVal, pClass);
                
                return pClass.cast(retObj);

                //return (T)Util.JSON.Convert2Obj(sElVal, pClass);//depreciated
            }
            
            public static long size(ssoRedisLettuce lettuce, String pListKey)
            {
                return RedisAPI.JLists.size(lettuce, pListKey);
            }

            public static long remove(ssoRedisLettuce lettuce, String... pKeys)
            {
                return RedisAPI.JLists.remove(lettuce, pKeys);
            }
            
            public static long removeEl(ssoRedisLettuce lettuce, String pListKey, int pCount, String pKey)
            {
                return RedisAPI.JLists.removeEl(lettuce, pListKey, pCount, pKey);
            }
            
        }
    }
    
    public static class framework 
    {
        public String getParam(String pName, ArrayList<HTTPReqParameter> paParams)
        {
            return frameworkMisc.getParameterValue(pName, paParams);
        }
    }

    public static class Sys
    {
        //This executes terminal command
        // For example (clamscan + parameter (....txt file)
        // CommandLine = "clamscan /Users/esabil/Documents/files/kasa2020_1.txt"
        public static RunTimeOutputs executeCommand(String pCommandLine)
        {
            RunTimeOutputs rtos = new RunTimeOutputs();
            try
            {
                Runtime rt = Runtime.getRuntime();
                rtos.proc = rt.exec(pCommandLine);

                InputStream stderr = rtos.proc.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);

                InputStream stdin = rtos.proc.getInputStream();
                InputStreamReader iso = new InputStreamReader(stdin);

                rtos.err = new BufferedReader(isr);
                rtos.out = new BufferedReader(iso);
                
                return rtos;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        public static String getOSName()
        {
            return System.getProperty("os.name");
        }
        
        public static boolean isOSWindows()
        {
            String sOSName = Util.Sys.getOSName();
            boolean bWindows = false;
            if(sOSName.trim().toLowerCase().indexOf("windows")>=0)
            {
                return true;
            }
            else
                return false;
        }

        public static boolean isOSMacOS()
        {
            String sOSName = getOSName();

            sOSName = sOSName.toLowerCase();

            int index = sOSName.indexOf("mac os");

            if (index>=0)
                return true;

            return false;
        }
        
        public static BigInteger generateSysId(int pPaddingExponent)
        {
            int iExp = (int)Math.pow(10, pPaddingExponent);//for javascript MAX must be 10 otherwise js rounds erronously
            
            String sSysId = Util.DateTime.GetDateTime_l().toString().substring(2) + 
                            String.valueOf(Util.Randomize.generateRandomNumber(0, iExp));//Javascript side erronously round the number if more than 100 (exceeds the maximum in total size)
            
            String sSysFormatted = Util.Str.rightPad(sSysId, "0", 17);
            
            return new BigInteger(sSysFormatted);//with milliseconds
        }
    }
    
    public static class Geo
    {
        public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
        
        public static BigDecimal calcDistKM(double userLat, double userLng,double venueLat, double venueLng)
        {
            try
            {
                double latDistance = Math.toRadians(userLat - venueLat);
                double lngDistance = Math.toRadians(userLng - venueLng);

                double a =  Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                            + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                            * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                
                BigDecimal bdFinal  = new BigDecimal(AVERAGE_RADIUS_OF_EARTH_KM * c).setScale(8,RoundingMode.CEILING);
                
                return bdFinal;
            }
            catch(Exception e)
            {
                String s = e.getMessage();
                return null;
            }
        }
    }
    
    public static class Str
    {
        static String QUOTE_SIGN = "\"";

        public static String ifEmpty(String pText, String pReplaceWith)
        {
            if(pText==null)
                return pReplaceWith;
            
            if(pText.trim().equals("")==true)
                return pReplaceWith;
            
            return pText;
        }
        
        public static String dequote(String value) 
        {
            return value.replaceAll("^\"|\"$", "");
        }

        public static String QUOTE(String pVal)
        {
            return QUOTE_SIGN + pVal + QUOTE_SIGN;
        }

        public static String SQUOTE(String pVal)//SINGLE QUOTE
        {
            return "'" + pVal + "'";
        }

        
        //Converts start letter of each word to Upper Case
        //The rest to the lower case
        public static String wordNormalize(String pStr)
        {
            String sFinal = "";
            String[] aWords = pStr.split(" ");
            
            for (int i=0; i<aWords.length; i++)
            {
                String sNormalizedWord = "";
                String sTemp = "";
                
                sTemp = aWords[i].toLowerCase();
                if (sTemp.length()>0)
                {
                    sNormalizedWord = sTemp.substring(0, 1).toUpperCase() + sTemp.substring(1);
                }

                if (i!=0)
                    sFinal += " ";
                
                sFinal += sNormalizedWord;
            }
            
            return sFinal;
        }

        //Clean word from special characters 
        public static String wordClean(String pStr, boolean pbCleanDigits)
        {
            String alphaAndDigits = "";

            if (pbCleanDigits==true)
                alphaAndDigits = pStr.replaceAll("[^a-zA-Z]+","");
            else
                alphaAndDigits = pStr.replaceAll("[^a-zA-Z0-9]+","");

            return alphaAndDigits;
        }
        
        public static Object ifNull(Object pVal, String pFill)
        {
            if(pVal==null)
                return pFill;

            return pVal;
        }
        
        public static String isNullorEmpty(String pStr, String psFill)
        {
            //if (StringUtils.isNullOrEmpty(pStr)==true)
            if ((pStr == null) || (pStr.trim().length()==0))
            {
                pStr = psFill;
            }
            
            return pStr;
        }
        
        //also is amount
        public static boolean isNumeric(String pStr)
        {
            /*
            for (char c : pStr.toCharArray())
            {
                if (!Character.isDigit(c)) return false;
            }
            return true;
            */
            if (pStr == null || pStr.isBlank()) 
                return false;
            
            try {
                Double.parseDouble(pStr);
                return true;
            } 
            catch (NumberFormatException e) 
            {
                return false;
            }
        }
        
        //must have Dot sign otherwise use isNumeric
        public static boolean isAmount(String pStr)
        {
            boolean bDotFound = false;
            int i=0;
            for (char c : pStr.trim().toCharArray())
            {
                
                if (!Character.isDigit(c)) 
                {
                    if ((c=='.') || (c==',') || ((c=='-') && (i==0)) )//minus in first position
                    {
                        if (c=='.')
                        {
                            bDotFound = true;
                        }
                        
                        continue;
                    }
                    else
                        return false;
                }
                i++;
            }
            
            if (bDotFound==true)
                return true;
            else
                return false;

        }

        //Shows pattern match result between two strings
        public static int SIMIL(String psBaseStr, String psTargetStr)
        {
            //---------------------------------------------
            //EDIT: EB - March 10, 2018 
            // I came accross the problem when split function was trying to split 
            // #1 "LER (AUTOMOTIVE)" and #2 " ("
            // The reason was ( sign was propriatery for regex and therefore throwing exception
            // http://www.dreamincode.net/forums/topic/281985-unclosed-group-near-index-error-when-using-stringsplit/
            //-----------------------------------------

            String sBaseStr   = psBaseStr.replaceAll("[^a-zA-Z0-9]", "");
            String sTargetStr = psTargetStr.replaceAll("[^a-zA-Z0-9]", "");

            Simil patternMatch = new Simil(sBaseStr);

            return patternMatch.getSimilarityInPercentFor(sTargetStr);
        }

        public static String rightPad(String psSource, String psPadChar, int piMaxLen)
        {
            String sPadding = "";
            
            for (int i=0; i<( piMaxLen - psSource.trim().length() ); i++)
            {
                sPadding += psPadChar;
            }

            return (psSource + sPadding);
        }
        
        public static String lcaseButFirst(String psSource)
        {
            String sNewSource = "";
            
            if (psSource!=null)
            {
                if (psSource.length()>0)
                {
                    sNewSource = psSource.substring(0,1).toUpperCase() + psSource.substring(1).toLowerCase();
                }
            }
            
            return sNewSource;
        }
        
        public static String lcaseButFirst_EveryWord(String psSource)
        {
            String sNewSource = "";
            
            if (psSource!=null)
            {
                if (psSource.length()>0)
                {
                    String[] sSourceParts = psSource.split(" ");
                    for (String sPart: sSourceParts)
                    {
                        sPart = lcaseButFirst(sPart);
                        
                        if (sNewSource.length()==0)
                            sNewSource = sPart;
                        else                            
                            sNewSource += " " + sPart;
                    }
                }
            }
            return sNewSource;
        }
        
        public static String leftPad(String psSource, String psPadChar, int piMaxLen)
        {
            String sPadding = "";
            
            for (int i=0; i<( piMaxLen - psSource.trim().length() ); i++)
            {
                sPadding += psPadChar;
            }

            return (sPadding + psSource);
        }

    }            

    public static class Types
    {
        public static Integer Generic2SQLTypes(Class pClassType)
        {
            if ((pClassType==int.class) || (pClassType==Integer.class))
            {
                return java.sql.Types.INTEGER;
            }
            else if (pClassType==BigInteger.class) 
            {
                return java.sql.Types.BIGINT;
            }
            else if ((pClassType==long.class) || (pClassType==Long.class))
            {
                return java.sql.Types.BIGINT;
            }
            else if ((pClassType==BigDecimal.class) || (pClassType==BigDecimal.class))
            {
                return java.sql.Types.DECIMAL;
            }
            else if (pClassType==String.class)
            {
                return java.sql.Types.VARCHAR;
            }
            else if (pClassType==Date.class)
            {
                return java.sql.Types.DATE;
            }
            else if ((pClassType==Short.class) || (pClassType==short.class))
            {
                return java.sql.Types.INTEGER;
            }
            else if ((pClassType==Byte.class) || (pClassType==byte.class))
            {
                return java.sql.Types.BIT;
            }
            
            return null;
            
        }
        
        
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                          SubClass Format
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Format
    {
        public static String maskDigits(String input, int digitsToMask, boolean maskFromLeft, char maskChar) 
        {
            if (input == null || input.length() == 0 || digitsToMask <= 0) 
            {
                return input;
            }

            int length = input.length();
            StringBuilder maskedString = new StringBuilder();

            if (maskFromLeft) 
            {
                // Mask digits from the left
                int endIndex = Math.min(digitsToMask, length);
                for (int i = 0; i < endIndex; i++) 
                {
                    maskedString.append(maskChar);
                }
                maskedString.append(input.substring(endIndex));
            } 
            else 
            {
                // Mask digits from the right
                int startIndex = Math.max(0, length - digitsToMask);
                maskedString.append(input.substring(0, startIndex));
                for (int i = startIndex; i < length; i++) 
                {
                    maskedString.append(maskChar);
                }
            }

            return maskedString.toString();
        }
        
        public static String replaceTurkishCharsWEnglish(String pStr)
        {
            return pStr
                    .replaceAll("ç", "c")
                    .replaceAll("Ç", "C")
                    .replaceAll("ğ", "g")
                    .replaceAll("Ğ", "G")
                    .replaceAll("ı", "i")
                    .replaceAll("İ", "I")
                    .replaceAll("ö", "o")
                    .replaceAll("Ö", "O")
                    .replaceAll("ş", "s")
                    .replaceAll("Ş", "S")
                    .replaceAll("ü", "u")
                    .replaceAll("Ü", "U");
        }

        public static int toInt(String psIntVal)
        {
            return Integer.parseInt(psIntVal);
        }

        public static long toLong(String psIntVal)
        {
            return Long.parseLong(psIntVal);
        }

        public static String toStr(Date pDateVal, String pDTimeFormat)
        {
            //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            DateFormat df = new SimpleDateFormat(pDTimeFormat);
            
            String sDate = df.format(pDateVal);
            
            return sDate;
        }

        public static Long toLong(Date pDateVal, String pDTimeFormat)
        {
            //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            DateFormat df = new SimpleDateFormat(pDTimeFormat);
            
            String sDate = df.format(pDateVal);
            
            return Util.Format.toLong(sDate);
        }
        
        public static String toStr(int piIntVal)
        {
            return Integer.toString(piIntVal);
        }

        public static String toStr(long pLongVal)
        {
            return Long.toString(pLongVal);
        }

        public static byte[] hexToBytes(String hex) 
        {
            int len = hex.length();
            byte[] result = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                     + Character.digit(hex.charAt(i+1), 16));
            }
            return result;
        }
        
        //toDate should be different        
        public static String Str2Date(String pStr, String pFormat)
        {
            String sFormatted = "";
            int    index = 0;
            
            if (pFormat.equals("####-##-## ##:##:##")==true)
            {
                int strLen = pStr.length();
                if (strLen>=14)
                {
                    sFormatted = pStr.substring(0, 4);//YYYY
                    sFormatted += "-";
                    index = 4;
                }
                else
                {
                    sFormatted = pStr;
                    return sFormatted;
                }

           }
           else if (pFormat.equals("##-##-## ##:##:##")==true)
           {
               int strLen = pStr.length();
               if (strLen>=12)
               {
                    sFormatted = pStr.substring(0, 2);//YY
                    sFormatted += "-";
                    index = 2;
               }
               else
               {
                   sFormatted = pStr;
                   return sFormatted;
               }

           }
           else
           {
               sFormatted = pStr;
               return sFormatted;
           }
           
           sFormatted += pStr.substring(index, index+2);//MM
           sFormatted += "-";
           index+=2;
           
           sFormatted += pStr.substring(index, index+2);//DD
           sFormatted += " ";
           index+=2;
           
           sFormatted += pStr.substring(index, index+2);//HH
           sFormatted += ":";
           index+=2;
           
           sFormatted += pStr.substring(index, index+2);//MI
           sFormatted += ":";
           index+=2;
           
           sFormatted += pStr.substring(index, index+2);//SS
           sFormatted += ":";
           index+=2;
           
           sFormatted += pStr.substring(index);//sss

           return sFormatted;
        }    
        
        // Timeinsecs (125sec converted to 2m) for instance
        public static String Time2Display(long pTimeSec)
        {
            long Min  = 60 * 1;
            long Hour = Min * 60;
            long Day  = Hour * 24;
            long Month = Day * 30;

            if (pTimeSec==-1)
                return "";

            //> month (30)
            if (pTimeSec>Month)
            {
                long iTimes = pTimeSec / Month;
                if (iTimes>1)
                    return Math.round(iTimes) + " months";
                else
                    return Math.round(iTimes) + " month";
            }
            else if(pTimeSec>Day)
            {
                long iTimes = pTimeSec / Day;
                if (iTimes>1)
                    return Math.round(iTimes) + " d";
                else
                    return Math.round(iTimes) + " d";
            }
            else if (pTimeSec>Hour)
            {
                long iTimes = pTimeSec / Hour;

                return Math.round(iTimes) + " h";
            }
            else
            {
                long iTimes = pTimeSec / Min;

                if (iTimes>0)
                    return Math.round(iTimes) + " min";
                else
                    return "now";
            }
        }
        
        //comes with decimal part
        public static String Distance2Display(String psDistance)
        {
            String sDistance = psDistance;
        
            String[] aDistanceParts = sDistance.split("\\.");

            if (aDistanceParts[0].length()>=3)
            {
                //If distance too long ignore decimal
                long lDist1   = Long.parseLong(aDistanceParts[0]);
                Double dDist1 = lDist1/1000D;
                String sDist = Double.toString(Math.round(dDist1 * 10)/10D);   
                return sDist;
                //return aDistanceParts[0];
            }
            else
            {
                return aDistanceParts[0] + "." + aDistanceParts[1].substring(0,1);
            }
        }
        
        //1000 = 1k
        public static String Number2Display(long pNumber)
        {
            String sDisplay = "";
            int iMILLION  = 1000000;
            int HUNDERD_THOUSAND = 100000;
            int TEN_THOUSAND = 10000;
            int iTHOUSAND = 1000;
            int iHUNDRED  = 100;
            
            if (pNumber>iMILLION)
            {
                long iTimes = pNumber / iMILLION;
                long iRemainder = pNumber % iMILLION;
                
                long iRemainderShort = iRemainder / HUNDERD_THOUSAND;//100k
                
                if (iRemainderShort>0)
                {
                    sDisplay = iTimes + "." + iRemainderShort + "M";
                }
                else
                {
                    sDisplay = iTimes + "M";
                }
            }
            else if (pNumber>HUNDERD_THOUSAND)
            {
                long iTimes = pNumber / iTHOUSAND;
                long iRemainder = pNumber % iTHOUSAND;
                
                long iRemainderShort = iRemainder / 100;//100k
                if (iRemainderShort>100)
                {
                    sDisplay = iTimes + "." + iRemainderShort + "k";
                }
                else
                {
                    sDisplay = iTimes + "k";
                }
            }
            else if (pNumber>iTHOUSAND)
            {
                long iTimes = pNumber / iTHOUSAND;
                long iRemainder = pNumber % iTHOUSAND;
                
                long iRemainderShort = iRemainder / 100;//100k
                if (iRemainderShort>0)
                {
                    sDisplay = iTimes + "." + iRemainderShort + "k";
                }
                else
                {
                    sDisplay = iTimes + "k";
                }
            }
            else
            {
                sDisplay = Long.toString(pNumber);
            }
            
            return sDisplay;
        }
    }

    // This library use Hikari
    public static class Database2
    {
        public static HikariDataSource connect(String psDBHostName, 
                                               String psUserName, 
                                               String psPassword,
                                               int    pMaxPoolSize) throws Exception
        {
            try
            {
                HikariDataSource dataSource;

                // Configure HikariCP
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(psDBHostName); // "jdbc:mysql://localhost:3306/your_database"
                config.setUsername(psUserName); // Replace with your username
                config.setPassword(psPassword); // Replace with your password
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");

                config.setMaximumPoolSize(pMaxPoolSize); // Set maximum pool size
                if(pMaxPoolSize>2)
                    config.setMinimumIdle(pMaxPoolSize/2); // Set minimum idle connections
                else
                    config.setMinimumIdle(1); // Set minimum idle connections

                //config.setMinimumIdle(pMaxPoolSize); // Set minimum idle connections 
                config.setIdleTimeout(30000); // Idle timeout in milliseconds
                config.setMaxLifetime(1800000); // Max lifetime of a connection in milliseconds
                config.setConnectionTimeout(5000); // milliseconds — how long to WAIT for a connection to become available from the pool
                //config.addDa
                
                dataSource = new HikariDataSource(config);

                return dataSource;
            }
            catch(Throwable  t)
            {
                t.printStackTrace();
                throw new Exception(t.getMessage());
            }
            /*
            catch(Exception e)
            {
                String s = e.getMessage();
                throw e;
            }*/
        }

        public static Connection getConnection(HikariDataSource pDataSource) throws Exception 
        {
            return pDataSource.getConnection();
        }

        public static void close(HikariDataSource pDataSource) 
        {
            if (pDataSource != null) 
            {
                pDataSource.close();
            }
        }
    }
    
    // This will be depreicated 
    public static class Database
    {
        public static Connection connect(String psDBHostName, String psUserName, String psPassword) throws Exception
        {
            return Database.connect(psDBHostName, psUserName, psPassword, false);
        }
        
        public static Connection connect(String psDBHostName, 
                                         String psUserName, 
                                         String psPassword, 
                                         boolean pbThrowException) throws Exception
        {
            try
            {
                //Class.forName("com.mysql.jdbc.Driver");
                Class.forName("com.mysql.cj.jdbc.Driver");

                DriverManager.setLoginTimeout(3);

                Connection con = DriverManager.getConnection(psDBHostName, psUserName, psPassword);

                return con;
            }
            catch(Exception e)
            {
                if(pbThrowException==false)
                    return null;
                
                throw e;
            }
        }
        
        @Deprecated
        public static Connection ConnectDB( String psDBHostName, 
                                            String psUserName, 
                                            String psPassword, 
                                            int piPort, 
                                            String psDbName)
        {
            try
            {
                MysqlDataSource dataSource = new MysqlDataSource();
                dataSource.setUser(psUserName);
                dataSource.setPassword(psPassword);
                dataSource.setPort(piPort);
                dataSource.setDatabaseName(psDbName);
                dataSource.setServerName(psDBHostName);

                Connection conn = dataSource.getConnection();
                //conn.close();
                return conn;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        public static PreparedStatement setStatement(Connection pCon, String psStmt) throws Exception
        {
            try
            {
                return pCon.prepareStatement(psStmt);
            }
            catch(Exception e)
            {
                throw e;
            }
        }

        //INSERT / UPDATE
        public static int execute(PreparedStatement pStmt) throws Exception
        {
            try
            {
                return pStmt.executeUpdate();
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        //SELECT / SP / FUNCTION
        public static ResultSet execute(PreparedStatement pStmt, boolean pbSP) throws Exception
        {
            try
            {
                return pStmt.executeQuery();
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        public static boolean nextRow(ResultSet pRs)
        {
            try
            {
                return pRs.next();
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        public static void closeCursor(ResultSet pRs)
        {
            try
            {
                pRs.close();
            }
            catch(Exception e)
            {
                
            }
        }
        
        public static boolean close(Connection pCon)
        {
            try
            {
                pCon.close();
                
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        public static Object getColumn(ResultSet pRs, String pColName) throws Exception
        {
            try
            {
                ResultSetMetaData rsmd = pRs.getMetaData();

                String sColName = "";
                Object oVal;
                for (int CIndex=1; CIndex<rsmd.getColumnCount()+1;CIndex++)
                {
                    sColName = rsmd.getColumnName(CIndex);
                    oVal     = pRs.getObject(CIndex); 
                    
                    if (sColName.toLowerCase().equals(pColName.toLowerCase())==true)
                        return oVal;
                }
                
                return null;
            }
            catch(Exception e)
            {
                throw e;
            }
        }

        //CallableStatement (sp) extens PreparedStatement
        public static void setParameter(PreparedStatement pStmt, int pIndex, String psVal) throws Exception
        {
            try
            {
                //if NULL
                //pStmt.setNull(pIndex, java.sql.Types.);

                pStmt.setString(pIndex, psVal);
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        public static void setParameter(PreparedStatement pStmt, int pIndex, BigDecimal pdVal) throws Exception
        {
            try
            {
                //if NULL
                //pStmt.setNull(pIndex, java.sql.Types.);
                pStmt.setBigDecimal(pIndex, pdVal);
                
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        public static void setParameter(PreparedStatement pStmt, int pIndex, long plVal) throws Exception
        {
            try
            {
                //if NULL
                //pStmt.setNull(pIndex, java.sql.Types.);

                pStmt.setLong(pIndex, plVal);
            }
            catch(Exception e)
            {
                throw e;
            }
        }

        public static void setParameter(PreparedStatement pStmt, int pIndex, int piVal) throws Exception
        {
            try
            {
                //if NULL
                //pStmt.setNull(pIndex, java.sql.Types.);

                pStmt.setInt(pIndex, piVal);
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        public static RowColumn getColumn(List<RowColumn> pRow, String psColumnName, Object pValIfNull)
        {
            
            for (RowColumn RowCol: pRow)
            {
                if (RowCol.Name.toUpperCase().trim().equals(psColumnName.trim().toUpperCase())==true)
                {
                    return RowCol;
                }
            }
            
            if (pValIfNull!=null)
            {
                RowColumn NullVal = new RowColumn();
                NullVal.Name = "null";
                NullVal.Val  = pValIfNull;
                return NullVal;
            }

            return null;
        }

        public static ssoDBRowLimits calculateRowLimits(int pRowPerPage, int pPageIndex)
        {
            ssoDBRowLimits limits = new ssoDBRowLimits();
            
            int iRowPerPage = pRowPerPage;//25;//DEFAULT
            int iPageIndex  = pPageIndex;
            int iOffset     = iPageIndex *  iRowPerPage;//skip first N records
            int iLimit      = pRowPerPage;//(iPageIndex+1) * iRowPerPage;//50 per page

            limits.limit  = iLimit;
            limits.offset = iOffset;

            return limits;
        }

        public static String getValString(List<RowColumn> pRow, String pColName)
        {
            for (RowColumn RowN:pRow)
            {
                if (RowN.Name.toLowerCase().equals(pColName.toLowerCase())==true)
                {
                    if (RowN.Val==null)
                        return "";
                    else
                        return RowN.Val.toString();
                }
            }

            return "";
        }
        
        public static Object getVal(List<RowColumn> pRow, String pColName)
        {
            for (RowColumn RowN:pRow)
            {
                if (RowN.Name.toLowerCase().equals(pColName.toLowerCase())==true)
                {
                    if (RowN.Val==null)
                        return null;
                    else
                        return RowN.Val;
                }
            }

            return null;
        }

    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                          SubClass DateTime
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class DateTime
    {
        public static String getMonthName(String pMonthNo)
        {
            int iMonthNo = Integer.parseInt(pMonthNo);
            switch(iMonthNo)
            {
                case 1:
                    return "january";
                case 2:
                    return "february";
                case 3:
                    return "march";
                case 4:
                    return "april";
                case 5:
                    return "may";
                case 6:
                    return "june";
                case 7:
                    return "july";
                case 8:
                    return "august";
                case 9:
                    return "september";
                case 10:
                    return "october";
                case 11:
                    return "november";
                case 12:
                    return "december";
            }
            
            return "";
        }
        
        //jar file needed under global/datetime
        public static String subtractDays(Date pDate, int pDayLen, String pFormat)
        {
            String sFormat = "yyyyMMdd";//default
            if (pFormat.trim().length()>0)
                pFormat = sFormat;

            //Date date = new Date(); // Or where ever you get it from
            Date daysAgo = new org.joda.time.DateTime(pDate).minusDays(pDayLen).toDate();
            DateFormat dateFormat = new SimpleDateFormat(sFormat);
            String strDate = dateFormat.format(daysAgo);

            return strDate;
        }
        
        public static String subtractYears(String pDate, int pYearLen, String pFormat)
        {
            // Define the formatter for the input and output format
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pFormat);

            // Parse the given date to a LocalDate object
            LocalDate date = LocalDate.parse(pDate, formatter);

            // Subtract one year from the parsed date
            LocalDate dateOneYearAgo = date.minusYears(1);

            // Format the result back to YYYYMMDD
            String result = dateOneYearAgo.format(formatter);
            
            return result;
        }

        //pDate = yyyyMMddHHmmssS
        public static Integer getDayOfYear(String pDate)
        {
            Date dtInput = new Date();
            dtInput = Str2Date(pDate);
            
            org.joda.time.DateTime dtFinal = new org.joda.time.DateTime(dtInput);
            
            return dtFinal.getDayOfYear();
        }
        
        public static int getDayOfYear(String pDate, String psFormat)
        {
            Date dtInput = new Date();
            dtInput = Str2Date(pDate, psFormat);
            
            org.joda.time.DateTime dtFinal = new org.joda.time.DateTime(dtInput);
            
            return dtFinal.getDayOfYear();
        }
        
        public static int getDifferenceInDays(String pDate1, String pDate2)
        {
            Date dtInput1 = new Date();
            dtInput1 = Str2Date(pDate1);
            org.joda.time.DateTime dtDate1 = new org.joda.time.DateTime(dtInput1);

            Date dtInput2 = new Date();
            dtInput2 = Str2Date(pDate2);
            org.joda.time.DateTime dtDate2 = new org.joda.time.DateTime(dtInput2);
            
            int year1 = dtDate1.getYear();
            int year2 = dtDate2.getYear();
            
            int day1 = dtDate1.getDayOfYear();
            int day2 = dtDate2.getDayOfYear();

            return ((year2 - year1) * 365) + (day2 - day1);
        }
        
        // pDateType = d - day, M - month, s - seconds, h - hours, m - minutes
        public static int convert2Seconds(String pDateType, int pLength)
        {
            if (pDateType.trim().equals("d")==true)//day
            {
                return pLength * 24 * 60 * 60;
            }
            else if (pDateType.trim().equals("h")==true)//minute
            {
                return pLength * 60 * 60;
            }
            else if (pDateType.trim().equals("m")==true)//minute
            {
                return pLength * 60;
            }
            else if (pDateType.trim().equals("s")==true)//seconds
            {
                return pLength;
            }
            
            return -1;
        }

        public static Long GetDateTime_l_wo_MSeconds()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }
        
        // forexample
        // "yyyyMMdd" input format
        // output "yyyy-MM-dd"
        public static String formatDate(String pDate, String pInputFormat, String pOutputFormat)
        {
            SimpleDateFormat inputFormat = new SimpleDateFormat(pInputFormat);
            SimpleDateFormat outputFormat = new SimpleDateFormat(pOutputFormat);

            try 
            {
                // Parse the input date string
                Date date = inputFormat.parse(pDate);

                // Format the date into the desired format
                String formattedDate = outputFormat.format(date);
                //System.out.println("Formatted Date: " + formattedDate);
                
                return formattedDate;
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                return "";
            }
        }
        
        //For example:
        // pFormat = yyyyMMdd
        public static Long GetDateTime(String pFormat)
        {
            
            SimpleDateFormat    DFormat = new SimpleDateFormat(pFormat);

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);

            Long    lDateNow            = Long.parseLong(sDateNow.substring(0, pFormat.length()));

            return  lDateNow;
        }

        //Min: YYMMDDHHmmSS
        public static Long difference(String pDate1, String pDate2)
        {
            String sDate1 = "000000000000" + pDate1;
            String sDate2 = "000000000000" + pDate2;
            
            Long lDate1   = Util.Format.toLong(sDate1.substring(sDate1.length()-12, sDate1.length()));
            Long lDate2   = Util.Format.toLong(sDate2.substring(sDate2.length()-12, sDate2.length()));
            
            return (lDate1 - lDate2);
        }

        public static Long differenceInSeconds(String pFormat, long pDate1, long pDate2)
        {
            return differenceInSeconds( pFormat, Long.toString(pDate1), Long.toString(pDate2) );
        }

        public static Long differenceInSeconds(String pFormat, String pDate1, String pDate2)
        {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");

            try 
            {
                SimpleDateFormat sdf = new SimpleDateFormat(pFormat);

                // Parse the date strings
                Date parsedDate1 = sdf.parse(pDate1);
                Date parsedDate2 = sdf.parse(pDate2);

                // Calculate the difference in milliseconds
                long differenceInMillis = Math.abs(parsedDate2.getTime() - parsedDate1.getTime());

                // Convert milliseconds to seconds
                long differenceInSeconds = differenceInMillis / 1000;

                return differenceInSeconds;
                //System.out.println("Difference in seconds: " + differenceInSeconds);
            } 
            catch (Exception e)
            {
                e.printStackTrace();
                
                return (long)-1;
            }

        }
        
        public static Long GetDateTime_l_w_YY()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYMMddHHmmssSSS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }

        public static Long GetDateTime_l()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }

        // Forexample; pFormat = YYYYMMdd
        public static Long GetDateTime_l(String pFormat)
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat(pFormat);

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }

        public static String GetDateTime_s(String psFormat)
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat(psFormat);

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            //Long    lDateNow            = Long.parseLong(sDateNow);

            return  sDateNow;
        }

        public static Integer getYear(int pMinusYear)
        {
            return Util.DateTime.getYear() - 1;
        }
        
        public static Integer getYear()
        {
            int iCurrentYear = LocalDate.now().getYear();
            
            return iCurrentYear;
        }
        
        public static long GetDate()
        {
            return GetDateTime_l("YYYYMMdd");
        }

        public static int getCurrentYear()
        {
            return Year.now().getValue();
        }
        
        public static int getCurrentMonth()
        {
            LocalDate currentDate = LocalDate.now();
            
            int iMonth = currentDate.getMonthValue();
            
            return iMonth;
        }
        
        public static int getCurrentDay()
        {
            LocalDate currentDate = LocalDate.now();
            
            int currentDay = currentDate.getDayOfMonth();
            
            return currentDay;
        }
        
        public static String GetDateTime_s()
        {
            return GetDateTime_s("yyyyMMddHHmmssSSS");
            /*
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYYYMMddHHmmssS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  sDateNow;
            */
        }

        public static String Date2Str(Date pDate, String pFormat)
        {
            //Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat(pFormat);

            return dateFormat.format(pDate);
        }

        public static boolean isFormatCorrect(String psDate, String psFormat)
        {
            try
            {
                Date dt = Str2Date(psDate, psFormat);
                
                if (dt==null)
                    return false;
                else
                    return true;
            }
            catch(Exception e)
            {
                return false;
            }
            
        }
        
        public static Date Str2Date(String psDate)
        {
            try
            {
                String sDate = "";
                if (psDate.length()<=8)
                {
                    sDate = Util.Str.rightPad(psDate, "0", 15);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                return formatter.parse(sDate);
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static Date Str2Date(String psDate, String pFormat)
        {
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat(pFormat);
                formatter.setLenient(false);

                return formatter.parse(psDate);
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static long ms2Seconds(long plMS)
        {
            return plMS/1000;
        }
        
        public static long ms2Minutes(long plMS)
        {
            return plMS/(1000*60);//seconds x 60
        }
        
        public static long ms2Hours(long plMS)
        {
            return plMS/(1000*60*60);//seconds x 60 = min x 60 = hour
        }

        public static long ms2Days(long plMS)
        {
            return plMS/(1000*60*60*24);//seconds x 60 = min x 60 = hour x 24 = day
        }
        
        public static boolean isDateValidYYYYMMDD(String pDateYYYYMMDD)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            try 
            {
                // Try to parse the date
                LocalDate parsedDate = LocalDate.parse(pDateYYYYMMDD, formatter);
                
                // Manually validate day range based on the parsed date
                
                //int day = parsedDate.getDayOfMonth();
                int day = Integer.parseInt(pDateYYYYMMDD.substring(6,8));
                int maxDayOfMonth = parsedDate.lengthOfMonth();

                if (day > maxDayOfMonth) 
                {
                    return false;  // Invalid day for the given month
                }
                
                return true;  // If successful, the date is valid
            } 
            catch (DateTimeParseException e) 
            {
                return false; // If exception occurs, it's an invalid date
            }
        }

    }

    /*
    String from = "eshabil_bulbul@yahoo.com";
    String to   = "esabil@shipshuk.com";
    String subject = "Test Activation";
    String message = "Test Activation";
    String pwd     = "Ayse12345";
    */
        
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass JSON
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class HTTP
    {
        //make sure there is no SQL injection
        public static boolean isParamSafe(String pParam)
        {
            //if (pParam.contains("'")==true)//remove this don't add
            //    return false;
            
            if (pParam.indexOf("INSERT")>=0)
                return false;
            
            if (pParam.indexOf("TRUNCATE")>=0)
                return false;
            
            if (pParam.indexOf("DELETE")>=0)
                return false;
            
            return true;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //                              WARNING
        // In order to use this function on mother project
        // the mother project should have the following libraries as well
        // commons-logging-1.2.jar
        // httpclient-4.5.2.jar
        // httpcore-4.4.4.jar
        // java-json.jar
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static JSONObject sendGET(String pURLMsg)
        {
            try
            {
                CloseableHttpClient httpClient;
                httpClient = HttpClients.createDefault();

                HttpGet get = new HttpGet(pURLMsg);

                CloseableHttpResponse response =  httpClient.execute(get);

                HttpEntity entity = response.getEntity();

                JSONObject HTTPresponse = new JSONObject(EntityUtils.toString(entity));

                return HTTPresponse;
            }

            catch (ClientProtocolException e) 
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return null;
            } 
            catch (IOException e) 
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return null;
            } 
            catch (ParseException e) 
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return null;
            } 
            catch (JSONException e) 
            {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return null;
            }
            
        }
        
        public static String sendHTTPRequest(String pMethod, String pURL, String pParameterName, String pParameterVal)
        {
            String sMSG = pParameterName + "=" + pParameterVal;
            
            return sendHTTPRequest(pMethod, pURL, sMSG);
        }
        
        public static String addMessageParam(String psMsg, String psParamName, String psParamVal)
        {
            if (psMsg.length()>0)
                psMsg += "&";
            
            psMsg += psParamName + "=" + psParamVal;
            
            return psMsg;
        }
        
        /*
        public static String fixString4HTTP(String psStr)
        {
            String sStr1 = psStr.replace("&", "and");
            return sStr1.replace("%", "*");
        }
        */
        public static String fixString4HTTP(String psStr)
        {
            //String sStr1 = psStr.replaceAll("&", "%26");//replace with ## and convert it back on the server side for instance H R or Max&Co data will be broken otherwise
            String sStr1 = psStr.replaceAll("  ", " ");//2 spaces to 1 space
            sStr1 = sStr1.replaceAll("   ", " ");//3 spaces to 1 space
            sStr1 = sStr1.replaceAll("#", "");
            sStr1 = sStr1.replaceAll("\"", "`");
            sStr1 = sStr1.replaceAll("\\r", "&");
            sStr1 = sStr1.replaceAll("\\n", "&");
            sStr1 = sStr1.replaceAll("%", "%25");

            sStr1 = sStr1.replaceAll("&", "%26");
            return sStr1;//
        }

        public static int getURLSize(String psURL)
        {
            try
            {
                URL url = new URL(psURL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int iContentSize = connection.getContentLength();

                connection.getInputStream().close();;

                return iContentSize;
            }
            catch(Exception e)
            {
                return -1;
            }
        }
        
        // String sResp = Util.HTTP.sendHTTPRequest("POST", sURL, sMsg);
        public static String sendHTTPRequest(String pMethod, String pURL, String pMsg)
        {
            try
            {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //
                // WARNING: The following code has replaced the code commented its below
                // 
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                if (pMethod.toUpperCase().equals("GET")==true)
                {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(pURL))
                            .build();

                    HttpResponse<String> response = client.send(request,
                            HttpResponse.BodyHandlers.ofString());

                    //System.out.println(response.body());
                    return response.body();

                }
                else//post : check recaptcha uses this following flow 
                {
                    
                    //URL url = new URL("https://test.payeco.com:9443/DnaOnline/servlet/DnaPayB2C");
                    URL url = new URL(pURL);

                    // instantiate the HttpURLConnection with the URL object - A new`
                    // connection is opened every time by calling the openConnection
                    // method of the protocol handler for this URL.
                    // 1. This is the point where the connection is opened.
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // set connection output to true
                    connection.setDoOutput(true);

                    // instead of a GET, we're going to send using method="POST"
                    if (pMethod.toUpperCase().equals("GET")==true)
                        connection.setRequestMethod("GET");
                    else
                        connection.setRequestMethod("POST");

                    //String sMSG = "request_text=" + pMsg;
                    //String sMSG = pParameterName + "=" + pParameterVal;
                    String sMSG = pMsg;

                    connection.setRequestProperty("Content-Length", "" + Integer.toString(sMSG.getBytes().length));
                    //connection.setRequestProperty("Content-Language", "en-US");  
                    //connection.setRequestProperty("Content-Language", "zh");            
                    //connection.setRequestProperty("content-type", "text/plain; charset=utf-8");
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
                    // instantiate OutputStreamWriter using the output stream, returned
                    // from getOutputStream, that writes to this connection.
                    // 2. This is the point where you'll know if the connection was
                    // successfully established. If an I/O error occurs while creating
                    // the output stream, you'll see an IOException.
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                    if (sMSG.trim().length()>0)
                        writer.write(sMSG);

                    // Closes this output stream and releases any system resources
                    // associated with this stream. At this point, we've sent all the
                    // data. Only the outputStream is closed at this point, not the
                    // actual connection
                    writer.close();
                    // if there is a response code AND that response code is 200 OK, do
                    // stuff in the first if block
                    int RespCode = connection.getResponseCode(); // == HttpURLConnection.HTTP_OK)        

                    BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) 
                    {
                        response.append(inputLine);
                    }
                    in.close();

                    return response.toString();
                }
            }

            catch(Exception e)
            {
                int i=0;
                return null;
            }
        }
        
        public static boolean sendRecieveMsg(String pSendType, String psURL, String psMsg)
        {
            String sResponse = Util.HTTP.sendHTTPRequest(pSendType, psURL, psMsg);

            if ((sResponse.equals("")==true) || (sResponse.equals("\"\"")==true))//for json responses and string
                return true;
            else
                return false;
        }
        
        public static String decodeURIComponent(String pData)
        {
            try
            {
                return java.net.URLDecoder.decode(pData, "UTF-8");
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static String encodeURIComponent(String pData)
        {
            try
            {
                return java.net.URLEncoder.encode(pData, "UTF-8");
                //return java.net.URLDecoder.decode(pData, "UTF-8");
            }
            catch(Exception e)
            {
                return null;
            }
        }

    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          Callback 
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Callback
    {
        public static Object getParamValue(ArrayList<ssoCallbackParam> paParams, String pName)
        {
            try
           {
                int iLen = paParams.size();

                for (int i=0; i<iLen; i++)
                {
                    ssoCallbackParam prm = new ssoCallbackParam();
                    prm = paParams.get(i);
                    if (prm.name.toLowerCase().equals(pName)==true)
                    {
                        return prm.val;
                    }
                }

                return null;
            }
            catch(Exception e)
            {
                String msg = e.getMessage();
                
                return null;
            }
        }
    }
    
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass JSON
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class JSON
    {
        public static String getField(JSONObject pJSObj, String pFieldName)
        {
            try
            {
                Object fld = pJSObj.get(pFieldName);
                
                if (fld instanceof Integer)
                {
                    return Integer.toString((int)fld);
                }
                else if (fld instanceof Long)
                {
                    return Long.toString((long)fld);
                }
                else if (fld instanceof Double)
                {
                    return Double.toString((double)fld);
                }
                else
                {
                    return pJSObj.getString(pFieldName);
                }
            }
            catch(Exception e)
            {
                return "";
            }
        }
        
        public static String SourceSeperated2JSON(String psSeperator, String psSourceSeperated)
        {
            String   psPostIdsOnBrowser = psSourceSeperated;
            String   sPostsOnBrowser  = "";            
            String[] saPostsOnBrowser = psPostIdsOnBrowser.split(psSeperator);
        
            sPostsOnBrowser = "{";
            for (int index=0; index<saPostsOnBrowser.length; index++)
            {
                if (index!=0)
                {
                    sPostsOnBrowser += ",";
                }

                sPostsOnBrowser += "\"" + saPostsOnBrowser[index] + "\"" + ":" + "\"F\""; 
            }
            sPostsOnBrowser += "}";
            
            return sPostsOnBrowser;
        }
        
        public static Iterator<JsonNode> disolveArrayValues(String pjsonVals)
        {
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(pjsonVals);
                Iterator<JsonNode> iterator = root.getElements();
                return iterator;
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static Iterator<String> disolveArrayKeys(String pjsonVals)
        {
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(pjsonVals);
                Iterator<String> iterator = root.getFieldNames();
                return iterator;
            }
            catch(Exception e)
            {
                return null;
            }

        }

        public static Set<String> keys(JsonObject pJSO)
        {
            return pJSO.keySet();
        }

        public static String getValue(JsonObject pjsObj, String pKey)
        {
            try
            {
                //return pjsObj.get(pKey).toString();
                return pjsObj.get(pKey).getAsString();
            }
            catch(Exception e)
            {
                return "";
            }
        }
        
        public static boolean isEndofArray(Iterator<JsonNode> pjRoot)
        {
            if (pjRoot.hasNext()==true)
                return false;
            else
                return true;
        }
        
        public static JsonNode getNextArrayItem(Iterator<JsonNode> pjRoot)
        {
            return pjRoot.next();
        }
        
        public static JsonObject toJsonObject(String psStr)
        {
            try
            {
                JsonParser parser = new JsonParser();
                return parser.parse(psStr).getAsJsonObject();
                //return new JsonObject(psStr);
            }
            catch(Exception e)
            {
                return new JsonObject();
            }
        }
        
        public static ArrayList<String> getKeys(String pjsString)
        {
            JsonObject jsOptAll = Util.JSON.toJsonObject(pjsString);
            Set<String> hKeys = Util.JSON.keys(jsOptAll);
            ArrayList<String> aKeys = new ArrayList<>(hKeys);
            if(aKeys.size()==0)
                aKeys.add("");
            return aKeys;
        }

        /*
            JAVA-JSON.jar / org.json
            JSONObject obj = new JSONObject(jsonString);
            String name = obj.getString("name");
        */
        public static JSONObject toObject(String psObj)
        {
            try
            {
                JSONObject obj = new JSONObject(psObj);

                return obj;
            }
            catch(Exception e)
            {
                return new JSONObject();
            }
        }

        public static Object toObject(String psStr, Class<?> pClass)
        {
            try
            {
                return Convert2Obj(psStr, pClass);
                
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static Object toObjectWithJackson(String psStr, Class<?> pClass)
        {
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(psStr, pClass);

                return obj;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        public static String toString(ArrayList<String> paList)
        {
            //return String.join(", ", paList);
            Gson gson = new Gson();
            String json = gson.toJson(paList);
            return json;
        }

        public static String toString(Object pObj)
        {
            return Convert2JSON(pObj);
        }

        public static String toStringWithJackson(Object pObj)
        {
            try
            {
                StringWriter RetJSON = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                mapper.writeValue(RetJSON, pObj);

                return RetJSON.toString();
            }
            catch(Exception e)
            {
                return "";
            }
        }

        /*
            Sample Use:

            JsonArray jsonArray = (JsonArray) Util.JSON.toArray(sPostCodes);

            for (int j=0; j<jsonArray.size();j++)
            {

                JsonObject item = (JsonObject)jsonArray.get(j);

                JsonElement jePostCode  = item.get(KEY_POSTCODE);

            }
        */
        public static JsonArray toArray(String sJSONBuffer)
        {
            try
            {
                /*
                JSONArray jsArray = new JSONArray(sJSONBuffer);
                
                return jsArray;
                */
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = (JsonArray) jsonParser.parse(sJSONBuffer);
                
                return jsonArray;
            }
            catch(Exception e)
            {
                return null;
            }
        }

        // This uses GSON 
        public static boolean isExistInArray(JsonArray pJSArray, String pKey, String pVal)
        {
            try
            {
                for(int i=0;i<pJSArray.size();i++)
                {
                    JsonObject itm = (JsonObject)pJSArray.get(i);

                    JsonElement keyVal = itm.get(pKey);
                    
                    if (keyVal!=null)
                    {
                        if(keyVal.toString().replace("\"", "").equals(pVal)==true)
                            return true;
                    }
                    
                }
                
                return false;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        //public static StringWriter Convert2JSON(Object pObj)
        public static String Convert2JSON(Object pObj)
        {
            try
            {
                Gson gs = new Gson();

                String jsonString = new Gson().toJson(pObj);

                return jsonString;
                //return gs.toJson(pObj);
                /*
                StringWriter RetJSON = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                mapper.writeValue(RetJSON, pObj);

                //Array JSON
                //http://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
                //List<Object> myObjects = mapper.readValue("", new TypeReference<List<Object>>(){});
                        
                return RetJSON;
                */
            }

            catch(Exception e)
            {
                return null;

            }
        }
        
        public static Object Convert2Obj(String psJSON, Class<?> pClass)
        {
            try
            {
                Gson gs = new Gson();
                
                Object obj = gs.fromJson(psJSON, pClass);
                
                return obj;
                /*
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(psJSON, pClass);
                
                return obj;
                */
            }
            
            catch(Exception e)
            {
                return null;

            }
            
        }
        
        public static boolean isJSON(String psJsonStream)
        {
            try
            {
                boolean rc = isJSONObject(psJsonStream);
                if (rc==false)
                {
                    rc = isJSONArray(psJsonStream);
                    if (rc==false)
                        return false;
                }
                
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        public static boolean isJSONObject(String psJsonStream)
        {
            try
            {
                new JSONObject(psJsonStream); 
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        public static boolean isJSONArray(String psJsonStream)
        {
            try
            {
                new JSONArray(psJsonStream);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        public static void parseJSONArray()
        {
            try
            {
                // IMPORTANT 
                // For JSON Array parsing this function WON'T work
                // This is just a placeholder 
                // Use the following example to parse JSON Arrays
                
                // Example
                //test[] jsoArr = gson.fromJson(sMultiArgs, test[].class);


                
                /*
                Gson gson = new Gson();

                Type ListType = new TypeToken<ArrayList<pClass>>(){}.getType();
                return gson.fromJson(psJsonArray, ListType);
                */
                
                //return JSOArray;
            }
            catch(Exception e)
            {
                return ;
            }
        }

        public static org.json.simple.JSONObject parseJSON(String pjsSource)
        {
            try
            {
                
                JSONParser parser   = new JSONParser();
                Object oJSInfo      = (Object)parser.parse(pjsSource);
                org.json.simple.JSONObject jsAddr   = (org.json.simple.JSONObject)oJSInfo;
                
                return jsAddr;
                /*
                for (int i=0;i<jsAddr.size(); i++)
                {
                    sLat = (String)jsAddr.get("lat");
                    sLon = (String)jsAddr.get("lon");
                }
                */
            }
            catch(Exception e)
            {
                return null;
            }
        }

    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          Random
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    
    public static class Randomize
    {
        public static int generateRandomNumber(int min, int max)
        {
            Random rand = new Random();

            //int  n = rand.nextInt(50) + 1;
            int  n = rand.nextInt(max) + 1;
            
            return n;
        }
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass Files
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Files
    {
        public static boolean MoveFile(String pSourcePath, String pDestPath)
        {
            try
            {
                File fileToMove = new File(pSourcePath);

                return fileToMove.renameTo(new File(pDestPath));
            }
            catch(Exception e)
            {
                return false;
            }
        }
        
        public static boolean CopyFile(String pSourcePath, String pDestPath, String pDestFileName) throws Exception 
        {
            try
            {
                Path source = Paths.get(pSourcePath);
                Path targetDir = Paths.get(pDestPath);
                
                java.nio.file.Files.createDirectories(targetDir); // ensures the folder exists, whatever name you use next has no effect on this
                
                Path destination = targetDir.resolve(pDestFileName);
                java.nio.file.Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                
                return true;
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static String GenerateFileName_w_YYMMDDHHM1(String pFileFolder, String pFileName, String pExtension)
        {
            String sFileExtensionYYYYMMDDHH = GetFileNameExtensionYYMMDDHHM1();
            
            //Create File Name
            String sFileName = pFileFolder + File.separator + pFileName + "_" +  sFileExtensionYYYYMMDDHH + "." + pExtension;
            
            return sFileName;
        }
        
        //FileName YYMMDDH1 (00 if between 0-30, 30 if between 30-60)
        public static String GetFileNameExtensionYYMMDDHHM1()
        {
            String YYYYMMDDHH = Util.DateTime.GetDateTime_s().substring(0,10);
            String MinNumber = Util.DateTime.GetDateTime_s().substring(10,11);
            
            if (Long.parseLong(MinNumber)>2)
                return YYYYMMDDHH + "30";//Second half of hour
            else
                return YYYYMMDDHH + "00";//First half of hour

        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static boolean isFileExist(String pFilePath)
        {
            try
            {           
                File varTmpDir = new File(pFilePath);
                boolean exists = varTmpDir.exists();

                return exists;
            }

            catch(Exception e)
            {
                return false;
            }
        }
        
        public static void deleteFile(String pFileName)
        {
            //delete file
            File f = new File(pFileName);
            boolean success = f.delete();
            
        }
        

        /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            
            To Read File
            
            Go to https://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
            
           !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
        public static boolean Write2File(String pFileName, String pData)
        {
            return Write2File(pFileName, pData, true);//by default
        }
        
        public static boolean Write2File(String pFileName, String pData, boolean pbAppend)
        {
            try
            {
                FileWriter  fw = new FileWriter(pFileName, pbAppend);
                fw.write(pData + "\r\n");
                fw.close();

                return true;
            }
            catch(Exception e)
            {
                System.out.println("Exception: " + e.getMessage());
                return false;
            }
        }
        
        public static String ReadAllFile(String psFilePath) throws Exception
        {
            String sContent = "";
            
            String sLine = "";

            FileReader inFile = new  FileReader(psFilePath);
            BufferedReader inStream = new BufferedReader(inFile);

            while ((sLine = inStream.readLine()) != null) 
            {
                /** 
                  Your implementation  
                **/
                sContent += sLine;
            }
            inStream.close();
            
            return sContent;
        }
        
        public static boolean isDirExist(String pDirPath)
        {
            File theDir = new File(pDirPath);
 
            // if the directory does not exist, create it
            return theDir.exists();
        }

        public static void CreateDir(String pDirPath)
        {
            File theDir = new File(pDirPath);

            if (isDirExist(pDirPath)==false)
            {
                //theDir.mkdir();
                theDir.mkdirs();
            }
        }
        /*
        public static void CreateDirs(String pFileName)
        {
            try
            {
                File file = new File(pFileName);
                File parentDir = file.getParentFile();

                if (parentDir != null && !parentDir.exists()) 
                {
                    parentDir.mkdirs(); // Create missing directories
                }
            }
            catch(Exception e)
            {
                String s = e.getMessage();
            }
        }
        */
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass XML
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class XML
    {

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static String ReadXMLElementAttribute(String pFilePath, String pXMLNodName, String pAttName, String pAttVal, String pNextAttName)
        {
            String sVal = "";

            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();        
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document document = builder.parse(new File(pFilePath));
                Element rootElement = document.getDocumentElement();

                sVal = ReadXMLAttribValue(pXMLNodName, pAttName, pAttVal, pNextAttName, rootElement);

                return sVal;
            }

            catch(ParserConfigurationException e)
            {
                return null;
            }

            catch(IOException e)
            {
                return null;
            }

            catch(SAXException e)
            {
                return null;
            }        

        }

        //<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test"/>
        //
        // pNodeName     = property
        // pAttribRefTag = name (Condition 1)
        // pAttRefTagVal = javax.persistence.jdbc.url (Condition 2)
        // pAttribValTag = value
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        static String ReadXMLAttribValue(String pNodeName, String pAttribName, String pAttVal, String pNextAttribName, Element element)
        {

            NodeList list = element.getElementsByTagName(pNodeName);
            if (list != null && list.getLength() > 0)
            {
                for (int i=0;i<list.getLength();i++)
                {
                    Node    Nodx    = list.item(i);
                    Element Elx     = (Element) Nodx;
                    String  AttVal = Elx.getAttribute(pAttribName);

                    if (pAttVal.toUpperCase().equals(AttVal.toUpperCase())==true)
                    {
                        return Elx.getAttribute(pNextAttribName);
                    }
                }
            }

            return null;
        }

        //Function Overload
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static String ReadXMLElementAttribute(String pFilePath, int pNth, String pNodeName, String pAttribName)
        {
            String sVal = "";

            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document document = builder.parse(new File(pFilePath));
                Element rootElement = document.getDocumentElement();

                sVal = Read_Nth_XMLAttribVal(rootElement, pNth, pNodeName, pAttribName);

                return sVal;
            }

            catch(ParserConfigurationException e)
            {
                return null;
            }

            catch(IOException e)
            {
                return null;
            }

            catch(SAXException e)
            {
                return null;
            }        

        }

        //<persistence-unit name="TestWebAppPU" transaction-type="RESOURCE_LOCAL">
        //
        // pNodeName     = persistence-unit (Condition 1)
        // pAttribRefTag = name
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        static String Read_Nth_XMLAttribVal(Element element, int pNth, String pNodeName, String pAttribName)
        {
            int index=0;

            NodeList list = element.getElementsByTagName(pNodeName);
            if (list != null && list.getLength() > 0)
            {
                for (int i=0;i<list.getLength();i++)
                {
                    Node    Nodx    = list.item(i);
                    Element Elx     = (Element) Nodx;
                    String  AttVal = Elx.getAttribute(pAttribName);

                    //if (pAttribRefTag.toUpperCase().equals(AttName.toUpperCase())==true)
                    //{
                        if (index==pNth)
                        {
                            return AttVal;//Elx.getAttribute(pAttribRefTag);
                        }
                    //}
                }
            }

            return null;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static ArrayList<Node> ReadXMLElementValues(String pFilePath, String pNodeName, String pElName, String pAttName )
        {
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();        
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document document = builder.parse(new File(pFilePath));
                Element rootElement = document.getDocumentElement();

                return ReadXMLElements(rootElement, pNodeName, pElName);

            }

            catch(ParserConfigurationException e)
            {
                return null;
            }

            catch(IOException e)
            {
                return null;
            }

            catch(SAXException e)
            {
                return null;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        static ArrayList<Node> ReadXMLElements(Element element, String pNodeName, String pElName)
        {
            ArrayList<Node>  ElList = new ArrayList<Node>();

            NodeList list = element.getElementsByTagName(pNodeName);
            if (list != null && list.getLength() > 0)
            {
                for (int n=0;n<list.getLength();n++)
                {
                    Node    Nodx    = list.item(n);
                    Element Elx     = (Element) Nodx;
                    //for (int i=0;i<list.getLength();i++)
                    for (int i=0;i<Elx.getElementsByTagName(pElName).getLength();i++)
                    {                
                        ElList.add(Elx.getElementsByTagName(pElName).item(i));
                    }
                }
            }

            return ElList;
        }
        
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass Serialazable
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Serialazable
    {
        public static void Serialize(String pFilePath, Object pObj) throws Exception
        {
            try
            {
                //FileOutputStream fileOut = new FileOutputStream("/tmp/employee.ser");
               FileOutputStream fileOut = new FileOutputStream(pFilePath);

                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(pObj);
                out.close();
                fileOut.close();

            }
            catch(IOException e)
            {
                ErrorCode Err = new ErrorCode();
                //Err = ErrorCodes.Persistence.EXCEPTION.exception();
                Err.addDescription(e.getMessage());
                throw Err;
            }        
        }

        public static Object Deserialize(String pFilePath) throws Exception
        {
            Object Obj = null;

            try
            {
                FileInputStream fileIn = new FileInputStream(pFilePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Obj = in.readObject();
                in.close();
                fileIn.close();

                return Obj;
            }

            catch(IOException | ClassNotFoundException e)
            {
                ErrorCode Err = new ErrorCode();
                //Err = ErrorCodes.Persistence.EXCEPTION.exception();
                Err.addDescription(e.getMessage());
                throw Err;
            }       
        }
    }
    

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass Retention
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!    
    public static class Arrays
    {
        // pTwoColArray seperated with '-'
        // This function by default sorts the column in Asc
        // If you want to sort the column in different send the pbCol flags false
        //
        // For example;
        // 0-0
        // 1-1
        // 0-2
        // will be sorted as 0-0, 0-2, 1-1 by default. 
        // 
        // Say we have this 
        // 0-0
        // 0-1
        // 0-2
        // 1-1
        // 1-0
        // and we want to sort it desc for the second col. In this case we choose pCol2 false
        public static String[] BiColumnSort(String[] pTwoColArray, boolean pbCol1Asc, boolean pbCol2Asc)
        {
            //pbCol1Asc = TRUE (Always) - initial version of the method

            //1. Sort First
            //   if pbCol2Asc = false
            //2.  For each 1st column group run another sort 
            //3.  Reverse order of the sort result

            //------------------------------------------------------------------
            //1. Sort 
            //------------------------------------------------------------------
            String[] paSourceArray = new String[pTwoColArray.length];
            String[] paSortedArray = new String[pTwoColArray.length];

            paSourceArray = pTwoColArray;
            
            java.util.Arrays.sort(paSourceArray);

            ArrayList<String> sortedGroup = new ArrayList<String>();
            if (pbCol2Asc==false)
            {
                String prevGroupKey = "";//first col
                ArrayList<String> newGroup = new ArrayList<String>();

                boolean bLastGroupComplete = false;
                for (int i=0; i<paSourceArray.length; i++)
                {
                    String[] aCurParts = paSourceArray[i].split("-");
                    bLastGroupComplete = false;
                    
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    // GROUPS 2nd Col Vals
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if ((prevGroupKey=="") || (prevGroupKey.equals(aCurParts[0])==true))//current group key 
                    {
                        newGroup.add(paSourceArray[i]);
                    }
                    else
                    {
                        bLastGroupComplete = true;
                        
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        //Sort reverse
                        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        for (int j=newGroup.size()-1; j>=0; j--)
                        {
                            sortedGroup.add(newGroup.get(j));
                        }

                        newGroup = new ArrayList<String>();
                        newGroup.add(paSourceArray[i]);
                        
                        if (i==(paSourceArray.length-1))
                            bLastGroupComplete = false;
                    }
                    prevGroupKey = aCurParts[0];
                }
                
                if (bLastGroupComplete==false)
                {
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //Sort reverse
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    for (int j=newGroup.size()-1; j>=0; j--)
                    {
                        sortedGroup.add(newGroup.get(j));
                    }
                }
                
                for(int k=0; k<sortedGroup.size(); k++)
                {
                    paSortedArray[k] = sortedGroup.get(k);
                }
                
            }
            else
            {
                paSortedArray = paSourceArray;
            }
            
            if (pbCol1Asc==false)
            {
                String[] paSortedArrayFinal = new String[pTwoColArray.length];
                int iz=0;
                //DON'T SORT
                for (int z=paSortedArray.length-1;z>=0;z--)
                {
                    paSortedArrayFinal[iz++] = paSortedArray[z];
                }
                
                return paSortedArrayFinal;
            }
            
            return paSortedArray;
            
        }
        
        public static <T> List<T> distinct(ArrayList<T> paList)
        {
            // Convert ArrayList to HashSet to remove duplicates
            Set<T> set = new HashSet<>(paList);

            // Convert HashSet back to ArrayList
            return new ArrayList<>(set);
        }
        
        public static String[] distinct(String[] array) 
        {
            // Use LinkedHashSet to preserve insertion order and ensure uniqueness
            Set<String> uniqueSet = new LinkedHashSet<>(java.util.Arrays.asList(array));

            // Convert the set back to a String[] array
            return uniqueSet.toArray(new String[0]);
        }
        
        public static ArrayList<Object> distinctObject(ArrayList<Object> paObjList, String psDistinctFieldName)
        {
            try
            {
                ArrayList<Integer> ToBeAddedIndexes = new ArrayList<Integer>();
                ArrayList<String>  TempList      = new ArrayList<String>();
                ArrayList<Object>  NewObjList    = new ArrayList<Object>();

                String  FldVal;
                boolean rc =false;
                int     index = 0;
                for (Object NewObj: paObjList)
                {
                    FldVal = Util.Retention.GetObjectFieldValue(NewObj, psDistinctFieldName).toString();
                    
                    rc = isExist(TempList, FldVal);
                    if (rc==false)
                    {
                        TempList.add(FldVal);
                        ToBeAddedIndexes.add(index);
                    }
                    
                    index++;
                }
                
                for (Integer ix:ToBeAddedIndexes)
                {
                    NewObjList.add(paObjList.get(ix));
                }
                
                return NewObjList;
                
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static ArrayList<Long> distinctLong(ArrayList<Long> paList)
        {
            ArrayList<Long> NewList = new ArrayList<Long>();
            boolean rc = false;
            
            for (Long NewLng: paList)
            {
                rc = isExist(NewList, NewLng);
                if (rc==false)
                    NewList.add(NewLng);
            }
            
            return NewList;
        }

        //former: distinctString
        public static ArrayList<String> distinctString(ArrayList<String> paList)
        {
            ArrayList<String> NewList = new ArrayList<String>();
            boolean rc = false;
            
            for (String NewStr: paList)
            {
                rc = isExist(NewList, NewStr);
                if (rc==false)
                    NewList.add(NewStr);
            }
            
            return NewList;
        }
        
        public static boolean isExist(ArrayList<Long> paList, Long plNumber)
        {
            for (Long NumberN: paList)
            {
                if (NumberN.longValue()==plNumber.longValue())
                {
                    return true;
                }
            }
            
            return false;
        }
        
        public static boolean isExist(ArrayList<String> paList, String psTxt)
        {
            for (String Str: paList)
            {
                if (Str.equals(psTxt)==true)
                {
                    return true;
                }
            }
            
            return false;
        }
        
        public static Field[] appendArray(Field[] array, Field x)
        {
            Field[] result = new Field[array.length + 1];

            for(int i = 0; i < array.length; i++)
                result[i] = array[i];

            result[result.length - 1] = x;

            return result;
        }
        
        public static String[] appendArray(String[] array, String x)
        {
            String[] result = new String[array.length + 1];

            for(int i = 0; i < array.length; i++)
                result[i] = array[i];

            result[result.length - 1] = x;

            return result;
        }
        
        public static String[] concatArrays(String[] psArray1, String[] psArray2)
        {
            String[] sArray = new String[psArray1.length + psArray2.length];
            
            int index = 0;
            
            for (int i=0;i<psArray1.length;i++)
            {
                sArray[index] = new String();
                sArray[index++] = psArray1[i];
            }

            for (int i=0;i<psArray2.length;i++)
            {
                sArray[index] = new String();
                sArray[index++] = psArray2[i];
            }
            
            return sArray;
        }

    }
    
    public static class Methods
    {
        //Returns the caller of getName name
        public static String getName()
        {
            //index 0 returns "getName"
            //index 1 returns the caller of "getName"
            String nameOfMethod = new Throwable().getStackTrace()[1].getMethodName();
            return nameOfMethod;
        }
        
        public static String hash()
        {
            //index 1 returns the caller
            String CallerMethodName = new Throwable().getStackTrace()[1].getMethodName();
            String sHash = Util.Session.calcSessionHash(CallerMethodName);
            return sHash;
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass Retention
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Reflection
    {
        // piLevelUp = 1 if the method just above
        // piLevelUp = 2 if the method above in 2 layer
        public static String getCallerMethodName(int piLevelUp)
        {
            try
            {
                //String sMethodName = new Throwable().getStackTrace()[1].getMethodName();
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                // The stackTrace[3] represents the caller method three levels up
                StackTraceElement callerStackTraceElement = stackTrace[piLevelUp];
                String callerMethodName = callerStackTraceElement.getMethodName();

                return callerMethodName;
            }
            catch(Exception e)
            {
                throw e;
            }
        }
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //
    //                          SubClass Retention
    //
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class Retention
    {
        
        public static boolean isClassValid(String pEntityClassName)
        {
            try
            {
                Class<?>    EntityClass     = Class.forName(pEntityClassName);

                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }

        public static Field[] getObjectFields(String pClassName)
        {
            try
            {
                Class<?> MyPackageClass     = Class.forName(pClassName);
                Field SuperClaFields[] = null;
                Field    ClaFields[]        = MyPackageClass.getDeclaredFields();
                //Class<?> MyPackageClass     = pObjectInstance.getClass();
                //Field    ClaFields[]        = MyPackageClass.getDeclaredFields();                
                        
                if (MyPackageClass.getSuperclass()!=null)
                {
                    int iIndex = ClaFields.length;

                    SuperClaFields  = MyPackageClass.getSuperclass().getDeclaredFields();
                }
                
                for (int i=0; i<ClaFields.length; i++)
                {
                    SuperClaFields = Util.Arrays.appendArray(SuperClaFields, ClaFields[i]);
                    //ClaFields = appendArray(ClaFields, SuperClaFields[i]);
                }

                
                return SuperClaFields;
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static Field[] getObjectDeclaredFields(Object pObjectInstance)
        {            
            return getObjectFields(pObjectInstance.getClass().getName());
        }
        
        public static String[] getObjectKeyFieldNames(Object pObjectInstance)
        {
            try
            {
                String[] KeyCols = new String[20];//Maximum 20 key column
                
                Class<?> ObjClass       =   pObjectInstance.getClass();
                Field    ClaFields[]    =   ObjClass.getDeclaredFields();

                if (ObjClass.getSuperclass()!=null)
                {
                    int iIndex = ClaFields.length;

                    Field SuperClaFields[]  = ObjClass.getSuperclass().getDeclaredFields();
                    for (int i=0; i<SuperClaFields.length; i++)
                    {
                        ClaFields = Util.Arrays.appendArray(ClaFields, SuperClaFields[i]);
                        //ClaFields = appendArray(ClaFields, SuperClaFields[i]);
                    }
                }

                int    index = 0;
                
                for(Field Fld:ClaFields)
                {
                    String sColName = Fld.getName();                    
                    
                    Annotation Anno = Fld.getDeclaredAnnotation(Column.class);
                    
                    if (Anno!=null)
                    {
                        Column ACol = (Column)Anno;
                        
                        if (ACol.unique()==true)
                        {
                            KeyCols[index++] = sColName;
                        }
                    }
                }
                
                return KeyCols;
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        public static Object GetObjectFieldValue(Object pObjectInstance, Class<?> pAnnoClass) //throws ErrorCodeX
        {
            try
            {
                //Field[] Flds = pObjectInstance.getClass().getDeclaredFields();
                Field[] Flds = getObjectDeclaredFields(pObjectInstance);

                for(Field Fld:Flds)
                {
                    Annotation[] Annos = Fld.getDeclaredAnnotations();
                    for (Annotation Anno:Annos)
                    {
                        if (Anno.annotationType().getName().equals(pAnnoClass.getName())==true)
                        {
                            //return value
                            return GetObjectFieldValue(pObjectInstance, Fld.getName());
                        }                        
                    }
                    //Annotation Anno = Fld.getDeclaredAnnotation(pAnnoClass);

                }

                return null;
            }
            catch(Exception e)
            {
                return null;
                //ErrorCode Err = new ErrorCodeX();
                //Err = ErrorCode_Prs.EXCEPTION.GetError("Exception: " + e.getMessage());
                //throw Err;
            }
        }

        //Retention
        public static Object GetObjectFieldValue(Object pObjectInstance, String pFieldName) throws Exception
        {
            try
            {
                Object oVal = getObjClassFieldValue(pObjectInstance, pFieldName, false);
                
                if (oVal==null)
                {
                    oVal = getObjClassFieldValue(pObjectInstance, pFieldName, true);
                }
                
                return oVal;
            }
            catch(Exception e)
            {
                ErrorCode Err = new ErrorCode();
                //Err = ErrorCodes.Util.EXCEPTION.exception();
                Err.addDescription("Requested Field Name: " + pFieldName + e.getMessage());
                throw Err;
            }

        }
        
        private static Object getObjClassFieldValue(Object pObjectInstance, String pFieldName, boolean pbSuperClass)
        {
            try
            {
                Field Fld = null;
                if (pbSuperClass==false)
                    Fld = pObjectInstance.getClass().getDeclaredField(pFieldName);
                else
                    Fld = pObjectInstance.getClass().getSuperclass().getDeclaredField(pFieldName);

                Object EVal   = Fld.get(pObjectInstance);

                return EVal;
            }

            catch(NoSuchFieldException | IllegalAccessException e)
            {
                return null;
            }        
        }
    }
    
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //                          SubClass DateTime
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static class crypto
    {
        public static class argon2
        {
            // Salt is auto generated returned in create 
            public static String generate(String psPwd)
            {
                try
                {
                    //String sPwdNSaltData = pEmail.trim().substring(0, 20) + pRegDate.trim() + pPwdClean.trim(); // Currently Argon2 manages salting data itself. Therefore these won't be needed
                    String sPwdNSaltData = psPwd;

                    // Create an Argon2 instance
                    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2i);

                    // Password to be hashed
                    String password = sPwdNSaltData;//sPwdNSaltData;//"yourPassword123";//salt is auto generated in the library you need to change the library if you want to inject salt

                    // Argon2 parameters
                    int iterations = 3;
                    int memory = 65536;
                    int parallelism = 1;

                    try 
                    {
                        // Hash the password
                        String hash = argon2.hash(iterations, memory, parallelism, password);
                        //System.out.println("Hashed password: " + hash);

                        // Verifying the password
                        // boolean matches = argon2.verify(hash, password);
                        // System.out.println("Password verification result: " + matches);

                        return hash;
                    }
                    finally 
                    {
                        argon2.wipeArray(password.toCharArray());
                    }
                }
                catch(Exception e)
                {
                    String s = e.getMessage();

                    return "";
                }
            }

            public static String getEncodePart(String pArgonEncodeHash, String pEncodePartSign)
            {
                String[] aArgonEncodeParts = pArgonEncodeHash.split(",");

                for(int i=0;i<aArgonEncodeParts.length;i++)
                {
                    if(aArgonEncodeParts[i].trim().indexOf(pEncodePartSign)==0)
                        return aArgonEncodeParts[i];
                }

                return "";
            }

            //pPwdClean = we use pwd-signature ( sha256(pwd) )
            public static boolean verify(String pPwdEncode, String pPwdClean)
            {
                // Create an Argon2 instance
                Argon2 argon2 = Argon2Factory.create();

                // Verifying the password
                boolean matches = argon2.verify(pPwdEncode, pPwdClean);
                //System.out.println("Password verification result: " + matches);

                return matches;
            }

            public static boolean verifyChar(String pPwdEncode, String pPwdClean)
            {
                // Create an Argon2 instance
                Argon2 argon2 = Argon2Factory.create();
                
                byte[] pwdBytes = Util.Format.hexToBytes(pPwdClean);
                //char[] pwdChars = new String(pwdBytes, StandardCharsets.ISO_8859_1).toCharArray();
                String base64 = Base64.getEncoder().encodeToString(pwdBytes);
                char[] pwdChars = base64.toCharArray();

                // Verifying the password
                boolean matches = argon2.verify(pPwdEncode, pwdChars);
                //System.out.println("Password verification result: " + matches);

                return matches;
            }

        }// argon2

        public static class rsa
        {
            public static void addSessionKey(ssoSessionKeys poKeys)
            {
                RSAMisc.addSessionKey(poKeys);
            }
            
            public static int getNoOfKey(int pKeyLen)
            {
                return RSAMisc.getNoOfKey(pKeyLen);
            }
            
            public static ssoSessionKeys pickKey(int pKeyLen)
            {
                return RSAMisc.pickKey(pKeyLen);
            }
            
            public static ssoSessionKeys getKey(String psKeyIndex)
            {
                return RSAMisc.getKey(psKeyIndex);
            }
            
            public static ssoRSAKeyPair generateRSAKeyPair(int pLen)
            {
                ssoRSAKeyPair newKeyPair = new ssoRSAKeyPair();

                if (pLen%1024==0)//otherwise default 1024
                    RSA.setKeyPairLength(pLen);
                
                newKeyPair = RSA.generateKeyPair();
                
                return newKeyPair;
            }

            public static PublicKey decodeRSAPublicKey(String psKey)
            {
                return RSA.decodePublicKey(psKey);
            }

            public static PrivateKey decodeRSAPrivateKey(String psKey)
            {
                return RSA.decodePrivateKey(psKey);
            }

            //max 128 length (1024 bits )
            public static byte[] encrypt(PrivateKey pPriKey, String psData)
            {
                try
                {
                    return RSA.encrypt(pPriKey, psData);
                }
                catch(Exception e)
                {
                    return null;
                }
            }
            
            public static byte[] decrypt(PublicKey pPubKey, byte[] psData)
            {
                try
                {
                    return RSA.decrypt(pPubKey, psData);
                }
                catch(Exception e)
                {
                    return null;
                }
            }
            
        }

        public static class sha256
        {
            public static String calculateSHA256(String psData)
            {
                try
                {
                   return SHA256.calculate(psData);
                }
                catch(Exception e)
                {
                    return "";
                }
            }
        }
        
        public static class crc32
        {
            public static long calculate(String psInput)
            {
                 String input = psInput;

                // get bytes from string
                byte bytes[] = input.getBytes();

                Checksum checksum = new CRC32();

                // update the current checksum with the specified array of bytes
                checksum.update(bytes, 0, bytes.length);

                // get the current checksum value
                long checksumValue = checksum.getValue();
                
                return checksumValue;
            }
        }
    
        public static class md5
        {
            public static String calculate(String pInput)
            {
                try
                {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(pInput.getBytes());
                    byte[] digest = md.digest();
                    StringBuilder sb = new StringBuilder();
                    
                    for (byte b : digest) 
                    {
                        sb.append(String.format("%02x", b & 0xff));
                    }
                    
                    return sb.toString();
                }
                catch(Exception e)
                {
                    return "";
                }
            }
        }
    }

    public static class console
    {
        public static void println(String pText, boolean pbSkipFormat)
        {
            if(pbSkipFormat==true)
                System.out.println(pText);
            else
            {
                String sTimeStamp = Util.DateTime.GetDateTime_s();
                System.out.println("[" + sTimeStamp + "] " + ":> " + " Data: " + pText);
            }
        }
        
        public static void clear(String pMsg)
        {
            try 
            {
                
                final String os = System.getProperty("os.name");
                if (os.contains("Windows")) 
                {
                    // For Windows
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } 
                else 
                {
                    // For Unix-like systems
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
                
                System.out.println("Util.console.clear() called: " + pMsg);
            } 
            catch (final Exception e) 
            {
                // Handle exceptions, e.g., InterruptedException
                e.printStackTrace();
            }
        }

    }

    public static class misc
    {
        public static Object getInstance(String pClassName)
        {
            try
            {

                Class<?> MyPackageClass = Class.forName(pClassName);//"webapi.USER");
                Constructor<?> constructor = MyPackageClass.getDeclaredConstructor();
                Object MyClassInstance = constructor.newInstance();

                return MyClassInstance;
            }
            catch(Exception e)
            {
                String s = e.getCause().toString();
                return null;
            }

        }
        
        public static Object ifNull(Object pVal, Object pFill)
        {
            if (pVal==null)
                return pFill;
            
            return pVal;
        }
    }
    
    public static class Token
    {
        // max 24 bytes
        public static String generateBase64()
        {
            UUID uuid = UUID.randomUUID();
            byte[] uuidBytes = uuid.toString().getBytes();
            String base64Encoded = Base64.getEncoder().encodeToString(uuidBytes);

            return base64Encoded;
        }
    }
    
    public static class Facebook
    {
        // Facebook returns failed if the token is expired.
        // In other words, if we received a positive response that means 
        // the token still valid.
        public static ssoLoginResults authenticate(String pFBAccessToken, String pFBUserId)
        {
            ssoLoginResults results = new ssoLoginResults();
            
            try
            {
                //String sURL = "https://graph.facebook.com/me?access_token=" + pFBAccessToken;
                String sURL = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + pFBAccessToken;
                //String sURL = "https://graph.facebook.com/me?access_token=" + "123";

                String sResp = Util.HTTP.sendHTTPRequest("GET", sURL, "");

                //JSON Parse
                // If valid the result should be as following
                //{
                //  "name": "Esabil Bulbul",
                //  "id": "10157968733184541"
                //}   

                org.json.simple.JSONObject jsRsp = Util.JSON.parseJSON(sResp);

                results.userId     = (String)jsRsp.get("id");
                results.email      = (String) jsRsp.get("email");  // Email
                results.picURL     = (String) ((org.json.simple.JSONObject) ((org.json.simple.JSONObject) jsRsp.get("picture")).get("data")).get("url"); // Picture URL

                if (results.userId.equals(pFBUserId)==true)
                    results.verified = true;

                return results;
            }
            catch(Exception e)
            {
                throw e;
            }
        }
    }


}
