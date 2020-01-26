import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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

    public void prog() {
        switch (look.tag) {
            case '(':
                int lnext_prog = code.newLabel();
                stat();
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            default:
                error(look.toString());
        }
    }

    public void stat() {
        switch (look.tag) {
            case '(':
                match('(');
                statp();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    public void statp() {
        int lnext_true;
        int lnext_false;
		int read_id_addr;
        switch (look.tag) {
            case Tag.READ:
                match(Tag.READ);
                if (look.tag == Tag.ID) {
                    read_id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (read_id_addr == -1) {
                        read_id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, read_id_addr);
                } else
                    error("Error in grammar (stat) after read with " + look);
                break;
            case Tag.COND:
                lnext_true=code.newLabel();
                lnext_false=code.newLabel();
                int lnext_l=code.newLabel();
                match(Tag.COND);
                bexpr(lnext_true, lnext_false);
                code.emitLabel(lnext_true);
                stat();
                elseopt(lnext_false, lnext_l);
                break;
            case Tag.WHILE:
                int btrue = code.newLabel();
                lnext_true=code.newLabel();
                lnext_false=code.newLabel();
                match(Tag.WHILE);
                bexpr(lnext_true, lnext_false);
                code.emitLabel(lnext_true);
                stat();
                code.emit(OpCode.GOto, btrue);
                code.emitLabel(lnext_false);
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
                code.emit(OpCode.invokestatic, 1);
                break;
            case '=':
                match('=');
                if (look.tag == Tag.ID) {
                    int readAddress = st.lookupAddress(((Word) look).lexeme);
                    if (readAddress == -1) {
                        readAddress = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    expr();
                    code.emit(OpCode.istore, readAddress);
					break;
					
                }
        }
    }

    private void statlist() {
        switch (look.tag) {
            case '(':
                stat();
                statlistp();
                break;
            default:
                error(look.toString());
        }
    }

    private void statlistp() {
        switch (look.tag) {
            case '(':
                stat();
                statlistp();
                break;
            case ')':
                break;
            default:
                error(look.toString());
        }
    }

    private void expr() {
        switch (look.tag) {
            case Tag.NUM:
			int val = Integer.parseInt(((NumberTok)look).lexeme);
			int num_val=val;
			match(Tag.NUM);
            code.emit(OpCode.ldc, num_val);
                break;
            case Tag.ID:
                int readAddress = st.lookupAddress(((Word) look).lexeme);
                if (readAddress == -1)
                    error("var " + ((Word) look).lexeme + " has not been declared");
                match(Tag.ID);
                code.emit(OpCode.iload, readAddress);
                break;
            case '(':
                match('(');
                exprp();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                exprlist();
                code.emit(OpCode.iadd);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                exprlist();
                code.emit(OpCode.imul);
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            default:
                error(look.toString());
        }
    }

    private void exprlist() {
        switch (look.tag) {
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                exprlistp();
                break;
            default:
                error(look.toString());
        }
    }

    private void exprlistp() {
        switch (look.tag) {
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                exprlistp();
                break;
            case ')':
                break;
            default:
                error(look.toString());
        }
    }

    private void bexpr(int lnext_true, int lnext_false) {
        switch (look.tag) {
            case '(':
                match('(');
                int next = code.newLabel();
                bexprp(lnext_true, lnext_false);
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    private void bexprp(int lnext_false, int lnext_true) {
        OpCode opCode = OpCode.if_icmpeq;
        switch (look.tag) {
            case Tag.RELOP:
                switch (((Word) look).lexeme) {
                    case "<":
                        opCode=OpCode.if_icmplt;
                        break;
                    case "<=":
                        opCode=OpCode.if_icmple;
                        break;
                    case ">":
                        opCode=OpCode.if_icmpgt;
                        break;
                    case ">=":
                        opCode=OpCode.if_icmpge;
                        break;
                    case "<>":
                        opCode=OpCode.if_icmpne;
                        break;
                    default:
                        error(look.toString());
                }
                match(Tag.RELOP);
                break;
				case Tag.AND:
					int and_true=code.newLabel();
					match(Tag.AND);
					bexpr(and_true, lnext_false);
					code.emitLabel(and_true);
					bexpr(lnext_true, lnext_false);
					code.emitLabel(lnext_false);
				break;
				case Tag.OR:
				int or_false=code.newLabel();
					match(Tag.OR);
					bexpr(lnext_true, or_false);
					code.emitlabel(or_false);
					bexpr(lnext_true, lnext_false);
					break;
				case Tag.NEG:
					match(Tag.NEG);
					bexpr(lnext_false, lnext_true);
					break;
				break;
            default:
                error(look.toString());
        }
        expr();
        expr();
        code.emit(opCode, lnext_true);
        code.emit(OpCode.GOto, lnext_false);
    }

    private void elseopt(int lnext_false, int end) {
        switch (look.tag) {
            case '(':

                match('(');
                match(Tag.ELSE);
                code.emit(OpCode.GOto, end);
                code.emitLabel(lnext_false);
                stat();
                code.emitLabel(end);
                match(')');
                break;
            case ')':
                code.emitLabel(lnext_false);
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
            Translator translator = new Translator(lex, br);
            translator.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}