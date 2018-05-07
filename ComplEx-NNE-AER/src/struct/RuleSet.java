package struct;

import util.StringSplitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class RuleSet {
    private ArrayList<Rule> lstRules;
    public void load(String fnInput) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(fnInput));
        lstRules = new ArrayList<Rule>();
        String line = "";
        while((line = reader.readLine()) != null){
            String[] tokens = line.split("\t");
            Rule rule = new Rule();
            String ruleString = tokens[0];
            rule.conf = Double.parseDouble(tokens[1]);
            String[] ruleStrings = ruleString.split(",");
            rule.add(new Relation(Integer.parseInt(ruleStrings[ruleStrings.length-1]),1));
            for(int i = 0; i < ruleStrings.length - 1; i++){
                int rel = Integer.parseInt(ruleStrings[i]);
                int dir = 1;
                if(rel < 0){
                    rel = -1 * rel;
                    dir = -1;
                }
                rule.add(new Relation(rel, dir));
            }
            lstRules.add(rule);
        }
        reader.close();
    }
    public ArrayList<Rule> rules(){
        return lstRules;
    }
}
