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

    /*
        prog --> {stat.next=newLabel()}stat{emit(stat.next)}EOF
    */
    public void prog() {
        switch (look.tag) {
            case '(':
                int lnext_prog = code.newLabel();
                stat(lnext_prog);
				System.out.println("LNEXT_PROG "+lnext_prog);
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
	
	/*
		stat --> (statp)
	*/
    public void stat(int lnext) {
        switch (look.tag) {
            case '(':
                match('(');
                statp(lnext);
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    public void statp(int lnext) {
        int lnext_true;
        int lnext_false;
        int read_id_addr;
        switch (look.tag) {
			/*
				statp --> READ ID {emit(istore(&ID))}
			*/
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
				/*
					
					
					creo le etichette per il caso true e per il caso false. Creo inoltre l'etichetta lnext_l 
					che uso per indicare la fine del caso else. Non è utilizzata direttamente qui per qualche elaborazione
					ma serve solo per essere passata come parametro al metodo elseopt e indica il punto a cui saltare 
					quando ho finito di eseguire il codice dell'else.
				*/
            case Tag.COND:
				lnext_true = code.newLabel();
				lnext_false = code.newLabel();
				int lnext_end = code.newLabel();
				match(Tag.COND);
				bexpr(lnext_true, lnext_false);
				code.emitLabel(lnext_true);
				stat(lnext);
				elseopt(lnext_false, lnext_end);
    			break;
				
				/*
					
					
					L'idea alla base è che fino a quando l'espressione booleana è vera, torno sempre all'inizio del ciclo.
					Utilizzo quindi btrue per indicare l'etichetta iniziale del corpo del ciclo.
				*/
            case Tag.WHILE:
				int btrue = code.newLabel();
                match(Tag.WHILE);
                int while_true = code.newLabel();
                int while_false = lnext;
                code.emitLabel(btrue);
                bexpr(while_true, while_false);
                code.emitLabel(while_true);
                stat(lnext );
                code.emit(OpCode.GOto, btrue);
                code.emitLabel(while_false);
                break;
				/*
					statp --> DO statlist
				*/
            case Tag.DO:
                match(Tag.DO);
				int s_next= code.newLabel();
                statlist(s_next);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist('p');
                break;
            case '=':
                match('=');
                read_id_addr = st.lookupAddress(((Word) look).lexeme);
                if (read_id_addr == -1) {
                    read_id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore, read_id_addr);
                break;
            default:
                error("Syntax error in statp");
        }
    }

    private void statlist(int lnext) {
        switch (look.tag) {
            case '(':
                stat(lnext);
				int statlistp_next= code.newLabel();
                statlistp(statlistp_next);
                break;
            default:
                error(look.toString());
        }
    }
	//nuova etichetta per statlistp
    private void statlistp(int lnext) {
        switch (look.tag) {
            case '(':
                stat(lnext);
				int new_label=code.newLabel();
                statlistp(new_label);
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
                int num_val = Integer.parseInt(((NumberTok) look).lexeme);
                match(Tag.NUM);
                code.emit(OpCode.ldc, num_val);
                break;
            case Tag.ID:
                int read_id_address = st.lookupAddress(((Word) look).lexeme);
                if (read_id_address == -1)
                    error("var " + ((Word) look).lexeme + " has not been declared");
                code.emit(OpCode.iload, read_id_address);
                match(Tag.ID);
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
        char opType = ' ';
        switch (look.tag) {
			/*
				exprp --> + exprlist {emit(iadd)}
			*/
            case '+':
                match('+');
                opType = '+';
                exprlist(opType);
                break;
				/*
					exprp --> -expr expr {emit(isub)}
				*/
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                opType = '*';
                exprlist(opType);
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

    private void exprlist(char opType) {
        switch (look.tag) {
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                char exprlistp_opType = opType;
                if (opType == 'p') {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(exprlistp_opType);
                break;
            default:
                error(look.toString());
        }
    }

    private void exprlistp(char opType) {
        switch (look.tag) {
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                if (opType == '+') {
                    code.emit(OpCode.iadd);
                } else if (opType == '*') {
                    code.emit(OpCode.imul);
                } else if (opType == 'p') {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opType);
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
                bexprp(lnext_true, lnext_false);
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    private void bexprp(int lnext_true, int lnext_false) {
        // inizializzazione a '==' se non serve e' perché c'è un errore
        OpCode opCode = OpCode.if_icmpeq;
        switch (look.tag) {
            case Tag.RELOP:
                switch (((Word) look).lexeme) {
                    case "==":
                        opCode = OpCode.if_icmpeq;
                        break;
                    case "<":
                        opCode = OpCode.if_icmplt;
                        break;
                    case "<=":
                        opCode = OpCode.if_icmple;
                        break;
                    case "<>":
                        opCode = OpCode.if_icmpne;
                        break;
                    case ">":
                        opCode = OpCode.if_icmpgt;
                        break;
                    case ">=":
                        opCode = OpCode.if_icmpge;
                        break;
                    default:
                        error("Error in grammar (bexprp)");
                }
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(opCode, lnext_true);
                code.emit(OpCode.GOto, lnext_false);
                break;
            case Tag.AND:
                int and_true = code.newLabel();
                match(Tag.AND);
                bexpr(and_true, lnext_false);
                code.emitLabel(and_true);
                bexpr(lnext_true, lnext_false);
                break;
            case Tag.OR:
                int or_false = code.newLabel();
                match(Tag.OR);
                bexpr(lnext_true, or_false);
                code.emitLabel(or_false);
                bexpr(lnext_true, lnext_false);
                break;
            case Tag.NOT:
                match(Tag.NOT);
                bexpr(lnext_false, lnext_true);
                break;
            default:
                error("\nsyntax error in bexprp");
        }
    }

    private void elseopt(int lnext_false, int end) {
        switch (look.tag) {
            case '(':
                match('(');
				match(Tag.ELSE);
                code.emit(OpCode.GOto, end);
				 code.emitLabel(lnext_false);
				
				System.out.println("LNEXT_FALSE "+lnext_false);
               
				System.out.println("END "+end);
                stat(end);
				System.out.println("STO EMETTENDO END");
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