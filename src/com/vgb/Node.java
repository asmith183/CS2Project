package com.vgb;

/**
 * Represents a node for use in the sorted linked list implementation
 * 
 * @param <T>
 */
public class Node<T> {

	private T item;
	private Node<T> next;

	public Node(T item) {
		super();
		this.item = item;
	}

	public Node<T> getNext() {
		return this.next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public T getItem() {
		return this.item;
	}

	/**
	 * Returns true if the node has a next node, returns false otherwise
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return (this.next != null);
	}

	/**
	 * Returns true if the node is a tail, false otherwise
	 * 
	 * @return
	 */
	public boolean isTail() {
		return (this.next == null);
	}

}
