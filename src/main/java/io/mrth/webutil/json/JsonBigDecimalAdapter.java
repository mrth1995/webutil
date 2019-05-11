package io.mrth.webutil.json;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class JsonBigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		if (StringUtils.isNotEmpty(v)) {
			return new BigDecimal(v);
		} else {
			return null;
		}
	}

	@Override
	public String marshal(BigDecimal v) throws Exception {
		if (v != null) {
			return v.toPlainString();
		} else {
			return null;
		}
	}

}
