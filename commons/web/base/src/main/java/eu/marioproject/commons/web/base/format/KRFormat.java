package eu.marioproject.commons.web.base.format;

import javax.ws.rs.core.MediaType;

/**
 * Additional MIME types for knowledge representation formats.
 * 
 * @author andrea.nuzzolese
 * @author alexdma
 * 
 */
public class KRFormat extends MediaType {

    /**
     * "text/owl-functional"
     */
    public static final String FUNCTIONAL_OWL = "text/owl-functional";

    /**
     * "text/owl-functional"
     */
    public static final MediaType FUNCTIONAL_OWL_TYPE = new MediaType("text", "owl-functional");

    /**
     * "text/owl-manchester"
     */
    public static final String MANCHESTER_OWL = "text/owl-manchester";

    /**
     * "text/owl-manchester"
     */
    public static final MediaType MANCHESTER_OWL_TYPE = new MediaType("text", "owl-manchester");

    /**
     * "text/rdf+nt"
     */
    public static final String N_TRIPLE = "text/rdf+nt";

    /**
     * "text/rdf+nt"
     */
    public static final MediaType N_TRIPLE_TYPE = new MediaType("text", "rdf+nt");

    /**
     * "text/rdf+n3"
     */
    public static final String N3 = "text/rdf+n3";

    /**
     * "text/rdf+n3"
     */
    public static final MediaType N3_TYPE = new MediaType("text", "rdf+n3");

    /**
     * "application/owl+xml"
     */
    public static final String OWL_XML = "application/owl+xml";

    /**
     * "application/owl+xml"
     */
    public static final MediaType OWL_XML_TYPE = new MediaType("application", "owl+xml");

    /**
     * "application/rdf+json"
     */
    public static final String RDF_JSON = "application/rdf+json";

    /**
     * "application/rdf+json"
     */
    public static final MediaType RDF_JSON_TYPE = new MediaType("application", "rdf+json");

    /**
     * "application/rdf+xml"
     */
    public static final String RDF_XML = "application/rdf+xml";

    /**
     * "application/rdf+xml"
     */
    public static final MediaType RDF_XML_TYPE = new MediaType("application", "rdf+xml");

    /**
     * "text/turtle"
     */
    public static final String TURTLE = "text/turtle";

    /**
     * "text/turtle"
     */
    public static final MediaType TURTLE_TYPE = new MediaType("text", "turtle");
    
    /**
     * "application/x-turtle" (pre-registration MIME type for text/turtle)
     */
    public static final String X_TURTLE = "application/x-turtle";

    /**
     * "application/x-turtle" (pre-registration MIME type for text/turtle)
     */
    public static final MediaType X_TURTLE_TYPE = new MediaType("application", "x-turtle");
    
    /**
     * "application/ld+json"
     */
    public static final String JSON_LD = "application/ld+json";

    /**
     * "application/x-turtle" (pre-registration MIME type for text/turtle)
     */
    public static final MediaType JSON_LD_TYPE = new MediaType("application", "ld+json");
    
    /**
     * "text/csv"
     */
    public static final String TEXT_CSV = "text/csv";

    /**
     * "text/csv" (pre-registration MIME type for text/turtle)
     */
    public static final MediaType TEXT_CSV_TYPE = new MediaType("text", "csv");

}
