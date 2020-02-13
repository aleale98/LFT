
public class es1_9 {
	
	/*
		Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono il tuo nome 
		e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome con un altro qualsiasi. 
		Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve accettare la stringa “Paolo” 
		(cioe` il nome scritto correttamente), ma anche le stringhe “Pjolo”, “caolo”, “Pa%lo”, “Paola” e “Parlo” 
		(il nome dopo la sostituzione di un carattere), ma non “Eva”, “Perro”, “Pietro” oppure “P*o*o”.
	*/
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()){
            final char c=s.charAt(i++);
            switch (state){
                case 0:
                    if(c!='a' && c!=' '){
                        state=1;
                    }else if(c=='a'){
                        state=8;
                    }else{
                        state=-1;
                    }
                    break;
                case 1:
                    if(c=='l')
                        state=2;
                    else
                        state=-1;
                    break;
                case 2:
                    if(c=='e')
                        state=3;
                    else
                        state=-1;
                    break;
                case 3:
                    if(c=='s')
                        state=4;
                    else
                        state=-1;
                    break;
                case 4:
                    if(c=='s')
                        state=5;
                    else
                        state=-1;
                    break;
                case 5:
                    if(c=='i')
                        state=6;
                    else
                        state=-1;
                    break;
                case 6:
                    if(c=='o')
                        state=7;
                    else
                        state=-1;
                    break;
                case 7:
                    if(c==' ')
                        state=7;
                    else
                        state=-1;
                    break;
                case 8:
                    if(c!='l' && c!=' ')
                        state=2;
                    else if(c=='l')
                        state=9;
                    else
                        state=-1;
                    break;
                case 9:
                    if(c!='e' && c!=' ')
                        state=3;
                    else if(c=='e')
                        state=10;
                    else
                        state=-1;
                    break;
                case 10:
                    if(c!='s' && c!=' ')
                        state=4;
                    else if(c=='s')
                        state=11;
                    else
                        state=-1;
                    break;
                case 11:
                    if(c!='s' && c!=' ')
                        state=5;
                    else if(c=='s')
                        state=12;
                    else
                        state=-1;
                    break;
                case 12:
                    if(c!='i' && c!=' ')
                        state=6;
                    else if(c=='i')
                        state=13;
                    else
                        state=-1;
                    break;
                case 13:
                    if(c!=' ')
                        state=7;
                    else
                        state=-1;
                    break;
            }
        }
        return state==7;
    }
	
	public static void main(String[] args){
		System.out.println(es1_9.scan("alessio"));
		System.out.println(es1_9.scan("blessio"));
		System.out.println(es1_9.scan("akessio"));
		System.out.println(es1_9.scan("al*ssio"));
		System.out.println(es1_9.scan("ale!sio"));
		System.out.println(es1_9.scan("ales=io"));
		System.out.println(es1_9.scan("aless,o"));
		System.out.println(es1_9.scan("alessi)"));
		System.out.println(es1_9.scan("eva"));
		System.out.println(es1_9.scan("andrea"));
		
	}
}
