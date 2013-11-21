/**
 * 
 */
package edu.ku.eecs;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author QtotheC
 *
 */
public class LeafNode extends TreeNode {
	private int leftSiblingPtr;
	private int rightSiblingPtr;
	
	public LeafNode(PageTable pages, int order)
	{
		super(pages, order);
		keys = new int[treeOrder];
		Arrays.fill(keys, -1);
		pointers = new int[treeOrder];
		Arrays.fill(pointers, -1);
		leftSiblingPtr = -1;
		rightSiblingPtr = -1;
	}

	@Override
	public int search(int key) {
		for (int i=0; i< keys.length; i++) {
			if (key == keys[i]) return pointers[i];
		}
		return -1;
	}

	@Override
	public void insert(int key, int value) throws Exception {
		if (numElements() >= keys.length) {
			// no more room for insertion
			throw new LeafNodeFullException();
		}
		else {
			int insertIndex = numElements();
			for (int i=0; i<numElements(); i++) {
				if (keys[i] >= key || keys[i] == -1) {
					if (keys[i] == key) throw new KeyExistsException();
					insertIndex = i;
					break;
				}
			}
			for (int i=numElements()-1; i >= insertIndex; i--) { // shift values down to make room for insertion
				keys[i+1] = keys[i];
				pointers[i+1] = pointers[i];
			}
			keys[insertIndex] = key;
			pointers[insertIndex] = value;
		}
	}
	
	public int insertionPoint(int key) throws KeyExistsException {
		for (int i=0; i<keys.length; i++ ) {
			if (keys[i] >= key || keys[i] == -1) {
				if (keys[i] == key) throw new KeyExistsException();
				return i;
			}
		}
		return numElements();
	}

	@Override
	public void delete(int key) throws KeyNotFoundException, LeafUnderflowException {
		if (numElements() == 0) {
			throw new KeyNotFoundException();
		}
		int deleteIndex = -1;
		for (int i=0; i<keys.length; i++) {
			if (keys[i] == key) {
				keys[i] = -1;
				pointers[i] = -1;
				deleteIndex = i;
				break;
			}
		}
		for (int i=deleteIndex; i<keys.length && deleteIndex != -1; i++) { // shift elements left to maintain continuity
			if (i+1 == keys.length) {
				keys[i] = -1;
				pointers[i] = -1;
			}
			else {
				keys[i] = keys[i+1];
				pointers[i] = pointers[i+1];
			}
		}
		if (deleteIndex == -1) throw new KeyNotFoundException();
		if (!isRoot() && numElements() < Math.ceil(treeOrder/2.0)) {
			throw new LeafUnderflowException();
		}
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	
	@Override
	protected byte[] flatten() {
		int keySize = 9;
		int ptrSize = 6;
		int siblingPtrSize = 4;
		ByteBuffer buff = ByteBuffer.allocate(keySize * treeOrder + ptrSize * treeOrder + siblingPtrSize*2);
		buff.order(ByteOrder.nativeOrder());
		for (int i=0; i<keys.length; i++) {
			buff.putInt(keys[i]);
			buff.position(buff.position()+(keySize-4));
			buff.putInt(pointers[i]);
			buff.position(buff.position()+(ptrSize-4));
		}
		buff.putInt(leftSiblingPtr);
		buff.putInt(rightSiblingPtr);
		return buff.array();
	}

	@Override
	protected void unflatten(byte[] array) {
		int keySize = 9;
		int ptrSize = 6;
		ByteBuffer buff = ByteBuffer.wrap(array);
		buff.order(ByteOrder.nativeOrder());
		for (int i=0; i<keys.length; i++) {
			keys[i] = buff.getInt();
			buff.position(buff.position()+(keySize-4));
			pointers[i] = buff.getInt();
			buff.position(buff.position()+(ptrSize-4));
		}
		leftSiblingPtr = buff.getInt();
		rightSiblingPtr = buff.getInt();
	}
	
	public int leftSiblingPtr() { return leftSiblingPtr; }
	public void leftSiblingPtr(int ptr) { leftSiblingPtr = ptr; }
	
	public int rightSiblingPtr() { return rightSiblingPtr; }
	public void rightSiblingPtr(int ptr) { rightSiblingPtr = ptr; }

	@Override
	public int numElements() {
		int counter = 0;
		for (int i=0; i<keys.length; i++) {
			if (keys[i] != -1) counter++;
			else break;
		}
		return counter;
	}
}
