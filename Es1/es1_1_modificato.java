public class es1_1_modificato {
	/*
		Come deve essere modificato il DFA in Figure 1 per riconoscere il linguaggio complementare,
		ovvero il linguaggio delle stringhe di 0 e 1 che non contengono 3 zeri consecutivi? 
		Progettare e implementare il DFA modificato, e testare il suo funzionamento.
	*/
    public static boolean scan(String s){

        int state=0;
        int i=0;

        while(state>=0 && i<s.length()){
            final char ch=s.charAt(i++);
            switch (state){
                case 0:
                    if(ch=='0'){
                        state=1;
                    }else if(ch=='1'){
                        state=0;
                    }else{
                        state=-1;
                    }
                    break;
                case 1:
                    if(ch=='0'){
                        state=2;
                    }else if(ch=='1'){
                        state=0;
                    }else{
                        state=-1;
                    }
                    break;
                case 2:
                    if(ch=='0'){
                        state=3;
                    }else if(ch=='1'){
                        state=0;
                    }else{
                        state=-1;
                    }
                    break;
                case 3:
                    if(ch=='0' || ch=='1')
                        state=3;
                    else
                        state=-1;
                    break;
            }
        }
        return state == 0 || state==1 || state==2;
    }
	
	public static void main(String[] args){
		System.out.println(es1_1_modificato.scan("000"));
		System.out.println(es1_1_modificato.scan("001011100111"));
		System.out.println(es1_1_modificato.scan("010111"));
		System.out.println(es1_1_modificato.scan("1"));
		System.out.println(es1_1_modificato.scan("10111011001110"));
		System.out.println(es1_1_modificato.scan("111111"));
		System.out.println(es1_1_modificato.scan("0"));
		System.out.println(es1_1_modificato.scan("1"));
		System.out.println(es1_1_modificato.scan("01001"));
		System.out.println(es1_1_modificato.scan("01001111001111000"));
	}
}