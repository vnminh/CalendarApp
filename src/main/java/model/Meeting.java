package model;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "meeting_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Meeting {
	@Id
	@GeneratedValue(generator = "my-generator")
    @GenericGenerator(name = "my-generator", 
      parameters =@Parameter(name = "prefix", value = "ME"), 
      strategy = "model.MeetingIDGenerator")
	private String id;
	private String name;
	private Time timeFrom;
	private Time timeTo;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String location;
	@OneToMany(mappedBy = "meeting",  orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Reminder> listRmd = new ArrayList<Reminder>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Time getFrom() {
		return timeFrom;
	}
	public void setFrom(Time from) {
		this.timeFrom = from;
	}
	public Time getTo() {
		return timeTo;
	}
	public void setTo(Time to) {
		this.timeTo = to;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Reminder> getListRmd() {
		return listRmd;
	}
	public void setListRmd(List<Reminder> rmds)
	{
		this.listRmd = rmds;
	}
	public abstract boolean containUser(String id);
	public abstract void setUser(List<User> user);
	public abstract void deleteUser(User user);
	public abstract void addUser(User user);
	public abstract void addListUser(List <User> users);
}
