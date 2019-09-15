/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.util;

import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
//import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLType;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
/**
 *
 * @author Administrator
 */
public final class Util
{
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
    
    public static class Sys
    {
        public static long generateSysId(int pPaddingExponent)
        {
            int iExp = (int)Math.pow(10, pPaddingExponent);//for javascript MAX must be 10 otherwise js rounds erronously
            
            String sSysId = Util.DateTime.GetDateTime_l().toString().substring(2) + 
                            String.valueOf(Util.Randomize.generateRandomNumber(0, iExp));//Javascript side erronously round the number if more than 100 (exceeds the maximum in total size)
            
            String sSysFormatted = Util.Str.rightPad(sSysId, "0", 17);
            
            return Long.parseLong(sSysFormatted);//with milliseconds
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
        
        public static String QUOTE(String pVal)
        {
            return QUOTE_SIGN + pVal + QUOTE_SIGN;
        }
        
        public static String SQUOTE(String pVal)//SINGLE QUOTE
        {
            return "'" + pVal + "'";
        }

        
        //Converts start Sign of each word to Upper Case
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
            for (char c : pStr.toCharArray())
            {
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }
        
        public static boolean isAmount(String pStr)
        {
            int i=0;
            for (char c : pStr.trim().toCharArray())
            {
                
                if (!Character.isDigit(c)) 
                {
                    if ((c=='.') || (c==',') || ((c=='-') && (i==0)) )//minus in first position
                        continue;
                    else
                        return false;
                }
                i++;
            }
            return true;

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

    public static class Database
    {
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
        public static Long GetDateTime_l_wo_MSeconds()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYYYMMddHHmmss");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }

        //For example:
        // pFormat = YYYYMMddHHmmssS
        public static Long GetDateTime(String pFormat)
        {
            
            SimpleDateFormat    DFormat = new SimpleDateFormat(pFormat);

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

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
        
        public static Long GetDateTime_l_w_YY()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYMMddHHmmssS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }
        
        public static Long GetDateTime_l()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYYYMMddHHmmssS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  lDateNow;
        }

        public static String GetDateTime_s()
        {
            SimpleDateFormat    DFormat = new SimpleDateFormat("YYYYMMddHHmmssS");

            Date    DateNow             = new Date();
            String  sDateNow            = DFormat.format(DateNow);
            Long    lDateNow            = Long.parseLong(sDateNow);

            return  sDateNow;
        }
        
        public static Date Str2Date(String psDate)
        {
            try
            {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssS");
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

        public static String sendHTTPRequest(String pMethod, String pURL, String pMsg)
        {
            try
            {
                //URL url = new URL("https://test.payeco.com:9443/DnaOnline/servlet/DnaPayB2C");
                URL url = new URL(pURL);

                // instantiate the HttpURLConnection with the URL object - A new
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
        
        public static StringWriter Convert2JSON(Object pObj)
        {
            try
            {            
                StringWriter RetJSON = new StringWriter();
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
                mapper.writeValue(RetJSON, pObj);

                //Array JSON
                //http://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
                //List<Object> myObjects = mapper.readValue("", new TypeReference<List<Object>>(){});
                        
                return RetJSON;
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
                ObjectMapper mapper = new ObjectMapper();
                Object obj = mapper.readValue(psJSON, pClass);
                
                return obj;
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
        
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        public static String GenerateFileName_w_YYMMDDHHM1(String pFileFolder, String pFileName, String pExtension)
        {
            String sFileExtensionYYYYMMDDHH = GetFileNameExtensionYYMMDDHHM1();
            
            //Create File Name
            String sFileName = pFileFolder + "/" + pFileName + "_" +  sFileExtensionYYYYMMDDHH + "." + pExtension;
            
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
                return false;
            }
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
                theDir.mkdir();
            }
        }
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
        public static class rsa
        {
            public static ssoRSAKeyPair generateRSAKeyPair()
            {
                ssoRSAKeyPair newKeyPair = new ssoRSAKeyPair();

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
    }

}
