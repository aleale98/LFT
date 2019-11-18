//1.5
public class D {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == ' ') {
                        state = 0;
                    } else if (c >= 'a' && c <= 'k') {
                        state = 1;
                    } else if (c >= 'l' && c <= 'z') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if ((c >= 'a' && c <= 'z') || c == ' ') {
                        state = 1;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if ((c >= 'a' && c <= 'z') || c == ' ') {
                        state = 2;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 4;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 3, 5:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 4, 6:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 4;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 1) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;

            }


        }
        return state == 5 || state == 6;
    }
}
