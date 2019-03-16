package com.rentalagency.me.Test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rentalagency.me.dao.QueryDAO;
import com.rentalagency.me.model.ParkingRequest;
import com.rentalagency.me.model.ParkingRequest.colSpot;
import com.rentalagency.me.model.ParkingRequest.rowSpot;
import com.rentalagency.me.model.Request;
import com.rentalagency.me.model.User;
import com.rentalagency.me.model.UserAccount;

public class QueryTests {

	private ConfigurableApplicationContext context;

	private QueryDAO querydao;

	@Before
	public void initApplicationContext() {
		context = new ClassPathXmlApplicationContext("/servlet-context.xml");

		System.out.println(context.getBeanDefinitionCount());
		querydao = context.getBean(QueryDAO.class);
	}

	@After
	public void closeApplicationContext() {
		if (context != null) {
			context.close();
			context = null;
		}
	}

	// @Test
	public void notNull() {
		assert (context != null);
		assert (querydao != null);
	}
	
//	@Test
	public void submitRequestByIdTest() {
		ParkingRequest prq = new ParkingRequest();
		prq.setDescription("Parking Request from Test for account 2");
		prq.setCsp(colSpot.E);
		prq.setRsp(rowSpot.TWO);
		prq.setStatus(false);
		querydao.submitRequestById(prq,2);
	}


	
//	@Test
	public void getListOfUsersTest() {
		List<User> l = querydao.getListOfUsers();
		assert(l.size() != 0);
		System.out.println(l.size());
		for(User u : l) {
			System.out.println(u.getUser_id());
		}
	}

//	@Test
	public void getListOfUserAccount() {
		List<UserAccount> l = querydao.getListOfUserAccount();
		assert(l.size() != 0);
	}
	
	/*
	 * Parking Request Test
	 */
//	@Test
	public void getParkingRequestList() {
		List<Request> prl = querydao.getRequestList();
		for(Request p : prl) {
			System.out.println(p.getDescription());
		}
		assert (prl.size() != 0);
	}
	
//	@Test
	public void getParkingRequestByUserIdTest() {
		List<ParkingRequest> prl = querydao.getParkingRequestListByUserId(2);		
		assert (prl.size() != 0);
	}
	
	@Test
	public void deleteRequestById() {
		ParkingRequest prq = new ParkingRequest();
		prq.setDescription("Temporary Request to be deleted 2");
		prq.setCsp(colSpot.E);
		prq.setRsp(rowSpot.TWO);
		prq.setStatus(true);
		querydao.submitRequestById(prq,2);
		
		ParkingRequest prqUpdated = querydao.getParkingRequestByUserIdByDescription(2,prq.getDescription());
		
		// prqUpdated will have a value: 
		assert(prqUpdated != null);
		
		// Delete Request by Id
		querydao.deleteParkingRequestById(prqUpdated.getRequest_id());
		
		ParkingRequest nullRequest = querydao.getParkingRequestById(prqUpdated.getRequest_id());
		
		assert(nullRequest == null);
		
		
	}
}