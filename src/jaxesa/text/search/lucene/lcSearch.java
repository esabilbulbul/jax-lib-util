/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.text.search.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import jaxesa.util.Util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Lucene search algorithm by California 
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *                            EXAMPLE USE CASE
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * lcSearch lcs = new lcSearch("c:/esa/lucene6index", "");
 * 
 * List<Document> documents = new ArrayList<>();
 * 
 * IndexWriter writer = lcs.createWriter();
 * 
 * Document doc = new Document();
 * lcs.addField(doc, "kword", "esabil");
 * lcs.addField(doc, "kword", "bulbul");
 * lcs.addField(doc, "kword", "esabil");
 * lcs.addField(doc, "kword", "sefa");
 * lcs.addField(doc, "kword", "kevser");
 * documents.add(doc);
 * lcs.runIndexing(writer, documents);
 * lcs.closeWriter(writer);
 * 
 * IndexReader reader = lcs.getIndexReader(INDEX_DIR, "");
 * ArrayList<lcoField> flds = new ArrayList<lcoField>();
 * flds = lcs.getFieldFrequencies(reader, "kword", 2);
 * 
 * @author Administrator
 */
public class lcSearch 
{
    String gINDEX_ROOT = "";
    String gINDEX_DIR  = "";

    public lcSearch(String pIndexRoot, String pIndexDir)
    {
        gINDEX_ROOT = pIndexRoot;
        gINDEX_DIR  = pIndexDir;
    }

    public IndexWriter createWriter() throws IOException
    {
        String sTimeStamp = Util.DateTime.GetDateTime_s();
        
        //Check if the dir and folder exists
        if(Util.Files.isDirExist(gINDEX_ROOT)==false)
            Util.Files.CreateDir(gINDEX_ROOT);

        if(Util.Files.isDirExist(gINDEX_DIR)==false)
        {
            if (gINDEX_DIR.trim().length()>0)
                Util.Files.CreateDir(gINDEX_DIR);
        }

        String sPath = "";
        if (gINDEX_DIR.trim().length()>0)
            sPath = gINDEX_ROOT + "\\" + gINDEX_DIR;
        else
            sPath = gINDEX_ROOT;

        FSDirectory dir = FSDirectory.open(Paths.get(sPath));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(dir, config);
        return writer;
    }

    public boolean runIndexing(IndexWriter pWriter, List<Document> pDocuments)
    {
        try
        {
            //Let's clean everything first
            pWriter.deleteAll();

            pWriter.addDocuments(pDocuments);
            
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
    public boolean closeWriter(IndexWriter pWriter)
    {
        try
        {

            pWriter.commit();
            pWriter.close();
            
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }
    
    public IndexReader getIndexReader(String pIndexRoot, String pIndexDir)
    {
        try
        {
            String sIndexRoot = pIndexRoot;
            String sIndexDir  = pIndexDir;
            String sPath = "";

            if (pIndexRoot.trim().length()==0)
                sIndexRoot = gINDEX_ROOT;

            if (pIndexDir.trim().length()==0)
                sIndexDir = gINDEX_DIR;

            if (sIndexDir.trim().length()>0)
                sPath = sIndexRoot + "\\" + sIndexDir;
            else
                sPath = sIndexRoot;
            
            Directory dir = FSDirectory.open(Paths.get(sPath));
            IndexReader reader = DirectoryReader.open(dir);
            
            return reader;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    //pMinFreq = 0 everything
    public ArrayList<lcoField> getFieldFrequencies(IndexReader pReader, String pFieldName, long pMinFreq)
    {
        try
        {
            ArrayList<lcoField> fields = new ArrayList<lcoField>();

            for (int i=0; i<pReader.maxDoc(); i++)
            {
                Terms trms = pReader.getTermVector(i, pFieldName);

                if (trms!=null)
                {
                    TermsEnum terms = trms.iterator();
                    while(terms.next()!=null) 
                    {
                        lcoField newField = new lcoField();

                        newField.name = terms.term().utf8ToString();
                        newField.freq = terms.totalTermFreq();                        
                        

                        if (newField.freq <= 0)
                        {
                            //Add everything
                            fields.add(newField);
                        }
                        else
                        {
                            if (newField.freq >= pMinFreq)
                            {
                                //Adds only if meet the criteria
                                fields.add(newField);
                            }
                        }
                    }
                }

            }
            
            return fields;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public void addField(Document pDocument, String psFieldName, String psFieldVal)
    {
        //Document document = new Document();
        //document.add(new StringField(psFieldName, psFieldVal , Field.Store.YES));        

        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS);            
        type.setStored(true);
        type.setStoreTermVectors(true);
        pDocument.add(new Field(psFieldName, psFieldVal, type));

        //return document;
    }

}
