package model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import java.util.*;

@Entity
@DiscriminatorValue("GrM")
public class GroupMeeting extends Meeting {
	
	@ManyToMany(mappedBy = "listGrM", cascade = CascadeType.ALL)
	private List<User> listUser = new ArrayList<User>();

	public List<User> getListUser() {
		return listUser;
	}

	public void setListUser(List<User> listUser) {
		this.listUser = listUser;
	}
	@Override
	public boolean containUser(String id)
	{
		for (User u: this.listUser)
		{
			if (u.getId().equals(id))
			{
				return true;
			}
		}
		return false;
	}
	@Override
	public void setUser(List<User> user)
	{
		this.setListUser(user);
	}
	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		this.listUser.add(user);
	}
	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		this.listUser.remove(user);
	}
	@Override
	public void addListUser(List<User> users) {
		// TODO Auto-generated method stub
		this.listUser.addAll(users);
	}
}
