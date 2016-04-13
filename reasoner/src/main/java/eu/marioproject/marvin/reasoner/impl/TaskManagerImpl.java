package eu.marioproject.marvin.reasoner.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.marioproject.marvin.reasoner.api.ReasoningManager;

/**
 * Basic implementation of the Reasoning Manager.
 * 
 * @author Andrea Nuzzolese
 *
 */
@Component(immediate = true)
@Service(ReasoningManager.class)
public class TaskManagerImpl implements ReasoningManager {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void performReasoning() {
		// TODO Auto-generated method stub
		
	}

}
