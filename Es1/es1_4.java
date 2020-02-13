public class es1_4 {
    /*
		Modificare l’automa dell’esercizio precedente in modo che riconosca le combinazioni di matricola e cognome di studenti del turno 2 o del turno 3 del laboratorio,
		 dove il numero di matricola e il cognome possono essere separati da una sequenza di spazi, e possono essere precedute e/o seguite da sequenze eventualmente vuote di spazi.
		 Per esempio, l’automa deve accettare la stringa “654321 Rossi” e “ 123456 Bianchi ” (dove, nel secondo esempio, ci sono spazi prima del primo carattere e dopo l’ultimo carattere), 
		ma non “1234 56Bianchi” e “123456Bia nchi”. L'automa deve accettare anche i cognomi composti.
	*/
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            char upperCh;

            switch (state) {
                case 0:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0)
                        state = 2;
                    else if(Character.isWhitespace(ch))
                        state=0;
                    else
                        state = -1;
                    break;

                case 1:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0)
                        state = 2;
                    else if(ch >= 'A' && ch <= 'K' || ch>='a' && ch<='k')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 3;
                    else
                        state = -1;
                    break;

                case 2:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0)
                        state = 1;
                    else if(ch >= 'L' && ch <= 'Z' || ch>='l' && ch<='z')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 4;
                    else
                        state = -1;
                    break;

                case 3:
                    if(ch >= 'A' && ch <= 'K' || ch>='a' && ch<='k')
                        state = 5;
                    else if(ch==' ')
                        state=3;
                    else
                        state = -1;
                    break;

                case 4:
                    if(ch >= 'L' && ch <= 'Z' || ch>='l' && ch<='z')
                        state = 5;
                    else if(ch==' ')
                        state=4;
                    else
                        state = -1;
                    break;

                case 5:
                    if(ch >= 'a' && ch <= 'z')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 6;
                    else
                        state = -1;
                    break;

                case 6:
                    if(ch >= 'A' && ch <= 'Z' || ch>='a' && ch<='z')
                        state = 5;
                    else if(ch==' ')
                        state=6;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 5 || state == 6;
    }
	
	public static void main(String[] args){
		System.out.println(es1_4.scan("123456 Bianchi"));
		System.out.println(es1_4.scan("654321 Rossi"));
		System.out.println(es1_4.scan("1234 56Bianchi"));
		System.out.println(es1_4.scan("  123456 Bianchi  "));
		System.out.println(es1_4.scan("123456De Gasperi"));
	}
}
