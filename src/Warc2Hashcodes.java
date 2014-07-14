/**
 * Created by wdwind on 14-6-11.
 */

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.DateUtils;

import org.jwat.warc.WarcReader;
import org.jwat.warc.WarcReaderFactory;
import org.jwat.warc.WarcRecord;
import org.jwat.common.HttpHeader;

import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Warc2Hashcodes {
    private String warcFile = "IAH-20080430204825-00000-blackbook.warc";
    private static final int threshold = 8;
    private boolean printAndSave = false;

    Warc2Hashcodes(){}

    Warc2Hashcodes(String s){
        warcFile = s;
    }

    Warc2Hashcodes(String s, boolean printAndSave){
        warcFile = s;
        this.printAndSave = printAndSave;
    }

    public List<String> getHashcode(){
        return getHashcode(printAndSave);
    }

    private List<String> getHashcode(boolean of){
        List<String> hashCodes = new ArrayList<String>();

        File file = new File( warcFile );
        try {
            InputStream in = new FileInputStream( file );

            int records = 0;
            int errors = 0;

            WarcReader reader = WarcReaderFactory.getReader( in );
            WarcRecord record;
            int num = 0;

            while ( (record = reader.getNextRecord()) != null ) {
                HttpHeader httpHeader = record.getHttpHeader();
                if (httpHeader == null) {}
                else if (httpHeader.contentType != null && httpHeader.contentType.contains("image"))
                {
                    String hc = "";
                    if (of){
                        String fileType = httpHeader.contentType.substring(httpHeader.contentType.indexOf("/") + 1);
                        printRecord(record, num, fileType);
                        hc = getHashcode("testImg" + num + "." + fileType);
                        ++num;
                    }else {
                        hc = getHashcode(record);
                    }
                    if (!hc.equals(""))
                        hashCodes.add(hc);
                }
                ++records;

//                if (!record.isValidBlockDigest) {
//                    errors += 1;
//                }
            }

            System.out.println("--------------");
            System.out.println("       Records: " + records);
            //System.out.println("        Errors: " + errors);
            System.out.println("--------------");
            reader.close();
            in.close();
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return hashCodes;
    }

    private String getHashcode(WarcRecord record){
        InputStream inputStream1 = null;
        inputStream1 = record.getPayload().getInputStream();
        String finger = produceFingerPrint(inputStream1);
        String date = record.header.warcDateStr;

        String lastModified = record.getHttpHeader().getHeader("last-modified").value;
        if (lastModified != null) {    // in case the header isn't set
            Date tempD = DateUtils.parseDate(lastModified);
            DateFormat df = new SimpleDateFormat("MMddyyyy");
            date = df.format(tempD);
        }

        //String url = record.header.warcTargetUriUri.getHost() + record.header.warcTargetUriStr;
        String url = URLEncoder.encode(record.header.warcTargetUriStr);
        //URLEncoder.encode("asdf", "UTF-8");

        return finger + "|" + date + "|" + url;
    }

    private String getHashcode(String file) throws FileNotFoundException {
        File f = new File(file);
        InputStream inputStream1 = new FileInputStream(f);

        return produceFingerPrint(inputStream1);
    }

    private void printRecord(WarcRecord record, int num, String fileType) throws IOException {
        //String warcContent2 = IOUtils.toString(record.getPayload().getInputStream());

        InputStream inputStream1 = null;
        inputStream1 = record.getPayload().getInputStream();

        // Process in program
        //String fp = produceFingerPrint(inputStream1);

        //Output to file
        String fileName = "testImg"+num+"."+fileType;
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(new File(fileName));
        IOUtils.copy(inputStream1, outputStream);
        outputStream.close();

        inputStream1.close();

        System.out.println("--------------");
        System.out.println("   BlockDigest: " + record.computedBlockDigest);
        System.out.println(" PayloadDigest: " + record.computedPayloadDigest);
        System.out.println("       Payload: " + record.getPayload().getTotalLength());
        //System.out.println(" fp: " + fp);
        //System.out.println("       Version: " + record.bMagicIdentified + " " + record.bVersionParsed + " " + record.major + "." + record.minor);
        //System.out.println("       TypeIdx: " + record.warcTypeIdx);
        //System.out.println("          Type: " + record.warcTypeStr);
        //System.out.println("      Filename: " + record.warcFilename);
        //System.out.println("     Record-ID: " + record.warcRecordIdUri);
        //System.out.println("          Date: " + record.warcDate);
        //System.out.println("Content-Length: " + record.contentLength);
        //System.out.println("  Content-Type: " + record.contentType);
        //System.out.println("     Truncated: " + record.warcTruncatedStr);
        //System.out.println("   InetAddress: " + record.warcInetAddress);
        //System.out.println("  ConcurrentTo: " + record.warcConcurrentToUriList);
        //System.out.println("      RefersTo: " + record.warcRefersToUri);
        //System.out.println("     TargetUri: " + record.warcTargetUriUri);
        //System.out.println("   WarcInfo-Id: " + record.warcWarcInfoIdUri);
        //System.out.println(" Payload: " + warcContent2);
        //System.out.println("IdentPloadType: " + record.warcIdentifiedPayloadType);
        //System.out.println("       Profile: " + record.warcProfileStr);
        //System.out.println("      Segment#: " + record.warcSegmentNumber);
        //System.out.println(" SegmentOrg-Id: " + record.warcSegmentOriginIdUrl);
        //System.out.println("SegmentTLength: " + record.warcSegmentTotalLength);
    }

    public static String produceFingerPrint(InputStream input){
        try {
            BufferedImage source = ImageIO.read(input);
            if (source == null){
                System.out.println("no image");
            }else{
                //System.out.println("image exist, height:" + source.getHeight());

                if (source.getHeight() < threshold || source.getWidth() < threshold)
                    return "";

                int width = 8;
                int height = 8;

                BufferedImage imSmall = ImageHelper.resize(source, width, height, false);

                int[] pixels = new int[width * height];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        pixels[i * height + j] = ImageHelper.rgbToGray(imSmall.getRGB(i, j));
                    }
                }

                int avgPixel = ImageHelper.average(pixels);

                int[] comps = new int[width * height];
                for (int i = 0; i < comps.length; i++) {
                    if (pixels[i] >= avgPixel) {
                        comps[i] = 1;
                    } else {
                        comps[i] = 0;
                    }
                }

                StringBuffer hashCode = new StringBuffer();
                for (int i = 0; i < comps.length; i+= 4) {
                    int result = comps[i] * (int) Math.pow(2, 3) + comps[i + 1] * (int) Math.pow(2, 2) + comps[i + 2] * (int) Math.pow(2, 1) + comps[i + 2];
                    hashCode.append(ImageHelper.binaryToHex(result));
                }

                return hashCode.toString() + "|" + source.getHeight() + "|" + source.getWidth();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }


    public static void main(String[] args) {
        Warc2Hashcodes t1 = new Warc2Hashcodes("IAH-20080430204825-00000-blackbook.warc", false);

        List<String> hcs;
        hcs = t1.getHashcode();

        System.out.println(hcs);
        System.out.println(hcs.size());
    }

}
