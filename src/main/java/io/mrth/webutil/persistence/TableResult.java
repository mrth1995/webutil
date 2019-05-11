package io.mrth.webutil.persistence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TableResult<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<E> data;
	private Long rowCount;

	public TableResult() {
	}

	public TableResult(List<E> data, Long rowCount) {
		this.data = data;
		this.rowCount = rowCount;
	}

	public List<E> getData() {
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

}
