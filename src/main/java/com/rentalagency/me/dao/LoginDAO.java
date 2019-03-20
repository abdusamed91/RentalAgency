package com.rentalagency.me.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rentalagency.me.bean.ConnectionProvider;
import com.rentalagency.me.bean.LoginBean;
import com.rentalagency.me.model.ParkingRequest;
import com.rentalagency.me.model.ParkingRequest.rowSpot;
import com.rentalagency.me.model.User;
import com.rentalagency.me.model.User.Role;
import com.rentalagency.me.model.UserAccount;

public class LoginDAO extends DAO{
	
	private final static Logger log = LoggerFactory.getLogger(LoginDAO.class);


	public void create(UserAccount ua,String role) {
		try {
			begin();
			User us = new User();
			us.setName(ua.getUsername());
			us.setRole(Role.valueOf(role));
			
			ua.setUser(us);
			us.setUserAccount(ua);	
			
					
			ParkingRequest prq = new ParkingRequest();
			prq.setDescription("One of Many Request: " + ua.getUsername() );
			prq.setRsp(rowSpot.NINE);
			prq.setEndTime(new Date());
			prq.setUser(us);

			us.getRequestSet().add(prq);			
			
			getSession().persist(ua);
			commit();
			
			
			
			log.info("Account creation Successful");
		} catch (HibernateException e) {
			log.warn("Unable To Add Message, Failure");
			rollback();
		} finally {
			close();
		}
	}
	
	

	public static boolean validate(LoginBean bean){  
		boolean status=false;  
		try{  
		Connection con = ConnectionProvider.getCon();  
		              
		PreparedStatement ps = con.prepareStatement(  
		    "select * from useraccount_table where username=? and password=?");  
		  
		ps.setString(1,bean.getEmail());  
		ps.setString(2, bean.getPass());  
		              
		ResultSet rs=ps.executeQuery();  
		status=rs.next();  
  
		}catch(Exception e){}  
		  
		return status;  
		}
	
	/*
	 * Get User Account by supplying id;
	 */
	@Test
	public UserAccount getUserAccountByUserName(String username) {
		UserAccount ua = null;
		try {
			begin();
			Criteria cr = getSession().createCriteria(UserAccount.class);
			Criterion userModify = Restrictions.eq("username", username);
			cr.add(userModify);
			cr.setMaxResults(1);
			
			ua = (UserAccount) cr.uniqueResult();
			assert(ua.getPassword() == "bomber");
			
		} catch(HibernateException e) {
			log.warn("Unable to modify user account");
			rollback();
		}finally {
			close();
		}
		return ua;		
	}

	/*
	 * Modify User Account password
	 */
	public void modifyUserAccountById(int user_id, String newPassword, String newUsername) {
		try {
			begin();
			Criteria cr = getSession().createCriteria(UserAccount.class);
			Criterion userModify = Restrictions.eq("user_id", new Integer(user_id));
			cr.add(userModify);
			cr.setMaxResults(1);
			
			UserAccount user = (UserAccount) cr.uniqueResult();
			user.setUsername(newUsername);
			user.setPassword(newPassword);
			
			getSession().update(user);
			
			commit();			
			
		} catch(HibernateException e) {
			log.warn("Unable to modify user account");
			rollback();
		}finally {
			close();
		}
	}
	
	public void deleteUserAccountById(int user_id) {
		try {
			begin();
			Criteria cr = getSession().createCriteria(UserAccount.class);
			Criterion userModify = Restrictions.eq("user_id", new Integer(user_id));
			cr.add(userModify);
			cr.setMaxResults(1);
			
			UserAccount user = (UserAccount) cr.uniqueResult();
		
			if(user != null) {
				getSession().delete(user);
			}
			
			
			commit();			
			
		} catch(HibernateException e) {
			log.warn("Unable to modify user account");
			rollback();
		}finally {
			close();
		}
	}
}
