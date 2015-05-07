package chat;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
import org.eclipse.swt.widgets.Text;

import chat.Said.Voice;
import static chat.Layouts.*;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Chat extends Composite {

//	Map<Class<?>, CompositeFactory<?>> classToCompositeFactoryMap = newHashMap();
	MutableClassToCompositeFactoryMap classToCompositeFactoryMap = MutableClassToCompositeFactoryMap.create();
	{
		classToCompositeFactoryMap.putFactory(String.class, new CompositeFactory<String>() {
			@Override
			public Composite create(String model, Composite parent, int style) {
				Composite composite = new Composite(parent, style);
				Text text = new Text(composite, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER);
				text.setText(model);
				GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
				return composite;
			}
		});
		
		//TODO add more class to CompositeFactory mappings 
	}

	Deque<Said> said = new LinkedList<Said>();
	final Voice us;

	public Chat(final Voice us, Composite parent, int style) {
		super(parent, style);
		
		this.us = us;
		
		singleColumn.applyTo(this);
		
		Composite buttons = new Composite(this, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(6).applyTo(buttons);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(buttons);
		
		final Button weSaid = new Button(buttons, SWT.BORDER);
		weSaid.setText("Us");
		weSaid.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				addExampleUs();
			}
		});
		GridDataFactory.swtDefaults().applyTo(weSaid);
		
		final Button theySaid = new Button(buttons, SWT.BORDER);
		theySaid.setText("Them");
		theySaid.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				addExampleThem();
			}
		});
		GridDataFactory.swtDefaults().applyTo(weSaid);
	}

	public void addExampleUs() {
		add(us.say("We said ..."));
	}

	public void addExampleThem() {
		add(them.say("They said ..."));
	}

	public void add(Said s) {
		// add centred timestamp?
		long maxGap = 1000 * 60;
		if (said.isEmpty() || s.when - said.peekLast().when > maxGap) {
			Label timeStamp = new Label(this, SWT.NONE);
			timeStamp.setText(TimeUtils.timeDate());
			GridDataFactory.swtDefaults()
//						.align(SWT.END, SWT.BEGINNING) // at right (but for extended margins)
					.align(SWT.CENTER, SWT.BEGINNING) // in middle
				.grab(true, false).applyTo(timeStamp);
		}
		
		said.add(s);
		
		render(s);
		
		last = s.when;
	}

	long last = -1;

	public void render(Said s) {
		Composite parent = this;
		
		boolean us = s.voice == this.us;
		
		Composite outer = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(outer);
		singleColumn.copy().extendedMargins(0, us ? 40 : 0, 0, 0).applyTo(outer);
		
		Composite entry = new Composite(
//				parent,
				outer,
//				SWT.NONE);
				SWT.BORDER);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
			.indent(us ? 0 : 50, 0).applyTo(entry);
		singleColumn
//				.copy().extendedMargins(0, 40, 0, 0)
				.applyTo(entry);

		/*
//		if (!us) {
			Label who = new Label(entry, SWT.NONE);
			who.setText(s.who()); // only show for whoever isn't us
//			GridDataFactory.swtDefaults().align(us ? SWT.BEGINNING: SWT.END, SWT.BEGINNING).applyTo(who);
//		}
		*/
		
		Composite whoTimeDate = new Composite(entry, SWT.NONE);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(whoTimeDate);
		GridLayoutFactory.swtDefaults().numColumns(2).margins(1, 1).applyTo(whoTimeDate);
		Label who = new Label(whoTimeDate, SWT.NONE);
		who.setText(s.who()); // only show for whoever isn't us
		if (last != -1 && s.when - last > 1000) {
			Label when = new Label(whoTimeDate, SWT.NONE);
//			when.setText(s.when() + " ago");
//			when.setText(TimeUtils.timeDate());
			when.setText(TimeUtils.time() + " (" + s.since(last) + " gap" + ")");
			GridDataFactory.swtDefaults()
					.align(SWT.END, SWT.BEGINNING) // at right (but for extended margins)
					.grab(true, false).applyTo(when);
		}

		
		Composite what = classToCompositeFactoryMap.getFactory(s.what).create(s.what, entry, SWT.NONE);//SWT.BORDER);
//		GridLayoutFactory.fillDefaults().applyTo(what);
		singleColumn.copy().margins(1, 1).applyTo(what);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(what);
		
		/*
		Label when = new Label(entry, SWT.NONE);
//		when.setText(s.when() + " ago");
		when.setText(TimeUtils.timeDate());
//		GridDataFactory.swtDefaults().align(us ? SWT.BEGINNING: SWT.END, SWT.BEGINNING).applyTo(when);
		GridDataFactory.swtDefaults().align(SWT.END, SWT.BEGINNING).applyTo(when); // always to right
		*/
		
		parent.layout();
	}

	private static final Voice them = new Said.Voice("WF");

	public static void main(String[] args) throws InterruptedException {
		final Display display = Display.getDefault();//new Display();
		Shell shell = new Shell(display);
		singleColumn.applyTo(shell);
		shell.setBounds(400, 400, 640, 480);
		
		final Voice us = new Said.Voice("GDA");
		final Chat chat = new Chat(us, shell, SWT.NONE);
		filling.applyTo(chat);
		
		shell.setText("Chat");
		shell.open();
		
//		Thread.sleep(1000);
		display.asyncExec(new Runnable() {@Override public void run() {chat.addExampleUs();}});
//		Thread.sleep(2000);
		display.asyncExec(new Runnable() {@Override public void run() {try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}});
		display.asyncExec(new Runnable() {@Override public void run() {chat.addExampleThem();}});
//		Thread.sleep(1000);
		display.asyncExec(new Runnable() {@Override public void run() {chat.addExampleThem();}});
//		Thread.sleep(2000);
		display.asyncExec(new Runnable() {@Override public void run() {chat.addExampleUs();}});
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		
		display.dispose();
		
		System.exit(0);
	}
}