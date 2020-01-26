import java.io.*;

public class Lexer {
    private static int line = 1;
    private char peek = ' ';

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
            // gestisco i casi di (, ), {, }, +, -, *, /, ; 
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
                 * Controlla il commento su una singola linea. Ignora ogni carattere dopo // fino alla nuova riga 
				 *	Poi chiama ricorsivamente il metodo per continuare l'analisi.
                 * */
                if (peek == '/') {
                    do {
                        readch(br);
                    } while (peek != '\n' && peek != Tag.EOF);
                    return lexical_scan(br);
                } else if (peek == '*') {
                    /*
                    * Legge la sequenza /*. Poi legge tutti i caratteri presenti sino alla sequenza di chiusura
                    *La logica utilizzata è simile a quella di un DFA
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
                    //Se il commento è stato chiuso correttamente, richiama ricorsivamente il metodo per continuare ad analizzare il codice
                    if (commentEnded == 2) {
                        return lexical_scan(br);
                    }
                    //Il commento non è stato chiuso correttamente
                    else {
                        System.err.println("Commento multilinea non chiuso correttamente");
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
					//Non svuoto peek per le stesse motivazioni di sopra
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
					//Non svuoto peek per le stesse motivazioni di sopra
                    return Word.assign;
                }
            case (char) -1:
			//restituisco il tag relativo alla fine del file
                return new Token(Tag.EOF);
            default:
			//gestisco il caso degli identificatori
                if (Character.isLetter(peek)) {
                    String id = "";
                    boolean b = true;
					//leggo qualsiasi cosa fino al primo spazio che incontro
                    while (Character.isDigit(peek) || Character.isLetter(peek) || peek == '_') {
                        id += peek;
                        readch(br);
                    }
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
					//Se non è una parola chiave, guardo che sia generato da questa espressione regolare
                    if (id.matches("([a-zA-Z]|(_(_)*[a-zA-Z0-9]))([a-zA-z0-9|_])*")) {
                        return new Word(Tag.ID, id);
                    } else {
                        System.err.println("Invalid keyword or identifier");
                        return null;
                    }
                } else if (Character.isDigit(peek)) {
					//Caso delle costanti numeriche
                    String num = "";
                    do {
                        num += peek;
                        readch(br);
                    } while (Character.isDigit(peek));
					//Se la stringa è composta o da un solo zero o da un numero arbitrario di cifre e NON comincia per 0, restituisco
					//il token relativo al numero
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

    public static int getLine() {
        return line;
    }
}