import java.io.*;

public class Lexer {
    private static int line = 1;
    private char peek = ' ';


    public static int getLine(){
        return line;
    }
	
	/*
		Metodo per controllare la validità delle costanti.
		Se la stringa inizia con zero setto valid a true e non eseguo il ramo
		dell'else if. Se la stringa non inizia con zero, controllo che inizi con un numero
		e se inizia con un numero controllo che tutti gli altri caratteri siano solo
		numeri. Non contemplo le costanti con virgola.
	*/
    private boolean isValidConstant(String s){
        int currIndex=0;
        boolean valid=false;
		if(s.charAt(currIndex)=='0'){
			valid=true;
		}else if(Character.isDigit(s.charAt(currIndex))){
            currIndex++;
            valid=true;
            for(int i=currIndex; i<s.length() && valid; i++){
                valid=Character.isDigit(s.charAt(i));
            }
        }
        return valid;
    }

	/*
		Controllo che la stringa che presumo essere un ID inizi o con una lettera o con un _
		Se comincia con una lettera o con un _ , allora controllo che tutti i caratteri seguenti
		siano o lettere o numeri o _ 
	*/
    private boolean isValidId(String s){
        boolean valid=false;
        int currentIndex=0;
        if(Character.isLetter(s.charAt(currentIndex)) || s.charAt(currentIndex)=='_'){
            currentIndex++;
            valid = true;
            for(int i=0; i<s.length() && valid; i++){
                valid=Character.isLetterOrDigit(s.charAt(i)) || s.charAt(i)=='_';
            }
        }
        return valid;
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
                 * Controlla il commento su una singola linea. Ignora ogni carattere dopo //
                 * Dopo chiama ricorsivamente il metodo lexical_scan per continuare
					l'analisi.
                 * */
                if (peek == '/') {
                    do {
                        readch(br);
                    } while (peek != '\n' && peek != Tag.EOF && peek!='\uFFFF');
                    return lexical_scan(br);
                } else if (peek == '*') {
                    /*
                    * Legge la sequenza /*. Dopo legge ogni carattere sino a quando non trova la sequenza
						di chiusura. Non uso la tecnica che ho usato per implementare il dfa perchè questa
						mi permette di gestire in modo più semplice il "leggo qualsiasi cosa"
						Ho riconosciuto la sottosequenza / * 
						Se riconosco un * incremento commentEnded portandolo a 1
						Se poi riconosco / incremento ancora commentEnded terminando il ciclo
						Teoricamente potrei riconoscere anche / all'interno del e in quel caso non faccio nulla
						se riconosco un * ma dopo non riconosco lo /, decremento commentEnded perchè sicuramente non 
						ho finito di leggere il commento.
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
                    //Il commento è stato chiuso correttamente. Chiama ricorsivamente il metodo lexical_scan
					//per continuare ad analizzare il sorgente.
                    if (commentEnded == 2) {
                        return lexical_scan(br);
                    }
                    //Il commento non è stato chiuso correttamente. Restituisco null.
                    else {
                        System.err.println("Commento multi-linea non chiuso correttamente.");
                        return null;
                    }
                } else {
                    return Token.div;
                }
            case ';':
                peek = ' ';
                return Token.semicolon;
				/*
					Se la sintassi dell && è errata, segnalo un errore e restituisco null.
				*/
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character " +
                            " after &: " + peek);
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
			/*
				Se non ho usato alcun caso prima, mi rimane da verificare o il caso dell'ID/parola chiave 
				o il caso della costante numerica.
				Se peek è una lettera allora presumibilmente sto gestendo o una parola chiave o un identificatore.
				Per cui, fino a quando leggo _, lettere o numeri leggo la stringa. Dopodichè la valido con 
				il metodo apposito.
			*/
                if (Character.isLetter(peek) || peek == '_') {
                    String id = "";
                    boolean b = true;
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
					//Se ho un ID valido, restituisco una nuova parola 
                    if (isValidId(id)) {
                        return new Word(Tag.ID, id);
                    } else {
                        System.err.println("Invalid keyword or identifier");
                        return null;
                    }
					/*
						Se non ho letto una lettera, allora ho letto un numero. 
						Leggo quindi tutti i numeri successivi.
						Dopodichè, con il metodo apposta valido la costante e restituisco un nuovo 
						NumberTok valorizzato con il numero.
					*/
                } else if (Character.isDigit(peek)) {
                    String num = "";
                    do {
                        num += peek;
                        readch(br);
                    } while (Character.isDigit(peek));
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

}