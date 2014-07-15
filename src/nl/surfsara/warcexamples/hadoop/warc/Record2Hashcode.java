/**
 * Created by wdwind on 14-7-14.
 */
package nl.surfsara.warcexamples.hadoop.warc;

import java.io.*;

import org.apache.http.client.utils.DateUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.jwat.warc.WarcRecord;
import org.jwat.common.HttpHeader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record2Hashcode {
    private static final int threshold = 8;
    private boolean printAndSave = false;

    private String hc = "";
    private WarcRecord record;

    Record2Hashcode(){}

    public static String getHashcode(WarcRecord record){
        try{
            HttpHeader httpHeader = record.getHttpHeader();
            if (httpHeader == null) {
                return "";
            }else if(httpHeader.contentType != null && httpHeader.contentType.contains("image")){
                InputStream inputStream1 = null;
                inputStream1 = record.getPayload().getInputStream();
		//byte[] b1 = IOUtils.toByteArray(inputStream1);
                //byte[] encoded = Base64.encodeBase64(b1);
                //String finger = new String(encoded, "US-ASCII");

                String finger = produceFingerPrint(inputStream1);
                if ("".equals(finger))
                    return "";

                Date tempD = record.header.warcDate;
                String date = "";

                String lastModified = record.getHttpHeader().getHeader("last-modified").value;

                if (lastModified != null) {    // in case the header isn't set
                    tempD = DateUtils.parseDate(lastModified);
                }

                DateFormat df = new SimpleDateFormat("MMddyyyy");
                if (tempD != null)
                    date = df.format(tempD);

                //String url = record.header.warcTargetUriUri.getHost() + record.header.warcTargetUriStr;
                String url = URLEncoder.encode(record.header.warcTargetUriStr);

                return finger + "|" + date + "|" + url;
        }else{
            return "";
        }
        }
        catch(Exception e){
		//return e.getMessage();
            return "";
        }
    }

    public static String produceFingerPrint(InputStream input) throws Exception{
//        try {
            BufferedImage source = ImageIO.read(input);
            if (source == null){
                System.out.println("no image");
		return "";
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
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        }

        //return "";
    }

}
