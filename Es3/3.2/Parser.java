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
    public void prog() {
        switch (look.tag) {
            case '(':
                stat();
                match(Tag.EOF);
                break;
            default:
                error(look.toString());
        }
    }

    private void statlist(){
        switch (look.tag){
            case '(':
                stat();
                statlistp();
                break;
            default:
                error(look.toString());
        }
    }

    private void statlistp(){
        switch (look.tag){
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

    private void stat() {
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

    private void statp() {
        switch (look.tag) {
            case Tag.COND:
                match(Tag.COND);
                bexpr();
                stat();
                elseopt();
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                bexpr();
                stat();
                break;
            case Tag.DO:
                match(Tag.DO);
                statlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                exprlist();
                break;
            case Tag.READ:
                match(Tag.READ);
                match(Tag.ID);
                break;
            case '=':
                match('=');
                match(Tag.ID);
                expr();
                break;
            default:
                error(look.toString());
        }
    }

    private void elseopt() {
        switch (look.tag) {
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

    private void bexpr() {
        switch (look.tag) {
            case '(':
                match('(');
                bexprp();
                match(')');
                break;
            default:
                error(look.toString());
        }
    }

    private void bexprp() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error(look.toString());
        }
    }

    private void expr() {
        switch (look.tag) {
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
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
        switch (look.tag){
            case '+':
                match('+');
                exprlist();
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                exprlist();
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

    private void exprlist(){
        switch (look.tag){
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

    private void exprlistp(){
        //System.out.println("exprListP: " + look.tag);
        switch(look.tag){
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

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/Users/alessio/Desktop/test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}