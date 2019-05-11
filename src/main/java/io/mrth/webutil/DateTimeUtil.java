package io.mrth.webutil;

import javax.enterprise.context.ApplicationScoped;
import java.util.Calendar;
import java.util.Date;

@ApplicationScoped
public class DateTimeUtil {

	public Date now() {
		return new Date();
	}
	
	public Calendar nowCalendar() {
		return Calendar.getInstance();
	}
}
