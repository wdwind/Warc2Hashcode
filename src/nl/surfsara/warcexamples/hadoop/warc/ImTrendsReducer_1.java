package nl.surfsara.warcexamples.hadoop.warc;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

class ImTrendsReducer_1 extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text pKey, Iterable<Text> pValues, Context pContext)
            throws IOException, InterruptedException {

        String out = "";
        Integer num = 0;

        for (Text t : pValues) {
            num ++;

            out += t.toString();
            out += "\t";
        }

        out = num.toString() + "\t" + out;

        pContext.write(pKey, new Text(out));
    }
}