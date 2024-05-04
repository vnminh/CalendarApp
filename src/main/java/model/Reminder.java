package model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class Reminder {
	@Id
	@GeneratedValue(generator = "my-generator")
    @GenericGenerator(name = "my-generator", 
      parameters =@Parameter(name = "prefix", value = "RM"), 
      strategy = "model.MyIDGenerator")
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meeting_id", referencedColumnName = "id")
	private Meeting meeting;
	private int minutesBeforeAlarm;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Meeting getMeeting() {
		return meeting;
	}
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	public int getSecondBeforeAlarm() {
		return minutesBeforeAlarm;
	}
	public void setSecondBeforeAlarm(int minutesBeforeAlarm) {
		this.minutesBeforeAlarm = minutesBeforeAlarm;
	}
}
