public class es1_5 {

    /*
        Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio di stringhe che contengono matricola e cognome di studenti
         del turno 2 o del turno 3 del laboratorio, ma in cui il cognome precede il numero di matricola
         (in altre parole, le posizioni del cognome e matricola sono scambiate rispetto all’Esercizio 1.3). Assicurarsi che il DFA sia minimo.
    */
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if ((c >= 'a' && c <= 'k') || (c >= 'A' && c <= 'K')) {
                        state = 1;
                    } else if ((c >= 'l' && c <= 'z') || (c >= 'L' && c <= 'Z')) {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if (Character.isLetter(c)) {
                        state = 1;
                    } else if (Character.isDigit(c)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 != 0) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if (Character.isLetter(c)) {
                        state = 4;
                    } else if (Character.isDigit(c)) {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 6:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 6;
                    } else {
                        state = -1;
                    }
            }
        }
        return state == 3 || state == 6;
    }

    public static void main(String[] args) {
        System.out.println(es1_5.scan("bianchi123456"));
        System.out.println(es1_5.scan("rossi654321"));
        System.out.println(es1_5.scan("bianchi12345"));
        System.out.println(es1_5.scan("rossi65432"));
    }
}
