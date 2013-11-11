package edu.ku.eecs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author hgrimberg
 * 
 */
public class PageTable {
	/**
	 * 
	 */
	private Page[] pages;
	/**
	 * 
	 */
	private Queue<Integer> free;
	/**
	 * 
	 */
	private Queue<Integer> used;

	/**
	 * 
	 * @param numOfPages
	 * @param pageSize
	 */
	public PageTable(int numOfPages, int pageSize) {

		free = new LinkedList<Integer>();
		used = new LinkedList<Integer>();

		for (int i = 0; i < numOfPages; i++) {
			pages[i] = new Page(pageSize);
			free.add(new Integer(i));
		}
	}

	/**
	 * 
	 * @param numOfPages
	 */
	public PageTable(int numOfPages) {
		for (int i = 0; i < numOfPages; i++) {
			pages[i] = new Page(500);
			free.add(new Integer(i));
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Page getFreePage() throws Exception {
		Integer freeIndex = free.poll();
		used.add(freeIndex);
		if (freeIndex == null) {
			throw new Exception();

		} else {
			return pages[freeIndex];
		}

	}

	/**
	 * 
	 * @param index
	 * @throws Exception
	 */
	public void deletePage(int index) throws Exception {
		if (used.contains(index)) {
			used.remove(index);
			Page toDelete = pages[index];
			toDelete.purge();
			free.add(index);

		} else {
			throw new Exception();
		}
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public Page getIndexedPage(int index) throws Exception {
		if (used.contains(index)) {
			return pages[index];
		} else {
			throw new Exception();
		}

	}

}