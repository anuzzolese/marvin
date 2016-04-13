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
import org.codehaus.jettison.json.JSONArray;

import eu.marioproject.commons.web.base.format.KRFormat;


/**
 * 
 * @author Andrea Nuzzolese
 *
 */

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Provider
public class JSONArrayWriter implements MessageBodyWriter<JSONArray> {

    public static final Set<String> supportedMediaTypes;
    static {
        Set<String> types = new HashSet<String>();
        types.add(KRFormat.APPLICATION_JSON);
        supportedMediaTypes = Collections.unmodifiableSet(types);
    }
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        String mediaTypeString = mediaType.getType() + '/' + mediaType.getSubtype();
        return JSONArray.class.isAssignableFrom(type) && supportedMediaTypes.contains(mediaTypeString);
    }

    @Override
    public long getSize(JSONArray t,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType) {
        return t.length();
    }

    @Override
    public void writeTo(JSONArray jsonArray,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String,Object> httpHeaders,
                        OutputStream out) throws IOException, WebApplicationException {
        
        out.write(jsonArray.toString().getBytes());
        out.flush();
    }

}
