public class es1_10 {
    /*
		Progettare e implementare un DFA con alfabeto {/, *, a} che riconosca il linguaggio di “commenti”  
	*/
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == '/') {
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if (c == '*') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (c == 'a' || c=='/') {
                        state = 4;
                    } else if (c == '*') {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (c == '*') {
                        state = 3;
                    } else if (c == 'a') {
                        state = 4;
                    } else if (c == '/') {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if (c == 'a' || c == '/') {
                        state = 4;
                    } else if (c == '*') {
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:
                    if (c == '/' || c == 'a' || c == '*') {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 5;
    }
	
	public static void main(String[] args){
		System.out.println(es1_10.scan("/****/"));
		System.out.println(es1_10.scan("/*a*a*/"));
		System.out.println(es1_10.scan("/*a/**/"));
		System.out.println(es1_10.scan("/**a///a/a**/"));
		System.out.println(es1_10.scan("/**/"));
		System.out.println(es1_10.scan("/*/*/"));
		System.out.println(es1_10.scan("/*/"));
		System.out.println(es1_10.scan("/**/***/"));
		
	}
}