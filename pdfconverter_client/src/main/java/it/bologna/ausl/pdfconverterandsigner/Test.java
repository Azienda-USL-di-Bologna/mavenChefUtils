package it.bologna.ausl.pdfconverterandsigner;

import java.io.*;
import org.apache.http.client.HttpResponseException;
import org.apache.tika.mime.MimeTypeException;

/**
 *
 * @author Administrator
 */
public class Test {
    public static void main(String[] args) throws IOException, MimeTypeException {
//        FileInputStream fileInputStream = new FileInputStream("c:/tmp/dislocazioni interessate da determine.xls");
//        byte[] inputStreamBytes = UtilityFunctions.inputStreamToBytes(fileInputStream);
//        TikaInputStream tikais = TikaInputStream.get(inputStreamBytes);
////        TikaInputStream tikais = TikaInputStream.get(new File("c:/tmp/dislocazioni interessate da determine.xls"));
//        TikaConfig config = TikaConfig.getDefaultConfig();
//        final Detector DETECTOR = new DefaultDetector(MimeTypes.getDefaultMimeTypes());
//        final Metadata metadata = new Metadata();
////        MediaType mediaType = config.getMimeRepository().detect(tikais, new Metadata());
////        System.out.println(DETECTOR.detect(tikais, metadata).toString());
//        String mediaTypeString = DETECTOR.detect(tikais, metadata).toString();
//        MimeType mimeType = config.getMimeRepository().forName(mediaTypeString);
//        String extension = mimeType.getExtension().substring(1);
//        System.out.println(mediaTypeString);
//        System.out.println(extension);
//        System.exit(0);
//        //MediaType mediaType = MimeTypes.getDefaultMimeTypes().detect(tikais, new Metadata());
////        System.out.println(mediaType.getSubtype());
//                Tika tika = new Tika();
//                tika.detect(tikais);
//                String fileType = tika.detect(tikais);
//                System.out.println(fileType);
//        System.exit(0);
        PdfConvertClient pdfc = new PdfConvertClient("https://gdml.internal.ausl.bologna.it/firmasemplice/PdfConvert", new File("tempdir"), null);
//      PdfConvertClient pdfc = new PdfConvertClient("http://localhost:8082/firmasemplice/PdfConvert", new File("tempdir"), null);
        File file = new File("C:/Chrysanthemum.jpg");
        
//        pdfc.convert("cafsaf", new File("c:/tmp/provaaaaaaa.pdf"), true, null);
        byte[] convert = null;
        try {
            convert = pdfc.convert(new FileInputStream(file), true, null);
            DataOutputStream dataos = new DataOutputStream(new FileOutputStream(file.getAbsoluteFile() + ".pdf"));
            dataos.write(convert);
        }
        catch (HttpResponseException ex) {
            System.out.println("Errore nel formato file:\n" + ex.getStatusCode() + "\n" + ex.getMessage());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
