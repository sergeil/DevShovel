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

	private Map<String, List<String>> interfacemap = new HashMap<String, List<String>>();
	private Map<String, List<String>> classmap = new HashMap<String, List<String>>();
	private List<String> singleClassList = new ArrayList<String>();
	
	static Logger logger = Logger.getLogger(ClassmapResourceProvider.class);
	
	public void addClass(String className, List<String> interfaces, String parent) {
		
		if (parent != null) {
			if (!classmap.containsKey(parent)) {
				classmap.put(parent, new ArrayList<String>());
			}
			classmap.get(parent).add(className);
		}
		if (interfaces != null) {
			for (String interfaceName : interfaces) {
				if (!interfacemap.containsKey(interfaceName)) {
					interfacemap.put(interfaceName, new ArrayList<String>());
				}
				interfacemap.get(interfaceName).add(className);
			}
		}
		if (interfaces == null && parent == null) {
			singleClassList.add(className);
		}
	}
	
	public ClassmapResourceProvider() {}
	
	public ClassmapResourceProvider(File dumpFile) {
		
//		ClassmapResourceProvider provider = this;
//		
//		List<String> interfaces = new ArrayList<String>();
//		interfaces.add("Interface\\Fauna");
//		provider.addClass("Animal", interfaces, null);
//		provider.addClass("Dog", null, "Animal");
//		
//		return;
		
		try {
			String jsonData = FileUtils.readFileToString(dumpFile);
			JSONObject jsonObject = JSONObject.fromObject( jsonData );
			jsonObject = (JSONObject)jsonObject.getJSONObject("classmap"); 
			
			JSONObject jsonIfaceMap = (JSONObject)jsonObject.getJSONObject("interfaces");
			
			for (Object key : jsonIfaceMap.keySet()) {
				String interfaceName = (String) key;
				
				logger.debug("Interface found:" + interfaceName);
				
				List<String> classes = new ArrayList<String>();
				
				for (Object object :((JSONArray)jsonIfaceMap.get(key))) {
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
				interfacemap.put(interfaceName, classes);
			}
			
			JSONObject jsonParentsMap = (JSONObject)jsonObject.getJSONObject("parents");
			
			for (Object key : jsonParentsMap.keySet()) {
				String parentClassName = (String) key;
				
				logger.debug("Parent class found:" + parentClassName);
				
				List<String> classes = new ArrayList<String>();
				
				for (Object className :((JSONArray)jsonParentsMap.get(key))) {
					
					if (className.getClass() == String.class) {
						classes.add((String)className);
						logger.debug("Class found:" + className);
					} else {
						logger.warn("Wrong type for record: " + className.getClass() + " Object: " + className);
					}
				}
				
				logger.debug("Creating record for parent class \"" + parentClassName + "\" record:" + classes);
				classmap.put(parentClassName, classes);
			}
			
			for (Object entry : (JSONArray)jsonObject.get("singles")) {
				String className = (String) entry;
				
				logger.debug("Single class found:" + className);
				
				singleClassList.add(className);
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException("Can not load classmap file", e);
		}
	}
	
	public List<Resource> getResources() {
		
		Map<String, Resource> resources = new HashMap<String,Resource>();
		
		for (String interfaceName : interfacemap.keySet()) {
			
			resources.put(interfaceName,new Resource(interfaceName));
			
			for (String className : interfacemap.get(interfaceName)) {
				resources.put(className, new Resource(className));
			}
		}
		
		for (String parentClassName : classmap.keySet()) {
			
			resources.put(parentClassName, new Resource(parentClassName));
			
			for (String className : classmap.get(parentClassName)) {
				resources.put(className, new Resource(className));
			}
		}
		
		for (String className : singleClassList) {
			
			resources.put(className, new Resource(className));
		}
		
		return new ArrayList<Resource>(resources.values());
	}

	public List<Connection> getResourceConnections(Resource resource) {
		List<Connection> connections = new ArrayList<Connection>();
		
		logger.info("Trying to get connections for " + resource.getDisplayName());
		
		if (interfacemap.containsKey(resource.getDisplayName())) {
			logger.debug(resource.getDisplayName() + " is an interface. Retreiving implementations.");
			for (String className : interfacemap.get(resource.getDisplayName())) {
				connections.add(new Connection(new Resource(className)));
				logger.debug("Implementation found: " + className);
			}
			
		} else {
			logger.debug(resource.getDisplayName() + " is a class. Retreiving interface names.");
			List<String> interfaceNameList = findInterfaceNameList(resource.getDisplayName());
			logger.debug("Interfaces found: " + interfaceNameList.size());
			if (interfaceNameList != null) {
				for (String interfaceName : interfaceNameList) {
					connections.add(new Connection(new Resource(interfaceName)));
					logger.debug("Interface found: " + interfaceName);
				}
			}
		}
		
		if (classmap.containsKey(resource.getDisplayName())) {
			logger.debug(resource.getDisplayName() + " has subclasses.");
			for (String className : classmap.get(resource.getDisplayName())) {
				connections.add(new Connection(new Resource(className)));
				logger.debug("Subclass found: " + className);
			}
		}
		String parentClassName = findParentClassName(resource.getDisplayName());
		if (parentClassName != null) {
			connections.add(new Connection(new Resource(parentClassName)));
			logger.debug(resource.getDisplayName() + " has parent class: " + parentClassName);
		}
		
		logger.info("Connections found: " + connections.size());
		
		return connections;
	}
	
	protected String findParentClassName(String className) {
		
		logger.debug("Resolving parent for class " + className);
		for (String parentClassName : classmap.keySet()) {
			
			for (String _className : classmap.get(parentClassName)) {
				if (_className.equals(className)) {
					
					logger.debug("Class has parent: " + parentClassName);
					return parentClassName;
				}
			}
		}
		logger.debug("Class has no parent.");
		return null;
	}
	protected List<String> findInterfaceNameList(String className) {
		
		List<String> interfaces = new ArrayList<String>();
		
		logger.debug("Resolving interfaces for class " + className);
		
		for (String interfaceName : interfacemap.keySet()) {
			for (String _className : interfacemap.get(interfaceName)) {
				if (_className.equals(className)) {
					interfaces.add(interfaceName);
					logger.debug("Found interface " + interfaceName);
				}
			}
		}
		String parent = findParentClassName(className);
		if (parent != null) {
			logger.debug("Class " + className + " has parent class " + parent + ". Resolving parent interfaces.");
			interfaces.addAll(findInterfaceNameList(parent));
		}
		return interfaces;
	}
}
