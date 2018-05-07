package complex_NNE_AER;

import util.Arguments;

public class Program {
	public static void main(String[] args) throws Exception {
		Arguments cmmdArg = new Arguments(args);
		ComplEx model = new ComplEx();
		String fnTrainTriples = "";
		String fnValidTriples = "";
		String fnTestTriples = "";
		String fnAllTriples = "";
		String fnRules = "";
		String strNumRelation = "";
		String strNumEntity = "";
		Boolean eval = false;

		try {
			fnTrainTriples = cmmdArg.getValue("train");
			if (fnTrainTriples == null || fnTrainTriples.equals("")) {
				Usage();
				return;
			}
			fnValidTriples = cmmdArg.getValue("valid");
			if (fnValidTriples == null || fnValidTriples.equals("")) {
				Usage();
				return;
			}
			fnTestTriples = cmmdArg.getValue("test");
			if (fnTestTriples == null || fnTestTriples.equals("")) {
				Usage();
				return;
			}
			fnAllTriples = cmmdArg.getValue("all");
			if (fnAllTriples == null || fnAllTriples.equals("")) {
				Usage();
				return;
			}
			fnRules = cmmdArg.getValue("rule");
			if (fnRules == null || fnRules.equals("")){
				Usage();
				return;
			}
			strNumRelation = cmmdArg.getValue("m");
			if (strNumRelation == null || strNumRelation.equals("")) {
				Usage();
				return;
			}
			strNumEntity = cmmdArg.getValue("n");
			if (strNumEntity == null || strNumEntity.equals("")) {
				Usage();
				return;
			}
			if (cmmdArg.getValue("k") != null && !cmmdArg.getValue("k").equals("")) {
				model.m_NumFactor = Integer.parseInt(cmmdArg.getValue("k"));
			}
			if (cmmdArg.getValue("lmbda") != null && !cmmdArg.getValue("lmbda").equals("")) {
				model.m_Lambda = Double.parseDouble(cmmdArg.getValue("lmbda"));
			}
			if (cmmdArg.getValue("gamma") != null && !cmmdArg.getValue("gamma").equals("")) {
				model.m_Gamma = Double.parseDouble(cmmdArg.getValue("gamma"));
			}
			if (cmmdArg.getValue("mu") != null && !cmmdArg.getValue("mu").equals("")) {
				model.m_Mu = Double.parseDouble(cmmdArg.getValue("mu"));
			}
			if (cmmdArg.getValue("neg") != null && !cmmdArg.getValue("neg").equals("")) {
				model.m_NumNegative = Integer.parseInt(cmmdArg.getValue("neg"));
			}
			if(cmmdArg.getValue("min_pca") !=null && !cmmdArg.getValue("min_pca").equals("")){
				model.m_pca = Double.parseDouble(cmmdArg.getValue("min_pca"));
			}
			if (cmmdArg.getValue("#") != null && !cmmdArg.getValue("#").equals("")) {
				model.m_NumIteration = Integer.parseInt(cmmdArg.getValue("#"));
			}

			model.initialization(strNumRelation, strNumEntity, fnTrainTriples, fnValidTriples, fnTestTriples, fnAllTriples, fnRules);
			System.out.println("\nStart learning ComplEx (NNE+AER) model");
			model.learn();
			System.out.println("Success.");
		} catch (Exception e) {
			e.printStackTrace();
			Usage();
			return;
		}
	}

	static void Usage() {
		System.out.println(
				"Usagelala: java ComplEx -train train_triples -valid valid_triples -test test_triples -all all_triples -rule rules" +
						"-m number_of_relations -n number_of_entities [options]\n\n"
						+

						"Options: \n"
						+ "   -k        -> number of latent factors (default 50)\n"
						+ "   -lmbda    -> regularization parameter (default 0.001)\n"
						+ "   -gamma    -> initial learning rate (default 0.1)\n"
						+ "   -neg      -> number of negative instances (default 2)\n"
						+ "   -#        -> number of iterations (default 1000)\n"
						+ "   -skip     -> number of skipped iterations (default 50)\n\n"
		);
	}
}
