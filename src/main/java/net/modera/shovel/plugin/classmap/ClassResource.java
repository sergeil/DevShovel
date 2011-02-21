package net.modera.shovel.plugin.classmap;

import java.io.File;
import net.modera.shovel.model.Resource;

public class ClassResource extends Resource {
	
	private File file;
	
	public ClassResource(String displayName) {
		super(displayName);
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String getResourceKey() {
		return "class";
	}
}
