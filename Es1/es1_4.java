public class C {
    //1.3-4
    public static boolean scan(String s){
        int state=0;
        int i=0;
        while(state>=0 && i<s.length()){
            final char c=s.charAt(i++);
            switch(state){
                case 0:
                    if(c==' '){
                        state=0;
                    }else if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==0){
                        state=1;
                    }else if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==1){
                        state=2;
                    }else{
                        state=-1;
                    }
                    break;
                case 1:
                    if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==1){
                        state=2;
                    }else if((c>='a'&&c<='k')||c==' '){
                        state=3;
                    }else{
                        state=-1;
                    }
                    break;
                case 2:
                    if(Character.isDigit(c) && Integer.parseInt(String.valueOf(c))%2==0){
                        state=1;
                    }else if((c>='l' && c<='z')||c==' '){
                        state=3;
                    }else{
                        state=-1;
                    }
                    break;
                case 3:
                    if(Character.isDigit(c)){
                        state=-1;
                    }
            }
        }
        return state==3;
    }
}
