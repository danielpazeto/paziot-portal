package com.pazeto.iot.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.pazeto.iot.shared.vo.Device;
import com.pazeto.iot.shared.vo.IoPort;
import com.pazeto.iot.shared.vo.User;

public class DAO {

	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class
			.getName());

	public DAO() {
		ObjectifyService.register(User.class);
		ObjectifyService.register(Device.class);
		ObjectifyService.register(IoPort.class);
	}

	private Objectify ofy() {
		return ObjectifyService.ofy();
	}

	User doAuthentication(User userToLogin) {
		User user = ofy().load().type(User.class)
				.filter("email", userToLogin.getEmail().toString())
				.filter("pwd", userToLogin.getPwd().toString()).first().now();
		if (user != null) {
			LOG.info("usuario pego ");
			LOG.info("email: " + user.getEmail());
			LOG.info("password: " + user.getPwd());
		} else {
			LOG.info("USUARIO NAO EXISTE");
		}
		return user;
	}

	/**
	 * List all objects of determined type
	 * 
	 * @param type
	 * @param user
	 *            - whether is not null filter user objects
	 * @return
	 */
	<T> List<T> listOjects(Class<T> type, User user) {

		LOG.info(type.getName());
		LOG.info(type.getClass().toString());

		Query<T> q = ofy().load().type(type);
		if (user != null) {
			LOG.info("listando "+type.getName()+" de "+user.getEmail());
			q = q.filter("userId", user.getId());
		}
		Iterable<T> list = q.list();

		List<T> objs = new ArrayList<T>();
		for (T obj : list) {
			objs.add(obj);
			if (obj instanceof Device) {
				LOG.info("Device  : " + ((Device) obj).getName());
			} else if (obj instanceof User) {
				LOG.info("Product  : " + ((User) obj).getName());
			}
		}
		LOG.info("" + objs.size());
		return objs;
	}

	/**
	 * Persist any type object
	 * 
	 * @param object
	 * @return
	 */
	private <T> String save(T object) {
		Key<T> key = ofy().save().entity(object).now();
		return key.getName();
	}

	public void addNewUser(User user) throws Exception {
		LOG.info(user.getEmail());
		if (ofy().load().type(User.class).filter("email", user.getEmail())
				.first().now() == null) {
			save(user);
		} else {
			throw new Exception("User already exists!");
		}
	}

	public void updateUser(User user) throws Exception {
		save(user);
	}

	public void persistDevice(Device dev) throws Exception {
		LOG.info(dev.getChipId()+"");
		
//		if (ofy().load().type(Device.class).id(dev.getChipId()).now() == null) {
			save(dev);
//		} else {
//			throw new Exception("Device already exists!");
//		}
	}

	public void updateDevice(Device dev) throws Exception {
		save(dev);
	}

}
