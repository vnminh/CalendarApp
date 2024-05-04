package model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.persistence.*;

import bll.MaApp;
@Entity
@DiscriminatorValue("App")
public class Appointment extends Meeting {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public void setUser(List<User> user)
	{
		this.user = user.get(0);
	}
	@Override
	public boolean containUser(String id)
	{
		return this.user.getId().equals(id);
	}
	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		this.setUser(user);
	}
	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
	}
	@Override
	public void addListUser(List<User> users) {
		// TODO Auto-generated method stub
		this.setUser(users.get(0));
	}
}
