public class es1_3 {
    /*
		Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono un numero di matricola seguito (subito) da un cognome,
		dove la combinazione di matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del laboratorio di Linguaggi Formali e Traduttori. 
		Si ricorda le regole per suddivisione di studenti in turni:
			• Turno T1: cognomi la cui iniziale e` compresa tra A e K, e il numero di matricola e` dispari; 
			• Turno T2: cognomi la cui iniziale e` compresa tra A e K, e il numero di matricola e` pari;
			• Turno T3: cognomi la cui iniziale e` compresa tra L e Z, e il numero di matricola e` dispari; 
			• Turno T4: cognomi la cui iniziale e` compresa tra L e Z, e il numero di matricola e` pari.
	*/
    public static boolean scan(String s){
        int state=0;
        int i=0;
        while(state>=0 && i<s.length()){
            final char c=s.charAt(i++);

            switch(state){
                case 0:
                    if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==0){
                        state=1;
                    }else if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==1){
                        state=2;
                    }else{
                        state=-1;
                    }
                    break;
                case 1:
					if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==0){
						state=1;
					} else if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==1){
                        state=2;
                    }else if(((c>='a'&&c<='k')||(c>='A'&&c<='K'))){
                        state=3;
                    }else{
                        state=-1;
                    }
                    break;
                case 2:
					if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==1){
						state=2;
					}else if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==0){
                        state=1;
                    }else if((c>='l' && c<='z')||(c>='L' && c<='Z')){
                        state=3;
                    }else{
                        state=-1;
                    }
                    break;
                case 3:
                    if((Character.isLetter(c))){
                        state=3;
                    }else{
                    	state=-1;
                    }
            }
        }
        return state==3;
    }
	
	public static void main(String[] args){
		System.out.println(es1_3.scan("123456Bianchi"));
		System.out.println(es1_3.scan("654321Rossi"));
		System.out.println(es1_3.scan("654321Bianchi"));
		System.out.println(es1_3.scan("123456Rossi"));
		System.out.println(es1_3.scan("2Bianchi"));
		System.out.println(es1_3.scan("122B"));
		System.out.println(es1_3.scan("654322"));
		System.out.println(es1_3.scan("Rossi"));
	}
}
