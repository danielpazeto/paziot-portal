package com.pazeto.iot.server;

import java.util.logging.Logger;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
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
				.filter("password", userToLogin.getPwd().toString())
				.first().now();
		if (user != null) {
			LOG.info("usuario pego ");
			LOG.info("email: " + user.getEmail());
			LOG.info("password: " + user.getPwd());
		} else {
			LOG.info("USUARIO NAO EXISTE");
		}
		return user;
	}

//	List<Product> listProducts(User user) {
//		Iterable<Product> list = ofy().load().type(Product.class)
//				.filter("idUser", user.getId());
//
//		List<Product> prods = new ArrayList<Product>();
//		for (Product product : list) {
//			prods.add(product);
//		}
//		return prods;
//	}

//	<T> List<T> listOjects(Class<T> type, User user) {
//
//		LOG.info(type.getName());
//		LOG.info(type.getClass().toString());
//		Iterable<T> list = ofy().load().type(type)
//				.filter("idUser", user.getId()).list();
//
//		List<T> objs = new ArrayList<T>();
//		for (T obj : list) {
//			objs.add(obj);
//			if (obj instanceof Company) {
//				LOG.info("Company  : " + ((Company) obj).getName());
//			} else if (obj instanceof Product) {
//				LOG.info("Product  : " + ((Product) obj).getName());
//			}
//		}
//		LOG.info("" + objs.size());
//		return objs;
//	}

//	public void saveProducts(List<Product> list) {
//		LOG.info("Salvando PRODUCTS = " + list.size());
//		ofy().save().entities(list).now();
//	}

	/**
	 * Persist any type object
	 * 
	 * @param object
	 */
	public <T> void persistObject(T object) {
		ofy().save().entity(object).now();
	}

}
