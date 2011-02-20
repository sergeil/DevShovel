package net.modera.shovel;

import java.util.ArrayList;
import java.util.List;

import net.modera.shovel.graph.ResourceTravelerProjection;
import net.modera.shovel.resourceproviders.ContextualResourceProvider;
import net.modera.shovel.resourceproviders.ResourceProvider;
import net.modera.shovel.traveler.ResourceTraveler;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
	
	public static void main(String[] args) {
		ApplicationContext cx =  new ClassPathXmlApplicationContext(
			new String[] {"spring/resource-providers-context.xml"}
		);
		ResourceProvider resourceProvider = (ResourceProvider)cx.getBean(ContextualResourceProvider.class);
		
		final Display d = new Display();
		Shell shell = new Shell(d);
		shell.setText("DevShovel");
		shell.setMaximized(true);
		shell.setLayout(new FillLayout());
		
		// business
		ResourceTraveler traveler = new ResourceTraveler();
		List<ResourceProvider> providers = new ArrayList<ResourceProvider>();
		providers.add(resourceProvider);
		traveler.setConnectionProviders(providers);
		
		ResourceTravelerProjection projection = new ResourceTravelerProjection(shell);
		projection.setTraveler(traveler);
		
		traveler.addListener(projection);
		traveler.beginWithResource(providers.get(0).findResources(null).get(0));
		
		// clean up
		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
	}
}
