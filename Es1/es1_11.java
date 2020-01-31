public class I {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == '/')
                        state = 1;
                    else if(c=='a' || c==' ')
                        state=0;
                    else
                        state=-1;
                    break;
                case 1:
                    if(c=='*')
                        state=2;
                    else if(c=='a')
                        state=0;
                    else
                        state=-1;
                    break;
                case 2:
                    if(c=='a' || c=='/')
                        state=4;
                    else if(c=='*')
                        state=3;
                    else
                        state=-1;
                    break;
                case 3:
                    if(c=='*')
                        state=3;
                    else if(c=='a')
                        state=4;
                    else if(c=='/')
                        state=5;
                    else
                        state=-1;
                    break;
                case 4:
                    if(c=='a' || c=='/')
                        state=4;
                    else if(c=='*')
                        state=3;
                    else
                        state=-1;
                    break;
                case 5:
                    if(c=='a' || c=='*')
                        state=5;
                    else if(c=='/')
                        state=0;

            }
        }
        return state == 5;
    }
}
