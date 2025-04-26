package lox;

public class Token {
    final TokenType type;
    final String lexeme;
    final int line;
    final String literal;
    Token(TokenType type, String lexeme, String literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.literal = literal;
    }
    public String toString() {
        return "Type: "+ type + " Lexeme: " + lexeme + " Literal: " + literal;
    }
}
