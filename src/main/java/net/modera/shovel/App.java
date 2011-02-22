package net.modera.shovel;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
	static Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		
		ApplicationContext context =
		    new ClassPathXmlApplicationContext(new String[] {"context.xml"});
		
		logger.info("Application is starting");
				
		((MainWindow)context.getBean("mainWindow")).show();
		
		return;
		
//		
//		
//		shell.open();
//		while (!shell.isDisposed()) {
//			while (!shell.getDisplay().readAndDispatch()) {
//				shell.getDisplay().sleep();
//			}
//		}
//		
//		return;
		
		//traveler.beginWithResource(providers.get(0).findResources("lucine").get(0));
		
		
		
		
		

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
