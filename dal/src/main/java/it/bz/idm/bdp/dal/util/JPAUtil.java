package it.bz.idm.bdp.dal.util;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
public class JPAUtil {

	private static EntityManagerFactory emFactory;
	private static final Properties properties = new Properties();
	static {
		try {
			properties.load(JPAUtil.class.getClassLoader().getResourceAsStream("META-INF/persistence.properties"));
			emFactory = Persistence.createEntityManagerFactory(properties.getProperty("persistenceunit"));
		}catch(Throwable ex){
			System.err.println("Cannot create EntityManagerFactory.");
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static EntityManager createEntityManager() {
		return emFactory.createEntityManager();
	}

	public static void close(){
		emFactory.close();
	}

	public static Object getInstanceByType(EntityManager em, String type)
			throws Exception {
		Set<ManagedType<?>> managedTypes = em.getEntityManagerFactory().getMetamodel().getManagedTypes();
		for (ManagedType<?> entity: managedTypes){
			if (entity.getJavaType().getSimpleName().equals(type)){
				return entity.getJavaType().newInstance();
			}
		}
		throw new Exception("ERROR: Cannot get any instance of type " + type + ". Type not found.");
	}

	public static String getEntityNameByObject(Object obj) throws Exception {
		for (EntityType<?> type: emFactory.getMetamodel().getEntities()) {
			if (obj.getClass().getTypeName().equals(type.getJavaType().getName()))
					return type.getName();
		}
		throw new Exception("ERROR: Cannot get any entity name for object "
				+ obj.getClass().getTypeName() + ". Class not found.");
	}

	public static Object getSingleResultOrNull(TypedQuery<? extends Object> query) {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
