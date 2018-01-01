package com.zevinar.crypto.utils.datastruct;

public class Wrapper<T> {
	private T innerElement;
	public Wrapper(){
		super();
	}
	public Wrapper(T innerElement) {
		this();
		this.innerElement = innerElement;
	}
	
	public T getInnerElement() {
		return innerElement;
	}
	public void setInnerElement(T innerElement) {
		this.innerElement = innerElement;
	}
	
	public boolean isEmpty() {
		return innerElement == null;
	}
}
