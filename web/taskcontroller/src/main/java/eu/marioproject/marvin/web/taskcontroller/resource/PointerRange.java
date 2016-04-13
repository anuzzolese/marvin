package eu.marioproject.marvin.web.taskcontroller.resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionBase2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointerRange extends FunctionBase2 {

    Logger log = LoggerFactory.getLogger(getClass());
    
    public static final String URI = "http://stlab.istc.cnr.it/tools/fred/functions/pointer_range";

    @Override
    public NodeValue exec(NodeValue offset, NodeValue classNode) {
        if(classNode.isIRI() && offset.isIRI()){
            Node node = classNode.asNode();
            Node offsetNode = offset.asNode();
            
            String offsetURI = offsetNode.getURI();
            String uri = node.getURI();
            
            String namespace = getNamespace(uri);
            String id = uri.substring(namespace.length());
            
            String label = "";
            for(char c : id.toCharArray()){
                if(Character.isUpperCase(c)){
                    if(!label.isEmpty()) label += "+";
                }
                label += c;
            }
            label = label.trim().toLowerCase();
            
            String regex = "(?i)\\_" + Pattern.quote(label) + "$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(offsetURI);
            boolean mathces = matcher.find();
            
            log.info("{} matches {}: {}", new Object[]{offsetURI, regex, mathces});
            
            return NodeValue.makeBoolean(mathces);
        }
         
        return NodeValue.makeBoolean(false);
    }
    
    private String getNamespace(String classResourceURI){
        int lastHash = classResourceURI.lastIndexOf("#");
        int lastSlash = classResourceURI.lastIndexOf("/");
        
        String type = null;
        
        if(lastHash > lastSlash){
            type = classResourceURI.substring(0, lastHash+1);
        }
        else{
            type = classResourceURI.substring(0, lastSlash+1);
        }
        
        return type;
    }

}
