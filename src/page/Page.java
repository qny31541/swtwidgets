package page;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Page extends Composite {
	
	Page outermost;
	
	public Page(Composite parent, int style) {
		super(parent, style | SWT.BORDER);
		singleColumn.applyTo(this);
		
//		Label label = new Label(this, SWT.BORDER);
//		label.setText("test");
//		GridDataFactory.swtDefaults().applyTo(label);
		
		
		if (parent instanceof Page) outermost = ((Page) parent).outermost;
		if (outermost == null) outermost = this;
		
		Button button = new Button(this, SWT.BORDER);
		GridDataFactory.swtDefaults().applyTo(button);
		button.setText("add");
		button.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				Page page = new Page(Page.this, SWT.BORDER);
				filling.applyTo(page);
				page.outermost.layout();
//				page.outer.layout();
				page.getParent().layout(); // == Page.this.layout() no?
			}
		});
	}

	static final GridLayoutFactory singleColumn = GridLayoutFactory.swtDefaults().numColumns(1);
	
	static final GridDataFactory filling = GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true);
	
	public static void main(String[] args) {
		Display display = Display.getDefault();//new Display();
		Shell shell = new Shell(display);
		singleColumn.applyTo(shell);
		shell.setBounds(400, 400, 640, 480);
		
		Page page = new Page(shell, SWT.NONE);
		filling.applyTo(page);
		
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}

