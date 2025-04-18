package lox;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int line;
    private int start, current;
    Scanner(String source){
        this.source = source;
    }
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }
    private void scanToken() {
        // scan and assign single character token
        char c = advance();
        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case '*': addToken(TokenType.STAR); break;
            // two character match to check
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL: TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL: TokenType.EQUAL); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL: TokenType.GREATER); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL: TokenType.LESS); break;
            case '/':
                // two cases 1. comment 2. division
                if(match('/')) {
                    // skip the entire line
                    while(!isAtEnd() && peek() != '\n') advance();
                } else{
                   addToken(TokenType.SLASH);
                }
                break;
                // ignore meaningless lexems
            case ' ':
            case '\t':
            case '\r':
                break;
            case '\n':
                line++;
                break;
            case '"':
                // store the literal and check for the end of the string
                string();
                break;
            default:
                if(isDigit(c)) {
                    number();
                } else {
                    Lox.error(line, "Unexpected Symbol: " + c);
                }
                break;
        }
    }
    private char advance() {
        return source.charAt(current++);
    }
    private boolean isAtEnd(){
        return current >= source.length();
    }
    private void addToken(TokenType type){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, null, line));
    }
    private void addToken(TokenType type, String text){
        tokens.add(new Token(type, text, null, line));
    }
    private boolean match(char c){
        if (!isAtEnd()){
            if(source.charAt(current) == c){
                current++;
                return true;
            }
        }
        return false;
    }
    private char peek(){
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }
    private char peekNext(){
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current+1);
    }
    private void string(){
        while(!isAtEnd() && peek() != '"'){
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }
        if(peek() == '"'){
            advance();
        }
        addToken(TokenType.STRING, source.substring(start+1, current-1));
    }
    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }
    private void number(){
        while(!isAtEnd() && isDigit(peek())){
            advance();
        }
        if(peek() == '.' && isDigit(peekNext())){
            advance();
            while(!isAtEnd() && isDigit(peek())) advance();
        }
        addToken(TokenType.NUMBER, source.substring(start, current));
    }
}
