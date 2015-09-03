package extispyb.core.configuration;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import extispyb.core.collection.Job;
import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.User;

import org.mongodb.morphia.Datastore;

public class MongoDriver {

		private static MongoDriver instance = null;
		private MongoClient mongoClient;
		private MongoDatabase database;
		private Datastore datastore;
		private Morphia morphia;
		private String db;
		
		protected MongoDriver() {
			String server = ExiSaxsProperties.getInstance().getProperties().get("database.server").toString();
			String port = ExiSaxsProperties.getInstance().getProperties().get("database.server.port").toString();
			this.db = ExiSaxsProperties.getInstance().getProperties().get("database.name").toString();
			this.mongoClient = new MongoClient( server , Integer.parseInt(port));
			this.database = this.mongoClient.getDatabase(db);
			
			/** Morphia **/
			this.morphia = new Morphia();
			this.morphia.map(User.class);
			this.morphia.map(Project.class);
			this.morphia.map(Run.class);
			
			
			this.datastore = morphia.createDatastore(this.mongoClient, this.db);
			this.datastore.ensureIndexes();
			
			
		}

		public static MongoDriver getInstance() {
			if (instance == null) {
				instance = new MongoDriver();
			}
			return instance;
		}
		
		public MongoDatabase getDatabase(){
			return this.database;
		}

		/**
		 * Old API needed to build gridFS in principle it will change in version 3.1
		 * @return
		 */
		public DB getDB(){
			return this.mongoClient.getDB(this.db);
		}
		
		public Datastore getDatastore(){
			return this.datastore;
		}
		
		public Morphia getMorphia(){
			return this.morphia;
		}

	}
