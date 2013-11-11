package edu.ku.eecs;

/**
 * 
 * @author hgrimberg
 * 
 */

public class Page {

	public byte[] contents;

	/**
	 * 
	 */

	public Page() {
		this.contents = new byte[500];
	}

	/**
	 * 
	 * @param size
	 */

	public Page(int size) {
		this.contents = new byte[size];
	}

	/**
	 * Delete everything on the page by writing zeros
	 */

	public void purge() {

		int size = contents.length;
		for (int i = 0; i < size; i++) {
			contents[i] = 0;
		}
		return;
	}

}
