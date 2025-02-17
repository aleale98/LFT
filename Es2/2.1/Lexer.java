import java.io.*;
import java.util.*;

public class Lexer {

    private List<String> identifiers = new ArrayList<String>() {
        {
            add("cond");
            add("when");
            add("then");
            add("else");
            add("while");
            add("do");
            add("seq");
            add("print");
            add("read");
        }
    };

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
	//assumo che un id valido sia generato dalla seguente regex: [a-zA-Z][a-zA-Z0-9]*
    private boolean isValidId(String s){
        int currIndex=0;
        boolean valid=false;
        if(Character.isLetter(s.charAt(currIndex))){
            currIndex++;
            valid=true;
            for(int i=currIndex; i<s.length() && valid; i++){
                valid=Character.isLetterOrDigit(s.charAt(i));
            }
        }
        return valid;
    }
	//assumo che una costante valida sia generata dalla seguente regex: 0|[1-9][0-9]*
    private boolean isValidConstant(String s){
        int currIndex=0;
        boolean valid=false;
        if(Character.isDigit(s.charAt(currIndex))){
            currIndex++;
            valid=true;
            for(int i=currIndex; i<s.length() && valid; i++){
                valid=Character.isDigit(s.charAt(i));
            }
        }
        return valid;
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
                peek = ' ';
                return Token.div;
            case ';':
                peek = ' ';
                return Token.semicolon;

            // ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //
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
                    while (Character.isDigit(peek)||Character.isLetter(peek)||peek=='_'){
                        id+=peek;
                        readch(br);
                    }
                    switch (id){
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
					//potevo usare anche id.matches("[a-zA-Z][a-zA-Z0-9]*");
                    if(isValidId(id)){
                        return new Word(Tag.ID, id);
                    }else{
                        System.err.println("Invalid keyword or identifier");
                        return null;
                    }
                } else if (Character.isDigit(peek)) {
                    String num = "";
                    do {
                        num += peek;
                        readch(br);
                    } while (Character.isDigit(peek));
					//potevo usare anche num.matches("0|[1-9][0-9]*") passando l'espressione regolare
                    if (isValidConstant(num)) {
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

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "./test.txt"; // il percorso del file da leggere
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

}