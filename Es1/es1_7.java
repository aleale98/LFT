//1.7
public class F {
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
}
