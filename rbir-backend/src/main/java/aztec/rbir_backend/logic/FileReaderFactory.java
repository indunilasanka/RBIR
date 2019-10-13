package aztec.rbir_backend.logic;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by asankai on 28/05/2017.
 */
public final class FileReaderFactory {

    public static String[] documentTypes = {"RESEARCH","ACT","PROCUREMENT","APPOINTMENT","GAZETTE","CIRCULAR"};

    public static String read(String filePath){
        String fileType = getFileExtension(filePath);
        MyFileReader reader = getFileReaderInstance(fileType);
        String content = null;
        if(reader != null) {
            content = reader.read(filePath);
        }
        return content;
    }

    public static MyFileReader getFileReaderInstance(String fileType){
        switch (fileType){
            case "PDF":
            case "pdf":
                return new PDFMyFileReader();
            case "CSV":
            case "csv":
                return new CSVMyFileReader();
            case "DOCX":
            case "docx":
                return new DOCXMyFileReader();
            case "PPTX":
            case "pptx":
                return new PPTXMyFileReader();
            case "TXT":
            case "txt":
                return new TXTMyFileReader();
            case "XLS":
            case "xls":
                return new XLSMyFileReader();
            case "XLSX":
            case "xlsx":
                return new XLSXMyFileReader();

        }
        return null;
    }

    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String IdentifyType(String filePath,String Content)
    {
        String TopContent = Content.substring(0,200).toUpperCase();
        String metaContent = ReadMetaData(filePath);
        //check in top of the document content
        for (String item : documentTypes) {
            if(TopContent.contains(item))
            {
                return item;
            }
        }
        //check in the metadata of the document
        for (String item : documentTypes) {
            if(metaContent.contains(item))
            {
                return item;
            }
        }
        return "OTHER";
    }

    public static String ReadMetaData(String filePath)
    {
        String metaContent = "";

        try {
            Metadata metadata = new Metadata();
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(1000000000);
            InputStream stream = new FileInputStream(filePath);
            ParseContext context = new ParseContext();
            parser.parse(stream,handler,metadata,context);

            for(String s : metadata.names())
            {
                metaContent = metaContent + " " + s + " " + metadata.get(s);
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return metaContent.toUpperCase();
    }

}
