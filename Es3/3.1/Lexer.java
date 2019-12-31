import java.io.*;

public class Lexer {
    private static int line = 1;
    private char peek = ' ';

    public static int getLine() {
        return line;
    }

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) {
            // ... gestire i casi di (, ), {, }, +, -, *, /, ; ... //
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br);
                /*
                 * check comment on one line. Ignore every character after // and until a new line
                 * Then recursively call the method to carry on the analysis.
                 * */
                if (peek == '/') {
                    do {
                        readch(br);
                    } while (peek != '\n' && peek != Tag.EOF);
                    return lexical_scan(br);
                } else if (peek == '*') {
                    /*
                    * Read the /* sequence. Then read every character until the closure sequence is read.
                    The logic is similar to the DFA.
                     */
                    int commentEnded = 0;
                    do {
                        readch(br);
                        if ((peek == '*' && commentEnded == 0) || (peek == '/' && commentEnded == 1))
                            commentEnded++;
                        else if (commentEnded > 0)
                            commentEnded--;
                    } while (commentEnded != 2 && peek != (char) Tag.EOF);
                    peek = ' ';
                    //If the comment is correctly closed, then call again the method to continue scanning the code
                    if (commentEnded == 2) {
                        return lexical_scan(br);
                    }
                    //The comment hasn't been correctly closed
                    else {
                        System.err.println("Multi-line comment uncorrectly closed");
                        return null;
                    }
                } else {
                    return Token.div;
                }
            case ';':
                peek = ' ';
                return Token.semicolon;
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character " +
                            " after |: " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    //Non è necessario svuotare peek perchè dopo c'è qualcosa da analizzare
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Word.assign;
                }
            case (char) -1:
                return new Token(Tag.EOF);
            default:
                if (Character.isLetter(peek)) {
                    String id = "";
                    boolean b = true;
                    while (Character.isDigit(peek) || Character.isLetter(peek) || peek == '_') {
                        id += peek;
                        readch(br);
                    }
                    id = id.trim();
                    switch (id) {
                        case "cond":
                            return Word.cond;
                        case "when":
                            return Word.when;
                        case "then":
                            return Word.then;
                        case "else":
                            return Word.elsetok;
                        case "while":
                            return Word.whiletok;
                        case "do":
                            return Word.dotok;
                        case "seq":
                            return Word.seq;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                    }
                    if (id.matches("([a-zA-Z]|(_(_)*[a-zA-Z0-9]))([a-zA-z0-9|_])*")) {
                        return new Word(Tag.ID, id);
                    } else {
                        System.err.println("Invalid keyword or identifier");
                        return null;
                    }
                } else if (Character.isDigit(peek)) {
                    String num = "";
                    do {
                        num += peek;
                        readch(br);
                    } while (Character.isDigit(peek));
                    if (num.matches("0|[1-9][0-9]*")) {
                        return new NumberTok(num);
                    } else {
                        System.err.println("Not valid number: " + num);
                        return null;
                    }
                } else {
                    System.err.println("Erroneous character: "
                            + peek);
                    return null;
                }
        }
    }
/*
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/alessio/Desktop/test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}