package chat;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;

public class Layouts {

	static final GridLayoutFactory singleColumn = GridLayoutFactory.swtDefaults().numColumns(1);

	static final GridDataFactory filling = GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true);

}
