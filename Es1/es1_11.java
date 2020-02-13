public class es1_11 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char c = s.charAt(i++);
            switch (state) {
                case 0:
                    if (c == '/')
                        state = 1;
                    else if(c=='a')
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
                        state=1;

            }
        }
        return state==0 ||
			state == 5;
    }
	
	public static void main(String[] args){
		System.out.println(es1_11.scan("aaa/****/aa"));
		System.out.println(es1_11.scan("aa/*a*a*/"));
		System.out.println(es1_11.scan("aaaa"));
		System.out.println(es1_11.scan("/****/"));
		System.out.println(es1_11.scan("/*aa*/"));
		System.out.println(es1_11.scan("a/**/***a"));
		System.out.println(es1_11.scan("a/**/***/a"));
		System.out.println(es1_11.scan("a/**/aa/***/a"));
		System.out.println(es1_11.scan("aaa/*/aa"));
		System.out.println(es1_11.scan("a/**//***a"));
		System.out.println(es1_11.scan("aa/*aa"));
		
	}
}
