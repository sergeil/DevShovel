package net.modera.shovel.plugin.classmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceProvider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ClassmapResourceProvider implements ResourceProvider {

	private Map<String, List<String>> classmap = new HashMap<String, List<String>>();
	
	static Logger logger = Logger.getLogger(ClassmapResourceProvider.class);
	
	public ClassmapResourceProvider(File dumpFile) {
		
		try {
			String jsonData = FileUtils.readFileToString(dumpFile);
			JSONObject jsonObject = JSONObject.fromObject( jsonData );
			jsonObject = (JSONObject)jsonObject.getJSONObject("classmap"); 
			jsonObject = (JSONObject)jsonObject.getJSONObject("iface"); 
			
			for (Object key : jsonObject.keySet()) {
				String interfaceName = (String) key;
				
				logger.debug("Interface found:" + interfaceName);
				
				List<String> classes = new ArrayList<String>();
				
				for (Object object :((JSONArray)jsonObject.get(key))) {
					JSONObject data = (JSONObject)object;
					
					Object className = data.get("classname");
					if (className.getClass() == String.class) {
						classes.add((String)className);
						logger.debug("Class found:" + className);
					} else {
						logger.warn("Wrong type for record: " + className.getClass() + " Object: " + className);
					}
				}
				
				logger.debug("Creating record for interface \"" + interfaceName + "\" record:" + classes);
				classmap.put(interfaceName, classes);
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException("Can not load classmap file", e);
		}
	}
	
	public List<Resource> getResources() {
		
		List<Resource> resources = new ArrayList<Resource>();
		
		for (String interfaceName : classmap.keySet()) {
			
			resources.add(new Resource(interfaceName));
			
			for (String className : classmap.get(interfaceName)) {
				resources.add(new Resource(className));
			}
		}
		
		return resources;
	}

	public List<Connection> getResourceConnections(Resource resource) {
		List<Connection> connections = new ArrayList<Connection>();
		
		logger.info("Trying to get connections for " + resource.getDisplayName());
		
		if (classmap.containsKey(resource.getDisplayName())) {
			for (String className : classmap.get(resource.getDisplayName())) {
				connections.add(new Connection(new Resource(className)));
			}
		} else {
			String interfaceName = findInterfaceName(resource.getDisplayName());
			if (interfaceName != null) {
				connections.add(new Connection(new Resource(interfaceName)));
			}
		}
		
		logger.info("Connections found: " + connections.size());
		
		return connections;
	}
	
	protected String findInterfaceName(String className) {
		
		for (String interfaceName : classmap.keySet()) {
			
			for (String _className : classmap.get(interfaceName)) {
				if (_className == className) {
					return interfaceName;
				}
			}
		}
		return null;
	}
}
