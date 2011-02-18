package net.modera.shovel;

import java.util.List;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class App {
	/**
	 * Merges 2 images so they appear beside each other
	 * 
	 * You must dispose this image!
	 * @param image1
	 * @param image2
	 * @param result
	 * @return
	 */
	public static Image mergeImages(Image image1, Image image2) {
		Image mergedImage = new Image(Display.getDefault(), image1.getBounds().width + image2.getBounds().width, image1.getBounds().height);
		GC gc = new GC(mergedImage);
		gc.drawImage(image1, 0, 0);
		gc.drawImage(image2, image1.getBounds().width, 0);
		gc.dispose();
		return mergedImage;
	}
	
	public static final int BACKSPACE = 8;
	public static final int ENTER = 13;
	
	public static void main(String[] args) {
		
		final Map figureListing = new HashMap();
		final StringBuffer stringBuffer = new StringBuffer();
		final Display d = new Display();
		FontData fontData = d.getSystemFont().getFontData()[0];
		fontData.height = 42;

		final Font font = new Font(d, fontData);

		Shell shell = new Shell(d);
		shell.setText("Graph Snippet 5");
		Image image1 = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
		Image image2 = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
		Image image3 = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		shell.setLayout(new FillLayout());
		shell.setSize(400, 400);

		final Graph g = new Graph(shell, SWT.NONE);
		g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		GraphNode n1 = new GraphNode(g, SWT.NONE, "org.eclipse.Information", image1);
		GraphNode n2 = new GraphNode(g, SWT.NONE, "org.eclipse.Warning", image2);
		GraphNode n3 = new GraphNode(g, SWT.NONE, "org.eclipse.Error", image3);
		figureListing.put(n1.getText().toLowerCase(), n1);
		figureListing.put(n2.getText().toLowerCase(), n2);
		figureListing.put(n3.getText().toLowerCase(), n3);

		new GraphConnection(g, SWT.NONE, n1, n2);
		new GraphConnection(g, SWT.NONE, n2, n3);
		n1.setLocation(10, 10);
		n2.setLocation(200, 10);
		n3.setLocation(200, 200);

		g.addKeyListener(new KeyAdapter() {

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
				Iterator iterator = figureListing.keySet().iterator();
				List list = new ArrayList();
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

		shell.open();
		while (!shell.isDisposed()) {
			while (!d.readAndDispatch()) {
				d.sleep();
			}
		}
		image1.dispose();
		image2.dispose();
		image3.dispose();
		font.dispose();

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
