package net.modera.shovel.graph;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;
import org.springframework.stereotype.Component;

import net.modera.shovel.model.Resource;

@Component
public class ResourceRenderer {
	
	public static final int NOT_ABLE = 0;
	public static final int CONFIDENCE_LOW = 1;
	public static final int CONFIDENCE_NORMAL = 2;
	public static final int CONFIDENCE_HIGH = 3;
	
	/**
	 * @param resource
	 * @return Confidence level
	 */
	public int isAbleToRender(Resource resource) {
		return CONFIDENCE_LOW;
	}
	
	public GraphNode render(Graph graph, Resource resource) {
		Image image1 = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
		return new GraphNode(graph, SWT.NONE, resource.getDisplayName(), image1);
	}
}
