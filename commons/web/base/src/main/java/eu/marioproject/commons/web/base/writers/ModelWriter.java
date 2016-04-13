package eu.marioproject.commons.web.base.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.jena.query.ARQ;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.RiotLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.commons.web.base.format.KRFormat;

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Provider
public class ModelWriter implements MessageBodyWriter<Model> {

    private final Logger log = LoggerFactory.getLogger(ModelWriter.class);
    
    public static final Set<String> supportedMediaTypes;
    static {
        Set<String> types = new HashSet<String>();
        types.add(KRFormat.N_TRIPLE);
        types.add(KRFormat.RDF_XML);
        types.add(KRFormat.TURTLE);
        types.add(KRFormat.RDF_JSON);
        types.add(KRFormat.JSON_LD);
        supportedMediaTypes = Collections.unmodifiableSet(types);
    }
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        String mediaTypeString = mediaType.getType() + '/' + mediaType.getSubtype();
        return Model.class.isAssignableFrom(type) && supportedMediaTypes.contains(mediaTypeString);
    }

    @Override
    public long getSize(Model model,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Model model,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String,Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        
        long start = System.currentTimeMillis();
        
        String mediaTypeString = mediaType.getType() + '/' + mediaType.getSubtype();
        
        RDFFormat riotType = RDFFormat.JSONLD;
        if(mediaTypeString.equals(KRFormat.N_TRIPLE))
            riotType = RDFFormat.NTRIPLES;
        else if(mediaTypeString.equals(KRFormat.RDF_XML))
            riotType = RDFFormat.RDFXML;
        else if(mediaTypeString.equals(KRFormat.TURTLE))
            riotType = RDFFormat.TURTLE;
        else if(mediaTypeString.equals(KRFormat.RDF_JSON))
            riotType = RDFFormat.RDFJSON;
        else if(mediaTypeString.equals(KRFormat.JSON_LD))
            riotType = RDFFormat.JSONLD;
        
        
        ARQ.init();
        RDFDataMgr.write(entityStream, model, riotType);
        
        log.debug("Serialized in {}ms", System.currentTimeMillis() - start);
    }
    
    public static void main(String[] args) {
		Model m = ModelFactory.createDefaultModel();
		RiotLib.prefixMap(m.getGraph());
	}
    
    

}
