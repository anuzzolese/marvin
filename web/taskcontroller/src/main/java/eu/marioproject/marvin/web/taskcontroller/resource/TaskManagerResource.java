package eu.marioproject.marvin.web.taskcontroller.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RIOT;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.format.KRFormat;
import eu.marioproject.commons.web.base.resource.BaseMarvinResource;
import eu.marioproject.marvin.taskcontroller.api.Status;
import eu.marioproject.marvin.taskcontroller.api.Task;
import eu.marioproject.marvin.taskcontroller.api.TaskManager;

/**
 * 
 * Web API for the Task Manager.
 * 
 * @author Andrea Nuzzolese
 *
 */

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Path("/taskmanager")
public class TaskManagerResource extends BaseMarvinResource {
       
   
	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Reference
    private TaskManager taskManager;
    
    @Context
    protected UriInfo uriInfo;

    @GET
    @Produces(value={
    		KRFormat.TURTLE,
    		KRFormat.N3,
    		KRFormat.RDF_JSON,
    		KRFormat.RDF_XML})
    public Response getRegisterdTasks(){
    	
    	String globalNs = "http://www.mario-project.eu/marvin/";
    	String taskNs = uriInfo.getRequestUri() + "/task/";
    	
    	Model model = ModelFactory.createDefaultModel();
    	Resource taskMClass = model.createResource(globalNs + "TaskManager");
    	Resource taskClass = model.createResource(globalNs + "Task");
    	org.apache.jena.rdf.model.Property manages = model.createProperty(globalNs + "manages");
    	org.apache.jena.rdf.model.Property status = model.createProperty(globalNs + "status");
    	
    	Resource taskM = model.createResource(uriInfo.getRequestUri().toString(), taskMClass);
    	
    	log.info("Task registered {}", taskManager.getTasks().size());
    	for(Task task : taskManager.getTasks()){
    		int id = task.getID();
    		Resource taskResource = model.createResource(taskNs + id, taskClass);
    		taskM.addProperty(manages, taskResource);
    		
    		taskResource.addLiteral(RDFS.label, id);
    		
    		String description = task.getDescription();
    		if(description != null && !description.isEmpty())
    			taskResource.addLiteral(RDFS.comment, task.getDescription());
    		
    		Status sts = task.getStatus();
    		taskResource.addLiteral(status, sts.toString());
    		
    		
    	}
    	
    	ResponseBuilder builder = Response.ok(model);
    	
    	return builder.build();
    	
    }
    
    @GET
    @Produces(value={
    		KRFormat.TURTLE,
    		KRFormat.N3,
    		KRFormat.RDF_JSON,
    		KRFormat.RDF_XML})
    @Path("/task/{id}")
    public Response getRegisterdTasks(@PathParam("id") int taskId){
    	
    	String globalNs = "http://www.mario-project.eu/marvin/";
    	String taskNs = uriInfo.getRequestUri() + "/task/";
    	
    	Model model = ModelFactory.createDefaultModel();
    	Resource taskMClass = model.createResource(globalNs + "TaskManager");
    	Resource taskClass = model.createResource(globalNs + "Task");
    	org.apache.jena.rdf.model.Property manages = model.createProperty(globalNs + "manages");
    	org.apache.jena.rdf.model.Property status = model.createProperty(globalNs + "status");
    	
    	Resource taskM = model.createResource(uriInfo.getRequestUri().toString(), taskMClass);
    	
    	Task task = taskManager.getTask(taskId);
    	
    	ResponseBuilder builder = null;
    	if(task != null){
			int id = task.getID();
			Resource taskResource = model.createResource(taskNs + id, taskClass);
			taskM.addProperty(manages, taskResource);
			
			taskResource.addLiteral(RDFS.label, id);
			
			String description = task.getDescription();
			if(description != null && !description.isEmpty())
				taskResource.addLiteral(RDFS.comment, task.getDescription());
			
			Status sts = task.getStatus();
			taskResource.addLiteral(status, sts.toString());
			builder = Response.ok(model);
    	}
    	else builder = Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND);
    	
    	
    	
    	return builder.build();
    	
    }
    
    
    @GET
    @Path("/task/{id}/run")
    public Response runTasks(@PathParam("id") int taskId){
    	
    	Task task = taskManager.getTask(taskId);
    	
    	Status status = taskManager.startTask(task);
    	log.info("STATUS {}", status);
    	ResponseBuilder builder = null;
    	if(status.equals(Status.running()))
    		builder = Response.ok();
    	else builder = Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
    	
    	
    	return builder.build();
    	
    }
    
    @GET
    @Path("/task/{id}/stop")
    public Response stopTasks(@PathParam("id") int taskId){
    	
    	Task task = taskManager.getTask(taskId);
    	
    	Status status = taskManager.stopTask(task);
    	
    	ResponseBuilder builder = null;
    	if(status.equals(Status.inactive()))
    		builder = Response.ok();
    	else builder = Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR);
    	
    	
    	return builder.build();
    	
    }
    
    public static void main(String[] args) {
    	Model model = ModelFactory.createDefaultModel();
    	Resource andrea = model.createResource("http://foo.org/Andrea");
    	andrea.addProperty(RDF.type, model.createResource("http://myont.org/Person"));
    	andrea.addProperty(RDF.type, FOAF.Person);
    	org.apache.jena.rdf.model.Property works = model.createProperty("http://myont.org/worksIn");
    	Resource Rome = model.createResource("http://dbpedia.org/resource/Rome");
    	andrea.addProperty(works, Rome);
    	
    	RDFDataMgr.write(System.out, model, RDFFormat.RDFXML_ABBREV);
    	
    	//model.write(System.out, "TURTLE");
	}


}
