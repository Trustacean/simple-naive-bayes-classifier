package naive_bayes.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BayesianClassifier {

    private List<String[]> trainDataset;
    private Map<String, Integer> featureFrequency;
    private Map<String, Double> probabilities;
    private Map<String, Integer> classFrequency;
    private List<String> classNames;
    private String[] headers;

    public BayesianClassifier(List<String[]> dataset) {
        if (dataset == null || dataset.isEmpty()) {
            throw new IllegalArgumentException("Dataset cannot be null or empty");
        }
        this.trainDataset = dataset.subList(1, dataset.size());
        this.headers = dataset.get(0);
        this.featureFrequency = new HashMap<>();
        this.probabilities = new HashMap<>();
        this.classFrequency = new HashMap<>();
        this.classNames = new LinkedList<>();

        Set<String> classSet = new HashSet<>();
        for (String[] row : this.trainDataset) {
            String className = row[row.length - 1];
            classSet.add(className);
            classFrequency.put(className, classFrequency.getOrDefault(className, 0) + 1);
        }

        this.classNames.addAll(classSet);
    }

    public void train() {
        assert this.trainDataset != null;

        int nrofFeatures = this.trainDataset.get(0).length - 1;

        Map<String, Set<String>> nrofUniqueFeature = new HashMap<>();

        // Count the number of occurrences of each feature
        for (String[] row : this.trainDataset) {
            for (int i = 0; i < nrofFeatures; i++) {
                String className = row[row.length - 1];
                String key = headers[i] + "|" + row[i] + "|" + className;
                featureFrequency.put(key, featureFrequency.getOrDefault(key, 0) + 1);
                nrofUniqueFeature.computeIfAbsent(headers[i], k -> new HashSet<>()).add(row[i]);
            }
        }

        for (String className : this.classNames) {
            int classCount = classFrequency.get(className);
            for (Map.Entry<String, Integer> entry : featureFrequency.entrySet()) {
                String key = entry.getKey();
                if (key.endsWith(className)) {
                    double probability = (double) (entry.getValue() + 1) / (classCount + nrofUniqueFeature.get(key.split("\\|")[0]).size());
                    probabilities.put(key, probability);
                }
            }
        }
    }

    public String predict(String[] instance) {
        assert instance != null && instance.length == headers.length - 1;
    
        double maxProbability = Double.NEGATIVE_INFINITY;
        String predictedClass = null;
    
        System.out.println("Prediction Process for Instance: " + String.join(", ", instance));
        System.out.println("--------------------------------------------------");
    
        for (String className : classNames) {
            // Calculate prior probability: P(class)
            double classPrior = (double) classFrequency.get(className) / trainDataset.size();
            System.out.println("Class: " + className);
            System.out.println("  Prior Probability P(" + className + "): " + classPrior);
    
            // Calculate likelihood: P(features | class)
            double likelihood = 1.0;
            System.out.println("  Likelihood Calculation:");
            for (int i = 0; i < instance.length; i++) {
                String key = headers[i] + "|" + instance[i] + "|" + className;
                double featureProbability = probabilities.getOrDefault(key, 1.0 / (classFrequency.get(className) + classNames.size()));
                System.out.println("    P(" + headers[i] + "=" + instance[i] + " | " + className + ") = " + featureProbability);
                likelihood *= featureProbability;
            }
            System.out.println("  Likelihood P(features | " + className + "): " + likelihood);
    
            // Calculate posterior probability: P(class | features) ∝ P(class) * P(features | class)
            double posterior = classPrior * likelihood;
            System.out.println("  Posterior Probability P(" + className + " | features): " + posterior);
    
            // Select the class with the highest posterior probability
            if (posterior > maxProbability) {
                maxProbability = posterior;
                predictedClass = className;
            }
    
            System.out.println("--------------------------------------------------");
        }
    
        System.out.println("Final Comparison:");
        System.out.println("  Predicted Class: " + predictedClass);
        System.out.println("  Maximum Posterior Probability: " + maxProbability);
        System.out.println("==================================================");
    
        return predictedClass;
    }
}
