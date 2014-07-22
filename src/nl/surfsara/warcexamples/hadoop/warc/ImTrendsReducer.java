package nl.surfsara.warcexamples.hadoop.warc;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ImTrendsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text pKey, Iterable<Text> pValues, Context pContext)
            throws IOException, InterruptedException {

        Map<String, ImagesInfo> hm = new HashMap<String, ImagesInfo>();

        for (Text t : pValues) {

            String s = t.toString();
            String[] items = s.split("\\|");

            //int count = hm.containsKey(items[0]) ? hm.get(items[0]).num : 0;
            ImagesInfo ii = hm.get(items[0]);

            if (ii == null){
                hm.put(items[0], new ImagesInfo(pKey.toString()));
                hm.get(items[0]).updateImagelist(Integer.parseInt(items[2]), Integer.parseInt(items[3]), items[0], items[1], items[5]);
            }else {
                //ii.increment(Integer.parseInt(items[2]), Integer.parseInt(items[3]), items[0], items[1], items[4]);
                ii.increment();
                ii.updateImagelist(Integer.parseInt(items[2]), Integer.parseInt(items[3]), items[0], items[1], items[5]);
            }
        }

        hm = MapUtil.sortByValue(hm);

        pContext.write(pKey, new Text(map2String(hm)));
    }

    private String map2String(Map map){
        String out = "";
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //out += pairs.getKey();
            //out += "\t";
            out += pairs.getValue().toString();
            out += "\t";
            it.remove(); // avoids a ConcurrentModificationException
        }

        return out;
    }


}