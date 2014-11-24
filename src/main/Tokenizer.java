package main;

public class Tokenizer {
	public String[] tokenizer(String input) {
		return input.replaceAll("(?!\")\\p{Punct}", "").toLowerCase().split("\\s+");
	}
	
	public static void main(String[] args) {
		Tokenizer token = new Tokenizer();
		String str = "Hello, everyone. Do you like the new layout?";
		String[] tokens = token.tokenizer(str);
		for (int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
		}
		
	}
}
