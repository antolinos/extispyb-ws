package extispyb.core.collection;

import java.util.UUID;

import extispyb.core.configuration.MongoDriver;

public class EntityObject {
	
	protected String internalId;

	public EntityObject(){
		this.internalId = UUID.randomUUID().toString();
	}
	
	public String getInternalId() {
		return internalId;
	}

	
	public void save(){
		MongoDriver.getInstance().getDatastore().save(this);
	}
}
