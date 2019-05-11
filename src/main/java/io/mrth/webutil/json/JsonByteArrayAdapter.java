package io.mrth.webutil.json;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JsonByteArrayAdapter extends XmlAdapter<String, byte[]> {


	@Override
	public byte[] unmarshal(String v) throws Exception {
		return Base64.decodeBase64(v);
	}

	@Override
	public String marshal(byte[] v) throws Exception {
		return Base64.encodeBase64String(v);
	}

}
