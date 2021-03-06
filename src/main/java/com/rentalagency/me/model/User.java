package com.rentalagency.me.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * User object which is mapped OneToOne to UserAccount
 * User contains attributes associated with a person such as
 * his name and his role in the organization. 
 * The entity is stored in the database as "user_table"
 * A user can create many request. Request being abstract,
 * future request can be easily be incorporated.
 * 
 * @author abdusamed
 *
 */
@Entity
@Table(name = "user_table")
public class User {

	// Attribute value is obtained from userAccount object associated this object
	@Id
	@GenericGenerator(name = "generator", strategy = "foreign", parameters = {
			@Parameter(name = "property", value = "userAccount") })
	@GeneratedValue(generator = "generator")
	private int user_id;

	// The associated userAccount with this object
	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private UserAccount userAccount;

	// Contains the set of request associated with this request 
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Request> requestSet = new HashSet<Request>();

	private String name;
	private Role role;

	/**
	 * In the current implementation, we have two roles, Regular & Manager. 
	 * If required more roles, it will be required to append them here
	 * @author abdusamed
	 *
	 */
	public enum Role {
		REGULAR("REGULAR"), MANAGER("MANAGER");

		private String value;

		private Role(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Set<Request> getRequestSet() {
		return requestSet;
	}

	public void setRequestSet(Set<Request> requestSet) {
		this.requestSet = requestSet;
	}

}
