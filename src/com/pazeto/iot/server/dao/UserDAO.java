package com.pazeto.iot.server.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pazeto.iot.shared.HibernateUtil;
import com.pazeto.iot.shared.dto.UserDTO;
import com.pazeto.iot.shared.vo.User;

/**
 * Class to access Users in database
 * 
 * @author Daniel
 * 
 */
public class UserDAO implements Serializable {

	public static Long addNewUser(User user) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "FROM UserDTO u WHERE u.email = :email";
			Query query = session.createQuery(hql);
			query.setParameter("email", user.getEmail());
			List results = query.list();
			if (results.size() > 0) {
				throw new Exception("User already exists!");
			}
			return (Long) session.save(new UserDTO(user));
		} finally {
			session.getTransaction().commit();
		}
	}

	public static ArrayList<User> listUsers() {
		Session session = HibernateUtil.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "FROM UserDTO";
			Query query = session.createQuery(hql);
			List results = query.list();
			ArrayList<User> users = new ArrayList<>();
			for (Object object : results) {
				users.add(new User((UserDTO)object));
			}
			return users;
		} finally {
			session.getTransaction().commit();
		}
	}

	public static User doAuthentication(User userToLogin) {
		Session session = HibernateUtil.getCurrentSession();
		try {
			System.out.println(userToLogin.getEmail());
			System.out.println(userToLogin.getPwd());
			session.beginTransaction();
			System.out.println(1);
			String hql = "FROM UserDTO u WHERE u.email = :email AND u.pwd= :pwd";
			System.out.println(2);
			Query query = session.createQuery(hql);
			System.out.println(3);
			query.setString("email", userToLogin.getEmail().toString());
			System.out.println(4);
			query.setString("pwd", userToLogin.getPwd().toString());
			System.out.println(5);
			query.setMaxResults(1);
			System.out.println(5.5);
			System.out.println(query.getQueryString());
			System.out.println(5.6);
			List results = query.list();
			System.out.println(results.size());
			if (results.size() >= 1) {
				return new User((UserDTO) results.get(0));
			}
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}
}
