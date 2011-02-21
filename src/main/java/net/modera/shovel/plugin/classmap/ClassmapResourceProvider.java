package net.modera.shovel.plugin.classmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceProvider;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class ClassmapResourceProvider implements ResourceProvider {

	private File dumpFile;
	
	public List<Resource> getResources() {
		
		List<Resource> resources = new ArrayList<Resource>();
		
		try {
			String jsonData = FileUtils.readFileToString(getDumpFile());
			JSONObject jsonObject = JSONObject.fromObject( jsonData );
			jsonObject = (JSONObject)jsonObject.getJSONObject("classmap"); 
			jsonObject = (JSONObject)jsonObject.getJSONObject("iface"); 
			
			for (Object key : jsonObject.keySet()) {
				String interfaceName = (String) key;
				
				resources.add(new Resource(interfaceName));
				
				for (Object object :((JSONArray)jsonObject.get(key))) {
					JSONObject data = (JSONObject)object;
					ClassResource res = new ClassResource((String)data.get("classname"));
					
					Object file = data.get("file");
					if (file.getClass() == String.class) {
						res.setFile(new File((String)file));
					}
					
					resources.add(res);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return resources;
	}

	public List<Connection> getResourceConnections(Resource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDumpFile(File dumpFile) {
		this.dumpFile = dumpFile;
	}

	public File getDumpFile() {
		return dumpFile;
	}

}
