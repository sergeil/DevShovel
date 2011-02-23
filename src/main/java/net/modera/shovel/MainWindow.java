package net.modera.shovel;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.graph.ResourceRenderer;
import net.modera.shovel.indexer.ResourceIndexer;
import net.modera.shovel.model.Connection;
import net.modera.shovel.model.Resource;
import net.modera.shovel.traveler.ResourceProvider;
import net.modera.shovel.traveler.ResourceTraveler;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("mainWindow")
public class MainWindow implements AutoCompleteProvider {

	static Logger logger = Logger.getLogger(MainWindow.class);

	protected Shell shell;

	protected Graph graph;

	protected Display display;
	protected ResourceTraveler traveler;

	protected Text queryTextField;

	@Autowired
	@Qualifier("main")
	protected ResourceProvider resourceProvider;

	@Autowired
	protected ResourceIndexer indexer;

	@Autowired
	private List<ResourceRenderer> renderers;

	public void show() {

		indexer.index(resourceProvider.getResources());

		shell.open();
		
		queryTextField.setFocus();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	protected String getQueryText() {
		if (queryTextField.getText().length() > 0) {
			return queryTextField.getText();
		} else {
			return "";
		}
	}

	private void clearGraph() {

		// remove all the connections

		graph.setSelection(null);

		Object[] objects = graph.getConnections().toArray();

		for (Object connection : objects) {
			((GraphConnection) connection).dispose();
		}

		objects = graph.getNodes().toArray();
		for (Object node : objects) {
			((GraphNode) node).dispose();
		}
	}
	


	public List<String> getAutoCompleteValues(String text) {
		List<String> values = new ArrayList<String>();
		try {
			String query = getQueryText();

			logger.info("Trying to find resources by query: " + query);
			List<Resource> resources = indexer.search(query);

			logger.info("Resources found: " + resources.size());
			//renderResourceList(resources);
			
			for (Resource resource : resources) {
				values.add(resource.getDisplayName());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return values;
	}

	public void redraw() {

		logger.info("redrawing graph");

		//clearGraph();
//
//		if (traveler.getCurrentResource() != null) {
//			logger.info("Rendering graph for a resource");
//			renderResourceGraph(traveler.getCurrentResource());
//		} else {
//			
//		}
//
		graph.setLayoutAlgorithm(new RadialLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}
	
	public void setResourceName(String name) {
		clearGraph();
		renderResourceGraph(new Resource(name));
		redraw();
	}

	public void renderResourceList(List<Resource> resources) {
		for (Resource resource : resources) {
			renderResourceGraph(resource);
		}
	}

	public void renderResourceConnections(Resource resource, GraphNode resourceNode) {
		
		for (Connection connection : resourceProvider
				.getResourceConnections(resource)) {

			GraphNode node = renderResource(graph,
					connection.getTargetResource());
			new GraphConnection(graph, SWT.NONE, resourceNode, node);
		}
	}
	
	public void renderResourceGraph(Resource resource) {

		GraphNode currentNode = renderResource(graph, resource);
		
		renderResourceConnections(resource, currentNode);
	}

	/**
	 * Find the best renderer and render the resource
	 * 
	 * @param graph
	 * @param resource
	 * @return
	 */
	public GraphNode renderResource(Graph graph, Resource resource) {
		ResourceRenderer bestRenderer = null;
		int bestConfidence = 0;
		for (ResourceRenderer renderer : renderers) {
			int confidence = renderer.isAbleToRender(resource);
			if (confidence > ResourceRenderer.NOT_ABLE
					&& confidence > bestConfidence) {
				bestConfidence = confidence;
				bestRenderer = renderer;
			}
		}
		if (bestRenderer == null) {
			throw new RuntimeException("Can not find renderer for resource:"
					+ resource.getClass());
		}
		return bestRenderer.render(graph, resource);
	}

	@Autowired
	public MainWindow(Display display, ResourceTraveler traveler) {
		
		this.traveler = traveler;
		this.display = display;

		shell = new Shell(display);
		shell.setText("Graph Snippet 5");
		shell.setMaximized(true);
		shell.setLayout(new FillLayout());

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		shell.setLayout(gridLayout);
		
		createTollbar(shell);
		createQueryBar(shell);
		createGraph(shell);
		createStatusBar(shell);
		createProgressBar(shell);
	}
	
	public void createGraph(Shell shell) {
		
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

		graph = new Graph(shell, SWT.NONE);
		
		graph.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		graph.setLayoutData(data);
		
		graph.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				System.out.println(((Graph) e.widget).getSelection());
				@SuppressWarnings("unchecked")
				List<GraphNode> nodes = (List<GraphNode>)((Graph) e.widget).getSelection();
				if(nodes.size() > 0) {
					GraphNode selectedNode = nodes.get(0);
					
					Object[] objects = graph.getConnections().toArray();
	
					for (Object connection : objects) {
						((GraphConnection) connection).dispose();
					}
	
					objects = graph.getNodes().toArray();
					for (Object node : objects) {
						if (selectedNode != node) {
							((GraphNode) node).dispose();
						}
					}
					
					renderResourceConnections(new Resource(nodes.get(0).getText()), selectedNode);
					
					graph.setLayoutAlgorithm( new RadialLayoutAlgorithm(
							LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void createStatusBar(Shell shell) {
		final Label status = new Label(shell, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		status.setLayoutData(data);
	}
	
	public void createProgressBar(Shell shell) {
		final ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.END;
		progressBar.setLayoutData(data);
	}
	
	public void createQueryBar(Shell shell) {
		
		GridData data;
		
		Label labelAddress = new Label(shell, SWT.NONE);
		labelAddress.setText("Address");

		final Text queryField = new Text(shell, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		queryField.setEchoChar('\0');
		queryField.setLayoutData(data);
		
		queryField.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent e) {}
            public void keyReleased(KeyEvent e) {
            	if (e.keyCode == SWT.ESC){
            		queryField.setText("");
            		clearGraph();
            	}
            }}
        );
		
		queryField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				//redraw();
			}
		});
		
		AutoComplete ac = new AutoComplete(queryField, AutoComplete.RESIZE, this);
		ac.setVisibleItemCount(40);
		ac.addListener(new AutoCompleteListener() {
			
			public void onSelect(String selectedVariant) {
				setResourceName(selectedVariant);
			}
		});
		queryTextField = queryField;
		
	}
	public void createTollbar(Shell shell) {
		
		ToolBar toolbar = new ToolBar(shell, SWT.NONE);
		ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
		itemBack.setText("Back");
		ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
		itemForward.setText("Forward");
		ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
		itemStop.setText("Stop");
		ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
		itemRefresh.setText("Refresh");
		ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
		itemGo.setText("Go");

		GridData data = new GridData();
		data.horizontalSpan = 3;
		toolbar.setLayoutData(data);
		
		// /* event handling */
		// Listener listener = new Listener() {
		// public void handleEvent(Event event) {
		// ToolItem item = (ToolItem)event.widget;
		// String string = item.getText();
		// // if (string.equals("Back")) browser.back();
		// // else if (string.equals("Forward")) browser.forward();
		// // else if (string.equals("Stop")) browser.stop();
		// // else if (string.equals("Refresh")) browser.refresh();
		// // else if (string.equals("Go")) browser.setUrl(location.getText());
		// }
		// };
		// browser.addProgressListener(new ProgressListener() {
		// public void changed(ProgressEvent event) {
		// if (event.total == 0) return;
		// int ratio = event.current * 100 / event.total;
		// progressBar.setSelection(ratio);
		// }
		// public void completed(ProgressEvent event) {
		// progressBar.setSelection(0);
		// }
		// });
		// browser.addStatusTextListener(new StatusTextListener() {
		// public void changed(StatusTextEvent event) {
		// status.setText(event.text);
		// }
		// });
		// browser.addLocationListener(new LocationListener() {
		// public void changed(LocationEvent event) {
		// if (event.top) location.setText(event.location);
		// }
		// public void changing(LocationEvent event) {
		// }
		// });
		// itemBack.addListener(SWT.Selection, listener);
		// itemForward.addListener(SWT.Selection, listener);
		// itemStop.addListener(SWT.Selection, listener);
		// itemRefresh.addListener(SWT.Selection, listener);
		// itemGo.addListener(SWT.Selection, listener);
		// location.addListener(SWT.DefaultSelection, new Listener() {
		// public void handleEvent(Event e) {
		// browser.setUrl(location.getText());
		// }
		// });
	}

}
