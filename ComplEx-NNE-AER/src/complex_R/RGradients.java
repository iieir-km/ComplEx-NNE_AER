package complex_R;
import struct.Matrix;
import struct.Relation;
import struct.Rule;

import java.util.ArrayList;

public class RGradients {
    public Rule dRule;
    public Matrix Real_MatrixR;
    public Matrix Imag_MatrixR;
    public Matrix Real_MatrixRGradient;
    public Matrix Imag_MatrixRGradient;
    public double dMu;
    public double dLambda;

    public RGradients(
            Rule inRule,
            Matrix inReal_MatrixR,
            Matrix inImag_MatrixR,
            Matrix inReal_MatrixRGradient,
            Matrix inImag_MatrixRGradient,
            double inMu,
            double inLambda){
        dRule = inRule;
        Real_MatrixR = inReal_MatrixR;
        Imag_MatrixR = inImag_MatrixR;
        Real_MatrixRGradient = inReal_MatrixRGradient;
        Imag_MatrixRGradient = inImag_MatrixRGradient;
        dMu = inMu;
        dLambda = inLambda;
    }

    public void calculateGradients() throws Exception{
        double confidence = dRule.confidence();
        Relation ruleHead = dRule.relations().get(0);
        Relation ruleBody = dRule.relations().get(1);

        int numOfFactors = Real_MatrixR.columns();
        for(int p = 0; p < numOfFactors; p++){
            double real_body = Real_MatrixR.get(ruleBody.rid(), p);
            double imag_body = Imag_MatrixR.get(ruleBody.rid(), p) * ruleBody.direction();
            double real_head = Real_MatrixR.get(ruleHead.rid(), p);
            double imag_head = Imag_MatrixR.get(ruleHead.rid(), p);
            /*
            if(real_body > real_head){
                //Calculate gradients of head
                Real_MatrixRGradient.add(ruleHead.rid(), p, -1 * dMu * confidence);
                //Calculate gradients of body
                Real_MatrixRGradient.add(ruleBody.rid(), p, dMu * confidence);
            }*/
            //Calculate gradients of head
            Real_MatrixRGradient.add(ruleHead.rid(), p, -2 * (real_body - real_head) * dMu * confidence);
            //Calculate gradients of body
            Real_MatrixRGradient.add(ruleBody.rid(), p, 2 * (real_body - real_head) * dMu * confidence);
            //Calculate gradinets of head
            Imag_MatrixRGradient.add(ruleHead.rid(), p, - 2 * (imag_body - imag_head) * dMu * confidence);
            //Calculate gradients of body
            Imag_MatrixRGradient.add(ruleBody.rid(), p, 2 * (imag_body - imag_head) * ruleBody.direction() * dMu * confidence);
        }
    }
}
