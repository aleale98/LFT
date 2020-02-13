
public class es1_7 {
	/*
		Progettare e implementare un DFA con alfabeto {a, b} che riconosca il linguaggio
		delle stringhe tali che a occorre almeno una volta in una delle prime tre posizioni della stringa.
		Il DFA deve accettare anche stringhe che contengono meno di tre simboli (ma almeno uno dei simboli deve essere a).
		Esempi di stringhe accettate: “abb”, “abbbbbb”, “bbaba”, “baaaaaaa”, “aaaaaaa”, “a”, “ba”, “bba”, “aa”, “bbabbbbbbbb”
		Esempi di stringhe non accettate: “bbbababab”, “b”
	*/
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == 'a')
                        state = 3;
                    else if (c == 'b')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 1:
                    if(c=='a')
                        state=3;
                    else if(c=='b')
                        state=2;
                    else
                        state=-1;
                    break;
                case 2:
                    if(c=='a')
                        state=3;
                    else
                        state=-1;
                    break;
                case 3:
                    if(c=='a' || c=='b'){
                        state=3;
                    }else{
                        state=-1;
                    }
            }
        }
        return state==3;
    }
	
	public static void main(String[] args){
		System.out.println(es1_7.scan("abb"));
		System.out.println(es1_7.scan("abbbbbb"));
		System.out.println(es1_7.scan("bbaba"));
		System.out.println(es1_7.scan("baaaaaaa"));
		System.out.println(es1_7.scan("aaaaaaa"));
		System.out.println(es1_7.scan("a"));
		System.out.println(es1_7.scan("ba"));
		System.out.println(es1_7.scan("bba"));
		System.out.println(es1_7.scan("aa"));
		System.out.println(es1_7.scan("bbabbbbbbbb"));
		System.out.println(es1_7.scan("bbbababab"));
		System.out.println(es1_7.scan("bbbbbbaaaaaa"));
		
	}
}
