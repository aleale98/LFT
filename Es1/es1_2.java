public class es1_2 {
    /*
		Progettare e implementare un DFA che riconosca il linguaggio degli identificatori in un linguaggio in stile Java: 
		un identificatore e` una sequenza non vuota di lettere, numeri, ed il simbolo di “underscore” _ 
		che non comincia con un numero e che non puo` essere composto solo dal simbolo _. 
		Compilare e testare il suo funzionamento su un insieme significativo di esempi.
	*/
    public static boolean scan(String s){
        int state=0;
        int i=0;
        while(state>=0 && i<s.length()){
            final char c=s.charAt(i++);
            switch(state){
                case 0:
                    if((c>='a'&& c<='z')){
                        state=1;
                    }else if(c=='_') {
                        state=2;
                    }else{
                        state=-1;
                    }
                    break;
                case 1:
                    if((c>='a'&& c<='z')||(c>='0'&&c<'9')||c=='_'){
                        state=1;
                    }else{
                        state=-1;
                    }
                    break;
                case 2:
                    if((c>='a'&& c<='z')||(c>='0'&&c<'9')){
                        state=1;
                    }else if(c=='_'){
                        state=2;
                    }else{
                        state=-1;
                    }
            }
        }
        return state==1;
    }
	
	public static void main(String[] args){
		System.out.println(es1_2.scan("x"));
		System.out.println(es1_2.scan("flag1"));
		System.out.println(es1_2.scan("x2y2"));
		System.out.println(es1_2.scan("x_1"));
		System.out.println(es1_2.scan("lft_lab"));
		System.out.println(es1_2.scan("_temp"));
		System.out.println(es1_2.scan("x_1_y_2"));
		System.out.println(es1_2.scan("x___"));
		System.out.println(es1_2.scan("__5"));
		System.out.println(es1_2.scan("5"));
		System.out.println(es1_2.scan("221B"));
		System.out.println(es1_2.scan("123"));
		System.out.println(es1_2.scan("9_to_5"));
		System.out.println(es1_2.scan("___"));
		
		
	}
}
