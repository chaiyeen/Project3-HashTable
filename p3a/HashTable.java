//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION ////////////////////////
// Title:           P3a HashTable                    							//
// Files:           HashTable,HashTableTest 									//	
// Course:          COMP 400, Spring, 2018										//
// Author:          Chaiyeen Oh													//
// Email:           coh26@wisc.edu												//
// Lecturer's Name: Andrew Kuemmel												//
// Due Date : 3/14 10pm															//
// 																				//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:			//
//   _X_ Write-up states that pair programming is allowed for this assignment.	//
//   _X_ We have both read and understand the course Pair Programming Policy.	//
//   _X_ We have registered our team prior to the team registration deadline.	//
//																				//
///////////////////////////// CREDIT OUTSIDE HELP ////////////////////////////////
//																				//
// Students who get help from sources other than their partner must fully 		//
// acknowledge and credit those sources of help here.  Instructors and TAs do 	//
// not need to be credited here, but tutors, friends, relatives, room mates, 	//
// strangers, and others do.  If you received no outside help from either type	//
//  of source, then please explicitly indicate NONE.							//
//																				//
// Persons:         X															//
// Online Sources:  X															//
//																				//
/////////////////////////////// 80 COLUMNS WIDE //////////////////////////////////
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * HashTable class contains inner class called Pair and several methods that works like a hashTable
 * it inserts, remove, gets LoadFactor, gets threshold and more to the table
 * This class will expand its hashTable if Load Factor is bigger than its Threshold to avoid the collisions
 * And for this project I decided to use Chained Buckets
 * 
 * @author chaiy
 *
 * @param <K>
 * @param <V>
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

	/**
	 * inner class named Pair that would work as a node 
	 * @author chaiy
	 *
	 * @param <K>
	 * @param <V>
	 */
	private class Pair<K,V> {
		private K k; // key k
		private V v; // key v

		/**
		 * constructor making a node 
		 * @param k
		 * @param v
		 */
		private Pair (K k, V v){
			this.k = k;
			this.v = v;
		}

		/**
		 * getter method for object v value
		 * @return V value
		 */
		private V getV() {
			return this.v;
		}

		/**
		 * getter method for K key
		 * @return K key
		 */
		private K getKey() {
			return this.k;  // returns this key
		}
	}


	private ArrayList<LinkedList <Pair<K,V>>> myTable; // arraylist of linked list that will work as a hashTable
	private ArrayList<LinkedList <Pair<K,V>>> second_hashTable; // arraylist of linked list that will be used for rehashing method

	//fields
	private double LFthreshold;
	private int Capacity=0;
	private int numKey=0;
	private Pair<K, V> pair;

	/**
	 * constructor method for HashTable class
	 */
	public HashTable() {

	}

	/**
	 * Constructs a new, empty hashtable (of Arraylist with Linked Lists inside)
	 * with the specified initial capacity and the specified load factor.
	 * 
	 * @param initialCapacity = initial capacity of the hashtable (>0)
	 * @param loadFactorThreshold
	 * 
	 */
	public HashTable(int initialCapacity, double loadFactorThreshold) {
		myTable = new ArrayList<LinkedList <Pair<K,V>>>(initialCapacity); 
		LFthreshold = loadFactorThreshold; 
		Capacity = initialCapacity;

		for(int i=0; i<initialCapacity; i++) { // add linked list inside of arraylist's "index"
			LinkedList<Pair<K,V>> newLList = new LinkedList(); 
			myTable.add(i, newLList); 
		}
	}
	
	/**
	 * this method will help to insert a method with specific key and value
	 * if the given key is null, it will throw an IllegalNullKeyException
	 * if the given key is already exists, it will throw an DuplicateKeyException
	 * 
	 * When the Load Factor threshold is reached, capacity must increase twice + 1 
	 * then the table is resized and elements are rehashed
	 * 
	 * @param K key
	 * @param V value
	 */
	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
		if(key == null) {
			throw new IllegalNullKeyException();
		}
		if(exists(key)) { // same key
			throw new DuplicateKeyException();
		}

		numKey++;
		// need resize/rehash
		if(getLoadFactor() >= getLoadFactorThreshold()) {
			Capacity = (2*Capacity) + 1;    // resize
			myTable = rehashing(Capacity); //  rehashed
		}

		int index = Math.abs(key.hashCode()%myTable.size());// hashCode CAN RETURN NEGATIVE NUMBER
		Pair<K, V> pair = new Pair<K, V>(key, value);
		myTable.get(index).add((Pair<K, V>) pair); // actually adding an element to the table
	}

	/**
	 * helper method for rehashing the array list of linked lists
	 * 
	 * @param givenCapacity
	 * @return ArrayList<LinkedList <Pair<K,V>>> resized, rehashed
	 */
	private ArrayList<LinkedList <Pair<K,V>>> rehashing(int givenCapacity) {
		// create empty second_hashTable where the elements from old table will be passed into
		second_hashTable = new ArrayList<LinkedList <Pair<K,V>>>(givenCapacity);
		for(int i=0; i<givenCapacity; i++) {
			LinkedList<Pair<K,V>> newLList = new LinkedList<Pair<K,V>>(); 
			second_hashTable.add(i, newLList); // add empty linked list inside each index
		}		

		// re-hashing from the old table elements(myTable)
		for(int j=0; j<myTable.size(); j++) {
			LinkedList<Pair<K,V>> eachList = myTable.get(j); 

			if(eachList != null) { // linked list
				for(int k=0; k<eachList.size(); k++) {
					if(eachList.get(k)!= null) { // found element from old array, will insert it to the new one
						int index = Math.abs(eachList.get(k).getKey().hashCode() % Capacity); // index for new table
						Pair<K, V> InsertPair = new Pair<K, V>(eachList.get(k).getKey(), eachList.get(k).getV());
						second_hashTable.get(index).add(InsertPair);
					}
				}

			}
		}	
		return second_hashTable;
	}

	/**
	 * method that checks if the arraylist of linked list has K value same as the given key K value
	 * @param key
	 * @return true if key exists in the myTable, otherwise returns false
	 */
	private boolean exists(K key) {
		int hashcode = Math.abs(key.hashCode());
		int whichIndex = Math.abs(hashcode % Capacity);

		LinkedList<Pair<K,V>> whichLL = new LinkedList<Pair<K,V>>(); // which Linked List from myTable(hashTable)
		whichLL = myTable.get(whichIndex);

		for(int i=0; i<whichLL.size(); i++) {
			if(whichLL.get(i).getKey().equals(key)) {
				return true;
			}   	
		}
		return false;
	}

	/**
	 * if the key is null, it throws an IllegalNullKeyException
	 * if the key is found, remove and decrease numKey
	 * 
	 * @param key
	 * @return true if the remove method successfully went through, otherwise false
	 * 
	 */
	@Override
	public boolean remove(K key) throws IllegalNullKeyException {
		if(key == null) {
			throw new IllegalNullKeyException();
		}

		if(exists(key)) { // if table have key
			int hashcode = key.hashCode();
			int whichIndex = Math.abs(hashcode % Capacity);

			LinkedList<Pair<K,V>> whichLL = new LinkedList<Pair<K,V>>();
			whichLL = myTable.get(whichIndex);

			for(int i=0; i<whichLL.size(); i++) {
				if(whichLL.get(i).getKey().equals(key)) {
					whichLL.remove(i); 
					numKey--;
					return true;
				}   	
			}
		}
		return false;
	}

	@Override
	/**
	 * If key is null, throw IllegalNullKeyException
	 * If key is not found, throw KeyNotFoundException()
	 * 
	 * Returns the value associated with the specified key
	 * Does not remove key or decrease number of keys
	 * 
	 * @param K key
	 * @return V value paired with key
	 */
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if(key == null) {
			throw new IllegalNullKeyException();
		}
		if(!exists(key)) {
			throw new KeyNotFoundException();
		}
		
		int checkingCode = Math.abs(key.hashCode());
		int whichIndex = Math.abs(checkingCode % Capacity); // which Linked List
		LinkedList<Pair<K,V>> whichLL = new LinkedList<Pair<K,V>>();
		whichLL = myTable.get(whichIndex);

		for(int i=0; i<whichLL.size(); i++) {
			if(whichLL.get(i).getKey().equals(key)) {
				return whichLL.get(i).getV();
			}   	
		}	
		return null;
	}

	/**
	 * getter for numKey
	 * @return Returns the number of key,value pairs in the data structure
	 */
	@Override
	public int numKeys() {
		return numKey;
	}

	/**
	 * Returns the load factor threshold that was passed into the constructor when creating the instance of the HashTable
	 * @return LFthreshold (double)
	 */
	@Override
	public double getLoadFactorThreshold() {
		return LFthreshold;
	}

	/**
	 * getter for load factor
	 * Load Factor = number of items/current table size
	 * 
	 * @return current load factor of myTable (hashTable)
	 */
	@Override
	public double getLoadFactor() {
		return (double)numKey/Capacity;
	} 

	/**
	 * Return the current Capacity (table size) of the hash table array
	 * 
	 * The initial capacity must be a positive integer, 1 or greater 
	 * and is specified in the constructor.
	 * 
	 * Once increased, the capacity never decreases
	 * 
	 * @return Capacity
	 */
	@Override
	public int getCapacity() {
		return Capacity;
	}

	/**
	 * Implement with one of the following collision resolution strategies.
	 * Define this method to return an integer to indicate which strategy.
	 * 
	 * @return  the collision resolution scheme used for this hash table.
	 *
	 */
	@Override
	public int getCollisionResolution() {
		return 5;      // 5 CHAINED BUCKET: array of linked nodes
	}

}
