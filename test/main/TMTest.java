package main;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.management.loading.MLet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This test class is all kinds of weird, as it kinda both tests the Turing machine implementation, and the turing
 * machines themselves.
 * 
 * @author Baste
 * 
 */
public class TMTest {

	private TM simpleTM;

	/**
	 * tm2 takes binary input. It goes to the end of the input, prints "F", and then goes back, flipping all of the bits
	 */
	private TM tm2;

	/**
	 * Does one run of bubblesort through tape of A, B, C.
	 */
	private TM oneBubble;

	/**
	 * Bubblesorts
	 */
	private TM bubbleSort;

	@Before
	public void setup() {
		setUpSimpleTM();
		setUpTm2();
		setUpOneBubble();
		setUpBubbleSort();
	}

	@Test
	public void simpleTM() {
		checkTM(simpleTM, new String[0], new String[] { "S", "0", "1" }, false);
	}

	@Test
	public void tm2() throws Exception {
		checkTM(tm2, new String[] { "0", "0", "1", "1" }, new String[] { "1", "1", "0", "0", "F" }, false);
	}

	@Test
	public void oneBubble1() throws Exception {
		checkTM(oneBubble, new String[] { "a", "b" }, new String[] { "a", "b" }, false);
		checkTM(oneBubble, new String[] { "b", "a" }, new String[] { "a", "b" }, false);
		checkTM(oneBubble, new String[] { "b", "b", "a", "a" }, new String[] { "b", "a", "a", "b" }, false);
		checkTM(oneBubble, new String[] { "b", "c" }, new String[] { "b", "c" }, false);
		checkTM(oneBubble, new String[] { "c", "b" }, new String[] { "b", "c" }, false);
		checkTM(oneBubble, new String[] { "c", "a" }, new String[] { "a", "c" }, false);
		checkTM(oneBubble, new String[] { "c", "b", "a" }, new String[] { "b", "a", "c" }, false);
	}

	@Test
	public void bubbleSort() throws Exception {
		checkTM(bubbleSort, new String[] { "c", "b", "a" }, new String[] { "a", "b", "c" }, false);
		checkTM(bubbleSort, new String[] { "b", "b", "a", "a" }, new String[] { "a", "a", "b", "b" }, false);
		checkTM(bubbleSort, new String[] { "c", "c", "b", "b", "a", "a", "c", "b", "a" }, new String[] { "a", "a", "a",
				"b", "b", "b", "c", "c", "c" }, false);
	}

	private void setUpBubbleSort() {
		// R: "Run" (normal)
		bubbleSort = new TM("R");
		bubbleSort.setTransition("R", "a", "R", TM.RIGHT);
		bubbleSort.setTransition("R", "b", "B", TM.RIGHT);
		bubbleSort.setTransition("R", "c", "C", TM.RIGHT);

		// B: "the last character read was b"
		bubbleSort.setTransition("B", "c", "C", TM.RIGHT);
		bubbleSort.setTransition("B", "b", "B", TM.RIGHT);

		bubbleSort.setTransition("B", "a", "Ba", "b");
		bubbleSort.setTransition("Ba", "b", "Wa", TM.LEFT);

		// C: "the last character read was c"
		bubbleSort.setTransition("C", "c", "C", TM.RIGHT);

		bubbleSort.setTransition("C", "b", "Cb", "c");
		bubbleSort.setTransition("Cb", "c", "Wb", TM.LEFT);

		bubbleSort.setTransition("C", "a", "Ca", "c");
		bubbleSort.setTransition("Ca", "c", "Wa", TM.LEFT);

		// Wx: "Write x"
		bubbleSort.setTransition("Wa", "b", "R", "a");
		bubbleSort.setTransition("Wa", "c", "R", "a");
		bubbleSort.setTransition("Wb", "c", "R", "b");

		// ML: "Mark last"
		bubbleSort.setTransition("R", TM.EMPTY, "ML", TM.LEFT);
		bubbleSort.setTransition("R", "a'", "ML", TM.LEFT);
		bubbleSort.setTransition("R", "b'", "ML", TM.LEFT);
		bubbleSort.setTransition("R", "c'", "ML", TM.LEFT);
		bubbleSort.setTransition("B", TM.EMPTY, "ML", TM.LEFT);
		bubbleSort.setTransition("B", "b'", "ML", TM.LEFT);
		bubbleSort.setTransition("B", "c'", "ML", TM.LEFT);
		bubbleSort.setTransition("C", TM.EMPTY, "ML", TM.LEFT);
		bubbleSort.setTransition("C", "c'", "ML", TM.LEFT);

		// RTS: "Return to start"
		bubbleSort.setTransition("ML", "a", "RTS", "a'");
		bubbleSort.setTransition("ML", "b", "RTS", "b'");
		bubbleSort.setTransition("ML", "c", "RTS", "c'");

		bubbleSort.setTransition("RTS", "a", "RTS", TM.LEFT);
		bubbleSort.setTransition("RTS", "a'", "RTS", TM.LEFT);
		bubbleSort.setTransition("RTS", "b", "RTS", TM.LEFT);
		bubbleSort.setTransition("RTS", "b'", "RTS", TM.LEFT);
		bubbleSort.setTransition("RTS", "c", "RTS", TM.LEFT);
		bubbleSort.setTransition("RTS", "c'", "RTS", TM.LEFT);

		bubbleSort.setTransition("RTS", TM.EMPTY, "R", TM.RIGHT);

		// F: "Finished" (clean up now
		bubbleSort.setTransition("R", "a'", "F", "a");
		bubbleSort.setTransition("R", "b'", "F", "b");
		bubbleSort.setTransition("R", "c'", "F", "c");

		bubbleSort.setTransition("F", "a'", "F", "a");
		bubbleSort.setTransition("F", "a", "F", TM.RIGHT);
		bubbleSort.setTransition("F", "b'", "F", "b");
		bubbleSort.setTransition("F", "b", "F", TM.RIGHT);
		bubbleSort.setTransition("F", "c'", "F", "c");
		bubbleSort.setTransition("F", "c", "F", TM.RIGHT);
	}

	private void setUpOneBubble() {
		oneBubble = new TM("R");
		oneBubble.setTransition("R", "a", "R", TM.RIGHT);
		oneBubble.setTransition("R", "b", "B", TM.RIGHT);
		oneBubble.setTransition("R", "c", "C", TM.RIGHT);

		oneBubble.setTransition("B", "c", "C", TM.RIGHT);
		oneBubble.setTransition("B", "b", "B", TM.RIGHT);

		oneBubble.setTransition("B", "a", "Ba", "b");
		oneBubble.setTransition("Ba", "b", "Wa", TM.LEFT);

		oneBubble.setTransition("C", "c", "C", TM.RIGHT);

		oneBubble.setTransition("C", "b", "Cb", "c");
		oneBubble.setTransition("Cb", "c", "Wb", TM.LEFT);

		oneBubble.setTransition("C", "a", "Ca", "c");
		oneBubble.setTransition("Ca", "c", "Wa", TM.LEFT);

		oneBubble.setTransition("Wa", "b", "R", "a");
		oneBubble.setTransition("Wa", "c", "R", "a");
		oneBubble.setTransition("Wb", "c", "R", "b");
		oneBubble.setTransition("", "", "", "");
	}

	private void setUpTm2() {
		tm2 = new TM("S");
		tm2.setTransition("S", "0", "S", TM.RIGHT);
		tm2.setTransition("S", "1", "S", TM.RIGHT);
		tm2.setTransition("S", TM.EMPTY, "F", "F");
		tm2.setTransition("F", "F", "R", TM.LEFT);
		tm2.setTransition("R", "0", "R'", "1");
		tm2.setTransition("R", "1", "R'", "0");
		tm2.setTransition("R'", "0", "R", TM.LEFT);
		tm2.setTransition("R'", "1", "R", TM.LEFT);
	}

	private void setUpSimpleTM() {
		simpleTM = new TM("S");

		simpleTM.setTransition("S", TM.EMPTY, "S", "S");
		simpleTM.setTransition("S", "S", "S0", TM.RIGHT);
		simpleTM.setTransition("S0", TM.EMPTY, "S0", "0");
		simpleTM.setTransition("S0", "0", "S1", TM.RIGHT);
		simpleTM.setTransition("S1", TM.EMPTY, "S1", "1");
	}

	private void checkTM(TM tm, String[] inputTape, String[] expected, boolean debug) {
		String[] result = tm.run(inputTape);
		if (debug)
			System.out.println(Arrays.toString(result));
		checkTapeEquality(expected, result);
	}

	private void checkTapeEquality(String[] expected, String[] result) {
		expected = stripEmpty(expected);
		result = stripEmpty(result);

		assertArrayEquals(expected, result);
	}

	private String[] stripEmpty(String[] expected) {
		LinkedList<String> expectedList = new LinkedList<>(Arrays.asList(expected));

		while (!expectedList.isEmpty() && expectedList.getFirst().equals(TM.EMPTY))
			expectedList.removeFirst();
		while (!expectedList.isEmpty() && expectedList.getLast().equals(TM.EMPTY))
			expectedList.removeLast();
		expected = expectedList.toArray(new String[0]);
		return expected;
	}
}