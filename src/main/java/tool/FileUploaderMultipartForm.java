package tool;

import java.io.InputStream;

import javax.ws.rs.FormParam;

public class FileUploaderMultipartForm {
	@FormParam("file")
	private InputStream inputStream;
	
//	@FormParam("name")
//	private String name;
//	
//	@FormParam("runId")
//	private String runId;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

//	public String getRunId() {
//		return runId;
//	}
//
//	public void setRunId(String runId) {
//		this.runId = runId;
//	}
	
	
}
