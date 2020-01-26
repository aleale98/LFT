public class NumberTok extends Token {

    public String lexeme;

    public NumberTok(String s){
        super(Tag.NUM);
        lexeme=s;
    }

    @Override
    public String toString(){
        return "<"+Tag.NUM+","+lexeme+">";
    }
}