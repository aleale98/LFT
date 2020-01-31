public class es1_8 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == 'a')
                        state = 1;
                    else if (c == 'b')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 1:
                    if (c == 'a')
                        state = 1;
                    else if (c == 'b')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if (c == 'a')
                        state = 1;
                    else if (c == 'b')
                        state = 3;
                    else
                        state = -1;
					break;
                case 3:
                    if (c == 'b')
                        state = 0;
                    else if (c == 'a')
                        state = 3;
                    else
                        state = -1;
					break;

            }

        }
        return state == 1 || state == 3;
    }
}
