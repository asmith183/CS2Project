package com.vgb;

import java.util.Comparator;
import java.util.Iterator;

/**
 * This is a linked list implementation that maintains sorted order based on the
 * given comparator given at instantiation.
 * 
 * @param <T>
 * 
 */
public class SortedLinkedList<T> implements Iterable<T> {

	private Node<T> head;
	private int size;
	private Comparator<T> comparator;

	public SortedLinkedList(Comparator<T> comparator) {
		super();

		this.comparator = comparator;
	}

	/**
	 * Adds an element to the list, maintaining sorted order
	 * 
	 * @param element
	 */
	public void add(T element) {
		if (element == null) {
			return;
		}

		Node<T> u = new Node<>(element);

		if (this.head == null || comparator.compare(element, head.getItem()) < 0) {
			u.setNext(head);
			head = u;
		} else {
			Node<T> current = head;

			while (current.hasNext() && comparator.compare(element, current.getNext().getItem()) >= 0) {
				current = current.getNext();

			}

			u.setNext(current.getNext());

			current.setNext(u);
		}

		size++;

		return;
	}

	/**
	 * Deletes an element from the list
	 * 
	 * @param element
	 * @return
	 */
	public boolean delete(T element) {
		int index = getIndex(element);

		if (index == -1) {
			return false;
		}

		if (index == 0) {
			head = head.getNext();
			size--;
			return true;
		}

		Node<T> current = this.head;

		for (int i = 0; i < index - 1; i++) {
			current = current.getNext();

		}

		current.setNext(current.getNext().getNext());
		size--;

		return true;
	}

	/**
	 * Returns the size of the list
	 * 
	 * @return
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns the index of the first instance of the given element if it exists,
	 * returns -1 otherwise
	 * 
	 * @param element
	 * @return
	 */
	public int getIndex(T element) {
		Node<T> current = head;
		int index = 0;

		while (current != null) {

			if (comparator.compare(element, current.getItem()) == 0) {
				return index;
			}

			current = current.getNext();
			index++;

		}
		return -1;
	}

	/**
	 * Iterator method for the list implementation
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> current = head;

			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public T next() {
				T item = current.getItem();
				current = current.getNext();
				return item;
			}

		};
	}

}
