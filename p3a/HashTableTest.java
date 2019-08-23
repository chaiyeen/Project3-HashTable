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
import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*; 
import org.junit.jupiter.api.Assertions;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Random;

/**
 * several methods testing the HashTable class 
 * @author chaiy
 *
 */
public class HashTableTest{

	// didn't need any code to run before each test
	@Before
	public void setUp() throws Exception {

	}

	// didn't need any code to run after each test
	@After
	public void tearDown() throws Exception {

	}

	/** 
	 * Tests that a HashTable returns an integer code
	 * indicating which collision resolution strategy 
	 * is used.
	 * REFER TO HashTableADT for valid collision scheme codes.
	 */
	@Test
	public void test000_collision_scheme() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>();
		int scheme = htIntegerKey.getCollisionResolution();
		if (scheme < 1 || scheme > 9) 
			fail("collision resolution must be indicated with 1-9");
	}

	/** IMPLEMENTED AS EXAMPLE FOR YOU
	 * Tests that insert(null,null) throws IllegalNullKeyException
	 */
	@Test
	public void test001_IllegalNullKey() {
		try {
			HashTableADT htIntegerKey = new HashTable<Integer,String>();
			htIntegerKey.insert(null, null);
			fail("should not be able to insert null key");
		} 
		catch (IllegalNullKeyException e) { /* expected */ } 
		catch (Exception e) {
			fail("insert null key should not throw exception "+e.getClass().getName());
		}
	}

	/**
	 * test constructor with initial Capacity given
	 */
	@Test
	public void test002_constructor_with_initialCapacity() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.75);
		if(htIntegerKey == null) 
			fail("constructor is not working");
	}

	/**
	 * see if insert method work when initial capacity value and threshold are given 
	 */
	@Test
	public void test003_insert_one() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "first");
			//should work

		} catch (IllegalNullKeyException e) {
			fail("shouldn't throw IllegalNullKeyException after an insert");

		} catch (DuplicateKeyException e) {
			fail("shouldn't throw DuplicateKeyException after an insert");
		}
	}

	/**
	 * test if the insert method appropriately throws an exception
	 * when identical key is thrown
	 */
	@Test
	public void test004_insert_identical_key() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(1, "two");
			fail("fail"); // should not allow getting identical key
		} catch (IllegalNullKeyException e) {
			fail("shouldn't throw IllegalNullKeyException after an insert");
		} catch (DuplicateKeyException e) {
			//should happen
		}
	}

	/**
	 * test if remove method from the hashtable is working appropriately
	 */
	@Test
	public void test005_remove_key() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.remove(1);

		} catch (IllegalNullKeyException e) {
			fail("shouldn't throw IllegalNullKeyException - test5");

		} catch (DuplicateKeyException e) {
			fail("shouldn't throw DuplicateKeyException - test5");		
		}
	}

	/**
	 * see if the remove method throws appropriate exception (illegalnullkeyexception) 
	 * when nonexisting key is told to be removed
	 */
	@Test
	public void test006_remove_nonexisting_key() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "one");
			if(htIntegerKey.remove(3) != false) {
				fail("should return false");
			}

		} catch (IllegalNullKeyException e) {
			//should happen
		} catch (DuplicateKeyException e) {
			fail("fail");
		}
	}

	/**
	 * test if key getter is working right
	 */
	@Test
	public void test007_get_key() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "first");
		} catch (IllegalNullKeyException e) {
			fail("should return inserted key's V value");        
		} catch (DuplicateKeyException e) {
			fail("should return inserted key's V value");        
		}
		try {
			if(htIntegerKey.get(1).equals("first")) {
				// should happen
				return;
			}
			else {
				fail("should return inserted key's V value");        
			}
		} catch (IllegalNullKeyException e) {
			fail("should return inserted key's V value");        
		} catch (KeyNotFoundException e) {
			fail("should return inserted key's V value");        
		}
	}

	/**
	 * see if the get key method throws an keyNotFoundException appropriately
	 * when they are told to get a key that doesn't exist
	 */
	@Test
	public void test008_get_key_doesnot_exist() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "first");
		} catch (IllegalNullKeyException e) {
			fail("should return inserted key's V value");        
		} catch (DuplicateKeyException e) {
			fail("should return inserted key's V value");        
		}
		try {
			if(htIntegerKey.get(2).equals("first")) {
				fail("should not return anything");        
			}
			else {
				fail("should throw an exception");        
			}
		} catch (IllegalNullKeyException e) {
			fail("should throw an key not found exception");        
		} catch (KeyNotFoundException e) {
			//should happen
		}
	}

	/**
	 * test if the rehashing method is appropriately working 
	 * after inserting three elements
	 */
	@Test
	public void test009_test_rehashing() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try { 
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(2, "two");
			htIntegerKey.insert(3, "three");
			//should work
		}
		catch(Exception e){
			fail("test9 fail");        	
		}
		if(htIntegerKey.numKeys() != 3) {
			fail("fail");
		}
	}

	/**
	 * test if the get Load Factor method is working appropriately 
	 */
	@Test
	public void test010_test_getLF() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(5, 0.7);

		try {
			htIntegerKey.insert(1, "one");
		}
		catch(Exception e) {
			fail("test 10 fail");
		}
		if(htIntegerKey.getLoadFactor() != 0.2) {
			fail("fail test 10");
		}
		//should happen
	}

	/**
	 * test if rehashing method is working appropriately when eleven elements with different keys are inserted 
	 */
	@Test
	public void test011_secondtime_test_rehashing() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.75);

		try { 
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(123, "two");
			htIntegerKey.insert(456, "three");
			htIntegerKey.insert(7000, "four");
			htIntegerKey.insert(37, "five");
			htIntegerKey.insert(666, "six");
			htIntegerKey.insert(7777, "seven");
			htIntegerKey.insert(88, "eight");
			htIntegerKey.insert(9, "nine");
			htIntegerKey.insert(100, "ten");
			htIntegerKey.insert(1100, "eleven");        	
			//should work
		}
		catch(Exception e){
			fail("test9 fail");        	
		}
		if(htIntegerKey.numKeys() != 11) {
			fail("fail");
		}
	}
	
	/**
	 * second test method for remove 
	 */
	@Test
	public void test012_remove_key_2() {
		HashTableADT htIntegerKey = new HashTable<Integer,String>(3, 0.7);
		try {
			htIntegerKey.insert(1, "one");
			htIntegerKey.insert(123, "two");
			htIntegerKey.insert(456, "three");
			htIntegerKey.insert(7000, "four");
			htIntegerKey.insert(37, "five");

			htIntegerKey.remove(1);
			htIntegerKey.remove(7000);
			htIntegerKey.remove(123);
			htIntegerKey.remove(456);
			htIntegerKey.remove(37);
			//should happen

		} catch (IllegalNullKeyException e) {
			fail("shouldn't throw IllegalNullKeyException - test5");

		} catch (DuplicateKeyException e) {
			fail("shouldn't throw DuplicateKeyException - test5");		
		}
	}

}
