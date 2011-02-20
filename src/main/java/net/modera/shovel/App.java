package net.modera.shovel;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.graph.ResourceTravelerProjection;
import net.modera.shovel.provider.LucineProvider;
import net.modera.shovel.traveler.ResourceProvider;
import net.modera.shovel.traveler.ResourceTraveler;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
	
	public static void main(String[] args) {
		
		ApplicationContext context =
		    new ClassPathXmlApplicationContext(new String[] {"context.xml"});
		
		final Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("Graph Snippet 5");
		shell.setMaximized(true);
		shell.setLayout(new FillLayout());
//		shell.setSize(400, 400);
		
		// bussines
		ResourceTraveler traveler = new ResourceTraveler();
		List<ResourceProvider> providers = new ArrayList<ResourceProvider>();
		providers.add(new LucineProvider());
		traveler.setConnectionProviders(providers);
		
		ResourceTravelerProjection projection = new ResourceTravelerProjection(shell);
		projection.setTraveler(traveler);
		
		traveler.addListener(projection);
		
		System.out.println(providers.get(0).getResources("lucine"));
		
		return;
		
		//traveler.beginWithResource(providers.get(0).findResources("lucine").get(0));
		
		// clean up
//		shell.open();
//		while (!shell.isDisposed()) {
//			while (!d.readAndDispatch()) {
//				d.sleep();
//			}
//		}
		
		
		

//		Display d = new Display();
//		Shell shell = new Shell(d);
//		shell.setText("GraphSnippet1");
//		shell.setLayout(new FillLayout());
//		shell.setSize(400, 400);
//
//		Graph g = new Graph(shell, SWT.NONE);
//
//		GraphNode n = new GraphNode(g, SWT.NONE, "Paper");
//		GraphNode n2 = new GraphNode(g, SWT.NONE, "Rock");
//		GraphNode n3 = new GraphNode(g, SWT.NONE, "Scissors");
//		new GraphConnection(g, SWT.NONE, n, n2);
//		new GraphConnection(g, SWT.NONE, n2, n3);
//		new GraphConnection(g, SWT.NONE, n3, n);
//		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
//
//		g.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				
//				System.out.println(((Graph) e.widget).getSelection());
//				@SuppressWarnings("unchecked")
//				List<GraphNode> nodes = (List<GraphNode>)((Graph) e.widget).getSelection(); 
//				nodes.get(0).dispose();
//				
//			}
//		});
//		
//		shell.open();
//		while (!shell.isDisposed()) {
//			while (!d.readAndDispatch()) {
//				d.sleep();
//			}
//		}
		
	}
}
