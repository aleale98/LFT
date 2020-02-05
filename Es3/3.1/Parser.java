import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + Lexer.getLine() + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
	/*
		start --> expr EOF
		
		guida(start --> expr EOF)={(, NUM}
		
	*/
    public void start() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                expr();
                match(Tag.EOF);
                break;
            default:
                error(look.toString());
        }
    }
	/*
		expr --> term exprp
		guida(expr --> term exprp)={(, NUM}
	*/
	private void expr() {
	        switch(look.tag) {
	            case '(':
	            case Tag.NUM:
	                term();
	                exprp();
	                break;
	            default:
	                error(look.toString());
	        }
	    }

    private void exprp() {
        switch (look.tag) {
			/*
				exprp --> + term exprp
				guida(exprp --> + term exprp)={+}
			*/
            case '+':
                match('+');
                term();
                exprp();
                break;
			/*
				exprp --> - term exprp
				guida(exprp --> - term exprp)={-}
			*/	
            case '-':
                match('-');
                term();
                exprp();
                break;
			/*
				exprp --> epsilon
				guida(exprp --> epsilon)={EOF, )}
			*/
            case ')':
            case Tag.EOF:
                break;
            default:
                error(look.toString());
                break;
        }
    }
	/*
		term --> fact termp
		guida(term --> fact termp)={(, NUM}
	*/
    private void term() {
        switch (look.tag) {
            case '(':
            case Tag.NUM:
                fact();
                termp();
                break;
            default:
                error(look.toString());
        }

    }

    private void termp() {
        switch (look.tag) {
			/*
				termp --> * fact termp
				guida(termp --> * fact termp)={*}
			*/
            case '*':
                match('*');
                fact();
                termp();
                break;
			/*
				termp --> / fact termp
				guida(termp --> / fact termp)={/}
			*/
            case '/':
                match('/');
                fact();
                termp();
                break;
			/*
				termp --> epsilon
				guida(termp --> epsilon)={+, -, ), EOF}
			*/
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                break;
            default:
                error(look.toString());
                break;
        }
    }

    private void fact() {
        switch (look.tag) {
			/*
				fact --> (expr)
				guida(fact --> (expr))={(}
			*/
            case '(':
                match('(');
                expr();
                match(')');
                break;
			/*
				fact --> NUM
				guida(fact --> NUM)={NUM}
			*/
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error(look.toString());
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/alessio/Desktop/test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}