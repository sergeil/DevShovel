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

	public List<Resource> getResources() {
		
		List<Resource> resources = new ArrayList<Resource>();
		
		File file = new File("D:\\coding\\workspace-php\\mn2\\app\\data\\cache\\shovel_iface.json");
		try {
			String jsonData = FileUtils.readFileToString(file);
			JSONObject jsonObject = JSONObject.fromObject( jsonData );
			
			for (Object key : jsonObject.keySet()) {
				String interfaceName = (String) key;
				
				for (Object object :((JSONArray)jsonObject.get(key))) {
					JSONObject data = (JSONObject)object;
					System.out.println(data.get("classname"));
					System.out.println(data.get("file"));
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

}
