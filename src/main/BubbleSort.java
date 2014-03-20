package main;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class demonstrates the bubblesort turing machine.
 * 
 * usage: run with command line arguments from the alphabet {a, b, c}:
 * 
 * -> java main/BubbleSort c b a c b a c b a 
 * 
 * @author Baste
 * 
 */
public class BubbleSort {

	public static void main(String[] args) {
		BubbleSort bs = new BubbleSort();
		bs.run(args);
	}

	public BubbleSort() {
		setUpBubbleSort();
	}

	public void run(String[] tape) {
		System.out.println(Arrays.toString(stripEmpty(bubbleSort.run(tape))));
	}

	private TM bubbleSort;

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

	/**
	 * Strips the empty starting and ending elements of the tape, for nice printout
	 * 
	 * [EMPTY, EMPTY, a, b, c, EMPTY] is turned into [a, b, c]
	 * 
	 * @param tape
	 *            a tape for the bubble sorting turing machine
	 * @return the same tape, with all empty spaces on the start and end of the tape removed
	 */
	private String[] stripEmpty(String[] tape) {
		LinkedList<String> expectedList = new LinkedList<>(Arrays.asList(tape));

		while (!expectedList.isEmpty() && expectedList.getFirst().equals(TM.EMPTY))
			expectedList.removeFirst();
		while (!expectedList.isEmpty() && expectedList.getLast().equals(TM.EMPTY))
			expectedList.removeLast();
		tape = expectedList.toArray(new String[0]);
		return tape;
	}

}
