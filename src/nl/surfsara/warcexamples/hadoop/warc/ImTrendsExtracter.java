/**
 *
 */
package nl.surfsara.warcexamples.hadoop.warc;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import org.jwat.warc.WarcRecord;

/**
 * Map function that from a WarcRecord extracts all images' hashcode. The resulting key,
 * values: time (yyyyMM), hashcode.
 * 
 * @author Feng Wang
 */
class ImTrendsExtracter extends Mapper<LongWritable, WarcRecord, Text, Text> {
	private Text Key = new Text();
	
	private static enum Counters {
		CURRENT_RECORD, NUM_HTTP_RESPONSE_RECORDS
	}

	@Override
	public void map(LongWritable key, WarcRecord value, Context context) throws IOException, InterruptedException {
		context.setStatus(Counters.CURRENT_RECORD + ": " + key.get());

		Record2Hashcode r1 = new Record2Hashcode();

		String hc = r1.getHashcode(value);
		if (!"".equals(hc) && hc != null) {
			String[] items = hc.split("\\|");//.substring(0, 3);
			//Text k = new Text(items[4]);
            Key.set(items[4].substring(0, 6) + items[0]);
			context.getCounter(Counters.NUM_HTTP_RESPONSE_RECORDS).increment(1);
			context.write(Key, new Text(hc));
		}
	}

}
