package model;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
@Entity
public class User {
	
	@Id
	@GeneratedValue(generator = "my-generator")
    @GenericGenerator(name = "my-generator", 
      parameters =@Parameter(name = "prefix", value = "US"), 
      strategy = "model.MyIDGenerator")
	private String id;
	private String name;
	@Column(unique = true)
	private String email;
	private String password;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Appointment> listApp = new ArrayList<Appointment>();
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "User_GroupMeeting",
	joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
	inverseJoinColumns = @JoinColumn(name = "GrM_id",referencedColumnName = "id"))
	private List<GroupMeeting> listGrM = new  ArrayList<GroupMeeting>();
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Appointment> getListApp() {
		return listApp;
	}
	public List<GroupMeeting> getListGrM() {
		return listGrM;
	}
	
}
