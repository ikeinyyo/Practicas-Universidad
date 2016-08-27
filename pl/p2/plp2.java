import org.antlr.runtime.*;

class plp2 {
	public static void main(String[] args) throws Exception {
		plp2Lexer lex = new plp2Lexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		plp2Parser parser = new plp2Parser(tokens);
		try {
			String trad = parser.s(); // S es la regla inicial
			System.out.println(trad);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
