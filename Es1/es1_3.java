public class C {
    //1.3
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
                    if(!(Character.isLetter(c))){
                        state=-1;
                    }else{
                    	state=3;
                    }
            }
        }
        return state==3;
    }
}
