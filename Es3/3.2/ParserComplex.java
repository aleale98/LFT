import java.io.*;

public class ParserComplex {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public ParserComplex(Lexer l, BufferedReader br) {
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
        if(look.tag == t) {
            if(look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
    public void prog(){
        switch(look.tag){
            case '(':
                stat();
                match(Tag.EOF);
                break;
            default:
                error(look.toString());
        }
    }

    public void statList(){
        switch(look.tag){
            case '(':
                stat();
                statListP();
                break;
            default:
                error(look.toString());
        }
    }

    public void statListP(){
        System.out.println("statListP: " + look.tag);
        switch(look.tag){
            case '(':
                stat();
                statListP();
                break;
            case ')':
                break;
            default:
                error(look.toString());
        }
    }

    public void stat(){
        switch(look.tag){
            case '(':
                match('(');
                statP();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    public void statP(){
        System.out.println("statP: " + look.tag);
        switch(look.tag){
            case '=':
                match('=');
                match(Tag.ID);
                expr();
                break;
            case Tag.COND:
                match(Tag.COND);
                bExpr();
                stat();
                elseOpt();
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bExpr();
                stat();
                break;
            case Tag.DO:
                match(Tag.DO);
                statList();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprList();
                break;
            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
                break;
            default:
                error(look.toString());
        }
    }

    public void elseOpt(){
        System.out.println("elseOpt: " + look.tag);
        switch(look.tag){
            case '(':
                match('(');
                match(Tag.ELSE);
                stat();
                match(')');
                break;
            case ')':
                break;
            default:
                error(look.toString());
        }
    }

    public void bExpr(){
        System.out.println("bExpr: " + look.tag);
        switch(look.tag){
            case '(':
                match('(');
                bExprP();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    public void bExprP(){
        System.out.println("bExprP: " + look.tag);
        switch(look.tag){
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error(look.toString());
        }
    }

    public void expr(){
        System.out.println("expr: " + look.tag);
        switch(look.tag){
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            case '(':
                match('(');
                exprP();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    public void exprP(){
        System.out.println("exprP: " + look.tag);
        switch(look.tag){
            case '+':
                match('+');
                exprList();
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                exprList();
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            default:
                error(look.toString());

        }
    }

    public void exprList(){
        System.out.println("exprList: " + look.tag);
        switch(look.tag){
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                exprListP();
                break;
            default:
                error(look.toString());
        }
    }

    public void exprListP(){
        System.out.println("exprListP: " + look.tag);
        switch(look.tag){
            case Tag.NUM:
            case Tag.ID:
            case '(':
                expr();
                exprListP();
                break;
            case ')':
                break;
            default:
                error(look.toString());
        }
    }



    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/alessio/Desktop/test.txt";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ParserComplex parser = new ParserComplex(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}