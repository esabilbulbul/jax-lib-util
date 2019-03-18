/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxesa.nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
//import static main.Nlptest.getChunks;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

/**
 * 
 * @author Administrator
 * 
 * Copyright © 2019, ShipShuk
 * www.shipshuk.com 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * This library uses Apache Open NLP
 * 
 * This class works with the models (files). Therefore for the minimum requirement following files
 * must be loaded for Language Processing. The minimum files are 
 * 
 * MUST MODEL FILES
 *   - Tagger Model File (if this not found, LANGUAGE NOT SUPPORTED)
 * 
 * CONDITIONAL/ADVANCED FILES
 *   - Token Model File (If this not found, whitespace tokenization will be used as alternative)
 *   - Sentence Model File (if this not found, full text will be taken as sentence) 
 * 
 * OPTIONAL/ADVANCED MODEL FILES
 *  - Chunker Model File
 *  - Person Model File
 *  - Location Model File
 * 
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *                          MAIN FUNCTION: getTags
 * 
 * This function takes the text and gives the words with the tags. Before this 
 * function called the language models must be initialized with nlpapi.init 
 * method
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * 
 * DEFAULT ACTIONS
 * -----------------------------------------------------------------------------
 * DetectSentence: If sentence-model file not found, the whole text will be 
 *                 taken as one sentence
 * 
 * Tokenize:       If tokenize-model file not found, whiteSpaceTokenization will
 *                 be used
 * 
 * Tagging         If POSTagger or Chunk not found, then the text can't be tokenized
 *                 and tagged
 * 
 * INFO: Tagging might have multiple options for model files. We are going to use
 *       maximum entropy model. max-ent.bin
 * 
 * -----------------------------------------------------------------------------
 * 
 * Text -> Sentence -> Language Detection
 * 
 * Text -> Sentence Detection -> Sentence(s) 
 *                                      1. -> Chunks -> Spans (words + tags) (If Chunk.bin supported)
 *                                      2. -> POS Tagger -> Spans (If Chunk.bin NOT supported)
 *                                      3. -> Tokens (words) -> names 
 *                                      4. -> Tokens (words) -> locations
 * 
 * API 1. Chunking Sentences thru Sentence Detection
 * API 2. POS Tagger (tags the words of the sentences) INFO: This will be used if the chunk.bin not exist
 * API 3. Name Detection  (model token.bin must)
 * API 4. Location Detection (model token.bin must)
 * 
 * Other APIs
 * API : Language Detenction (model language.bin must)
 * API : Tokenizer (split the words) for instance New York is one word (Sentece -> Tokens) (model token.bin must))
 * 
 * 
 * 
 * LANGUAGE CODES
 * ISO-639-3 language code 
 * https://www.loc.gov/standards/iso639-2/php/code_list.php
 * 
 * OFFICIAL DOCUMENTATION
 * https://opennlp.apache.org/docs/1.8.1/manual/opennlp.html#tools.langdetect
 * 
 * See TUTORIAL
 * https://www.tutorialspoint.com/opennlp/opennlp_tokenization.htm
 * 
 * 
 * Models
 * http://opennlp.sourceforge.net/models-1.5/
 * 
 * 
 * POS TAGS  (CHUNK)
 * https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
 * https://stackoverflow.com/questions/15059878/opennlp-chunker-and-postag-results
 * 
 * FOR LIST of TAGS
 * http://dpdearing.com/posts/2011/12/opennlp-part-of-speech-pos-tags-penn-english-treebank/
 * https://stackoverflow.com/questions/15059878/opennlp-chunker-and-postag-results
 * 
 * 
 * Parsing the Chunk
 * https://stackoverflow.com/questions/14708047/how-to-extract-the-noun-phrases-using-open-nlps-chunking-parser/15584358

 */
public final class nlpapi
{
    static String gLastLanguage;
    
    public static String LANGUAGE_SUPPORT_W_TAGGER = "T";
    public static String LANGUAGE_SUPPORT_W_CHUNK  = "C";
    
    public static String LANGUAGE_CODE_KEY_SAMI = "se";
    
    // MODEL FILES
    //--------------------------------------------------------------------------
    static String gModelLanguageFile        = "";//Only one file
    static ArrayList<ssoModelFile> gENModelSentenceFile      = new ArrayList<ssoModelFile>();//one per each language
    static ArrayList<ssoModelFile> gENModelTokenFile         = new ArrayList<ssoModelFile>();//one per each language
    
    //the following three may not be exist for each language especially Parser/Chunking one
    static ArrayList<ssoModelFile> gENModelNameTokenFile     = new ArrayList<ssoModelFile>();//one per each language
    static ArrayList<ssoModelFile> gENModelLocationTokenFile = new ArrayList<ssoModelFile>();//one per each language
    static ArrayList<ssoModelFile> gENModelParserFile        = new ArrayList<ssoModelFile>();//one per each language -> if you have this one no need to use the token modle file
    
    static ArrayList<ssoModelFile> gENModelPOSTaggerFile     = new ArrayList<ssoModelFile>();
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    static LanguageDetectorModel gLanguageModel;
    static ArrayList<ssoModel>   gSentenceModels       = new ArrayList<ssoModel>();//SentenceModel
    static ArrayList<ssoModel>   gTokenModels          = new ArrayList<ssoModel>();//TokenizerModel
    static ArrayList<ssoModel>   gNameTokenModels      = new ArrayList<ssoModel>();//TokenNameFinderModel
    static ArrayList<ssoModel>   gLocationTokenModels  = new ArrayList<ssoModel>();//TokenNameFinderModel
    static ArrayList<ssoModel>   gParserModels         = new ArrayList<ssoModel>();//ParserModel
    static ArrayList<ssoModel>   gPOSTaggerModels      = new ArrayList<ssoModel>();//POSModel
    
    //this is put due to fail rate of detecting the language
    //If the language predicted not in the priority one, then will check next three
    //If they are in the priority. If it is, then will continue with priority language.
    static ArrayList<ssoLangCode> gPriorityLanguages = new ArrayList<ssoLangCode>();
    static ArrayList<ssoTag>      gLangTags          = new ArrayList<ssoTag>();

    public static int LANG_TAG_TYPE_NOUN      = 1;
    public static int LANG_TAG_TYPE_ADJECTIVE = 2;
    
    public static String POS_TAG_PERSON   = "NP";
    public static String POS_TAG_LOCATION = "NL";
    
    public static void init(String pModelLanguage_filepath, 
                            ArrayList<ssoModelFile> pModelSentence_filepath,
                            ArrayList<ssoModelFile> pModelToken_filepath,
                            ArrayList<ssoModelFile> pModelNameToken_filepath,//person
                            ArrayList<ssoModelFile> pModelLocationToken_filepath,
                            ArrayList<ssoModelFile> pModelParser_filepath, //chunker
                            ArrayList<ssoModelFile> pModelPOSTagger_filepath
                           ) throws Exception
    {
        initLanguageTags();
        initPriorityLanguageCodes();

        gModelLanguageFile        = pModelLanguage_filepath;
        gENModelSentenceFile      = pModelSentence_filepath;
        gENModelTokenFile         = pModelToken_filepath;
        gENModelNameTokenFile     = pModelNameToken_filepath;
        gENModelLocationTokenFile = pModelLocationToken_filepath;
        gENModelParserFile        = pModelParser_filepath;
        gENModelPOSTaggerFile     = pModelPOSTagger_filepath;

        //Initializatin Lanuage Model
        if (pModelLanguage_filepath.trim().length()>0)
        {
            File modelFile = new File(gModelLanguageFile);
            gLanguageModel = new LanguageDetectorModel(modelFile);
        }

        if (pModelSentence_filepath.size()>0)
        {
            for(ssoModelFile SentModelFile: pModelSentence_filepath)
            {
                ssoModel newModel = new ssoModel();

                InputStream inputStream = new FileInputStream(SentModelFile.file);
                SentenceModel model = new SentenceModel(inputStream);
                
                newModel.lang  = SentModelFile.lang;
                newModel.model = model;
                gSentenceModels.add(newModel);
                //gENSentenceModel = new SentenceModel(inputStream);
            }
        }

        if (pModelToken_filepath.size()>0)
        {
            for(ssoModelFile TokenModelFile: pModelToken_filepath)
            {
                ssoModel newModel = new ssoModel();
                
                InputStream inputStream = new FileInputStream(TokenModelFile.file);
                TokenizerModel model = new TokenizerModel(inputStream);
                
                newModel.lang  = TokenModelFile.lang;
                newModel.model = model;
                
                gTokenModels.add(newModel);
            }
        }

        if (pModelNameToken_filepath.size()>0)
        {
            for(ssoModelFile NameTokenModelFile: pModelNameToken_filepath)
            {
                ssoModel newModel = new ssoModel();

                InputStream inputStream     = new FileInputStream(NameTokenModelFile.file);
                TokenNameFinderModel model  = new TokenNameFinderModel(inputStream);

                newModel.lang  = NameTokenModelFile.lang;
                newModel.model = model;

                gNameTokenModels.add(newModel);
            }
        }

        if (pModelLocationToken_filepath.size()>0)
        {
            for(ssoModelFile LocationTokenModelFile: pModelLocationToken_filepath)
            {
                ssoModel newModel = new ssoModel();

                InputStream inputStream    = new FileInputStream(LocationTokenModelFile.file);
                TokenNameFinderModel model = new TokenNameFinderModel(inputStream);

                newModel.lang  = LocationTokenModelFile.lang;
                newModel.model = model;
                
                gLocationTokenModels.add(newModel);
            }
        }
        
        if (pModelParser_filepath.size()>0)
        {
            for(ssoModelFile parserModelFile: pModelParser_filepath)
            {
                ssoModel newModel = new ssoModel();
                
                InputStream inputStreamParser = new FileInputStream(parserModelFile.file);
                ParserModel model = new ParserModel(inputStreamParser); 
                
                newModel.lang  = parserModelFile.lang;
                newModel.model = model;
                
                gParserModels.add(newModel);
            }
        }

        if (pModelPOSTagger_filepath.size()>0)
        {
            //InputStream modelStream = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-pos-maxent.bin");//Tagger File
            for(ssoModelFile POSTaggerModelFile: pModelPOSTagger_filepath)
            {
                ssoModel newModel = new ssoModel();
                
                InputStream modelStream = new FileInputStream(POSTaggerModelFile.file);//Tagger File
                POSModel model  = new POSModel(modelStream);
                
                newModel.lang = POSTaggerModelFile.lang;
                newModel.model = model;
                
                gPOSTaggerModels.add(newModel);
            }
        }
        
    }
    
    public static void initLanguageTags()
    {
        //English
        //----------------------------------------------------------------------
        addNewLangTag("en", "JJ"              , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("en", "JJS"             , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("en", "JJR"             , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("en", "NN"              , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("en", "NNS"             , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("en", "NNP"             , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("en", "NNPS"            , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("en", "FW"              , LANG_TAG_TYPE_NOUN     , "Noun");
        
        addNewLangTag("en", POS_TAG_PERSON    , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("en", POS_TAG_LOCATION  , LANG_TAG_TYPE_NOUN     , "Noun");
        
        //German
        //----------------------------------------------------------------------
        addNewLangTag("de", "ADJ", LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("de", "ADJD", LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("de", "ADJA", LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("de", "NN"  , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("de", "NE"  , LANG_TAG_TYPE_NOUN     , "Noun");

        
        //Danish
        //----------------------------------------------------------------------
        addNewLangTag("da", "AN"  , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("da", "NC"  , LANG_TAG_TYPE_NOUN     , "Noun");
        addNewLangTag("da", "NP"  , LANG_TAG_TYPE_NOUN     , "Noun");

        //Dutch
        //----------------------------------------------------------------------
        addNewLangTag("nl", "ADJ"  , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("nl", "N"    , LANG_TAG_TYPE_NOUN,      "Noun");

        //Portegues
        //----------------------------------------------------------------------
        addNewLangTag("pt", "ADJ"  , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("pt", "N"    , LANG_TAG_TYPE_NOUN,      "Noun");

        //SAMI
        //----------------------------------------------------------------------
        addNewLangTag("se", "AJ"  , LANG_TAG_TYPE_ADJECTIVE, "Adjective");
        addNewLangTag("se", "NN"  , LANG_TAG_TYPE_NOUN,      "Noun");

    }

    public static ArrayList<ssoTag> getLanguageTags(String pLan)
    {
        ArrayList<ssoTag> tags = new ArrayList<ssoTag>();

        for (ssoTag TagN:gLangTags)
        {
            if (TagN.lang.equals(pLan.toLowerCase().trim())==true)
            {
                tags.add(TagN);
            }
        }
        
        return tags;
    }

    public static void addNewLangTag(String pLang, String pTag, int pType, String pDesc)
    {
        ssoTag newTag = new ssoTag();

        newTag.lang = pLang;
        newTag.tag  = pTag;
        newTag.desc = pDesc;
        newTag.type = pType;

        gLangTags.add(newTag);
    }

    public static void initPriorityLanguageCodes()
    {
        //Initialize Priority Languages (2 digit)
        //---------------------------------------------------------------------
        addLanguageCode("en", "en");//english
        
        addLanguageCode("de", "de");//german
        
        addLanguageCode("da","da");//danish
        
        //gPriorityLanguages.add("es");//spanish//removed no must files 
        
        addLanguageCode("nl","nl");//dutch

        //Portugese
        addLanguageCode("pt", "pt");//portegues
        addLanguageCode("pt", "po");//portegues
     
        addLanguageCode(LANGUAGE_CODE_KEY_SAMI, LANGUAGE_CODE_KEY_SAMI);//sami languages
    }

    public static void addLanguageCode(String pKey, String pCodes)
    {
        String[] codes = pCodes.split(",");

        for(String codeN: codes)
        {
            ssoLangCode newCode = new ssoLangCode();
            
            newCode.key  = pKey;
            newCode.code = codeN;
            
            gPriorityLanguages.add(newCode);
        }

    }

    //With minimum requirements
    //ISO-639-3
    public static ssoLangModelPackage newLanguageModel(String pLangCode, String pModelSentenceFile, String pModelTokenFile, String pModelPOSTaggerFile)
    {
        ssoLangModelPackage newLangModelPack = new ssoLangModelPackage();

        ssoModelFile newModelSentence = new ssoModelFile();
        newModelSentence.lang = pLangCode;
        newModelSentence.file = pModelSentenceFile;
        newLangModelPack.Sentence.add(newModelSentence);

        ssoModelFile newModelToken = new ssoModelFile();
        newModelToken.lang = pLangCode;
        newModelToken.file = pModelTokenFile;
        newLangModelPack.Token.add(newModelToken);

        ssoModelFile newModelPOSTagger = new ssoModelFile();
        newModelPOSTagger.lang = pLangCode;
        newModelPOSTagger.file = pModelPOSTaggerFile;
        newLangModelPack.POSTagger.add(newModelPOSTagger);

        return newLangModelPack;
    }

    //if no tagger or chunk, lib can't tag the words
    public static boolean isLanguageSupported(String pLang)
    {
        String sType = getTokenizationSupportType(pLang);

        if (sType.trim().length()==0)
            return false;
        else
            return true;

    }

    // Check if PosTagger or Chunker model exist
    public static String getTokenizationSupportType(String pLang)
    {
        
        //Check if POS Tagger exist
        for (ssoModel modelN:gPOSTaggerModels)
        {
            if (pLang.toLowerCase().trim().substring(0, 2).equals(modelN.lang)==true)
                return LANGUAGE_SUPPORT_W_TAGGER;//supported with Tagger
        }

        //Check if Chunker exist
        for (ssoModel modelN:gParserModels)
        {
            if (pLang.toLowerCase().trim().substring(0, 2).equals(modelN.lang)==true)
                return LANGUAGE_SUPPORT_W_CHUNK;// Supported with Chunker
        }
        
        return "";//not supported
    }
    
    
    public static TokenizerModel getTokenModel(String pLanguage)
    {
        for (ssoModel modelN:gTokenModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (TokenizerModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }
        
        return null;
    }

    public static TokenNameFinderModel getNameTokenModel(String pLanguage)
    {
        for (ssoModel modelN:gNameTokenModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (TokenNameFinderModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }
        
        return null;

        //return gENNameTokenModel;//for now
    }

    public static TokenNameFinderModel getLocationTokenModel(String pLanguage)
    {
        for (ssoModel modelN:gLocationTokenModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (TokenNameFinderModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }
        
        return null;
        //return gENLocationTokenModel;//for now
    }

    public static SentenceModel getSentenceModel(String pLanguage)
    {
        for (ssoModel modelN:gSentenceModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (SentenceModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }

        return null;
        //return gENSentenceModel;//for now
    }

    public static ParserModel getParserModel(String pLanguage)
    {
        for (ssoModel modelN:gParserModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (ParserModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }

        return null;
        //return gENParserModel;//for now
    }

    public static POSModel getPOSModel(String pLanguage)
    {
        for (ssoModel modelN:gPOSTaggerModels)
        {
            if (pLanguage.trim().toLowerCase().substring(0, 2).equals(modelN.lang)==true)
            {
                return (POSModel)modelN.model;
            }
            //return gENTokenModel;//for now
        }

        return null;
        //return gENPOSModel;
    }

    public static String getLastLanguage()
    {
        return gLastLanguage;
    }
    //
    // MINIMUM REQUIREMENT:  Language-detect.bin model file
    // 
    // 
    public static String detectLanguage(String pSentence)
    {
        try
        {
            // load the trained Language Detector Model file
            //File modelFile = new File("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/langdetect-183.bin");
            //File modelFile = new File(gModelLanguageFile);

            //LanguageDetectorModel trainedModel = new LanguageDetectorModel(modelFile);

            // load the model
            LanguageDetector languageDetector = new LanguageDetectorME(gLanguageModel);

            //Language[] languages = languageDetector.predictLanguages("Puedo darte ejemplos de los métodos");
            //Language[] languages = languageDetector.predictLanguages("Hi. How are you? Welcome to Tutorialspoint. ");
            Language[] languages = languageDetector.predictLanguages(pSentence);
            //System.out.println("Predicted language: "+ languages[0].getLang());

            //return languages[0].getLang();
            return determineLanguage(languages);
        }
        catch(Exception e)
        {
            return "";
        }
    }
    
    // If first language prediction is not priority check next 2
    // If they are, check if the confidence fault rate is less than %10
    // then go with it
    public static String determineLanguage(Language[] pLanguages)
    {
        String sLan   = pLanguages[0].getLang();
        double iConfFirst = pLanguages[0].getConfidence();
        
        if ( (sLan.equals("swe")==true) || (sLan.equals("fin")==true) || (sLan.equals("nor")==true))
            sLan = LANGUAGE_CODE_KEY_SAMI;//sami languages
        
        //Is the language[0] priority languages
        for (ssoLangCode sPriLan: gPriorityLanguages)
        {
            if (sLan.toLowerCase().trim().substring(0, 2).equals(sPriLan.code.substring(0, 2))==true)
                return sPriLan.key;
        }

        //if not priority languages, check next 2 if they are in priority
        int IndexNextLang = 1;
        String sLanNext = pLanguages[IndexNextLang].getLang();
        for (ssoLangCode sPriLan: gPriorityLanguages)
        {
            if (sLanNext.toLowerCase().trim().substring(0, 2).equals(sPriLan.code.substring(0, 2))==true)
            {
                //Language found
                double iConfNext = pLanguages[IndexNextLang].getConfidence();

                if (Math.abs(iConfFirst-iConfNext)<0.1)//if the conf margin less than 10%
                    return sPriLan.key;
                else
                    return pLanguages[0].getLang();
            }
        }

        //if not priority languages, check next 2 if they are in priority
        IndexNextLang = 2;
        sLanNext = pLanguages[IndexNextLang].getLang();
        for (ssoLangCode sPriLan: gPriorityLanguages)
        {
            if (sLanNext.toLowerCase().trim().substring(0, 2).equals(sPriLan.code.substring(0, 2))==true)
            {
                //Language found
                double iConfNext = pLanguages[IndexNextLang].getConfidence();

                if (Math.abs(iConfFirst-iConfNext)<0.1)//if the conf margin less than 10%
                    return sPriLan.key;
                else
                    return pLanguages[0].getLang();
            }
        }

        return sLan;
    }

    //Splits the text into multiple sentences
    //
    //
    // MINIMUM REQUIREMENT:  Sentence.bin model file
    // 
    // 
    public static ArrayList<String> detectSentences(String pLang, String pText)
    {
        ArrayList<String> aSentences = new ArrayList<String>();

        try
        {
            //InputStream inputStream = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-sent.bin");
            //InputStream inputStream = new FileInputStream(pModelSentenceFile);
            //SentenceModel model = new SentenceModel(inputStream);

            SentenceModel model = getSentenceModel(pLang);

            if (model!=null)
            {
                //Instantiating the SentenceDetectorME class 
                SentenceDetectorME detector = new SentenceDetectorME(model);

                //Detecting the sentence
                String sentences[] = detector.sentDetect(pText);

                //Printing the sentences 
                for(String sent : sentences)
                {
                    //System.out.println(sent);
                    aSentences.add(sent);
                }
            }
            else
            {
                //Whole text is one sentece
                aSentences.add(pText);
            }

            return aSentences;
        }
        catch(Exception e)
        {
            return aSentences;
        }
    }

    // Split the sentence of a language into the words
    // via the sentence given
    //
    // MINIMUM REQUIREMENT:  Tokenizer.bin model
    // 
    // 
    public static ArrayList<String> tokenize(String pLang, String pSentence)
    {
        ArrayList<String> aTokens = new ArrayList<String>();

        //Loading the Tokenizer model 
        //InputStream inputStreamTokenizer = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-token.bin");
        //TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);

        TokenizerModel model = getTokenModel(pLang);

        if (model!=null)
        {
            //Instantiating the TokenizerME class 
            TokenizerME tokenizer = new TokenizerME(model);

            //Tokenizing the given raw text 
            String tokens[] = tokenizer.tokenize(pSentence);       

            //Printing the tokens  
            for (String a : tokens) 
            {
                //System.out.println(a); 
                aTokens.add(a);
            }
        }

        return aTokens;
    }

    // Step 1. getTokens
    // Step 2. getNameTokens
    //
    //
    //
    // MINIMUM REQUIREMENT:  Tokenizer.bin model + Chunk.bin model file
    // 
    // 
    public static ArrayList<String> detectNames(String pLang, String pSentence)
    {
        ArrayList<String> aNames = new ArrayList<String>();

        //----------------------------------------------------------------------
        // Step 1
        //----------------------------------------------------------------------
        TokenizerModel model = getTokenModel(pLang);

        if (model!=null)
        {
            //Instantiating the TokenizerME class 
            TokenizerME tokenizer = new TokenizerME(model);

            //Tokenizing the sentence in to a string array 
            String tokens[] = tokenizer.tokenize(pSentence); 

            //InputStream inputStreamNameFinder = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-ner-person.bin");
            //TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);


            //----------------------------------------------------------------------
            // Step 2
            //----------------------------------------------------------------------
            TokenNameFinderModel nameModel = getNameTokenModel(pLang);

            if (nameModel!=null)
            {
                //Instantiating the NameFinderME class 
                NameFinderME nameFinder = new NameFinderME(nameModel);       

                //Finding the names in the sentence 
                Span nameSpans[] = nameFinder.find(tokens);        

                //Printing the names and their spans in a sentence 
                for(Span s: nameSpans)
                {
                    //System.out.println(tokens[s.getStart()]);  
                    //System.out.println(s.toString()+"  "+tokens[s.getStart()]);
                    aNames.add(tokens[s.getStart()]);
                }
            }

        }

        return aNames;
    }

    // Step 1. getTokens
    // Step 2. getLocationTokens
    //
    // MINIMUM REQUIREMENT:  Tokenizer.bin model + NameLocation.bin model file
    // 
    // 
    public static ArrayList<String> detectLocations(String pLang, String pSentence)
    {
        ArrayList<String> aLocations = new ArrayList<String>();
                
        //----------------------------------------------------------------------
        // Step 1
        //----------------------------------------------------------------------
        TokenizerModel model = getTokenModel(pLang);

        if (model==null)
            return aLocations;//empty
        
        //Instantiating the TokenizerME class 
        TokenizerME tokenizer = new TokenizerME(model);

        //Tokenizing the sentence in to a string array 
        String tokens[] = tokenizer.tokenize(pSentence);

        //----------------------------------------------------------------------
        // Step 2
        //----------------------------------------------------------------------
        //InputStream inputStreamNameFinderLocation = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-ner-location.bin");
        //TokenNameFinderModel modelLocation = new TokenNameFinderModel(inputStreamNameFinderLocation); 
        TokenNameFinderModel modelLocation = getLocationTokenModel(pLang);

        //Instantiating the NameFinderME class 
        NameFinderME nameFinderLocation = new NameFinderME(modelLocation);

        //Finding the names of a location 
        Span nameSpansLocation[] = nameFinderLocation.find(tokens);        
        //Printing the spans of the locations in the sentence 
        for(Span s: nameSpansLocation)
        {
           //System.out.println(tokens[s.getStart()]); 
           aLocations.add(tokens[s.getStart()]);
        }
        
        return aLocations;
    }



    //chunk + parseSentence (hybrid)
    //
    //
    // MINIMUM REQUIREMENT:  Chunk.bin model file
    // 
    // 
    // this doesn't distinguish names or locations
    public static ArrayList<ssoPOSTag> tokenize_n_tag_w_chunker(String pLang, String pSentence)
    {
        ArrayList<ssoPOSTag> aWords = new ArrayList<ssoPOSTag>();
        
        try
        {
            //InputStream inputStreamParser = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-parser-chunking.bin");
            //ParserModel model = new ParserModel(inputStreamParser); 

            ParserModel model = getParserModel(pLang);
            if (model==null)
                return aWords;

            //Creating a parser 
            Parser parser = ParserFactory.create(model);

            //Parsing the sentence 
            //String sentence = "Tutorialspoint is the largest tutorial library.";
            Parse topParses[] = ParserTool.parseLine(pSentence, parser, 1); 

            for (Parse p : topParses) 
               p.show();        

            //Collect only Names and Adjectives
            List<Parse> chunks = new ArrayList<Parse>();
            for (Parse p : topParses)
            {
                //chunks = getChunks(p, "NN");
                chunks = getChunks(p, "");
            }

            //for (Parse c: chunks)
            for (int i=0; i<chunks.size(); i++)
            {
                ssoPOSTag newTag = new ssoPOSTag();

                //Span csp = c.getSpan();
                Span csp = chunks.get(i).getSpan();//span
                String sType = chunks.get(i).getType();
                int iStart = csp.getStart();
                int iEnd   = csp.getEnd();

                //System.out.println(sType + ": " + pSentence.substring(iStart, iEnd));
                newTag.tag  = sType.toUpperCase();
                newTag.word = pSentence.substring(iStart, iEnd);
                aWords.add(newTag);
                //aWords.add(sType + ":"  + pSentence.substring(iStart, iEnd));
            }

            return aWords;
        }
        catch(Exception e)
        {
            return aWords;
        }
    }


    // This will be used if chunking.bin not exist
    // For this method WhiteSpace tokenization will be used
    //
    // MINIMUM REQUIREMENT:  POSTTagger.bin model file
    // 
    // this doesn't distinguish names or locations
    public static ArrayList<ssoPOSTag> tokenize_n_tag_w_postagger(String pLang, String pSentence) 
    {
        ArrayList<ssoPOSTag> aWords = new ArrayList<ssoPOSTag>();

        try
        {
            POSTaggerME tagger = null;
            //InputStream modelStream = new FileInputStream("C:/NEOTEMP/CODE/SHIPSHUK/global/nlp/models/apache-nlp/en-pos-maxent.bin");//Tagger File

            POSModel model = getPOSModel(pLang);

            if (model==null)
                return aWords;

            tagger = new POSTaggerME(model);

            //ArrayList<String> aSentences = new ArrayList<String>();

            //aSentences = detectSentences(pLang, pSentence);

            //for (String sentence : aSentences) 
            //{
                String[] aTokens;
                ArrayList<String> Tokens = tokenize(pLang, pSentence);
                if (Tokens.size()==0)//if token model not found
                {
                    //String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);//The words will be split with white space. However, the words (one) like New York will be given as seperate words
                    aTokens = WhitespaceTokenizer.INSTANCE.tokenize(pSentence);//The words will be split with white space. However, the words (one) like New York will be given as seperate words
                }
                else
                {
                    aTokens = Tokens.toArray(new String[0]);
                }

                String[] tags = tagger.tag(aTokens);

                for (int i = 0; i < aTokens.length; i++) 
                {
                    ssoPOSTag newTag = new ssoPOSTag();

                    String word = aTokens[i].trim();
                    String tag = tags[i].trim();

                    newTag.tag  = tag.toUpperCase();
                    newTag.word = word;
                    aWords.add(newTag);
                    //aWords.add(tag + ":" + word + "  ");
                    //System.out.print(tag + ":" + word + "  ");
                }

            //}

            return aWords;
        }
        catch(Exception e)
        {
            return aWords;
        }
    }

    public static ArrayList<ssoPOSTag> getTags(String pText)
    {
        ArrayList<ssoPOSTag> AllTags = new ArrayList<ssoPOSTag>();

        // Step 1. Detect Language Code
        // Step 2. Split Sentences
        // Step 3. Split Tokens (words)
        // Step 4. Tag Tokens

        String sLang = nlpapi.detectLanguage(pText);
        if (sLang.trim().length()==0)
            gLastLanguage = "en";//default
        else
            gLastLanguage = sLang;

        ArrayList<String> sentences = new ArrayList<String>();

        //Split sentences
        sentences = detectSentences(sLang, pText);

        for (String sentenceN: sentences)
        {
            ArrayList<ssoPOSTag> tags = new ArrayList<ssoPOSTag>();

            //Check if chunker supported. If not go with pos tagger. 
            String supportType = getTokenizationSupportType(sLang);
            if (supportType.trim().length()>0)
            {
                //supported
                if (supportType.equals(LANGUAGE_SUPPORT_W_TAGGER)==true)
                {
                    //
                    tags = tokenize_n_tag_w_postagger(sLang, sentenceN);
                }
                else if (supportType.equals(LANGUAGE_SUPPORT_W_CHUNK)==true)
                {
                    //
                    tags = tokenize_n_tag_w_chunker(sLang, sentenceN);
                }

                if (tags.size()>0)
                    AllTags.addAll(tags);

            }

        }

        if (sLang.equals("en")==true)
        {
            for (String sentenceN: sentences)
            {
                ArrayList<String> aNames = new ArrayList<String>();
                ArrayList<ssoPOSTag> tags   = new ArrayList<ssoPOSTag>();

                //Add Names 
                aNames = detectNames(sLang, sentenceN);
                if (aNames!=null)
                {
                    for (String sName: aNames)
                    {
                        ssoPOSTag newTag = new ssoPOSTag();
                        newTag.tag  = POS_TAG_PERSON;//Noun Person
                        newTag.word = sName;

                        AllTags.add(newTag);
                    }
                }

                //Add Locations
                //Add Names 
                ArrayList<String> aLocations = new ArrayList<String>();
                aLocations = detectLocations(sLang, sentenceN);
                if (aLocations!=null)
                {
                    for (String sName: aLocations)
                    {
                        ssoPOSTag newTag = new ssoPOSTag();
                        newTag.tag  = POS_TAG_LOCATION;//Noun Location
                        newTag.word = sName;

                        AllTags.add(newTag);
                    }
                }

            }
            
        }

        return AllTags;
    }

    public static ArrayList<String> getWordsByTagType(String pLang, ArrayList<ssoPOSTag> pPOSTags, int pTagType)
    {
        ArrayList<String> words    = new ArrayList<String>();
        ArrayList<ssoTag> langTags = new ArrayList<ssoTag>();

        langTags = getLanguageTags(pLang);

        for (ssoPOSTag posTagN:pPOSTags)
        {
            //Check if this is among noun tags
            for (ssoTag langTagN: langTags)
            {
                if (langTagN.type==pTagType)//if noun/adjective 
                {
                    if (posTagN.tag.toUpperCase().trim().equals(langTagN.tag.toUpperCase().trim())==true)
                    {
                        words.add(posTagN.word);
                        break;
                    }
                }
            }
        }

        return words;
        
    }
    
    public static ArrayList<Parse> getChunks(Parse p, String pTag)
    {
        ArrayList<Parse> nounPhrases = new ArrayList<Parse>();
        //List<Parse> nouns = new ArrayList<Parse>();

        //if (p.getType().equals("NP"))
        if ( (p.getType().equals(pTag)) || (pTag.equals("")) )
        {
            if (p.getType().equals("TK")==false)//token general tag
            {
                nounPhrases.add(p);
            }
        }

        for (Parse child : p.getChildren()) 
        {
            ArrayList<Parse> nounPhrasesFound = new ArrayList<Parse>();
            nounPhrasesFound = getChunks(child, pTag);

            if (nounPhrasesFound.size()>0)
            {
                nounPhrases.addAll(nounPhrasesFound);
            }
            
        }
        
        return nounPhrases;
    }

}


