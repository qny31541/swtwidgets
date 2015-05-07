package chat;

interface ClassToCompositeFactoryMap {

	<T, F extends CompositeFactory<T>> F putFactory(Class<T> type, F factory);
//	<T, F extends CompositeFactory<? extends T>> F putFactory(Class<? extends T> type, F factory);

	<T, F extends CompositeFactory<T>> F getFactory(Class<T> type);
//	<T, F extends CompositeFactory<? extends T>> F getFactory(Class<? extends T> type);

}
