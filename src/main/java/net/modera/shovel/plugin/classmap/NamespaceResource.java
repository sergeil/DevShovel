package net.modera.shovel.plugin.classmap;

import org.springframework.stereotype.Component;
import net.modera.shovel.model.Resource;

@Component
public class NamespaceResource extends Resource {

	public NamespaceResource(String displayName) {
		super(displayName);
	}

	@Override
	public String getResourceKey() {
		return "namespace";
	}

}
