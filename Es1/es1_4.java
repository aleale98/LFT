public class es1_4 {
    //1.4
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            char upperCh;

            switch (state) {
                case 0:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0)
                        state = 2;
                    else if(Character.isWhitespace(ch))
                        state=0;
                    else
                        state = -1;
                    break;

                case 1:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 != 0)
                        state = 2;
                    else if(ch >= 'A' && ch <= 'K' || ch>='a' && ch<='k')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 3;
                    else if((ch >= 'L' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
                        state = -1;
                    break;

                case 2:
                    if(Character.isDigit(ch) && Character.getNumericValue(ch) % 2 == 0)
                        state = 1;
                    else if(ch >= 'L' && ch <= 'Z' || ch>='l' && ch<='z')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 4;
                    else if((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'z'))
                        state = -1;
                    break;

                case 3:
                    if(ch >= 'A' && ch <= 'K' || ch>='a' && ch<='k')
                        state = 5;
                    else if((ch >= 'L' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || Character.isDigit(ch))
                        state = -1;
                    break;

                case 4:
                    if(ch >= 'L' && ch <= 'Z' || ch>='l' && ch<='z')
                        state = 5;
                    else if((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'z') || Character.isDigit(ch))
                        state = -1;
                    break;

                case 5:
                    if(ch >= 'a' && ch <= 'z')
                        state = 5;
                    else if(Character.isWhitespace(ch))
                        state = 6;
                    else if(((ch >= 'A' && ch <= 'Z') || (ch>='a' && ch<='z')) || Character.isDigit(ch))
                        state = -1;
                    break;

                case 6:
                    if(ch >= 'A' && ch <= 'Z' || ch>='a' && ch<='z')
                        state = 5;
                    else if(Character.isDigit(ch))
                        state = -1;
                    break;
            }
        }

        return state == 5 || state == 6;
    }
}
