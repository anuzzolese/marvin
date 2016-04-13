package eu.marioproject.marvin.web.taskcontroller.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DC_10;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.resource.BaseMarvinResource;
import eu.marioproject.marvin.clients.robot.ApiException;
import eu.marioproject.marvin.clients.robot.api.IRobotApi;
import eu.marioproject.marvin.clients.robot.model.BatteryStatus;

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
@Path("/battery")
public class BatteryResource extends BaseMarvinResource {
       
   
	private Logger log = LoggerFactory.getLogger(getClass());
	
    @Reference
    private IRobotApi robotAPI;
    
    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getBatteryStatus(){
    	log.info("Managing battery request.");
    	BatteryStatus status = null;
    	ResponseBuilder builder = null;
		try {
			status = robotAPI.getBatteryStatus();
		} catch (ApiException e) {
			builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		}
		if(status != null){
	    	Integer statusValue = status.getStatus();
	    	builder = Response.ok(statusValue);
		}
		else builder = Response.status(Status.NOT_IMPLEMENTED);
		
		return builder.build();
    }

    
    public static void main(String[] args) {
    	
    	org.apache.jena.sparql.function.FunctionRegistry.get().put(PointerRange.URI, PointerRange.class);
    	
		Model model = FileManager.get().loadModel("graph.rdf");
		model.removeNsPrefix("r");
		model.removeNsPrefix("d");
		model.setNsPrefix("rdf", RDF.uri);
		model.setNsPrefix("dc-elements", DC_10.uri);
		model.write(System.out);
		/*
		//String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT DISTINCT ?c WHERE {      {?x a ?c}      UNION      {       ?y rdfs:subClassOf ?r .        ?r rdf:type owl:Restriction .        ?r owl:someValuesFrom ?c     }      UNION      {?c rdfs:subClassOf ?z}      UNION      {?w rdfs:subClassOf ?c . FILTER(isIRI(?c))} }";
		String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX semion: <http://ontologydesignpatterns.org/cp/owl/semiotics.owl#> PREFIX earmark: <http://www.essepuntato.it/2008/12/earmark#> PREFIX functions: <http://stlab.istc.cnr.it/tools/fred/functions/> SELECT DISTINCT ?c ?offset ?earmarkbegins ?earmarkends WHERE {      {?x a ?c}      UNION      {       ?y rdfs:subClassOf ?r .        ?r rdf:type owl:Restriction .        ?r owl:someValuesFrom ?c     }      UNION      {?c rdfs:subClassOf ?z}      UNION      {?w rdfs:subClassOf ?c . FILTER(isIRI(?c))}        ?offset semion:hasInterpretant ?c .        ?offset earmark:begins ?earmarkbegins .        ?offset earmark:ends ?earmarkends .        FILTER(functions:pointer_range(?offset, ?c))}";
		
		Query query = QueryFactory.create(sparql, Syntax.syntaxARQ);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = queryExecution.execSelect();
		ResultSetFormatter.out(System.out, resultSet);
		*/
	}

}
