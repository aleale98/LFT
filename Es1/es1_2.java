public class B {
    //Es 1.2
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
}
