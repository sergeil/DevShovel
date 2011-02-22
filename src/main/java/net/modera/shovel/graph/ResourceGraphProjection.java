package net.modera.shovel.graph;

import net.modera.shovel.traveler.ResourceTraveler;
import net.modera.shovel.traveler.TravelerEventListener;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("graphProjection")
public class ResourceGraphProjection implements TravelerEventListener {
	
	private ResourceTraveler traveler;

	protected Graph graph;
	
	@Autowired
	public void setTraveler(ResourceTraveler traveler) {
		this.traveler = traveler;
	}

	public ResourceTraveler getTraveler() {
		return traveler;
	}
	
//	
//	
//	/**
//	 * Find the best renderer and render the resource
//	 * 
//	 * @param graph
//	 * @param resource
//	 * @return
//	 */
//	public GraphNode renderResource(Graph graph, Resource resource) {
//		ResourceRenderer bestRenderer = null;
//		int bestConfidence = 0;
//		for (ResourceRenderer renderer : renderers) {
//			int confidence = renderer.isAbleToRender(resource);
//			if (confidence > ResourceRenderer.NOT_ABLE && confidence > bestConfidence) {
//				bestConfidence = confidence;
//				bestRenderer = renderer;
//			}
//		}
//		if (bestRenderer == null) {
//			throw new RuntimeException("Can not find renderer for resource:" +  resource.getClass());
//		}
//		return bestRenderer.render(graph, resource);
//	}
	
	public void render(GridData data, Shell shell) {
		
	}
	
	public void onReset() {
		
//		final Map<String,GraphNode> figureListing = new HashMap<String,GraphNode>();
//		final StringBuffer stringBuffer = new StringBuffer();
		
//		FontData fontData = shell.getDisplay().getSystemFont().getFontData()[0];
//		fontData.height = 42;

//		final Font font = new Font(shell.getDisplay(), fontData);
//
//		
//		Image image2 = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
//		Image image1 = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
//		Image image3 = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		
//		Resource res = getTraveler().getCurrentResource();
//		GraphNode currentNode = renderResource(graph, res);
//		
//		for (Connection connection : getTraveler().getConnections()) {
//			
//			GraphNode node = new GraphNode(g, SWT.NONE, 
//						connection.getTargetResource().getDisplayName(), image2);
//			new GraphConnection(g, SWT.NONE, currentNode, node);
//		}
		
//		GraphNode n3 = new GraphNode(g, SWT.NONE, "org.eclipse.Error", image3);
//		figureListing.put(n1.getText().toLowerCase(), n1);
//		figureListing.put(n2.getText().toLowerCase(), n2);
//		figureListing.put(n3.getText().toLowerCase(), n3);

//		new GraphConnection(g, SWT.NONE, n1, n2);
//		new GraphConnection(g, SWT.NONE, n2, n3);
		
//		n1.setLocation(10, 10);
//		n2.setLocation(200, 10);
//		n3.setLocation(200, 200);
		


		/*	g.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				boolean complete = false;
				if (e.keyCode == BACKSPACE) {
					if (stringBuffer.length() > 0) {
						stringBuffer.deleteCharAt(stringBuffer.length() - 1);
					}
				} else if (e.keyCode == ENTER) {
					complete = true;
				} else if ((e.character >= 'a' && e.character <= 'z') || (e.character >= 'A' && e.character <= 'Z') || (e.character == '.') || (e.character >= '0' && e.character <= '9')) {
					stringBuffer.append(e.character);
				}
				Iterator<String> iterator = figureListing.keySet().iterator();
				List<GraphNode> list = new ArrayList<GraphNode>();
				if (stringBuffer.length() > 0) {
					while (iterator.hasNext()) {
						String string = (String) iterator.next();
						if (string.indexOf(stringBuffer.toString().toLowerCase()) >= 0) {
							list.add(figureListing.get(string));
						}
					}
				}
				g.setSelection((GraphItem[]) list.toArray(new GraphItem[list.size()]));
				if (complete && stringBuffer.length() > 0) {
					stringBuffer.delete(0, stringBuffer.length());
				}

				g.redraw();
			}

		});

		g.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.setFont(font);
				e.gc.setClipping((Region) null);
				e.gc.setForeground(ColorConstants.black);
				e.gc.drawText(stringBuffer.toString(), 50, 50, true);
			}
		});
*/	}
	
	public void onDispose() {
//		image1.dispose();
//		image2.dispose();
//		image3.dispose();
//		font.dispose();
	}

	public void onTravelerMoved() {
		// TODO Auto-generated method stub
		
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return graph;
	}

	

	
}
