package com.azry.utils.logparser.helpers;

import org.apache.commons.collections4.iterators.EmptyIterator;

import java.util.AbstractQueue;
import java.util.Iterator;

public class EmptyQueue<T> extends AbstractQueue<T> {
	@Override
	public Iterator<T> iterator() {
		return EmptyIterator.emptyIterator();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean offer(T t) {
		return false;
	}

	@Override
	public T poll() {
		return null;
	}

	@Override
	public T peek() {
		return null;
	}
}
