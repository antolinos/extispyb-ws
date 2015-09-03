package tool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 * Crysol and PepsiSAXS
 * @author ademaria
 *
 */
public class StructureValidatorTool extends Tool {
	
	protected HashSet<String> getMandatoryParams() {
		HashSet<String> mandatoryParams = new HashSet<String>();
		mandatoryParams.add("subtractions");
		mandatoryParams.add("projectId");
		return mandatoryParams;
	}
	
	protected HashSet<String> getMandatoryFile() {
		HashSet<String> mandatoryParams = new HashSet<String>();
		mandatoryParams.add("file");
		return mandatoryParams;
	}
	
	
	
}
