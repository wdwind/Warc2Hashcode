package nl.surfsara.warcexamples.hadoop.warc;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Created by wdwind on 14-7-22.
 */

public class ImagesInfo implements Comparable<ImagesInfo>{

    protected class ImageInfo{
        private int width;
        private int height;

        private String hash;
        private String pHash;

        private String url;

        ImageInfo(int width, int height, String hash, String pHash, String url){
            this.width = width;
            this.height = height;
            this.hash = hash;
            this.pHash = pHash;
            this.url = url;
        }

        @Override
        public String toString(){
//            StringBuilder result = new StringBuilder();
//            String NEW_LINE = System.getProperty("line.separator");
//
//            result.append(" Width: " + width + NEW_LINE);
//            result.append(" Height: " + height + NEW_LINE);
//            result.append(" Hash: " + hash + NEW_LINE );
//            result.append(" pHash: " + pHash + NEW_LINE);
//            //Note that Collections and Maps also override toString
//            result.append(" url: " + url + NEW_LINE);

            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    private int num = 1;
    private List<ImageInfo> imageList = new ArrayList<ImageInfo>();
    private String month;

    ImagesInfo(String month){
        this.month = month;
    }

    public void increment () { ++num;}
    public int get(){return num;}

//    public void increment(int width, int height, String hash, String pHash, String url){
//        increment();
//        imageList.add(new ImageInfo(width, height, hash, pHash, url));
//    }

    public void updateImagelist(int width, int height, String hash, String pHash, String url){
        imageList.add(new ImageInfo(width, height, hash, pHash, url));
    }

//    @Override
//    public String toString(){
//        StringBuilder result = new StringBuilder();
//        String newLine = System.getProperty("line.separator");
//
//        Field[] fields = this.getClass().getDeclaredFields();
//
//        for (Field field : fields) {
//            result.append("  ");
//            try {
//                result.append(field.getName());
//                result.append(": ");
//                if (!"imageList".equals(field.getName()))
//                    //requires access to private field:
//                    result.append(field.get(this));
//            }
//            catch (IllegalAccessException ex) {
//                System.out.println(ex);
//            }
//            result.append(newLine);
//        }
//
//        StringBuilder sb = new StringBuilder();
//
//        for(ImageInfo ii : imageList)
//        {
//            sb.append(ii.toString());
//            sb.append("\t");
//        }
//        result.append(sb.toString());
//        result.append(newLine);
//
//        return result.toString();
//    }

    @Override
    public int compareTo(ImagesInfo iThat){
        return -Integer.compare(this.get(), iThat.get());
    }

    @Override
    public String toString(){
        Gson g = new Gson();
        return g.toJson(this);
    }

    public static void main(String[] args){
        ImagesInfo ii = new ImagesInfo("test1");

//        ii.increment(1,2,"1","2","t");
//        ii.increment(4,5, "a","b","c");

        System.out.println(ii.toString());
    }
}
