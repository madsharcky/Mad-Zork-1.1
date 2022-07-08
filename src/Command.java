/**
 * Class Command - Part of the "Zork" game.
 * 
 * This class holds information about a command that was issued by the user. A
 * command currently consists of two strings: a command word and a second word
 * (for example, if the command was "take map", then the two strings obviously
 * are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid command
 * words. If the user entered an invalid command (a word that is not known) then
 * the command word is <null>.
 * 
 * If the command had only one word, then the second word is <null>.
 * 
 * The second word is not checked at the moment. It can be anything. If this
 * game is extended to deal with items, then the second part of the command
 * should probably be changed to be an item rather than a String.
 * 
 *
 *
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

public class Command {
	
	private String commandWord;
	private String secondWord;

	/**
	 * Create a command object. First and second word must be supplied, but
	 * either one (or both) can be null. The command word should be null to
	 * indicate that this was a command that is not recognised by this game.
     * Usage - pCommand("firstWord", "secondWord");
     * @param firstWord - String
     * @param secondWord - String
     * @throws Exception
     */
	public Command(String firstWord, String secondWord) {
		this.commandWord = firstWord;
		this.secondWord = secondWord;
	}

	/**
	 * Return the command word (the first word) of this command. If the command
	 * was not understood, the result is null.
     * Usage - getCommandWord();
	 * @return - String
     * @throws Exception
     */
	public String getCommandWord() {
		return commandWord;
	}

	/**
	 * Return the second word of this command. Returns null if there was no
	 * second word.
     * Usage - getSecondWord();
	 * @return - String
     * @throws Exception
     */
	public String getSecondWord() {
		return secondWord;
	}

	/**
	 * Return true if this command was not understood.
     * Usage - isUnknown();
	 * @return - boolean
     * @throws Exception
     */
	public boolean isUnknown() {
		return (commandWord == null);
	}

	/**
	 * Return true if the command has a second word.
     * Usage - hasSecondWord();
	 * @return - boolean
     * @throws Exception
     */
	public boolean hasSecondWord() {
		return (secondWord != null);
	}
	/**
	 * Adjust the command object. First and second word must be supplied, but
	 * either one (or both) can be null. The command word should be null to
	 * indicate that this was a command that is not recognised by this game.
     * Usage - setCommand("firstWord", "secondWord");
     * @param firstWord - String
     * @param secondWord - String
     * @throws Exception
     */
	public void setCommand(String firstWord, String secondWord){
		this.commandWord = firstWord;
		this.secondWord = secondWord;
	}
}