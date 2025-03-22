/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package naive_bayes;

import java.util.List;

import naive_bayes.helper.BayesianClassifier;
import naive_bayes.helper.CsvHandler;

/**
 *
 * @author Trustacean
 */
public class NaiveBayes {
    public static void main(String[] args) {
        String file = "src/dataset/comp.csv";
        List<String[]> data = CsvHandler.readCsv(file, ';', true);
        
        BayesianClassifier model = new BayesianClassifier(data);
        model.train();

        String[] test = {"31...40", "low", "no", "fair"};
        String prediction = model.predict(test);

        System.out.println("Prediction: " + prediction);
    }
}
 