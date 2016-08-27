import org.antlr.runtime.*;

class plp4 {
	public static void main(String[] args) throws Exception {
		plp4Lexer lex = new plp4Lexer(new ANTLRFileStream(args[0]));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		plp4Parser parser = new plp4Parser(tokens);
		//try {
			String trad = parser.s(); // S es la regla inicial
			System.out.println(trad);
		//} catch (Exception e) {
		//	System.err.println(e.toString());
		//}
	}
}
