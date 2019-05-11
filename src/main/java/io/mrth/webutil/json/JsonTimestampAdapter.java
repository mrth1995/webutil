package io.mrth.webutil.json;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonTimestampAdapter extends XmlAdapter<String, Date> {

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Date unmarshal(String v) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(v);
	}

}
