package chat;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.MapConstraint;

import static com.google.common.base.Preconditions.checkNotNull;

class MutableClassToCompositeFactoryMap extends ForwardingMap<Class<?>, CompositeFactory<?>> implements ClassToCompositeFactoryMap {

	public static MutableClassToCompositeFactoryMap create() {
		return new MutableClassToCompositeFactoryMap(
				new HashMap<Class<?>, CompositeFactory<?>>());
	}

	public static <B> MutableClassToCompositeFactoryMap create(
			Map<Class<?>, CompositeFactory<?>> backingMap) {
		return new MutableClassToCompositeFactoryMap(backingMap);
	}

	private final Map<Class<?>, CompositeFactory<?>> delegate;

	private MutableClassToCompositeFactoryMap(Map<Class<?>, CompositeFactory<?>> delegate) {
		this.delegate = checkNotNull(delegate); 
	}

	@Override
	protected Map<Class<?>, CompositeFactory<?>> delegate() {
		return delegate;
	}

	public <T, F extends CompositeFactory<T>> F putFactory(Class<T> type, F factory) {
		return (F) delegate.put(type, factory);
//		return cast(type, put(type, factory));
	}

	public <T, F extends CompositeFactory<T>> F getFactory(Class<T> type) {
		return (F) delegate.get(type);
//		return cast(type, get(type));
	}

	public <T, F extends CompositeFactory<T>> F getFactory(T object) {
		return getFactory(object.getClass());
	}

//	private static CompositeFactory<?> cast(Class<?> type, CompositeFactory<?> factory) {
//		final Class<?> clazz = (Class<?>) factory.getClass();
//		final Class<?> subClass = CompositeFactory.class.asSubclass(clazz);
//		return (CompositeFactory<?>) subClass.cast(factory);
//	}
	
	private static <T, F extends CompositeFactory<T>> F cast(Class<T> type, F factory) {
		final Class<?> clazz = (Class<?>) factory.getClass();
		final Class<? extends F> subClass = (Class<? extends F>) CompositeFactory.class.asSubclass(clazz);
		return subClass.cast(factory);
	}

//		private static final long serialVersionUID = 0;
}
