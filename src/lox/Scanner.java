package lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int line;
    private int start, current;
    private static final HashMap<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    TokenType.AND);
        keywords.put("class",  TokenType.CLASS);
        keywords.put("else",   TokenType.ELSE);
        keywords.put("false",  TokenType.FALSE);
        keywords.put("for",    TokenType.FOR);
        keywords.put("fun",    TokenType.FUN);
        keywords.put("if",     TokenType.IF);
        keywords.put("nil",    TokenType.NIL);
        keywords.put("or",     TokenType.OR);
        keywords.put("print",  TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super",  TokenType.SUPER);
        keywords.put("this",   TokenType.THIS);
        keywords.put("true",   TokenType.TRUE);
        keywords.put("var",    TokenType.VAR);
        keywords.put("while",  TokenType.WHILE);
    }
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
                } else if(match('*')){
                    // multi line comment
                    System.out.println("Multiline comment");
                    while(!isAtEnd()){
                        if(peek() == '\n') line++;
                        if(match('*') && match('/')) break;
                        advance();
                    }
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
                } else if(isAlpha(c)) {
                    identifier();
                } else{
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
    private void identifier(){
        while(!isAtEnd() && peek() != ' ') advance();
        String text = source.substring(start, current);
        if(keywords.containsKey(text)) {
            addToken(keywords.get(text));
        } else{
            addToken(TokenType.IDENTIFIER, text);
        }
    }
    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }
    private boolean isAlpha(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
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
