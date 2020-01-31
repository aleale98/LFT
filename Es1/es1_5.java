public class es1_5 {
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
                    if ((c >= 'a' && c <= 'z')) {
                        state = 1;
                    } else if (Character.isDigit(c)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 != 0) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 3;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 != 0) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if ((c >= 'a' && c <= 'z')) {
                        state = 4;
                    } else if (Character.isDigit(c)) {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 != 0) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 6:
                    if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 == 0) {
                        state = 5;
                    } else if (Character.isDigit(c) && Integer.parseInt(String.valueOf(c)) % 2 != 0) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 3 || state == 6;
    }
}
