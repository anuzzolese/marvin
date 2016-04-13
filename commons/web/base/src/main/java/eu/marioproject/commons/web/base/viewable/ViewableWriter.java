package eu.marioproject.commons.web.base.viewable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Produces(MediaType.TEXT_HTML)
@Provider
public class ViewableWriter implements MessageBodyWriter<Viewable> {

    
    @Context
    private UriInfo uriInfo;
    
    @Reference
    private TemplateLoader templateLoader;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return Viewable.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Viewable t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final Viewable t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException {
        Writer out = new OutputStreamWriter(entityStream, "utf-8");
        renderPojo(new Wrapper(t.getPojo()), "html/" + getResolvedTemplatePath(t), out);
        out.flush();
    }

    private String getResolvedTemplatePath(Viewable t) {
        final String slahSeparatedPacakgeName = getEffectiveClass(t).getName().replace('.', '/');
        if (t.getTemplatePath().startsWith("/")) {
            return slahSeparatedPacakgeName + t.getTemplatePath();
        } else {
            return slahSeparatedPacakgeName + '/' + t.getTemplatePath();
        }
    }

    /**
     * Old school classical freemarker rendering, no LD here
     */
    public void renderPojo(Object pojo, final String templatePath, Writer out) {
        Configuration freemarker = new Configuration();
        freemarker.setDefaultEncoding("utf-8");
        freemarker.setOutputEncoding("utf-8");
        freemarker.setLocalizedLookup(false);
        freemarker.setObjectWrapper(new DefaultObjectWrapper());
        freemarker.setTemplateLoader(templateLoader);
        try {
            //should root be a map instead?
            freemarker.getTemplate(templatePath).process(pojo, out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("IOException while processing Template '"
                + templatePath + "' with Object '"+pojo+"' (class: "
                + pojo != null ? pojo.getClass().getName() : null + ")!",e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private Class getEffectiveClass(Viewable t) {
        if (t.getContextClass() != null) {
            return t.getContextClass();
        }
        return uriInfo.getMatchedResources().get(0).getClass();
    }

    static public class Wrapper {

        private Object wrapped;

        public Wrapper(Object wrapped) {
            this.wrapped = wrapped;
        }

        public Object getIt() {
            return wrapped;
        }
    }
}
