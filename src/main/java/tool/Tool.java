package tool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import extispyb.core.collection.Project;
import extispyb.core.collection.Run;
import extispyb.core.collection.Run.Status;


public class Tool {

	private String projectId;
	private Project project;
	
	public Tool(){
		
	}
	
	protected HashSet<String> getMandatoryParams() {
		return new HashSet<String>();
	}
	
	protected HashSet<String> getMandatoryFile() {
		return new HashSet<String>();
	}
	
	
	public void run(Map<String, List<InputPart>> formParts) throws Exception{
		if (this.check(formParts)){
			this.createRun(formParts);
			this.launch();
		}
	}

	protected void launch() {
	}

	protected void createRun(Map<String, List<InputPart>> formParts) throws Exception {
		this.project = Project.getByInternalId(this.projectId);
		if (project != null){
			System.out.println("Project Found **");
			
			Run run = new Run();
			run.setName(this.getName());
			run.setName(this.getDescription());
			run.setStatus(Status.CREATED);
			run.getInputs().putAll(this.getInput(formParts));
			
		}
		else{
			throw new Exception("Project with internatId: " + this.projectId + " not found" );
		}
	}

	private Map<? extends String, ? extends String> getInput(Map<String, List<InputPart>> formParts) {
		Map<String, String> input = new HashMap<String, String>();
		for (String fileFieldName : this.getMandatoryFile()) {
			input.put(fileFieldName, formParts.get(fileFieldName).toString());
		}
		
		System.out.println(input);
//		for (String fieldName : this.getMandatoryParams()) {
//			if (!formParts.containsKey(fieldName)){
//				throw new Exception(fieldName + " is required and not found");
//			}
//		}
		return input;
	}

	private String getDescription() {
		return null;
	}

	private String getName() {
		return null;
	}

	
	protected boolean check(Map<String, List<InputPart>> formDataPats) throws Exception{
		
		System.out.println(formDataPats.keySet().toString());
		for (String iterable_element : formDataPats.keySet()) {
			System.out.println(formDataPats.get(iterable_element).toString());
		}
		
		for (String fileFieldName : this.getMandatoryFile()) {
			if (!formDataPats.containsKey(fileFieldName)){
				throw new Exception(fileFieldName + " is required and not found");
			}
		}
		
		for (String fieldName : this.getMandatoryParams()) {
			if (!formDataPats.containsKey(fieldName)){
				throw new Exception(fieldName + " is required and not found");
			}
		}
		
		return true;
	} 
}
