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

        //private String hash;
        private String pHash;

        private String url;

        ImageInfo(int width, int height, /*String hash,*/ String pHash, String url){
            this.width = width;
            this.height = height;
            //this.hash = hash;
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
    private String hash;

    ImagesInfo(String month, String hash){
        this.month = month;
        this.hash = hash;
    }

    public void increment () { ++num;}
    public int get(){return num;}

    public void updateImagelist(int width, int height, /*String hash,*/ String pHash, String url){
        imageList.add(new ImageInfo(width, height, /*hash,*/ pHash, url));
    }

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
        ImagesInfo ii = new ImagesInfo("test1", "asd");

//        ii.increment(1,2,"1","2","t");
//        ii.increment(4,5, "a","b","c");

        System.out.println(ii.toString());
    }
}
