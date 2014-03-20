/*
 * WARRANTIES AND COPYRIGHTS ARE FOR SOULLESS PEOPLE
 * 
 * WTFPL IS PRETTY GREAT, THOUGH
 * 
 * http://www.wtfpl.net/
 */
package main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Implementation of a turing machine.
 * 
 * It uses Strings as both state names and symbols. The Strings "RIGHT", "LEFT" AND "EMPTY" are reserved.
 * 
 * This model doesn't have explicit accepting halting states. If the machine halts, it accepts a string. This isn't
 * really a full Turing machine, but as this implementation is used to check for correct tape states rather than, say
 * input acceptance, this is sufficient.
 * 
 * This model is also of the type that either prints a symbol OR moves. This model is just as strong as one that can do
 * both at the same time.
 * 
 * @author Baste
 * 
 */
public class TM {

	public final static String LEFT = "LEFT";
	public final static String RIGHT = "RIGHT";
	public final static String EMPTY = "EMPTY";

	/**
	 * AKA. "Delta" in the formal models
	 */
	private HashMap<StringSymbolPair, StringSymbolPair> stateTransistions;

	/**
	 * AKA. "s" in the formal models
	 */
	private String startState;

	/**
	 * Build delta (state transition functions) through setTransition. Run on a tape through run.
	 * 
	 * @param startState
	 *            starting state of the turing machine.
	 */
	public TM(String startState) {
		super();
		this.stateTransistions = new HashMap<>();
		this.startState = startState;
	}

	/**
	 * Defines one of the transitions in the transition function delta.
	 * 
	 * If the output is LEFT or RIGHT, this means that the tape head should move, rather than printing a symbol.
	 * 
	 * @param state
	 *            input state
	 * @param input
	 *            input symbol
	 * @param newState
	 *            output state
	 * @param writeSymbol
	 *            output symbol
	 */
	public void setTransition(String state, String input, String newState, String writeSymbol) {
		StringSymbolPair from = new StringSymbolPair(state, input);
		StringSymbolPair to = new StringSymbolPair(newState, writeSymbol);

		stateTransistions.put(from, to);
	}

	/**
	 * Runs the machine against a tape
	 * 
	 * @param tape
	 *            the tape to run against
	 * @return the state of the tape after the machine has halted.
	 */
	public String[] run(String[] tape) {
		LinkedList<String> symbols = new LinkedList<>();
		for (String symbol : tape) {
			symbols.add(symbol);
		}
		String currentState = startState;
		int position = 0;

		while (true) {
			// FIX OUTSIDE BOUNDS
			if (position < 0) {
				symbols.addFirst(EMPTY);
				position++;
				continue;
			}
			if (position == symbols.size()) {
				symbols.addLast(EMPTY);
				continue;
			}

			// READ String TRANSITION
			StringSymbolPair input = new StringSymbolPair(currentState, symbols.get(position));
			StringSymbolPair instruction = stateTransistions.get(input);

			// if we don't have a matching instruction, halt
			if (instruction == null) {
				// Uncomment for debugging!
				// System.out.println("tape is:\n" + symbols);
				// System.out.println("breaking at: " + input);
				// System.out.println("at index: " + position);
				break;
			}

			// DO String TRANSITION
			currentState = instruction.state;
			String symbol = instruction.symbol;
			if (symbol == RIGHT)
				position++;
			else if (symbol == LEFT)
				position--;
			else
				symbols.set(position, symbol);

		}

		return symbols.toArray(new String[0]);
	}

	/**
	 * Wrapper for a state/symbol pair.
	 * 
	 * Boilerplate all the way. Thanks to THE ECLIPSE FOUNDATION for autogenerating stuff.
	 * 
	 * @author Baste
	 * 
	 */
	private class StringSymbolPair {

		public String state;
		public String symbol;

		public StringSymbolPair(String state, String symbol) {
			super();
			this.state = state;
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return "(" + state + " / " + symbol + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
			return result;
		}

		// OMG LOOK AT THIS OVERKILL EQUALS METHOD, IT IS SO GREAT.
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StringSymbolPair other = (StringSymbolPair) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			if (symbol == null) {
				if (other.symbol != null)
					return false;
			} else if (!symbol.equals(other.symbol))
				return false;
			return true;
		}

		private TM getOuterType() {
			return TM.this;
		}

	}

}
