public class es1_6 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if(c=='0')
                        state=0;
                    else if(c=='1')
                        state=1;
                    else
                        state=-1;
                    break;
                case 1:
                    if(c=='0')
                        state=2;
                    else if(c=='1')
                        state=0;
                    else
                        state=-1;
                    break;
                case 2:
                    if(c=='0')
                        state=1;
                    else if(c=='1')
                        state=2;
                    else
                        state=-1;
                    break;
            }
        }
        return state==0;
    }
}
