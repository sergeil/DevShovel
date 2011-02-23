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
		
		logger.info("Application shutdown");
		
		return;
		
	}
}
