import org.antlr.runtime.*;

class plp3 {
	public static void main(String[] args) throws Exception {
		plp3Lexer lex = new plp3Lexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		plp3Parser parser = new plp3Parser(tokens);
		try {
			String trad = parser.s(); // S es la regla inicial
			System.out.println(trad);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
