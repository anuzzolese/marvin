package eu.marioproject.commons.web.base.viewable;


/**
 * This is a replacement for the jersey Vieable that allows rendering an 
 * arbitrary object using a Freemarker template specified by path.
 * 
 * Usage of this class promotes a bad programming style where the 
 * application logic is not clearly separated from the presentation but 
 * where backend method are called by the presentation layer.
 * 
 * Users should consider migrate to RdfViewable instead where instead of
 * an arbitrary Object a GraphNode representing a node in a graph is passed,
 * this approach also allows the response to be rendered as RDF.
 *
 */
public class Viewable {
    

    /**
     * This uses the class name of Pojo to prefix the template
     * 
     * @param templatePath the templatePath
     * @param graphNode the graphNode with the actual content
     */
    public Viewable(final String templatePath, final Object pojo) {
        this(templatePath, pojo, null);
    }
    
    /**
     * With this version of the constructor the templatePath is prefixed with
     * the slash-separated class name of clazz.
     * 
     */
    public Viewable(final String templatePath, final Object pojo, final Class<?> clazz) {
        this.templatePath = templatePath;
        this.pojo = pojo;
        this.contextClass = clazz;
    }
    
    private String templatePath;
    private Object pojo;
    private Class contextClass;
    
    public String getTemplatePath() {
        return templatePath;
    }
    
    public Object getPojo() {
        return pojo;
    }

    public Class getContextClass() {
        return contextClass;
    }

}
