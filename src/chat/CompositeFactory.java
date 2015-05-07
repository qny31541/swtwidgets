package chat;

import org.eclipse.swt.widgets.Composite;

interface CompositeFactory<T> {
	Composite create(T model, Composite parent, int style);
}
